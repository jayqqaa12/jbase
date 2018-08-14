//$Id: EhCache.java 10716 2006-11-03 19:05:11Z max.andersen@jboss.com $
/**
 * Copyright 2003-2006 Greg Luck, Jboss Inc
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayqqaa12.j2cache.ehcache;

import com.jayqqaa12.j2cache.Cache;
import com.jayqqaa12.j2cache.CacheConstans;
import com.jayqqaa12.j2cache.util.CacheException;
import net.sf.ehcache.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EhCache implements Cache {

    private net.sf.ehcache.Cache cache;

    EhCache(net.sf.ehcache.Cache cache) {
        this.cache = cache;
    }


    @Override
    public List<Object> keys(String region) {
        if (region == null) return new ArrayList<>();
        net.sf.ehcache.Cache ehcache = getCache(region);
        return ehcache.getKeys();
    }

    @Override
    public Object get(String region, Object key) throws CacheException {
        try {
            if (key == null)
                return null;
            else {
                if (region == null)
                    region = CacheConstans.EHCACHE_DEFAULT_REGION;

                net.sf.ehcache.Cache ehcache = getCache(region);
                Element element = ehcache.get(key);

                if (element != null)
                    return element.getObjectValue();
            }
            return null;
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }


    @Override
    public void set(String region, Object key, Object value, int seconds) throws CacheException {
        try {

            if (region == null)
                region = CacheConstans.EHCACHE_DEFAULT_REGION;
            if (value == null)
                remove(region, key);
            else {
                net.sf.ehcache.Cache ehcache = getCache(region);
                Element element = new Element(key, value);
                if (seconds > 0) element.setTimeToLive(seconds);
                ehcache.put(element);

            }

        } catch (IllegalArgumentException e) {
            throw new CacheException(e);
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }

    /**
     * ehcache 批量写入跟平常的一样
     * @param region
     * @param data
     * @param seconds
     * @throws CacheException
     */
    @Override
    public void batchSet(String region, Map<?, ?> data, int seconds) throws CacheException {

        data.forEach((k,v)->{
            set(region,k,v,seconds);
        });
    }

    @Override
    public <T> List<T> batchGet(String region) throws CacheException {
        List list = new ArrayList();
        getCache(region).getAll(keys(region)).forEach((k, v) -> {
            if (v != null) list.add(v.getObjectValue());
        });
        return list;
    }


    @Override
    public void remove(String region, Object key) throws CacheException {
        if (region == null)
            region = CacheConstans.EHCACHE_DEFAULT_REGION;
        if (key instanceof List) {
            List keys = (List) key;
            net.sf.ehcache.Cache ehcache = getCache(region);
            ehcache.removeAll(keys);
        } else {
            try {
                net.sf.ehcache.Cache ehcache = getCache(region);
                ehcache.remove(key);
            } catch (IllegalStateException e) {
                throw new CacheException(e);
            } catch (net.sf.ehcache.CacheException e) {
                throw new CacheException(e);
            }
        }

    }

    @Override
    public void clear(String region) throws CacheException {
        try {
            if (region == null)
                region = CacheConstans.EHCACHE_DEFAULT_REGION;
            net.sf.ehcache.Cache ehcache = getCache(region);
            ehcache.removeAll();
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }


    /**
     * update exprie time
     * ehcache不能更新时间，直接返回值
     *
     * @param key
     * @param seconds
     */
    @Override
    public Object exprie(String region, Object key, int seconds) {
        return get(region, key);
    }


    private net.sf.ehcache.Cache getCache(String region) {
        return this.cache;
    }
}