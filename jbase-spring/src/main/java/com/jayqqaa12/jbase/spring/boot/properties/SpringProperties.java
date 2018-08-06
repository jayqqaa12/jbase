package com.jayqqaa12.jbase.spring.boot.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * Created by yhj on 17/3/24.
 */
public class SpringProperties extends PropertySourcesPlaceholderConfigurer {

    private final Logger logger = LoggerFactory.getLogger(SpringProperties.class);

    private volatile static SpringProperties instance = new SpringProperties();

    private BeanFactory beanFactory;
    private Environment environment;

    private SpringProperties() {
        //Ingnore
    }

    public static SpringProperties instance() {

        return instance;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        this.beanFactory = beanFactory;

    }

    @Override
    public void setEnvironment(Environment environment) {
        super.setEnvironment(environment);
        this.environment = environment;

    }

    public String getProperty(String key) {

        return environment.getProperty(key);
    }

    public String getString(String key) {

        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        String value = getProperty(key);

        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    public Integer getInteger(String key) {

        return getInteger(key, null);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        String value = getProperty(key);

        return StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
    }

    public Long getLong(String key) {
        return getLong(key, null);
    }

    public Long getLong(String key, Long defaultValue) {
        String value = getProperty(key);

        return StringUtils.isEmpty(value) ? defaultValue : Long.parseLong(value);
    }


    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    public Double getDouble(String key, Double defaultValue) {

        String value = getProperty(key);

        return StringUtils.isEmpty(value) ? defaultValue : Double.parseDouble(value);
    }


    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }
}
