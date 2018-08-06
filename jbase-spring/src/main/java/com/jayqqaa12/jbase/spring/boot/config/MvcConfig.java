package com.jayqqaa12.jbase.spring.boot.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jayqqaa12.jbase.spring.boot.base.ConfigOrdered;
import com.jayqqaa12.jbase.spring.mvc.converter.FastJsonConverter;
import com.jayqqaa12.jbase.spring.mvc.converter.OrdinalToEnumConverterFactory;
import com.jayqqaa12.jbase.spring.mvc.handler.ApiVersionRequestMappingHandlerMapping;
import com.jayqqaa12.jbase.spring.mvc.handler.CustomExceptionHandler;
import com.jayqqaa12.jbase.spring.mvc.handler.GlobalExceptionHandler;
import com.jayqqaa12.jbase.spring.mvc.i18n.LocaleKit;
import com.jayqqaa12.jbase.spring.mvc.inteceptor.EffectInterceptor;
import com.jayqqaa12.jbase.spring.serialize.json.PageDeserializer;
import com.jayqqaa12.jbase.spring.serialize.json.PageableDeserializer;
import com.jayqqaa12.jbase.spring.serialize.json.SortDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.tuple.StringToDateConverter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.List;
import java.util.Locale;

@Configuration
@Order(ConfigOrdered.MVC)
public class MvcConfig extends WebMvcConfigurationSupport {

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverters() {

        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        ParserConfig.getGlobalInstance().putDeserializer(Pageable.class, new PageableDeserializer());
        ParserConfig.getGlobalInstance().putDeserializer(Page.class, new PageDeserializer());
        ParserConfig.getGlobalInstance().putDeserializer(Sort.class, new SortDeserializer());


        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.WriteEnumUsingName, false);

        FastJsonConverter fastConverter = new FastJsonConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.IgnoreErrorGetter,
                SerializerFeature.BrowserSecure,
                SerializerFeature.BrowserCompatible
        );

        fastConverter.setFastJsonConfig(fastJsonConfig);
        return fastConverter;
    }


    @Bean
    public CustomExceptionHandler customExceptionHandler() {
        return new CustomExceptionHandler();
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }


    @Bean
    public ApiVersionRequestMappingHandlerMapping handlerMapping() {
        return new ApiVersionRequestMappingHandlerMapping();
    }

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

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        resolver.setCookieName("language");
        resolver.setCookieMaxAge(3600);
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }


    @Bean
    @ConditionalOnBean(MessageSource.class)
    public LocaleKit localeKit(MessageSource messageSource, @Value("${config.lang:zh_CN}") Locale locale) {

        if (messageSource instanceof AbstractResourceBasedMessageSource) {
            AbstractResourceBasedMessageSource source = (AbstractResourceBasedMessageSource) messageSource;
            source.addBasenames("message_common");
        }

        LocaleKit kit = LocaleKit.of(messageSource);
        LocaleKit.setDefaultLocale(locale);
        return kit;
    }


    @Bean
    public FilterRegistrationBean hiddenHttpMethodFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new HiddenHttpMethodFilter());
        registration.addUrlPatterns("/");
        return registration;
    }


    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    }


//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("*")
//                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowCredentials(false).maxAge(3600);
//    }


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //以后改为权限控制 由api-gateway控制
//        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new EffectInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/**");
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.removeConvertible(String.class, Enum.class);
        registry.addConverterFactory(new OrdinalToEnumConverterFactory());
    }

}