package com.jayqqaa12.jbase.spring.exception;

/**
 * Created by 12 on 2017/8/22.
 */
public class RetryException extends  RuntimeException {
    protected String msg;
    public RetryException(String  msg) {
        super(msg);
        this.msg =msg;
    }

    public RetryException() {
    }
}
