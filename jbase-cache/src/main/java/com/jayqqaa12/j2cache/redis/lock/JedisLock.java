package com.jayqqaa12.j2cache.redis.lock;

import com.jayqqaa12.j2cache.redis.RedisClient;
import com.jayqqaa12.j2cache.serializer.SerializationUtils;
import com.jayqqaa12.j2cache.util.CacheException;

import java.io.IOException;

/**
 * Created by 12 on 2017/7/17.
 */
public class JedisLock extends AbstractRedisLock {

    private RedisClient client;

    public JedisLock(RedisClient jedis) {
        this.client = jedis;
    }

    @Override
    protected Long setnx(String key, String val) {
        try {
            return this.client.get().setnx(key.getBytes(), val.getBytes());
        }   finally {
            client.release();
        }
    }

    @Override
    protected void expire(String key, int expire) {
        try {
            this.client.get().expire(key.getBytes(), expire);
        }   finally {
            client.release();
        }
    }

    @Override
    protected String get(String key) {
        try {
            return SerializationUtils.deserialize(this.client.get().get(key.getBytes())).toString();
        } catch (IOException e) {
            throw new CacheException(e);
        }finally {
            client.release();
        }
    }

    @Override
    protected String getSet(String key, String newVal) {
        try {
            return SerializationUtils.deserialize(client.get().getSet(key.getBytes(), newVal.getBytes())).toString();
        } catch (IOException e) {
            throw new CacheException(e);
        }finally {
            client.release();
        }
    }

    @Override
    protected void del(String key) {

        try {
            client.get().del(key.getBytes());
        }   finally {
            client.release();
        }

    }

}
