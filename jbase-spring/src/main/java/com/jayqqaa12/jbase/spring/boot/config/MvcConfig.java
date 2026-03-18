package com.jayqqaa12.jbase.spring.boot.config;

import com.jayqqaa12.jbase.spring.mvc.exception.CustomExceptionHandler;
import com.jayqqaa12.jbase.spring.mvc.exception.GlobalExceptionHandler;
import com.jayqqaa12.jbase.spring.mvc.i18n.LocaleKit;
import com.jayqqaa12.jbase.spring.mvc.inteceptor.HeaderLocaleChangeInterceptor;
import com.jayqqaa12.jbase.spring.mvc.inteceptor.PagingFilter;
import com.jayqqaa12.jbase.spring.mvc.inteceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.List;
import java.util.Locale;

@Configuration
@ConditionalOnClass(LocaleResolver.class)
public class MvcConfig implements WebMvcConfigurer {

  @Bean
  public CustomExceptionHandler customExceptionHandler() {
    return new CustomExceptionHandler();
  }

  @Bean
  public GlobalExceptionHandler globalExceptionHandler() {
    return new GlobalExceptionHandler();
  }


  @Bean
  @ConditionalOnMissingBean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver resolver = new CookieLocaleResolver();
    resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
    resolver.setCookieName("language");
    resolver.setCookieMaxAge(3600);
    return resolver;
  }


  @Bean
  @ConditionalOnMissingBean
  public LocaleKit localeKit(
      MessageSource messageSource, @Value("${config.lang:zh_CN}") Locale locale) {

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


  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins("*")
        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        .allowCredentials(false).maxAge(3600);
  }


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");

    registry.addInterceptor(new PagingFilter()).addPathPatterns("/**");
    registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**");
    registry.addInterceptor(interceptor).addPathPatterns("/**");
    registry.addInterceptor(new HeaderLocaleChangeInterceptor()).addPathPatterns("/**");

  }


}
