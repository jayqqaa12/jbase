package com.jayqqaa12.jbase.spring.mvc.handler;

import com.jayqqaa12.jbase.spring.mvc.Resp;
import com.jayqqaa12.jbase.spring.mvc.RespCode;
import com.jayqqaa12.jbase.spring.mvc.i18n.LocaleKit;
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
                    body = Resp.response(RespCode.RESOURCE_NOT_FOUND, LocaleKit.get("common.resource.notFound"));
                    break;
                case BAD_REQUEST:
                    body = Resp.response(RespCode.RESOURCE_ERROR, LocaleKit.get("common.resource.error"));
                    break;
                case INTERNAL_SERVER_ERROR:
                    body = Resp.response(RespCode.SERVER_ERROR, LocaleKit.get("common.server.error"));
                    break;
                case METHOD_NOT_ALLOWED:

                    List<String> allows = headers.get(HttpHeaders.ALLOW);
                    if (allows.size() == 1 && "GET".equals(allows.get(0))) {
                        body = Resp.response(RespCode.REQ_METHOD_GET, LocaleKit.get("common.req.method.get"));
                    } else if (allows.size() == 1 && "POST".equals(allows.get(0))) {
                        body = Resp.response(RespCode.REQ_METHOD_POST, LocaleKit.get("common.req.method.post"));
                    } else if (allows.size() == 1 && "PUT".equals(allows.get(0))) {
                        body = Resp.response(RespCode.REQ_METHOD_PUT, LocaleKit.get("common.req.method.put"));
                    } else if (allows.size() == 1 && "DELETE".equals(allows.get(0))) {
                        body = Resp.response(RespCode.REQ_METHOD_DELETE, LocaleKit.get("common.req.method.delete"));
                    } else {
                        body = Resp.response(RespCode.REQ_METHOD_NOT_ALLOWED, LocaleKit.get("common.req.method.notAllow"));
                    }
                    break;
                case NOT_ACCEPTABLE:
                    body = Resp.response(RespCode.SERVER_ERROR, LocaleKit.get("common.server.error"));
                    break;
                case SERVICE_UNAVAILABLE:
                    body = Resp.response(RespCode.SERVER_ERROR, LocaleKit.get("common.server.error"));
                    break;
                case UNSUPPORTED_MEDIA_TYPE:
                    body = Resp.response(RespCode.REQ_MEDIA_UNSUPPORTED, LocaleKit.get("common.req.media.unsupported"));
                    break;
            }
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
