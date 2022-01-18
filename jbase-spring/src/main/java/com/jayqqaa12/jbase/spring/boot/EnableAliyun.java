package com.jayqqaa12.jbase.spring.boot;


import com.jayqqaa12.jbase.spring.util.UploadConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({UploadConfiguration.class})
public @interface EnableAliyun {

}
