package com.jayqqaa12.jbase.spring.mvc.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayqqaa12.jbase.spring.exception.BusinessException;
import com.jayqqaa12.jbase.spring.mvc.RespCode;
import feign.Util;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 为解决异常情况的适配器
 */
public class ResponseHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected boolean canRead(MediaType mediaType) {
        return true;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        String body = Util.toString(new InputStreamReader(inputMessage.getBody()));

        JSONObject obj = JSON.parseObject(body);
        Integer code=obj.getInteger("code");

        if (code!=null&&code != RespCode.SUCCESS) {
            throw new BusinessException(code ,obj.getString("msg"));
        }
        return JSON.parseObject(body,clazz);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {


    }


}
