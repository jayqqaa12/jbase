package com.jayqqaa12.j2cache;

import com.jayqqaa12.j2cache.redis.RedisPubSubClusterPolicy;
import com.jayqqaa12.j2cache.util.CacheException;
import com.jayqqaa12.j2cache.util.ConfigKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.jayqqaa12.j2cache.CacheConstans.LEVEL1;
import static com.jayqqaa12.j2cache.CacheConstans.LEVEL2;


/**
 * 缓存使用入口  请保持单例使用
 * <p>
 * set  设置数据同时放入一级和二级并通知其他节点
 * <p>
 * set1 只设置一级缓存
 * <p>
 * set2 只设置二级缓存
 * <p>
 * setn 设置数据但是 不发送通知
 */
public class J2Cache {
    private static final Logger LOG = LoggerFactory.getLogger(J2Cache.class);

    private static CacheChannel cache = null;
    private static LockKit lock = new LockKit();

    static {
        init();
    }

    private static void init() {
        CacheProviderHolder.init(ConfigKit.initFromConfig());
        RedisPubSubClusterPolicy policy = new RedisPubSubClusterPolicy(CacheConstans.REDIS_CHANNEL, CacheProviderHolder.getRedisClient());
        policy.connect();
        cache = new CacheChannel() {
            @Override
            public void sendEvictCmd(String region, Object key) {
                policy.sendEvictCmd(region, key);
            }

            @Override
            public void close() {
                policy.disconnect();
                CacheProviderHolder.shutdown(LEVEL1);
                CacheProviderHolder.shutdown(LEVEL2);
                LOG.info("j2cache closed ");
            }
        };

    }



    public static CacheChannel cache() {
        return cache;
    }

    public static LockKit lock() {
        return lock;
    }

    /**
     * 获取数据
     *
     * @param key
     * @param data
     * @return
     * @throws CacheException
     */
    public static <T> T get(Object key, CacheDataSource data) throws CacheException {
        return get(CacheConstans.NUllRegion, key, data, CacheConstans.DEFAULT_TIME, false);
    }


    /**
     * 注意region 不要使用变量
     *
     * @param region
     * @param key
     * @param data
     * @param <T>
     * @return
     * @throws CacheException
     */
    public static <T> T get(String region, Object key, CacheDataSource data) throws CacheException {
        return get(region, key, data, CacheConstans.DEFAULT_TIME, false);
    }


    public static <T> T get1(String region, Object key, CacheDataSource data, int sec) throws CacheException {

        Object obj = get1(region, key);
        if (obj == null) {
            obj = data.load();
            set1n(region, key, obj, sec);
        }
        return (T) obj;
    }

    public static <T> T get(Object key, CacheDataSource data, int sec) throws CacheException {
        return get(CacheConstans.NUllRegion, key, data, sec, false);
    }


    public static <T> T get(String region, Object key, CacheDataSource data, int sec) throws CacheException {
        return get(region, key, data, sec, false);
    }

    /**
     * 获取数据
     * 使用cache
     * 没有的话就调用获取数据接口来获取
     * <p>
     * 如果加了失效时间 redis 自动改为存 string 不存hash
     *
     * @param region
     * @param key
     * @param data
     * @return
     * @throws CacheException
     */
    public static <T> T get(String region, Object key, CacheDataSource data, int sec, boolean lock) throws CacheException {
        try {
            Object obj = cache().get(region, key);
            if (obj == null) {
//                if(lock)lock().spinLock(region+key,100);
                obj = data.load();
                cache().set(region, key, obj, sec, false);
            }

            return (T) obj;
        } finally {
//            if(lock)lock().unlock(region+key);
        }
    }

    public static void set(Object key, Object value, int seconds) {
        cache().set(CacheConstans.NUllRegion, key, value, seconds, true);
    }

    /**
     * set 缓存的数据
     * 使用region redis 默认使用hash
     * ehcache 使用指定的region缓存
     * 注意相同的 region只能设置一次 超时时间 ！(ehcache)
     * <p>
     * redis默认存的是hash 所以不能修改时间
     *
     * @param key
     * @param value
     */
    public static void set(String region, Object key, Object value, int seconds) {
        cache().set(region, key, value, seconds, true);
    }

    /**
     * set 1级缓存的数据
     * 使用region redis 默认使用hash
     * ehcache 使用指定的region缓存
     * 注意相同的 region只能设置一次 超时时间 ！(ehcache)
     * 但是不发送给其他节点来清空缓存
     *
     * @param key
     * @param value
     */
    public static void setn(String region, Object key, Object value, int seconds) {
        cache().set(region, key, value, seconds, false);
    }

    /**
     * set 1级缓存的数据
     * 使用region redis 默认使用hash
     * ehcache 使用指定的region缓存
     * 注意相同的 region只能设置一次 超时时间 ！(ehcache)
     *
     * @param region
     * @param key
     * @param value
     * @param seconds
     */
    public static void set1(String region, Object key, Object value, int seconds) {
        cache().set(LEVEL1, region, key, value, seconds, true);
    }

