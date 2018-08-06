package com.jayqqaa12.jbase.spring.serialize.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Type;

/**
 * PageRequest 的解析器
 *
 */
public class PageableDeserializer implements ObjectDeserializer {


    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONObject obj = (JSONObject) parser.parse();

        Sort sort=null;

        if(obj.getString("sort")!=null){
            sort= JSON.parseObject(obj.getString("sort"),Sort.class);
        }

        return (T) new PageRequest(obj.getInteger("pageNumber"), obj.getInteger("pageSize"),sort);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
