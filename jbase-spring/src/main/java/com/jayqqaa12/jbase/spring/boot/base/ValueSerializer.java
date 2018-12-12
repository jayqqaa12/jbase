package com.jayqqaa12.jbase.spring.boot.base;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;


/**
 * 发现fastjson 有个问题
 *
 * 如果设置为 writeclassname的时候
 *
 * Float Long等对象会带 F,L 后缀，导致json格式不对
 * 
 *
 */
public class ValueSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {


        if (object instanceof Float) {
            Float value = (Float) object;
            if (value == null) serializer.write(null);
            else  serializer.write(value.toString());
        }

        if (object instanceof Long) {
            Long value = (Long) object;
            if (value == null) serializer.write(null);
           else serializer.write(value.toString());
        }

        if (object instanceof Double) {
            Double value = (Double) object;
            if (value == null) serializer.write(null);
            else serializer.write(value.toString());
        }



    }
}
