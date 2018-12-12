package com.jayqqaa12.jbase.spring.boot.base;

import org.springframework.amqp.support.converter.DefaultClassMapper;

public class FastAmqpClassMapper extends DefaultClassMapper {
    /**
     * 构造函数初始化信任所有pakcage
     */
    public FastAmqpClassMapper() {
        super();
        setTrustedPackages("*");
    }
}
