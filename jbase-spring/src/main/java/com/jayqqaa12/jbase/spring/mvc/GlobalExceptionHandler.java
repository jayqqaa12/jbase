//package com.jayqqaa12.jbase.spring.mvc;
//
//import org.songbai.lemi.basics.mvc.Response;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.List;
//
//import static org.songbai.lemi.basics.mvc.ResponseCode.*;
//
//
//@ControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//
//    @Override
//    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        if (body == null) {
//            switch (status) {
//                case NOT_FOUND:
//                    body = Response.response(RESOURCE_NOT_FOUND, "资源没有找到");
//                    break;
//                case BAD_REQUEST:
//                    body = Response.response(RESOURCE_ERROR, "请求错误");
//                    break;
//                case INTERNAL_SERVER_ERROR:
//                    body = Response.response(SERVER_ERROR, "服务器内部异常");
//                    break;
//                case METHOD_NOT_ALLOWED:
//
//                    List<String> allows = headers.get(HttpHeaders.ALLOW);
//                    if (allows.size() == 1 && "GET".equals(allows.get(0))) {
//                        body = Response.response(PARAM_METHOD_GET, "请求必须是GET请求");
//                    } else if (allows.size() == 1 && "POST".equals(allows.get(0))) {
//                        body = Response.response(PARAM_METHOD_POST, "请求必须是POST请求");
//                    } else if (allows.size() == 1 && "PUT".equals(allows.get(0))) {
//                        body = Response.response(PARAM_METHOD_PUT, "请求必须是PUT请求");
//                    } else if (allows.size() == 1 && "DELETE".equals(allows.get(0))) {
//                        body = Response.response(PARAM_METHOD_DELETE, "请求必须是DELETE请求");
//                    } else {
//                        body = Response.response(METHOD_NOT_ALLOWED, "方法不被允许");
//                    }
//                    break;
//                case NOT_ACCEPTABLE:
//                    body = Response.response(SERVER_ERROR, "服务器内部异常");
//                    break;
//                case SERVICE_UNAVAILABLE:
//                    body = Response.response(SERVER_ERROR, "服务器内部异常");
//                    break;
//                case UNSUPPORTED_MEDIA_TYPE:
//                    body = Response.response(UNSUPPORTED_MEDIA_TYPE, "不支持的媒体类型");
//                    break;
//            }
//        }
//
//        return super.handleExceptionInternal(ex, body, headers, status, request);
//    }
//}
