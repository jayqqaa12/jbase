package com.jayqqaa12.jbase.jfinal.ext.ctrl;

import java.util.List;

import com.jayqqaa12.jbase.jfinal.ext.model.Model;
import com.jayqqaa12.model.json.SendJson;
import com.jfinal.plugin.activerecord.Page;

public class JsonController<T> extends Controller<T>
{
	private SendJson json = new SendJson();
	
	protected void renderJsonResult(boolean result) {
		if (result) renderJson200();
		else renderJson500();
	}

	
	protected void renderJson500() {
		renderJson("{\"code\":500,\"msg\":\"没有任何修改或 服务器错误\"}");
	}

	protected void renderJsonError(String msg) {
		renderJson("{\"code\":500,\"msg\":\" " + msg + " \"}");
	}

	protected void renderJson200() {
		renderJson("{\"code\":200}");
	}

 
	protected void setJsonData(String key, Model m){
		json.setData(key, m);
	}
	
	
	protected void setJsonData(  Model m){
		json.setData("data", m);
	}
	
	protected void setJsonData( List list){
		json.setData("data",list);
	}

	
	protected void setJsonData( Object obj){
		json.setData("data", obj);
	}
	
	protected void setJsonData( Page page){
		json.setData("list",page.getList());
		
		json.setData("total", page.getTotalRow());
	}
	protected void setJsonData(String key, List m){
		json.setData(key, m);
	}
	
	protected void setJsonData(String key,Object value){
		json.setData(key, value);
	}
	

	protected void sendJson( List list)
	{
		setJsonData("data", list);
		renderJson(json.toJson());
	}
	
	protected void sendJson(String key,Object obj){
		
		renderJson(json.setData(key, obj).toJson());
	}
	
	protected void sendJson(String key,List m){
		json.setData(key, m);
		renderJson(json.toJson());
	}

	protected void sendJson(int code, SendJson result)
	{
		if (code == 200) renderJson(result.toJson());
		else sendJson(code);
	}

	protected void sendJson(SendJson result)
	{
		renderJson(result.toJson());
	}

	protected void sendJson()
	{
		renderJson(json.toJson());
	}

	
	protected void sendJson(boolean result)
	{
		int code =200;
		if(!result)code=500;
		sendJson(code);
	}
	
	protected void setJsonCode(int code){
		json.code=code;
	}
	
	protected void sendJson(int code)
	{
		setJsonCode(code);
		renderJson(json.toJson());
	}

	protected void sendJson(Model m)
	{
		setJsonData(m);

		renderJson(json.toJson());
	}
	
	
	
	
 
	

}
