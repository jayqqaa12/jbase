package com.jayqqaa12.jbase.spring.mvc.handler;


import com.jayqqaa12.jbase.spring.mvc.annotation.ApiVersion;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;


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

        if (!StringUtils.isEmpty(rangeVersion)) {

            String[] coms = tokenizeToStringArray(rangeVersion);

            for (String s : coms) {

                if (s.contains("..")) {

                    String[] range = s.split("\\.\\.");

                    if (range.length < 2) {
                        throw new IllegalArgumentException("api version[" + s + "] is not allow");
                    }

                    int start = StringUtils.isEmpty(range[0]) ? 1 : Integer.parseInt(range[0]);
                    int end = StringUtils.isEmpty(range[1]) ? Integer.MAX_VALUE : Integer.parseInt(range[1]);

                    compareVersion.addRange(start, end);
                } else {
                    compareVersion.addSpecial(Integer.parseInt(s));
                }


            }
        }

        return compareVersion;
    }


    public String[] tokenizeToStringArray(String str) {
        return tokenizeToStringArray(str, ";,", true, true);
    }

    public String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return new String[0];
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List tokens = new ArrayList();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    public String[] toStringArray(Collection<String> collection) {
        return collection == null ? null :  collection.toArray(new String[collection.size()]);
    }

}
