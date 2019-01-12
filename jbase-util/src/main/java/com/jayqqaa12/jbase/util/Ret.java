package com.jayqqaa12.jbase.util;

import java.util.HashMap;

public class Ret extends HashMap {

    public static Ret of() {
        return new Ret();
    }

    public static Ret of(Object key, Object value) {
        Ret ret = new Ret();
        ret.put(key, value);
        return ret;
    }


    public boolean notNull(Object key) {
        return this.get(key) != null;
    }

    public boolean isNull(Object key) {
        return this.get(key) == null;
    }

    public boolean isTrue(Object key) {
        Object value = this.get(key);
        return value instanceof Boolean && ((Boolean) value).booleanValue();
    }

    public boolean isFalse(Object key) {
        Object value = this.get(key);
        return value instanceof Boolean && !((Boolean) value).booleanValue();
    }


}
