package com.jayqqaa12.jbase.spring.boot;


import com.jayqqaa12.jbase.spring.boot.zookeeper.properties.PropertiesConfiguration;
import com.jayqqaa12.jbase.spring.boot.zookeeper.ZookeeperConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ZookeeperConfiguration.class,
        PropertiesConfiguration.class
})
public @interface EnableConfig {
}
