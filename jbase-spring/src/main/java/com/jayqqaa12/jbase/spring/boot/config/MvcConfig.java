package com.jayqqaa12.jbase.spring.boot.config;

import com.jayqqaa12.jbase.spring.mvc.converter.OrdinalToEnumConverterFactory;
import com.jayqqaa12.jbase.spring.mvc.exception.CustomExceptionHandler;
import com.jayqqaa12.jbase.spring.mvc.exception.GlobalExceptionHandler;
import com.jayqqaa12.jbase.spring.mvc.i18n.LocaleKit;
import com.jayqqaa12.jbase.spring.mvc.inteceptor.EffectInterceptor;
import com.jayqqaa12.jbase.spring.mvc.inteceptor.HeaderLocaleChangeInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.List;
import java.util.Locale;

@Configuration
public class MvcConfig  implements WebMvcConfigurer {


    //fastjson 出现不兼容的情况
//    @Bean
//    public FastJsonHttpMessageConverter fastJsonHttpMessageConverters() {
//
//        return new FastJsonConverter();
//    }
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//
//        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter);
//
//        converters.add(fastJsonHttpMessageConverters());
//    }


    @Bean
    public CustomExceptionHandler customExceptionHandler() {
        return new CustomExceptionHandler();
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
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
    public HeaderLocaleChangeInterceptor headerLocaleChangeInterceptor(){
        return new HeaderLocaleChangeInterceptor();
    }

    @Bean
    public LocaleKit localeKit(MessageSource messageSource, @Value("${config.lang:zh_CN}") Locale locale) {

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


    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    }


//    @Bean
//    public HttpPutFormContentFilter putFilter() {
//        return new HttpPutFormContentFilter();
//    }


//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("*")
//                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowCredentials(false).maxAge(3600);
//    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //以后改为权限控制 由api-gateway控制
//        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new EffectInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(headerLocaleChangeInterceptor()).addPathPatterns("/**");

    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.removeConvertible(String.class, Enum.class);
        registry.addConverterFactory(new OrdinalToEnumConverterFactory());
    }

}