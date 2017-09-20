package com.jayqqaa12.jbase.util;

import com.alibaba.fastjson.JSON;
import com.jayqqaa12.jbase.exception.ErrorCode;

import java.util.Optional;

/**
 * 返回的统一json格式
 * Created by 12 on 2017/3/24.
 */
public class Resp<T> {
    private int code = 200;
    /**
     * 错误描述可写 可不写 可集中在 客户端 判断 code 得出
     */
    private String msg="ok";

    private T data;


    public static Resp of() {
        return new Resp();
    }

    public static Resp of(int code) {
        return new Resp(code);
    }

    public static Resp of(Object object) {
        return new Resp(object);
    }

    public static Resp of(Optional optional) {

        return new Resp(optional.get());
    }


    public static Resp of(ErrorCode code) {
        return new Resp(code.code, code.msg);
    }


    private Resp() {
    }

    public Resp(int code) {
        this.code = code;
    }

    public Resp(T data) {
        this.data = data;
    }

    public Resp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Resp(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String toJson() {
        String rst = JSON.toJSONString(this);
        return rst;
    }

    @Override
    public String toString() {
        return toJson();
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