    public static void set1n(String region, Object key, Object value, int seconds) {
        cache().set(LEVEL1, region, key, value, seconds, false);
    }


    /**
     * 获取数据
     * 使用region
     *
     * @param region
     * @param key
     * @return
     */
    public static <T> T get(String region, Object key) {
        return (T) cache().get(region, key);
    }


    public static <T> T get(Object key) {
        return (T) cache().get(key);
    }

    public static <T> T get1(String region, Object key) {
        return (T) cache().get(LEVEL1, region, key);
    }


    public static <T> T get1(Object key) {
        return (T) cache().get(LEVEL1, key);
    }

    public static <T> T get2(String region, Object key) {
        return (T) cache().get(LEVEL2, region, key);
    }

    public static <T> T get2(Object key) {
        return (T) cache().get(LEVEL2, key);
    }


    public static void set(Object key, Object value) {
        cache().set(key, value, true);
    }


    public static void set(String region, Object key, Object value) {
        cache().set(region, key, value, true);
    }


    public static void batchSet(Map<?, ?> data) {

        cache().batchSet(LEVEL1, data);
        cache().batchSet(LEVEL2, data);
    }

    public static void batchSet(String region, Map<?, ?> data) {
        cache().batchSet(LEVEL1, region, data);
        cache().batchSet(LEVEL2, region, data);
    }

    public static void batchSet(String region, Map<?, ?> data, int seconds) {
        cache().batchSet(LEVEL1, region, data, seconds);
        cache().batchSet(LEVEL2, region, data, seconds);
    }

    public static void batchSet2(Map<?, ?> data) {
        cache().batchSet(LEVEL2, data);
    }

    public static void batchSet2(String region, Map<?, ?> data) {
        cache().batchSet(LEVEL2, region, data);
    }


    public static void batchSet2(String region, Map<?, ?> data, int seconds) {
        cache().batchSet(LEVEL2, region, data, seconds);
    }


    public static <T> List<T> batchGet1(String region) {

        return cache().batchGet(LEVEL1, region);
    }

    public static <T> List<T> batchGet2(String region) {
        return cache().batchGet(LEVEL2, region);
    }


    public static void setn(Object key, Object value) {
        cache().set(key, value, false);
    }


    public static void setn(String region, Object key, Object value) {
        cache().set(region, key, value, false);
    }


    public static void set1(Object key, Object value) {
        cache().set(key, value, true);
    }

    public static void set1n(Object key, Object value) {

        cache().set(key, value, false);
    }

    /**
     * set 1级缓存的数据
     * 使用region redis 默认使用hash
     * ehcache 使用指定的region缓存
     * 注意相同的 region只能设置一次 超时时间 ！(ehcache)
     *
     * @param region
     * @param key
     * @param value
     */
    public static void set1(String region, Object key, Object value) {
        cache().set(LEVEL1, region, key, value, true);
    }

    public static void set1n(String region, Object key, Object value) {
        cache().set(LEVEL1, region, key, value, false);
    }


    /**
     * set 2级缓存的数据
     * 使用region redis 默认使用hash
     * ehcache 使用指定的region缓存
     *
     * @param region
     * @param key
     * @param value
     */
    public static void set2(String region, Object key, Object value) {
        cache().set(LEVEL2, region, key, value, true);
    }

    public static void set2n(String region, Object key, Object value) {
        cache().set(LEVEL2, region, key, value, false);
    }


    public static void set2(String region, Object key, Object value, int sec) {
        cache().set(LEVEL2, region, key, value, sec, true);
    }


    public static void set2n(Object key, Object value) {
        cache().set(LEVEL2, CacheConstans.NUllRegion, key, value, false);
    }

    /**
     * set 2级缓存的数据
     * ehcache 使用指定的region缓存
     * 注意相同的 region只能设置一次 超时时间 ！(ehcache)
     *
     * @param key
     * @param value
     */
    public static void set2(Object key, Object value, int seconds) {
        cache().set(LEVEL2, CacheConstans.NUllRegion, key, value, seconds, true);
    }

    public static void set2n(Object key, Object value, int seconds) {
        cache().set(LEVEL2, CacheConstans.NUllRegion, key, value, seconds, false);
    }

    public static <T> List<T> keys1(String region) {
        return cache().keys(LEVEL1, region);
    }

    public static <T> List<T> keys2(String region) {
        return cache().keys(LEVEL2, region);
    }

    public static void remove(String region, Object key) {
        cache().remove(region, key);
    }

    public static void remove(Object key) {
        cache().remove(key);
    }

    public static void remove(String region, List<Object> keys) {
        cache().remove(region, keys);
    }

    public static void remove(List<Object> keys) {
        cache().remove(keys);
    }


    /**
     * 清空指定region所有缓存
     *
     * @param region
     */
    public static void clear(String region) {
        cache().clear(region);
    }


    /**
     * 释放缓存资源 关闭jvm的时候使用！！
     */
    public static void close() {
        cache().close();
    }
}
