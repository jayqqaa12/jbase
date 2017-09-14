package com.jayqqaa12.jbase.util;

import com.alibaba.fastjson.JSON;
import com.jayqqaa12.jbase.exception.ErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 返回的统一json格式
 * Created by 12 on 2017/3/24.
 */
public class Resp {
    public int code = 200;
    /**
     * 错误描述可写 可不写 可集中在 客户端 判断 code 得出
     */
    public String msg;

    public Map data = new HashMap();

    public static Resp of() {
        return new Resp();
    }

    public static Resp of(int code) {
        return new Resp();
    }


    public static Resp of(ErrorCode code) {
        return new Resp(code.code,code.msg);
    }

    public Resp(String key, List list) {
        setData(key, list);
    }
    private Resp() {
    }

    public Resp(int code) {
        this.code = code;
    }

    public Resp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String toJson() {
        if (data != null && data.size() == 0) data = null;
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String rst = JSON.toJSONString(this);
        return rst;
    }

    @Override
    public String toString() {
        return toJson();
    }

//    public Resp setData(String key, Model m) {
//        if (m == null) return this;
//        this.data.put(key, m.getAttrs());
//        return this;
//    }

    public Resp setData(String key, Object value) {
        if (value == null) return this;
        this.data.put(key, value);
        return this;
    }

    public Resp setData(String key, List list) {
        if (list == null) return this;
        data.put(key, list);
        return this;
    }

    public void setData(Map<Object, Object> data) {

        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            Object key = entry.getKey();
            Object o = entry.getValue();
            this.data.put(key, o);
        }

    }


}
