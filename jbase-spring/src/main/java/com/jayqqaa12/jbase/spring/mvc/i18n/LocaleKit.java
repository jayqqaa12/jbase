package com.jayqqaa12.jbase.spring.mvc.i18n;

import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.regex.Pattern;

public class LocaleKit {

    public static final String MSG_PREFIX = "msg.";

    private static final String PREFIX = PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX;
    private static final String SUFFIX = PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX;

    private static PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper(PREFIX, SUFFIX);
    private static Pattern PATTERN = Pattern.compile("[\\w]+([\\.][\\w\\d]+)+");
    private static Locale DEFAULT_LOCALE = Locale.CHINA;
    private static final String[] PARAM_HOLDER = new String[]{"", "", "", "", "", ""};

    private static MessageSource messageSource;


    public static LocaleKit of(MessageSource messageSource) {
        LocaleKit.messageSource = messageSource;
        return new LocaleKit();
    }


    public static String resolverOrGet(String message, Object... args) {

        if (PATTERN.matcher(message).matches()) {
            return get(message, args);
        } else {
            return resolver(message, args);
        }
    }

    public static String resolverOrGet(int code, String message, Object... args) {

        if (StringUtils.isEmpty(message)) {
            return resolverOrGet(MSG_PREFIX + code, args);
        } else {
            return resolverOrGet(message, args);
        }
    }


    public static String resolver(String message, Object... args) {

        final Locale locale = getLocale();

        return placeholderHelper.replacePlaceholders(message, s -> internalResolver(s, locale, args));
    }

    public static String get(String code, Object... args) {

        return internalResolver(code, getLocale(), args);
    }


    private static String internalResolver(String code, Locale locale, Object... args) {

        if (messageSource == null) {
            return null;
        }

        if (args == null || args.length == 0) {
            args = PARAM_HOLDER;
        }

        String msg = code;
        try {
            msg = messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            //Ignore
        }

        return msg;
    }


    public static Locale getLocale() {
        Locale locale = LocaleContextHolder.getLocale();

        return locale == null ? DEFAULT_LOCALE : locale;
    }

    public static Locale getLocale(HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);

        return locale == null ? DEFAULT_LOCALE : locale;
    }

    public static void setDefaultLocale(Locale locale){
        DEFAULT_LOCALE = locale;
    }
}
