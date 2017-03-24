package com.jayqqaa12.jbase.web.spring.exception;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SpringMvcExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setStatus(200);
        response.setHeader("Content-Type", "text/model;charset=utf-8");
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
        }
//
//        Response resp = Response.response(ResponseCode.SERVER_ERROR, "服务器内部异常");
//        if (ex instanceof BusinessException) {
//            int code = ((BusinessException) ex).getCode();
//            String msg = ex.getMessage();
//            resp = Response.response(code, msg);
//        } else if (ex instanceof IllegalArgumentException) {
//            resp = Response.response(ResponseCode.PARAM_ERROR, "请求参数异常");
//            logger.error("user request " + request.getRequestURI() + " error!", ex);
//        } else if (ex instanceof SQLException) {
//            resp = Response.response(ResponseCode.SERVER_DB_ERROR, "数据库操作异常");
//            logger.error("user request " + request.getRequestURI() + " error!", ex);
//        } else if (ex instanceof org.springframework.validation.BindException || ex instanceof PropertyAccessException) {
//            resp = Response.response(ResponseCode.PARAM_ERROR, "请求参数异常");
//            logger.error("user request " + request.getRequestURI() + " error!", ex);
//        } else {
//            logger.error("user request " + request.getRequestURI() + " error!", ex);
//        }

//        try {
//            response.getWriter().print(JSON.toJSONString(resp));
//            response.flushBuffer();
//        } catch (IOException e) {
//        }

        return null;
    }


}
