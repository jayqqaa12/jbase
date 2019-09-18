package com.jayqqaa12.jbase.spring.mvc.exception;

import com.jayqqaa12.jbase.spring.mvc.Resp;
import com.jayqqaa12.jbase.spring.mvc.RespCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (body == null) {
            switch (status) {
                case NOT_FOUND:
                    body = Resp.response(RespCode.RESOURCE_NOT_FOUND);
                    break; 
                case INTERNAL_SERVER_ERROR:
                    body = Resp.response(RespCode.SERVER_ERROR);
                    break;
                case METHOD_NOT_ALLOWED:

                    List<String> allows = headers.get(HttpHeaders.ALLOW);
                    if (allows.size() == 1 && "GET".equals(allows.get(0))) {
                        body = Resp.response(RespCode.REQ_METHOD_GET);
                    } else if (allows.size() == 1 && "POST".equals(allows.get(0))) {
                        body = Resp.response(RespCode.REQ_METHOD_POST);
                    } else if (allows.size() == 1 && "PUT".equals(allows.get(0))) {
                        body = Resp.response(RespCode.REQ_METHOD_PUT);
                    } else if (allows.size() == 1 && "DELETE".equals(allows.get(0))) {
                        body = Resp.response(RespCode.REQ_METHOD_DELETE);
                    } else {
                        body = Resp.response(RespCode.REQ_METHOD_NOT_ALLOWED);
                    }
                    break;
                case NOT_ACCEPTABLE:
                    body = Resp.response(RespCode.SERVER_ERROR);
                    break;
                case SERVICE_UNAVAILABLE:
                    body = Resp.response(RespCode.SERVER_ERROR);
                    break;
                case UNSUPPORTED_MEDIA_TYPE:
                    body = Resp.response(RespCode.REQ_MEDIA_UNSUPPORTED);
                    break;
            }
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
