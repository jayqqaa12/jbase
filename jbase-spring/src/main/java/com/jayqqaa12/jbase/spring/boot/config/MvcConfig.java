package com.jayqqaa12.jbase.spring.boot.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jayqqaa12.jbase.spring.mvc.interceptor.EffectInteceptor;
import com.jayqqaa12.jbase.spring.mvc.converter.StringToDateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;


@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverters() {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastConverter.setFastJsonConfig(fastJsonConfig);

        return fastConverter;
    }


//    @Bean
//    public CustomExceptionHandler customExceptionHandler() {
//        return new CustomExceptionHandler();
//    }
//
//    @Bean
//    public GlobalExceptionHandler globalExceptionHandler() {
//        return new GlobalExceptionHandler();
//    }


//    @Bean
//    public ApiVersionRequestMappingHandlerMapping handlerMapping() {
//        return new ApiVersionRequestMappingHandlerMapping();
//    }

//    @Bean
//    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
//        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
//        registration.addUrlMappings("/");
//        return registration;
//    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter);

        converters.add(fastJsonHttpMessageConverters());
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new EffectInteceptor()).addPathPatterns("/**");
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDateConverter());
    }


}