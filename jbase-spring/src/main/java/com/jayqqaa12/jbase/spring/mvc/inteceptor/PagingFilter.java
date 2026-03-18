//  Copyright 2016 Leyantech Ltd. All Rights Reserved.

package com.jayqqaa12.jbase.spring.mvc.inteceptor;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class PagingFilter implements AsyncHandlerInterceptor {


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    String pageNum = request.getParameter("pageNum");
    String pageSize = request.getParameter("pageSize");

    MDC.put("pageNum", ObjectUtils.defaultIfNull(pageNum, "1"));
    MDC.put("pageSize", ObjectUtils.defaultIfNull(pageSize, "999999"));

    return true;
  }


}
