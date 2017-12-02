package com.jayqqaa12.jbase.spring.mvc.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by heavenick on 2016/5/10.
 */
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        String format="yyyy-MM-dd HH:mm:ss";
        if(source.length() == 10) {
            format = "yyyy-MM-dd";
        } else if(source.length() == "yyyy-MM-dd HH:mm:ss.SSS".length()) {
            format = "yyyy-MM-dd HH:mm:ss.SSS";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}