package com.jayqqaa12.j2cache;

import com.jayqqaa12.j2cache.util.CacheException;

import java.util.List;

/**
 */
public interface Cache {


    /**
     * get all keys the of region
     *
     * @param region
     * @return
     */
    <T>  List<T>  keys(String region);

    /**
     * Get an item from the of
     *
     * @param region of region
     * @param key    of key
     * @return the cached object or null
     */
    Object get(String region, Object key) throws CacheException;

    /**
     * Add an item to the of
     * failfast semantics
     *
     * @param region
     * @param key     of key
     * @param value   of value
     * @param seconds of Expiration time
     */
    void set(String region, Object key, Object value, int seconds) throws CacheException;

   
    /**
     * @param key Cache key
     *            Remove an item from the of
     */
    void remove(String region, Object key) throws CacheException;

    /**
     * Clear the of
     */
    void clear(String region) throws CacheException;

    /**
     * update exprie time
     *
     * @param key
     * @param seconds
     */
    Object exprie(String region, Object key, int seconds);

}
