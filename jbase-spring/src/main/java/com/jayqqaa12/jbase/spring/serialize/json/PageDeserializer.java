package com.jayqqaa12.jbase.spring.serialize.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Type;

public class PageDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {

        JSONObject jsonObject = JSON.parseObject(parser.getInput());
        PageImpl<T> page = parser.parseObject(PageImpl.class);
        PageRequest request = new PageRequest(jsonObject.getInteger("number"), jsonObject.getInteger("size"));


        return (T) new PageImpl(page.getContent(), request, jsonObject.getInteger("totalElements"));
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}

