package com.jayqqaa12.jbase.spring.exception;

import com.jayqqaa12.jbase.spring.mvc.i18n.LocaleKit;

public class ResolveMsgException extends BusinessException {


    private Object[] args;

    public ResolveMsgException(int code, Object... args) {
        super(code);
        this.args = args;
    }

    public ResolveMsgException(String msg, Object... args) {
        super(msg);
        this.args = args;
    }

    public ResolveMsgException(int code, String msg, Object... args) {
        super(code, msg);
        this.args = args;
    }

    public ResolveMsgException(int code, String msg, Throwable e, Object... args) {
        super(code, msg, e);
        this.code = code;
        this.msg = msg;
        this.args = args;
    }


    public Object[] getArgs() {
        return args;
    }

    @Override
    public String getMessage() {
        return LocaleKit.resolverOrGet(this.code, this.msg, this.args);
    }
}
