package com.jayqqaa12.jbase.spring.boot.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayqqaa12.jbase.spring.exception.BusinessException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Configurable
public class RestConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseError());
        return restTemplate;
    }



    public class CustomResponseError extends DefaultResponseErrorHandler {
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            HttpStatus statusCode = getHttpStatusCode(response);
            switch (statusCode.series()) {
                case CLIENT_ERROR:
                    throw new HttpClientErrorException(statusCode, response.getStatusText(),
                            response.getHeaders(), getResponseBody(response), getCharset(response));
                case SERVER_ERROR:
                    JSONObject jsonObject = JSON.parseObject(new String(getResponseBody(response)));
                    throw new BusinessException(jsonObject.getInteger("code"));
                default:
                    throw new RestClientException("Unknown status code [" + statusCode + "]");
            }
        }
    }


}
