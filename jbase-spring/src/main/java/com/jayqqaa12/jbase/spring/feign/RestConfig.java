package com.jayqqaa12.jbase.spring.feign;

import com.alibaba.fastjson.JSON;
import com.jayqqaa12.jbase.spring.exception.BusinessException;
import com.jayqqaa12.jbase.spring.mvc.Resp;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


/**
 * 跟dubbo有冲突
 */
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
                    Resp req = JSON.parseObject(getResponseBody(response), Resp.class);
                    throw new BusinessException(req.getCode(),req.getMsg(),null);
                default:
                    throw new RestClientException("Unknown status code [" + statusCode + "]");
            }
        }
    }


}
