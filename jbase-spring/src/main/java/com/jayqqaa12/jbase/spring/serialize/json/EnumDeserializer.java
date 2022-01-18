package com.jayqqaa12.jbase.spring.serialize.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.jayqqaa12.jbase.spring.db.BaseModel;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;


/**
 * spring mvc 不能把字符串解析成 enum
 * 所以用这个转换一下字符串的
 */
@Slf4j
public class EnumDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        parser.parse();
        String input = parser.getInput();
        Integer value = JSON.parseObject(input)
                .getInteger((String) fieldName);

        if (value == null) return null;

        if (type == (BaseModel.Deleted.class)) {
            return (T) BaseModel.Deleted.valueOf(value);
        } else {
            return (T) BaseModel.Status.valueOf(value);
        }
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
