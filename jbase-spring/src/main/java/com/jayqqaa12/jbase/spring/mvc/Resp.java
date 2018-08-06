package com.jayqqaa12.jbase.spring.mvc;

import com.jayqqaa12.jbase.spring.mvc.i18n.LocaleKit;


public class Resp {
    protected int code;
    protected String msg;

    private Resp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static Resp response(int code) {
        return new Resp(code, LocaleKit.resolverOrGet(code, null));
    }

    public static Resp response(int code, String msg) {
        return new Resp(code, LocaleKit.resolverOrGet(code, msg));
    }



    public static <E> DataResponse<E> response(int code, String msg, E data) {
        return new DataResponse<E>(code, LocaleKit.resolverOrGet(code, msg), data);
    }


    public static Resp success() {
        return new Resp(RespCode.SUCCESS, LocaleKit.get("common.success"));
    }

    public static <E> DataResponse<E> success(E data) {
        return new DataResponse<E>(RespCode.SUCCESS, LocaleKit.get("common.success"), data);
    }



    public static Resp error() {
        return new Resp(RespCode.SERVER_ERROR, LocaleKit.get("common.fail"));
    }

    public static Resp error(String msg) {
        return new Resp(RespCode.SERVER_ERROR, LocaleKit.resolverOrGet(RespCode.SERVER_ERROR, msg));
    }

    public static <E> DataResponse<E> error(E data) {
        return new DataResponse<E>(RespCode.SERVER_ERROR, LocaleKit.get("common.fail"), data);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public static class DataResponse<T> extends Resp {
        T data;

        public DataResponse(int code, String msg, T data) {
            super(code, msg);
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }



}
