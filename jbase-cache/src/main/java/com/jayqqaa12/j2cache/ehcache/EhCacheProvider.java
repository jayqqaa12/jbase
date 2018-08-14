/**
 * Copyright 2014-2015 Oschina
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

import com.jayqqaa12.j2cache.CacheConstans;
import com.jayqqaa12.j2cache.CacheProvider;
import com.jayqqaa12.j2cache.util.CacheException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


public class EhCacheProvider implements CacheProvider {

    private final static Logger log = LoggerFactory.getLogger(EhCacheProvider.class);

    private CacheManager manager;
    private ConcurrentHashMap<String, EhCache> caches;

    @Override
    public String name() {
        return "EHCACHE";
    }


    public EhCache buildCache(String name, boolean isCreate) throws CacheException {
        if (name == null)
            name = CacheConstans.EHCACHE_DEFAULT_REGION;
        if (caches.size() > 100) {
            throw new CacheException("can't build more of greater than 100  check you code can't use diff region ");
        }


        EhCache ehcache = caches.get(name);

        if (ehcache == null) {
            if (!isCreate) return null;

            try {
                synchronized (caches) {
                    ehcache = caches.get(name);
                    if (ehcache == null) {
                        net.sf.ehcache.Cache cache = manager.getCache(name);
                        if (cache == null) {
                            cache = new Cache(name, 20_000, false, true, 0, 0);
                            manager.addCache(cache);
                            log.debug("started use EHCache region: " + name);
                        }
                        ehcache = new EhCache(cache);
                        caches.put(name, ehcache);
                    }
                }
            } catch (net.sf.ehcache.CacheException e) {
                throw new CacheException(e);
            }
        }
        return ehcache;
    }


    public void start(Properties properties) throws CacheException {
        System.setProperty("net.sf.ehcache.skipUpdateCheck", "true");
        if (manager != null) {
            log.warn("Attempt to restart an already started EhCacheProvider.");
            return;
        }
        manager = CacheManager.getInstance();

        this.caches = new ConcurrentHashMap<>();
    }


    public void stop() {
        if (manager != null) {
            manager.shutdown();
            caches.clear();
            manager = null;
        }
    }

}
