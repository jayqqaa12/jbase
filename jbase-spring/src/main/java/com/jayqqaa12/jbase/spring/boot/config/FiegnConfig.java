package com.jayqqaa12.jbase.spring.boot.config;

import com.alibaba.fastjson.JSON;
import com.jayqqaa12.jbase.spring.exception.BusinessException;
import com.jayqqaa12.jbase.spring.mvc.Resp;
import com.jayqqaa12.jbase.spring.mvc.RespCode;
import com.jayqqaa12.jbase.spring.mvc.converter.ResponseHttpMessageConverter;
import feign.Feign;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.hystrix.HystrixFeign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Created by 12 on 2017/12/2.
 */
@Configuration
@ConditionalOnClass({Feign.class, HystrixFeign.class})
public class FiegnConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }


    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(() -> new HttpMessageConverters(new ResponseHttpMessageConverter())));
    }


    public class CustomErrorDecoder implements ErrorDecoder {

        public Exception decode(String s, Response response) {
            try {
                if (response.body() != null) {
                    String body = Util.toString(response.body().asReader());
                    Resp req = JSON.parseObject(body, Resp.class);
                    if (req.getCode() != RespCode.SUCCESS) {
                        return new BusinessException(req.getCode());
                    }
                }
            } catch (IOException e) {
                return e;
            }
            return new Exception("UNKOWN ERROR");
        }
    }


}
