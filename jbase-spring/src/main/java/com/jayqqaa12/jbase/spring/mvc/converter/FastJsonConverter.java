package com.jayqqaa12.jbase.spring.mvc.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jayqqaa12.jbase.spring.mvc.inteceptor.EffectInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class FastJsonConverter extends FastJsonHttpMessageConverter {

    public FastJsonConverter() {
        setSupportedMediaTypes(Arrays.asList(
                new MediaType[]{MediaType.APPLICATION_JSON,
                new MediaType("application", "*+json")}
        ));
    }

    @Override
    public void writeInternal(Object obj, HttpOutputMessage outputMessage)
            throws IOException {
        String text = "";
        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
            text += obj;
        } else {
            text = JSON.toJSONString(obj, getFeatures());
        }

        String call = EffectInterceptor.callBack.get();
        if (StringUtils.isNotBlank(call)) {

            if (obj instanceof String) {
                text = call + "(\"" + text + "\")";
            } else {
                text = call + "(" + text + ")";
            }
        }
        OutputStream out = outputMessage.getBody();
        byte[] bytes = text.getBytes(getCharset());
        out.write(bytes);
    }
}
