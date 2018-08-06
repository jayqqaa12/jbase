package com.jayqqaa12.jbase.spring.mvc.handler;


import com.jayqqaa12.jbase.spring.mvc.annotation.ApiVersion;
import com.jayqqaa12.jbase.spring.util.StringUtil;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;


public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {


    @Override
    protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {

        ApiVersion apiVersion = handlerType.getAnnotation(ApiVersion.class);

        return createApiVersionCondition(apiVersion);
    }


    @Override
    protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {

        ApiVersion apiVersion = method.getAnnotation(ApiVersion.class);

        return createApiVersionCondition(apiVersion);
    }

    private RequestCondition<ApiVersionCondition> createApiVersionCondition(ApiVersion apiVersion) {

        if (apiVersion == null) {
            return null;
        }

        ApiVersionCondition condition = new ApiVersionCondition(apiVersion.model(), apiVersion.value());

        condition.setCompatibly(getRangeVersion(apiVersion.compatibly()));
        condition.setIncompatibly(getRangeVersion(apiVersion.incompatible()));

        return condition;
    }

    private ApiVersionCondition.CompareVersion getRangeVersion(String rangeVersion) {

        ApiVersionCondition.CompareVersion compareVersion = new ApiVersionCondition.CompareVersion();

        if (StringUtil.isNotEmpty(rangeVersion)) {

            String[] coms = StringUtil.tokenizeToStringArray(rangeVersion);

            for (String s : coms) {

                if (s.contains("..")) {

                    String[] range = s.split("\\.\\.");

                    if (range.length < 2) {
                        throw new IllegalArgumentException("api version[" + s + "] is not allow");
                    }

                    int start = StringUtil.isEmpty(range[0]) ? 1 : Integer.parseInt(range[0]);
                    int end = StringUtil.isEmpty(range[1]) ? Integer.MAX_VALUE : Integer.parseInt(range[1]);

                    compareVersion.addRange(start, end);
                } else {
                    compareVersion.addSpecial(Integer.parseInt(s));
                }


            }
        }

        return compareVersion;
    }


}
