// Copyright 2021 Leyantech Ltd. All Rights Reserved.

package com.jayqqaa12.jbase.spring.mvc.resp;

import com.jayqqaa12.jbase.spring.mvc.Resp;
import com.jayqqaa12.jbase.spring.mvc.resp.page.IPageData;
import com.jayqqaa12.jbase.spring.mvc.resp.page.PageData;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 12
 */
@Configuration
public class RespConfig {

  @Bean
  @ConditionalOnMissingBean
  public RespMode respMode() {
    return RespMode.NO_PACKING;
  }


  @Bean
  @ConditionalOnMissingBean
  public IPageData pageData(){
    return new PageData();
  };


  @Bean
  @ConditionalOnMissingBean
  public Resp resp() {
    return new Resp();
  }


}
