package com.jayqqaa12.jbase.exception;

/**
 * Created by 12 on 2017/7/14.
 */
public class LockException extends  RuntimeException {

    public LockException(String s) {
        super(s);
    }

    public LockException(String s, Throwable e) {
        super(s, e);
    }

    public LockException(Throwable e) {
        super(e);
    }

}
