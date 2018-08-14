package com.jayqqaa12.j2cache;

import com.jayqqaa12.j2cache.util.CacheException;

import java.util.Properties;


public interface CacheProvider {

    String name();

    Cache buildCache(String regionName, boolean isCreate) throws CacheException;

    void start(Properties props) throws CacheException;

    void stop();

}
