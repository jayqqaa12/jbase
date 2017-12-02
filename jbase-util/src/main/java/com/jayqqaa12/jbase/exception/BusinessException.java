package com.jayqqaa12.jbase.exception;

/**
 * Created by 12 on 2017/2/24.
 */
public class BusinessException extends RuntimeException {

    protected int code;
    protected String msg;


    public BusinessException(int code) {
        this.code = code;
    }

    public BusinessException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        if (msg != null) return msg;
        else return errorMsg();
    }

    protected String errorMsg() {

        return "";
    }
}
