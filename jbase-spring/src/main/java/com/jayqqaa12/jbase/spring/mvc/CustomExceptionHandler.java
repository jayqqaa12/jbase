//package com.jayqqaa12.jbase.spring.mvc;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.songbai.lemi.basics.exception.BusinessException;
//import org.songbai.lemi.basics.exception.RetryException;
//import org.songbai.lemi.basics.mvc.Response;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.sql.SQLException;
//
//
//@ControllerAdvice(annotations = {RestController.class, Controller.class})
//@ResponseBody
//public class CustomExceptionHandler {
//
//    protected final Log logger = LogFactory.getLog(CustomExceptionHandler.class);
//
//    /**
//     * 操作数据库出现异常
//     */
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(SQLException.class)
//    public Response handleException(SQLException e) {
//        logger.error("操作数据库出现异常:", e);
//        return Response.response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "数据库操作异常");
//    }
//
//    @ExceptionHandler(value = {BusinessException.class})
//    @ResponseStatus(HttpStatus.OK)
//
//    public final Response handleBusinessException(BusinessException ex) {
//        logger.error("业务异常", ex);
//
//        return Response.response(ex.getCode(), ex.getMessage());
//    }
//
//
//    @ExceptionHandler(value = {IllegalArgumentException.class})
//    @ResponseStatus(HttpStatus.OK)
//    public final Response handleArgumenteException(IllegalArgumentException ex) {
//        logger.error("请求参数异常", ex);
//        return Response.response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "请求参数异常 :" + ex.getMessage());
//    }
//
//
//    @ExceptionHandler(value = {RetryException.class})
//    @ResponseStatus(HttpStatus.OK)
//    public final Response handleRetryException(RetryException ex) {
//        logger.error("并发更新异常", ex);
//        return Response.response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "请求失败，请重试");
//    }
//
//    @ExceptionHandler(value = {Exception.class})
//    @ResponseStatus(HttpStatus.OK)
//    public final Response handleGeneralException(Exception ex) {
//        logger.error("其他异常", ex);
//
//        return Response.response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部异常");
//    }
//
//
//}
