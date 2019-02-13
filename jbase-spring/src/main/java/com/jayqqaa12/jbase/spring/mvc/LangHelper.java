package com.jayqqaa12.jbase.spring.mvc;

import com.jayqqaa12.j2cache.J2Cache;
import com.jayqqaa12.jbase.spring.mvc.i18n.LocaleKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * 多语言工具类
 *
 * @author: 12
 * @create: 2019-01-21 10:05
 **/
@Component
public class LangHelper {


    public static final String USER_LANG = "user:lang";

    
    @Autowired
    J2Cache j2Cache;


    private Locale getLocale(Long userId) {
        String lang = j2Cache.get(USER_LANG, userId);
        if (lang != null) return StringUtils.parseLocaleString(lang);
        return Locale.CHINA;
    }




    public String getMsg(String key, Long userId) {

        return LocaleKit.get(key, getLocale(userId));
    }


}
