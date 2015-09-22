package com.jayqqaa12.jbase.jfinal.ext;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/***
 * 
 * 使用 api 接口的时候用
 * 
 * @author 12
 *
 */
public class JsonExceptionInterceptor implements Interceptor {

	private String key = "/";
	
	public JsonExceptionInterceptor() {
	}
	public JsonExceptionInterceptor(String key) {
		this.key = key;
	}

	@Override
	public void intercept(Invocation ai) {
		String url = ai.getActionKey();

		if (url.contains(key) ) {
			try {
				ai.invoke();
			} catch (NullParamException e) {
				ai.getController().renderJson("{\"code\":404,\"data\":{}}");
				e.printStackTrace();
			} catch (Exception e) {
				ai.getController().renderJson("{\"code\":500,\"data\":{}}");
				e.printStackTrace();
			}
		}
		else ai.invoke();
	}
 
}
