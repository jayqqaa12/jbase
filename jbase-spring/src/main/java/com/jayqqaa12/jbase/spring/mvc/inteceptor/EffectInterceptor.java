package com.jayqqaa12.jbase.spring.mvc.inteceptor;

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
public class EffectInterceptor extends HandlerInterceptorAdapter {

	private final static String JSONP_CALLBACK = "d_callback";

	Logger logger = LoggerFactory.getLogger(EffectInterceptor.class);
	static ThreadLocal<Effect> effs = new ThreadLocal<>();
	public static ThreadLocal<String> callBack = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String call = getCallbackFromQueryString(request);

		if (StringUtils.isEmpty(call)) {
			String accept = request.getHeader("Accept");

			if (accept != null && (accept.contains("application/javascript") || accept.contains("text/javascript"))) {
				call = request.getParameter(JSONP_CALLBACK);
			}
		}

		if (StringUtils.isNotBlank(call)) {
			callBack.set(call);
		}

		Effect e = new Effect();
		e.request = request.getServletPath();
		e.start = System.currentTimeMillis();
		effs.set(e);
		return true;
	}

	private String getCallbackFromQueryString(HttpServletRequest request) {
		String call = null;
		String queryString = request.getQueryString();

		if(StringUtils.isEmpty(queryString)){
			return null;
		}

		String[] pathParam = queryString.split("&");

		if (pathParam != null && pathParam.length > 0 ) {
			for (String s : pathParam) {

				if (s.contains("=")) {
					String k = s.substring(0, s.indexOf("="));
					String v = s.substring(s.indexOf("=") + 1);

					if (JSONP_CALLBACK.equals(k)) {
						call = v;
						break;
					}
				}
			}
		}

		return call;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
		callBack.remove();
		Effect e = effs.get();
		if (e != null) {
			long timeCost = System.currentTimeMillis() - e.start;
			logger.info("po {} cost {} ms", e.request, timeCost);
		}
		effs.remove();
	}

	static class Effect {
		String request;
		long start;
		long end;
	}
}