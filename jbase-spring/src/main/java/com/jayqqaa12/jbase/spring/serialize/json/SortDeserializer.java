package com.jayqqaa12.jbase.spring.serialize.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Type;
import java.util.List;

public class SortDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        parser.parse();
        List<Sort.Order> list = JSONArray.parseArray(parser.getInput(), Sort.Order.class);
        return (T) new Sort(list);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
