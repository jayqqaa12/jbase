package com.jayqqaa12.j2cache.redis;

import com.jayqqaa12.j2cache.Cache;
import com.jayqqaa12.j2cache.CacheProvider;
import com.jayqqaa12.j2cache.util.CacheException;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Redis 缓存管理，实现对多种 Redis 运行模式的支持和自动适配，实现连接池管理等
 *
 * @author Winter Lau (javayou@gmail.com)
 * @author wendal
 */
public class RedisCacheProvider implements CacheProvider {

    private final static Logger log = LoggerFactory.getLogger(RedisCacheProvider.class);
    private static RedisClient redisClient;
    private String namespace;
    private RedisCache cache;

    public String name() {
        return "redis";
    }

    public static RedisClient getClient() {
        if (redisClient == null) throw new CacheException("j2cache redis not init ");
        return redisClient;
    }

    @Override
    public void start(Properties props) {
        this.namespace = props.getProperty("cache.namespace","");
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            HashMap<String, String> props2 = new HashMap<>();
            props.forEach((k, v) -> props2.put((String) k, (String) v));
            BeanUtils.populate(poolConfig, props2);

            String hosts = props.getProperty("redis.hosts");
            String mode = props.getProperty("redis.mode");
            String cluster_name = props.getProperty("redis.cluster.name");
            String password = props.getProperty("redis.password");
            int database = Integer.parseInt(props.getProperty("redis.database"));
            this.redisClient = new RedisClient.Builder()
                    .mode(mode)
                    .hosts(hosts)
                    .password(password)
                    .cluster(cluster_name)
                    .database(database)
                    .poolConfig(poolConfig).newClient();

            log.info(String.format("Redis client starts with mode(%s), db(%d), namespace(%s)", mode, database, namespace));

            cache = new RedisCache(this.namespace, redisClient);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Failed to init redis client.", e);
        }
    }

    @Override
    public void stop() {
        try {
            redisClient.close();
        } catch (IOException e) {
            log.warn("Failed to close redis connection.", e);
        }
    }


    @Override
    public Cache buildCache(String regionName, boolean autoCreate) {

        return cache;
    }

}
