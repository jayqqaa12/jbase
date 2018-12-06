package com.jayqqaa12.jbase.spring.boot;


import com.jayqqaa12.jbase.spring.boot.base.FlywayStrategy;
import com.jayqqaa12.jbase.spring.boot.config.MybatisConfig;
import com.jayqqaa12.jbase.spring.boot.config.MybatisPlusConfig;
import com.jayqqaa12.jbase.spring.boot.config.RabbitConfig;
import com.jayqqaa12.jbase.spring.helper.ProfileHelper;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        MybatisPlusConfig.class,
        MybatisConfig.class,
        RabbitConfig.class,
        FlywayStrategy.class,
        ProfileHelper.class

})
@EnableAsync(proxyTargetClass = true)
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public @interface EnableBasic {

}
