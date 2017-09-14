package com.jayqqaa12.jbase.web.jfinal.ext.exception;

import com.jayqqaa12.jbase.exception.ErrorCode;
import com.jayqqaa12.jbase.exception.JbaseErrorCodeException;
import com.jayqqaa12.jbase.web.jfinal.ext.JbaseConfig;
import com.jayqqaa12.jbase.web.jfinal.ext.model.vo.SendJson;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.LogKit;

/***
 * 使用 api 接口的时候用
 *
 * @author 12
 *
 */
public class JsonExceptionInterceptor implements Interceptor {

	protected String key = "/";

	public JsonExceptionInterceptor() {
	}

	public JsonExceptionInterceptor(String key) {
		this.key = key;
	}

	@Override
	public void intercept(Invocation inv) {
		String url = inv.getActionKey();
		if (url.contains(key)) trycatch(inv);
		else inv.invoke();
	}

	protected void addError(Invocation inv, int code,String msg) {
		inv.getController().renderJson(new SendJson(code,msg).toJson());
	}

	private void trycatch(Invocation inv) {
		try {
			inv.invoke();
		} catch (Exception e) {
			handleErrorCodeException(inv, e);
		}
	}

	private void handleErrorCodeException(Invocation inv, Exception e) {

		if (e instanceof JbaseErrorCodeException) {

			addError(inv, ((JbaseErrorCodeException) e).getErrorCode(),((JbaseErrorCodeException) e).getMessage());

			if(JbaseConfig.isDevMode()) LogKit.error(e.getMessage(),e);
		} else {
			handleError(inv, e);
		}
	}

	protected void handleError(Invocation inv, Exception e) {

		LogKit.error(e.getMessage(),e);
		addError(inv, ErrorCode.SERVER_ERROR.code,e.getMessage());


	}

}
