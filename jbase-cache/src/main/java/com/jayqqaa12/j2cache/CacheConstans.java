package com.jayqqaa12.j2cache;


/**
 * 统一用常量获取
 */
public class CacheConstans {

    public static final int LEVEL_ALL = 0;
    public final static int LEVEL1 = 1;
    public final static int LEVEL2 = 2;

    //配置文件名字
    public final static String CONFIG_FILE = "j2cache.properties";//可以不带.properties
    public final static String EHCACHE = "ehcache";
    public final static String REDIS = "redis";
    public final static String EHCACHE_DEFAULT_REGION = "__DEFAULT__";
    //默认存储时间
    public final static int DEFAULT_TIME = 0;
    //redis channel消息订阅默认频道
    public final static String REDIS_CHANNEL = "_DEFAULT_";
    //空的RGION
    public static String NUllRegion = null;


}
