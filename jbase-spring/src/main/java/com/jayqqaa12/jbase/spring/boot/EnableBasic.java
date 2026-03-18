package com.jayqqaa12.jbase.spring.boot;


import com.jayqqaa12.jbase.spring.boot.config.KafkaConfig;
import com.jayqqaa12.jbase.spring.boot.config.MybatisConfig;
import com.jayqqaa12.jbase.spring.boot.config.MybatisPlusConfig;
import com.jayqqaa12.jbase.spring.boot.config.RedisConfig;
import com.jayqqaa12.jbase.spring.boot.config.RedisLockConfig;
import com.jayqqaa12.jbase.spring.mapstruct.BaseMapStructStrategy;
import com.jayqqaa12.jbase.spring.task.TaskManger;
import com.jayqqaa12.jbase.spring.util.ProfileHelper;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        ProfileHelper.class,
        RedisConfig.class,
        MybatisConfig.class,
        MybatisPlusConfig.class,
        KafkaConfig.class,
        RedisLockConfig.class,
        TaskManger.class,
        BaseMapStructStrategy.class,
})
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public @interface EnableBasic {

}
