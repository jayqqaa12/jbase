package com.jayqqaa12.jbase.web.spring.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理所有注解过的拦截器
 * <p>
 * by 12
 */
public abstract class ValidateInterceptor extends HandlerInterceptorAdapter {



    public String success;
    public String failed;

    private  HttpServletRequest request;
    private  HttpServletResponse response;



    public boolean isHandler(Object handler) {
        if (!(handler instanceof HandlerMethod))
            return false;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Interceptor interceptor = handlerMethod.getMethodAnnotation(Interceptor.class);
        if (interceptor == null)
            return false;

        for (Class clazz : interceptor.value()) {
            if (clazz.getSimpleName().equals(this.getClass().getSimpleName()) && clazz.equals(this.getClass().getName()))
                return true;
        }

        success = interceptor.success();
        failed = interceptor.failed();
        return true;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.request=request;
        this.response=response;

        if (isHandler(handler)) {
            return handler(response, interceptor(request, response));
        }
        return super.preHandle(request, response, handler);
    }

    public abstract boolean interceptor(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 运行结果
     *
     * @param response
     * @param isInterceptor 是否拦截,true拦截,跳转向failed指向页面,false:不拦截,跳转向success指向页面;
     *                      success和failed为空时不做任何操作
     * @return
     * @throws Exception
     */
    public boolean handler(HttpServletResponse response, boolean isInterceptor) throws Exception {
        if (!isInterceptor) {
            if (!success.equals("")) {
                response.sendRedirect(success);
                return false;
            }
        } else {
            if (!failed.equals("")) {
                response.sendRedirect(failed);
                return false;
            }
        }
        return true;
    }


    //FIXME 常用的验证方法 如长度验证 email 验证等

    public boolean assertEqual(String field ,String field2) {
        String value1 = request.getParameter(field);
        String value2 = request.getParameter(field2);
        if ((value1 == value2) || (value1 != null && value2 != null && value1.equals(value2))) {
            return true;
        }
        return false;
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");

    public boolean assertEmail(String  field) {
        String value = request.getParameter(field);
        if (!StringUtils.isEmpty(value)) {
            Matcher matcher = EMAIL_PATTERN.matcher(value);
            return matcher.matches();
        }
        return true;
    }


    public boolean assertLength(String  field ,int maxLen,int minLen) {
        String value = request.getParameter(field);
        if (value != null) {
            int len = value.length();
            return len >= minLen && len <= maxLen;
        }
        return false;
    }

    public boolean assertPhone(String field) {
        String value = request.getParameter(field);
        if (!StringUtils.isEmpty(value)) {
            Matcher matcher = PHONE_PATTERN.matcher(value);
            return matcher.matches();
        }
        return true;
    }


    public boolean assertNotEmpty(String  field) {
        String value = request.getParameter(field);
        return !StringUtils.isEmpty(value);
    }


    public boolean assertUrl(String  field) {
        String value = request.getParameter(field);
        if (!StringUtils.isEmpty(value)) {
            try {
                new URL(value);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public boolean assertPattern(String  field,String expression) {
        String value = request.getParameter(field);
        if (!StringUtils.isEmpty(value)) {
            Matcher matcher =  Pattern.compile(expression).matcher(value);
            return matcher.matches();
        }
        return true;
    }



}