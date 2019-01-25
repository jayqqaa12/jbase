package com.jayqqaa12.jbase.spring.mvc.inteceptor;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HeaderLocaleChangeInterceptor extends LocaleChangeInterceptor {

    public static final String DEFAULT_PARAM_NAME = "lang";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        String newLocale = request.getHeader(DEFAULT_PARAM_NAME);
        if (newLocale != null) {
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            if (localeResolver == null) {
                throw new IllegalStateException(
                        "No LocaleResolver found: not in a DispatcherServlet request?");
            }
            try {
                localeResolver.setLocale(request, response,  StringUtils.parseLocaleString(newLocale));
            } catch (IllegalArgumentException ex) {
            }
        }
        return true;
    }


}
