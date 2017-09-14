package com.jayqqaa12.jbase.util;

import java.util.HashMap;
import java.util.Map;

public class Ret {
    private Map<Object, Object> data = new HashMap();

    public Ret() {
    }

    public static Ret create() {
        return new Ret();
    }

    public static Ret create(Object key, Object value) {
        return (new Ret()).put(key, value);
    }

    public Ret put(Object key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Ret put(Map map) {
        this.data.putAll(map);
        return this;
    }

    public Ret put(Ret ret) {
        this.data.putAll(ret.data);
        return this;
    }

    public <T> T get(Object key) {
        return (T) this.data.get(key);
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public int size() {
        return this.data.size();
    }

    public Ret clear() {
        this.data.clear();
        return this;
    }

    public boolean equals(Ret ret) {
        return ret != null && this.data.equals(ret.data);
    }

    public boolean containsKey(Object key) {
        return this.data.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.data.containsValue(value);
    }

    public boolean notNull(Object key) {
        return this.data.get(key) != null;
    }

    public boolean isNull(Object key) {
        return this.data.get(key) == null;
    }

    public boolean isTrue(Object key) {
        Object value = this.data.get(key);
        return value instanceof Boolean && ((Boolean)value).booleanValue();
    }

    public boolean isFalse(Object key) {
        Object value = this.data.get(key);
        return value instanceof Boolean && !((Boolean)value).booleanValue();
    }

    public <T> T remove(Object key) {
        return (T) this.data.remove(key);
    }

    public Map<Object, Object> getData() {
        return this.data;
    }
}
