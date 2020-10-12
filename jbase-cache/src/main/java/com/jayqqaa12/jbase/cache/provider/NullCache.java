package com.jayqqaa12.jbase.cache.provider;

import com.jayqqaa12.jbase.cache.core.Cache;
import com.jayqqaa12.jbase.cache.core.CacheObject;
import com.jayqqaa12.jbase.cache.util.CacheException;


public class NullCache implements Cache {


    @Override
    public CacheObject get(String key) throws CacheException {
        return null;
    }

    @Override
    public void set(String key, CacheObject value) throws CacheException {

    }

    @Override
    public void set(String key, CacheObject value, int expire) throws CacheException {

    }


    @Override
    public void delete(String key) throws CacheException {

    }
}
