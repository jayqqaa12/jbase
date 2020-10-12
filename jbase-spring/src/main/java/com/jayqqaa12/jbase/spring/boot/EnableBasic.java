package com.jayqqaa12.jbase.spring.boot;


import com.jayqqaa12.jbase.spring.helper.ProfileHelper;
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
        ProfileHelper.class ,
//        ConfigHelper.class,
//        LangHelper.class,
})
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public @interface EnableBasic {

}
