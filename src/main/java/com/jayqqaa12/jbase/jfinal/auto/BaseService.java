/**
 * Copyright (c) 2011-2013, jayqqaa12 12shu (476335667@qq.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayqqaa12.jbase.jfinal.auto;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jayqqaa12.jbase.jfinal.ext.model.Model;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Page;


/***
 * 与业务相关写在service 层
 *
 * @author 12
 */
public class BaseService<M extends Model> {

    protected M dao;

    private static Map<Class<? extends BaseService>, BaseService> INSTANCE_MAP = new HashMap<>();

    protected BaseService setDao(M dao) {
        this.dao = dao;
        return this;
    }

    public BaseService() {

        Type genericSuperclass = getClass().getGenericSuperclass();
        try {
            Class<Model> 	clazz = (Class<Model>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
            dao = (M)Model.me(clazz);
        } catch (Exception e) {
        }

        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Class clazz = field.getType();
            if (Model.class.isAssignableFrom(clazz) && clazz != Model.class) {
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.set(this, Model.me(clazz));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException();
                }
            }
        }

    }


    public static <Ser extends BaseService> Ser me(Class<Ser> clazz) {

        Ser service = (Ser) INSTANCE_MAP.get(clazz);
        if (service == null) {
            try {
                synchronized (clazz) {
                    service = clazz.newInstance();
                    INSTANCE_MAP.put(clazz, service);
                }
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            }
        }


        return service;
    }

    public Page<M> findAll(int p, int c) {
        return dao.findAll(p, c);
    }

    public M findById(Object id) {
        return (M) dao.findById(id);
    }

    public M findByIdNotNull(Object id) {
        return (M) dao.findByIdNotNull(id);
    }

    public M findByIdCache(Object id) {
        return (M) dao.findByIdCache(id);
    }

    public M findByIdCacheNotNull(Object id) {
        return (M) dao.findByIdCacheNotNull(id);
    }


    public List<M> findAll() {
        return dao.findAll();
    }

    public boolean update(M m) {
        return m.update();
    }

    public boolean save(M m) {
        return m.save();
    }

    public boolean deleteAll() {
        return dao.deleteAll() > 0;
    }

    public boolean batchDelete(String ids) {
        return dao.batchDelete(ids);
    }

    public boolean deleteById(Object id) {
        return dao.deleteById(id);
    }

    public Long getAllCount() {
        return dao.getAllCount();
    }

    public Long getCount(String sql) {
        return dao.getCount(sql);
    }


    public List<M> findAllByCache() {
        return dao.findAllByCache();
    }


    protected String sql(String key) {
        return dao.sql(key);
    }

}
