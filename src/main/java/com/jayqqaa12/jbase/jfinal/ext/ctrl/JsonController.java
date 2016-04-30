package com.jayqqaa12.jbase.jfinal.ext.ctrl;

import java.util.List;

import com.jayqqaa12.jbase.jfinal.ext.exception.ErrorCode;
import com.jayqqaa12.jbase.jfinal.ext.model.Model;
import com.jayqqaa12.model.json.SendJson;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

public class JsonController<T> extends Controller<T> {
	private SendJson json = new SendJson();

	private  static final   int serviceError =    ErrorCode.SERVER_ERROR.code;


	protected void sendJsonError(String msg) {
		renderJson("{\"code\":500,\"msg\":\" " + msg + " \"}");
	}
 

	protected void setJsonData(String key, Model m) {
		json.setData(key, m);
	}

	protected void setJsonData(Model m) {

		json.setData("data", m);
	}

	protected void setJsonData(List list) {

		json.setData("data", list);
	}

	protected void setJsonData(Object obj) {
		json.setData("data", obj);
	}

	protected void setJsonData(Page page) {
		if (page != null) {
			json.setData("list", page.getList());
			json.setData("total", page.getTotalRow());
		} else json.code =   serviceError;
	}

	protected void setJsonData(String key, List m) {
		json.setData(key, m);
	}

	protected void setJsonData(String key, Object value) {
		json.setData(key, value);
	}

	protected void sendJson(Page page) {
		setJsonData(page);
		renderJson(json.toJson());
	}

	protected void sendJson(Ret ret) {
		if (ret != null) json.setData(ret.getData());
		else json.code = serviceError;

		renderJson(json.toJson());
	}

	protected void sendJson(List list) {
		setJsonData("data", list);
		renderJson(json.toJson());
	}

	protected void sendJson(String key, Object obj) {

		renderJson(json.setData(key, obj).toJson());
	}

	protected void sendJson(String key, List m) {
		json.setData(key, m);
		renderJson(json.toJson());
	}

	protected void sendJson(int code, SendJson result) {
		if (code == 200) renderJson(result.toJson());
		else sendJson(code);
	}

	protected void sendJson(SendJson result) {
		renderJson(result.toJson());
	}

	protected void sendJson() {
		renderJson(json.toJson());
	}

	protected void sendJson(boolean result) {
		int code = 200;
		if (!result) code = serviceError;
		sendJson(code);
	}

	protected void setJsonCode(int code) {
		json.code = code;
	}

	protected void sendJson(int code,String msg) {
		json.code = code;
		json.msg=msg;
		renderJson(json.toJson());
	}

	protected void sendJson(int code) {
		setJsonCode(code);
		renderJson(json.toJson());
	}

	protected void sendJson(Model m) {
		setJsonData(m);

		renderJson(json.toJson());
	}


}
