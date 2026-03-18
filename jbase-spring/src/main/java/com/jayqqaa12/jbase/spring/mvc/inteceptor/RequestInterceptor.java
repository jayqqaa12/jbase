package com.jayqqaa12.jbase.spring.mvc.inteceptor;


import com.alibaba.fastjson2.JSONObject;
import org.springframework.aop.support.AopUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class RequestInterceptor implements AsyncHandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {

    if (handler instanceof HandlerMethod) {
      StringBuilder sb = new StringBuilder(1000);
      //获取请求参数
      Enumeration em = request.getParameterNames();
      JSONObject data = new JSONObject();
      while (em.hasMoreElements()) {
        String name = (String) em.nextElement();
        String value = request.getParameter(name);
        data.put(name, value);
      }

      HandlerMethod h = (HandlerMethod) handler;
      sb.append("-------------------------------------------------------------\n");
      sb.append("Controller: ").append(AopUtils.getTargetClass(h.getBean()).getSimpleName())
          .append("\n");
      sb.append("Method    : ").append(h.getMethod().getName()).append("\n");
      sb.append("Params    : ").append(data).append("\n");
      sb.append("URI       : ").append(request.getRequestURI()).append("\n");
      sb.append("HTTPMethod: ").append(request.getMethod()).append("\n");
      sb.append("URL       : ").append(request.getRequestURL()).append("\n");
      sb.append("-------------------------------------------------------------\n");

    }

    return true;
  }


}
