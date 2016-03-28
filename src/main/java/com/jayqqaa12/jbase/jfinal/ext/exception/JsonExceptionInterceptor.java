package com.jayqqaa12.jbase.jfinal.ext.exception;

import com.jayqqaa12.jbase.jfinal.ext.JbaseConfig;
import org.zbus.rpc.RpcException;

import com.jayqqaa12.model.json.SendJson;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

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

			if(JbaseConfig.isDevMode()) e.printStackTrace();
		} else {
			handleError(inv, e);
		}
	}

	protected void handleError(Invocation inv, Exception e) {
		addError(inv, ErrorCode.SERVER_ERROR.code,e.getMessage());
		e.printStackTrace();
	}

}
