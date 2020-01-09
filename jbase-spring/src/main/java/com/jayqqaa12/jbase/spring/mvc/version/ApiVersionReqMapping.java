package com.jayqqaa12.jbase.spring.mvc.version;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @author: 12
 * @create: 2019-09-18 13:17
 **/
@Component
public class ApiVersionReqMapping extends RequestMappingHandlerMapping {


    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        ApiVersion v = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        if (v == null)
            return null;
        return new ApiVersionRequestCondition(new ApiVersionRequestCondition.ApiVersionExpression(v));
    }

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        ApiVersion v = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        if (v == null)
            return null;
        return new ApiVersionRequestCondition(new ApiVersionRequestCondition.ApiVersionExpression(v));
    }

}
