//package com.jayqqaa12.jbase.spring.interceptor;
//
//
//import com.alibaba.fastjson.JSON;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.songbai.lemi.basics.mvc.Response;
//import org.songbai.lemi.basics.mvc.TokenUtil;
//import org.songbai.lemi.basics.mvc.UserUtil;
//import org.songbai.lemi.basics.mvc.annotation.InnerOnly;
//import org.songbai.lemi.basics.mvc.annotation.LimitLess;
//import org.songbai.lemi.basics.utils.http.IpTools;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class AuthInterceptor extends HandlerInterceptorAdapter {
//
//    private static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//
////        System.out.println(request.getRequestURI());
//
//
//        HandlerMethod method = (HandlerMethod) handler;
//        InnerOnly only = method.getMethodAnnotation(InnerOnly.class);
//        if (only != null) {
//            String ip = IpTools.getIpAddress(request);
//            return IpTools.isInnerIP(ip);
//        }
//        UserUtil.flush();
////		UserUtil.recordHost(po.getHeader("Host"));
//        boolean isAuthed = false;
//        String userToken = TokenUtil.checkAndReturnToken(request);
//        if (userToken != null) {
//            UserUtil.injectToken(userToken);
//            Integer userId = UserUtil.getUserId();
//            isAuthed = (userId != null);
//        }
//
//        if ( !isAuthed && !isLimitLess(method)) {
//            response.setContentType("text/json;charset=UTF-8");
//            if (!StringUtils.isBlank(userToken)) {
//                response.getWriter().print(JSON.toJSONString(Response.response(503, "登录超时，请重新登录")));
//            } else {
//                response.getWriter().print(JSON.toJSONString(Response.response(503, "您未登录，请重新登录")));
//            }
//
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//                           ModelAndView modelAndView) throws Exception {
//        UserUtil.flush();
//        super.postHandle(request, response, handler, modelAndView);
//    }
//
//    private boolean isLimitLess(HandlerMethod method) {
//        LimitLess less = method.getMethodAnnotation(LimitLess.class);
//
//        if (less == null) {
//            less = method.getBeanType().getAnnotation(LimitLess.class);
//        }
//
//        return less != null;
//    }
//}
