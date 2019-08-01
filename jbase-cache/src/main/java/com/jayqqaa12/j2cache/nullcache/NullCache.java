package com.jayqqaa12.j2cache.nullcache;

import com.jayqqaa12.j2cache.Cache;
import com.jayqqaa12.j2cache.util.CacheException;

import java.io.Serializable;
import java.util.List;


public class NullCache implements Cache {

    @Override
    public List<Object> keys(String region) {
        return null;
    }





    @Override
    public Object get(String region, Object key) throws CacheException {
        return null;
    }

    @Override
    public void set(String region, Object key, Object value, int seconds) throws CacheException {

    }

    
    public void remove(String region, Object key) throws CacheException {

    }


    public void clear(String region) throws CacheException {

    }

    @Override
    public Object exprie(String region, Object key, int seconds) {
        return null;
    }


    public Object exprie(String region, Serializable key, int seconds) {
        return null;
    }
}
