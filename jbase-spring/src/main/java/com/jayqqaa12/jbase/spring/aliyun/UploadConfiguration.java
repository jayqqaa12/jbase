package com.jayqqaa12.jbase.spring.aliyun;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UploadConfiguration {


    @Bean
    @ConditionalOnProperty({"config.aliyun.oss.accessKey", "config.aliyun.oss.url"})
    public AliyunOssHelper aliyunOssHelper() {
        return new AliyunOssHelper();
    }


    @Bean
    @ConditionalOnProperty({"config.aliyun.oss.accessKey", "config.aliyun.context.endpointHost"})
    public AuthenticationHelper authenticationHelper() {
        return new AuthenticationHelper();
    }


    @Bean
    @ConditionalOnBean(AliyunOssHelper.class)
    public ImageHelper imageHelper() {
        return new ImageHelper();
    }

}
