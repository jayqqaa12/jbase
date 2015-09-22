package com.jayqqaa12.jbase.jfinal.ext.ctrl;

import java.util.List;

import com.jayqqaa12.jbase.jfinal.ext.model.Model;
import com.jayqqaa12.model.json.SendJson;

public class JsonController<T> extends Controller<T>
{
	protected SendJson json = new SendJson();
	
	public void renderJsonResult(boolean result) {
		if (result) renderJson200();
		else renderJson500();
	}

	
	public void renderJson500() {
		renderJson("{\"code\":500,\"msg\":\"没有任何修改或 服务器错误\"}");
	}

	public void renderJsonError(String msg) {
		renderJson("{\"code\":500,\"msg\":\" " + msg + " \"}");
	}

	public void renderJson200() {
		renderJson("{\"code\":200}");
	}

	public SendJson getJsonObject(){
		return json;
	}
	
	public void setJsonData(String key, Model m){
		json.setData(key, m);
	}
	
	
	public void setJsonData(  Model m){
		json.setData("data", m);
	}
	
	public void setJsonData( List list){
		json.setData("data",list);
	}

	
	public void setJsonData( Object obj){
		json.setData("data", obj);
	}
	
	public void setJsonData(String key, List m){
		json.setData(key, m);
	}
	
	public void setJsonData(String key,Object value){
		json.setData(key, value);
	}
	

	public void sendJson(String key, List list)
	{

		renderJson(new SendJson(key, list).toJson());
	}
	
	public void sendJson(String key,Object obj){
		
		renderJson(new SendJson().setData(key, obj).toJson());
	}

	public void sendJson(int code, SendJson result)
	{
		if (code == 200) renderJson(result.toJson());
		else sendJson(code);
	}

	public void sendJson(SendJson result)
	{
		renderJson(result.toJson());
	}

	public void sendJson()
	{
		renderJson(json.toJson());
	}

	
	public void sendJson(boolean result)
	{
		int code =200;
		if(!result)code=500;
		sendJson(code);
	}
	
	public void sendJson(int code)
	{

		renderJson(new SendJson(code).toJson());
	}

	public void sendJson(Model m)
	{

		renderJson(new SendJson(m).toJson());
	}
	
	
	
	
 
	

}
