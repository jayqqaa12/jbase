package com.jayqqaa12.jbase.jfinal.ext;

import com.jayqqaa12.model.json.SendJson;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/***
 * 
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
	
	protected void addError(Invocation inv,int code){
		inv.getController().renderJson(new SendJson(code).toJson());
	}

	protected void trycatch(Invocation inv) {
		try {
			inv.invoke();
		} catch (NullParamException e) {
			addError(inv,404);
			e.printStackTrace();
		} catch (Exception e) {
			addError(inv,500);
			e.printStackTrace();
		}
	}


}
