package com.jayqqaa12.jbase.web.spring.auth;


import com.jayqqaa12.jbase.util.IpKit;
import com.jayqqaa12.jbase.web.spring.auth.annotation.InnerOnly;
import com.jayqqaa12.jbase.web.spring.auth.annotation.LimitLess;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInteceptor extends HandlerInterceptorAdapter {
	
	private static Logger logger=LoggerFactory.getLogger(AuthInteceptor.class);


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod method=(HandlerMethod) handler;
		InnerOnly only=method.getMethodAnnotation(InnerOnly.class);
		if(only!=null){
			String ip= IpKit.getIp(request);
			return IpKit.isInnerIP(ip);
		}
		UserUtil.flush();
		UserUtil.recordHost(request.getHeader("Host"));
		boolean isAuthed = false;
		String userToken = TokenUtil.checkAndReturnToken(request);
		if (userToken != null) {
			UserUtil.injectToken(userToken);
			Integer userId = UserUtil.getUserId();
			isAuthed =( userId != null);
		}
		if (isAuthed) {
			return true;
		}
		LimitLess less=method.getMethodAnnotation(LimitLess.class);
		if (less==null) {
			response.setContentType("text/json;charset=UTF-8");
			if(!StringUtils.isBlank(userToken)){
//				response.getWriter().print(JSON.toJSONString(ErrorCode.UNAUTHORIZED));
			}else{
//				response.getWriter().print(JSON.toJSONString(ErrorCode.UNAUTHORIZED));
			}
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		UserUtil.flush();
		super.postHandle(request, response, handler, modelAndView);
	}
}
