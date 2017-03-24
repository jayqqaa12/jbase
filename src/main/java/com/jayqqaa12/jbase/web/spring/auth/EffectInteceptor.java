package com.jayqqaa12.jbase.web.spring.auth;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用来记录请求的执行内部执行时间，分析性能
 * 
 * @author Boyce 2016年7月21日 下午2:50:18
 */
public class EffectInteceptor extends HandlerInterceptorAdapter {

	Logger logger = LoggerFactory.getLogger(EffectInteceptor.class);
	static ThreadLocal<Effect> effs = new ThreadLocal<>();
	public static ThreadLocal<String> callBack = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String call=request.getParameter("d_callback");
		if(StringUtils.isNotBlank(call)){
			callBack.set(call);
			System.out.println("call");
		}
		
		Effect e = new Effect();
		e.request = request.getServletPath();
		e.start = System.currentTimeMillis();
		effs.set(e);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		callBack.remove();
		Effect e = effs.get();
		if (e != null) {
			long timeCost = System.currentTimeMillis() - e.start;
			logger.info("request {} cost {} ms", e.request, timeCost);
		}
		effs.remove();
	}

	static class Effect {
		String request;
		long start;
		long end;
	}
}
