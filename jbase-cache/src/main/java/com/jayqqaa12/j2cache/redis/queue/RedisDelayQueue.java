package com.jayqqaa12.j2cache.redis.queue;

import com.alibaba.fastjson.JSON;
import com.jayqqaa12.j2cache.CacheProviderHolder;
import com.jayqqaa12.j2cache.redis.RedisClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.sortedset.ZAddParams;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * TODO 序列化方式要抽象一下
 */
public class RedisDelayQueue implements DelayQueue {
    private static final Logger LOG = LoggerFactory.getLogger(RedisDelayQueue.class);

    private transient final ReentrantLock lock = new ReentrantLock();
    private final Condition available = lock.newCondition();
    private RedisClient redisClient = CacheProviderHolder.getRedisClient();
    private long MAX_TIMEOUT = 525600000; // 最大超时时间不能超过一年
    private int unackTime = 60 * 1000;
    private String redisKeyPrefix;
    private byte[] messageStoreKey;
    private byte[] realQueueName;

    private DelayQueueProcessListener delayQueueProcessListener;

    private volatile boolean isEmpty = false;
    private volatile boolean status = true;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RedisDelayQueue(String redisKeyPrefix, DelayQueueProcessListener delayQueueProcessListener) {
        this.redisKeyPrefix = redisKeyPrefix;
        this.messageStoreKey = (redisKeyPrefix + ":delay_msg").getBytes();
        this.realQueueName = (redisKeyPrefix + ":delay_queue").getBytes();
        this.delayQueueProcessListener = delayQueueProcessListener;
    }


    @Override
    public boolean push(Message message) {
        if (message.getCreateTime() == null || message.getTimeout() > MAX_TIMEOUT) {
            throw new IllegalArgumentException("Maximum delay time should not be exceed one year");
        }
        if (message.getId() == null) {
            throw new IllegalArgumentException("message id can't is null");
        }

        try {
            String json = JSON.toJSONString(message);
            redisClient.get().hset(messageStoreKey, message.getId().getBytes(), json.getBytes());
            double priority = message.getPriority() / 100;
            double score = Long.valueOf(System.currentTimeMillis() + message.getTimeout()).doubleValue() + priority;
            redisClient.get().zadd(realQueueName, score, message.getId().getBytes());
            delayQueueProcessListener.pushCallback(message);
            isEmpty = false;
            return true;
        } finally {
            redisClient.release();
        }
    }

