package com.jayqqaa12.jbase.util;

import java.util.HashMap;
import java.util.Map;

public class Ret extends HashMap {

    public Ret() {
    }

    public static Ret of() {
        return new Ret();
    }

    public static Ret of(Object key, Object value) {
        return (new Ret()).put(key, value);
    }

    public Ret put(Object key, Object value) {
        this.put(key, value);
        return this;
    }

    public Ret put(Map map) {
        this.putAll(map);
        return this;
    }

    public Ret put(Ret ret) {
        this.putAll(ret);
        return this;
    }


    public boolean notNull(Object key) {
        return this.get(key) != null;
    }

    public boolean isNull(Object key) {
        return this.get(key) == null;
    }

    public boolean isTrue(Object key) {
        Object value = this.get(key);
        return value instanceof Boolean && ((Boolean)value).booleanValue();
    }

    public boolean isFalse(Object key) {
        Object value = this.get(key);
        return value instanceof Boolean && !((Boolean)value).booleanValue();
    }

    
}
