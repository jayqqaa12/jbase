package com.jayqqaa12.jbase.spring.boot.config;

import com.jayqqaa12.jbase.spring.feign.FeignReqInterceptor;
import com.jayqqaa12.jbase.spring.feign.ResponseHttpMessageConverter;
import com.jayqqaa12.jbase.spring.feign.CustomErrorDecoder;
import feign.*;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by 12 on 2017/12/2.
 */
@Configuration
@ConditionalOnClass({Feign.class})
public class FiegnConfig {

  @Bean
  public ErrorDecoder errorDecoder() {
    return new CustomErrorDecoder();
  }
  @Bean
  public Decoder feignDecoder() {
    return new ResponseEntityDecoder(
        new SpringDecoder(() -> new HttpMessageConverters(new ResponseHttpMessageConverter())));
  }
  //feign请求把 header带上
  @Bean
  public RequestInterceptor headerInterceptor() {
    return new FeignReqInterceptor();
  }


}
