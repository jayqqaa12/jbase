package com.jayqqaa12.j2cache.util;

/**
 * 缓存相关的异常
 */
public class CacheException extends RuntimeException {

    private static final long serialVersionUID = -5112528854998647834L;

    public CacheException(String s) {
        super(s);
    }

    public CacheException(String s, Throwable e) {
        super(s, e);
    }

    public CacheException(Throwable e) {
        super(e);
    }

}
