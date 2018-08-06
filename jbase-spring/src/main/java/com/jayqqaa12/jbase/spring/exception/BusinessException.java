package com.jayqqaa12.jbase.spring.exception;

import com.jayqqaa12.jbase.spring.mvc.RespCode;
import com.jayqqaa12.jbase.spring.mvc.i18n.LocaleKit;

/**
 * Created by 12 on 2017/2/24.
 */
public class BusinessException extends RuntimeException {

    protected int code;
    protected String msg;


    public BusinessException(int code) {
        this(code, LocaleKit.MSG_PREFIX + code, null);
    }

    public BusinessException(String msg) {
        this(RespCode.SERVER_ERROR, msg, null);
    }

    public BusinessException(int code, String msg) {
        this(code, msg, null);
    }

    public BusinessException(int code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {

        return LocaleKit.resolverOrGet(this.code, this.msg);
    }
}