    public void listen() {
        executorService.execute(() -> {
            while (status) {
                try {
                    byte[] id = peekId();
                    if ( id== null) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e){
                        }
                        continue;
                    }
                    String json = new String(redisClient.get().hget(messageStoreKey, id));
                    Message message = JSON.parseObject(json, Message.class);
                    if (message == null) continue;
                    long delay = message.getCreateTime() + message.getTimeout() - System.currentTimeMillis();

                    if (delay <= 0) {
                        delayQueueProcessListener.peekCallback(message);
                    } else {
                        LockSupport.parkNanos(this, TimeUnit.NANOSECONDS.convert(delay, TimeUnit.MILLISECONDS));
                        delayQueueProcessListener.peekCallback(message);
                    }
                    ack(message.getId());
                } finally {
                    redisClient.release();
                }

            }
        });

    }


    @Override
    public void close() {
        status = false;
        executorService.shutdown();
        LOG.info("redis delay queue is exist ");
    }


    @Override
    public boolean ack(String messageId) {


        try {
            redisClient.get().zrem(getUnackQueueName(), messageId.getBytes());
            Long removed = redisClient.get().zrem(realQueueName, messageId.getBytes());
            Long msgRemoved = redisClient.get().hdel(messageStoreKey, messageId.getBytes());

            LOG.debug("ack msgid {}, zset {} hash {}", messageId, removed, msgRemoved);
            if (removed > 0 && msgRemoved > 0) {
                return true;
            }
            return false;
        } finally {
            redisClient.release();
        }

    }

    @Override
    public boolean setUnackTimeout(String messageId, long timeout) {
        try {
            double unackScore = Long.valueOf(System.currentTimeMillis() + timeout).doubleValue();

            Double score = redisClient.get().zscore(getUnackQueueName(), messageId.getBytes());
            if (score == null) {
                redisClient.get().zadd(getUnackQueueName(), unackScore, messageId.getBytes());
                return true;
            }

            return false;
        } finally {
            redisClient.release();
        }

    }

    @Override
    public boolean setTimeout(String messageId, long timeout) {

        try {
            String json = new String(redisClient.get().hget(messageStoreKey, messageId.getBytes()));
            if (StringUtils.isEmpty(json)) {
                return false;
            }
            Message message = JSON.parseObject(json, Message.class);
            message.setTimeout(timeout);
            Double score = redisClient.get().zscore(realQueueName, messageId.getBytes());
            if (score != null) {
                double priorityd = message.getPriority() / 100;
                double newScore = Long.valueOf(System.currentTimeMillis() + timeout).doubleValue() + priorityd;
                ZAddParams params = ZAddParams.zAddParams().xx();
                long added = redisClient.get().zadd(realQueueName, newScore, messageId.getBytes(), params);
                if (added == 1) {
                    json = JSON.toJSONString(message);
                    redisClient.get().hset(messageStoreKey, message.getId().getBytes(), json.getBytes());
                    return true;
                }
                return false;
            }
            return false;
        } finally {
            redisClient.release();
        }

    }

    @Override
    public Message get(String messageId) {
        try {
            String json = new String(redisClient.get().hget(messageStoreKey, messageId.getBytes()));
            if (StringUtils.isEmpty(json)) return null;
            return JSON.parseObject(json, Message.class);
        } finally {
            redisClient.release();
        }

    }


    @Override
    public boolean contain(String messageId) {

        try {
            return redisClient.get().hexists(messageStoreKey, messageId.getBytes());
        } finally {
            redisClient.release();
        }
    }

    @Override
    public long size() {

        try {
            return redisClient.get().zcard(realQueueName);
        } finally {
            redisClient.release();
        }

    }

    @Override
    public void clear() {

        try {
            redisClient.get().del(realQueueName);
            redisClient.get().del(getUnackQueueName());
            redisClient.get().del(messageStoreKey);
        } finally {
            redisClient.release();
        }


    }

    private byte[] peekId() {
        try {
            if (!isEmpty) {
                lock.lockInterruptibly();
                double max = Long.valueOf(System.currentTimeMillis() + MAX_TIMEOUT).doubleValue();
                Set<byte[]> scanned = redisClient.get().zrangeByScore(realQueueName, 0, max, 0, 1);

                if (scanned.iterator().hasNext()) {

                    byte[] messageId = scanned.iterator().next();

                    redisClient.get().zrem(realQueueName, messageId);
                    setUnackTimeout(new String(messageId), unackTime);
                    if (size() == 0) isEmpty = true;
                    available.signal();
                    lock.unlock();
                    return messageId;
                }

            }
        } catch (InterruptedException e) {
            LOG.error(" redis queue error {}", e);
            available.signal();
            lock.unlock();
        }finally {
            redisClient.release();
        }
        return null;
    }

    public void processUnacks() {

        try {
            int batchSize = 1_000;
            double now = Long.valueOf(System.currentTimeMillis()).doubleValue();

            Set<Tuple> unacks = redisClient.get().zrangeByScoreWithScores(getUnackQueueName(), 0, now, 0, batchSize);
            for (Tuple unack : unacks) {
                double score = unack.getScore();
                String member = unack.getElement();
                String payload = new String(redisClient.get().hget(messageStoreKey, member.getBytes()));
                if (payload == null) {
                    redisClient.get().zrem(getUnackQueueName(), member.getBytes());
                    continue;
                }
                redisClient.get().zadd(realQueueName, score, member.getBytes());
                redisClient.get().zrem(getUnackQueueName(), member.getBytes());
            }
        } finally {
            redisClient.release();
        }


    }

    private byte[] getUnackQueueName() {
        return (redisKeyPrefix + ":delay_unack").getBytes();
    }


    @Override
    public int getUnackTime() {
        return this.unackTime;
    }

    @Override
    public void setUnackTime(int time) {
        this.unackTime = time;
    }

}
