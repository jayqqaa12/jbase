package com.jayqqaa12.jbase.spring.mvc;

import com.jayqqaa12.jbase.spring.mvc.i18n.LocaleKit;
import lombok.Data;


@Data
public class Resp {
    private int code;
    private String msg;
    private Object data;


    public  Resp(){
    }

    private Resp(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Resp response(int code) {
        return new Resp(code, LocaleKit.resolverOrGet(code, null), null);
    }

    public static Resp response(int code, String msg) {
        return new Resp(code, LocaleKit.resolverOrGet(code, msg), null);
    }

    public static Resp response(int code, String msg, Object data) {
        return new Resp(code, LocaleKit.resolverOrGet(code, msg), data);
    }


    public static Resp success() {
        return   Resp.response(RespCode.SUCCESS );
    }


    public static Resp error() {
        return   Resp.response(RespCode.SERVER_ERROR );
    }

    public static Resp error(String msg) {
        return new Resp(RespCode.SERVER_ERROR, LocaleKit.resolverOrGet(RespCode.SERVER_ERROR, msg), null);
    }




}
