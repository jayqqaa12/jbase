package com.jayqqaa12.jbase.spring.mvc.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by heavenick on 2016/5/10.
 */
public class DateToStringConverter implements Converter<Date, String> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String convert(Date source) {

        return dateFormat.format(source);
    }
}
