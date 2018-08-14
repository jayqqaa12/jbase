package com.jayqqaa12.jbase.spring.mvc.handler;

import com.jayqqaa12.jbase.exception.LockException;
import com.jayqqaa12.jbase.spring.exception.BusinessException;
import com.jayqqaa12.jbase.spring.exception.RetryException;
import com.jayqqaa12.jbase.spring.mvc.Resp;
import com.jayqqaa12.jbase.spring.mvc.i18n.LocaleKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;


@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class CustomExceptionHandler {

    protected final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    /**
     * 操作数据库出现异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    public Resp handleException(SQLException e) {
        logger.error("操作数据库出现异常:", e);
        return Resp.response(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocaleKit.get("common.server.db.error"));
    }

    @ExceptionHandler(value = {BusinessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)

    public final Resp handleBusinessException(BusinessException ex) {
        logger.warn("业务异常 {}", serializeError(ex));

        return Resp.response(ex.getCode(), ex.getMessage());
    }


    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Resp handleArgumenteException(IllegalArgumentException ex) {
        logger.warn("请求参数异常 {}", serializeError(ex));
        return Resp.response(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocaleKit.resolverOrGet(ex.getMessage()));
    }


    @ExceptionHandler(value = {RetryException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Resp handleRetryException(RetryException ex) {
        logger.warn("并发更新异常 {}", serializeError(ex));
        return Resp.response(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocaleKit.get("common.req.retry"));
    }

    @ExceptionHandler(value = {LockException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Resp handleLockException(LockException ex) {
        logger.warn("幂等性异常 {}", serializeError(ex));
        return Resp.response(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocaleKit.get("common.req.fast"));
    }


    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Resp handleGeneralException(Exception ex) {
        logger.error("其他异常", ex);

        return Resp.response(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocaleKit.get("common.server.error"));
    }

    private String serializeError(Exception ex) {

        StringBuffer sb = new StringBuffer();

        sb.append(System.lineSeparator());

        sb.append("\t").append(ex.getClass().getName()).append(":").append(ex.getMessage()).append(System.lineSeparator());

        AtomicInteger index = new AtomicInteger();
        for (StackTraceElement element : ex.getStackTrace()) {


            sb.append("\t\tat ").append(element.getClassName()).append(".").append(element.getMethodName());

            if (element.getLineNumber() >= 0) {
                sb.append("(").append(element.getLineNumber()).append(")");
            }

            sb.append(System.lineSeparator());
            if (index.incrementAndGet() > 10) {
                break;
            }
        }

        return sb.toString();
    }


}
