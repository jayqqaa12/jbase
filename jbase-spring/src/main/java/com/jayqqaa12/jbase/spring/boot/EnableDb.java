package com.jayqqaa12.jbase.spring.boot;


import com.jayqqaa12.jbase.spring.boot.base.FlywayStrategy;
import com.jayqqaa12.jbase.spring.boot.config.MybatisConfig;
import com.jayqqaa12.jbase.spring.boot.config.MybatisPlusConfig;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        MybatisPlusConfig.class,
        MybatisConfig.class,
        FlywayStrategy.class
})
@EnableTransactionManagement
public @interface EnableDb {

}
