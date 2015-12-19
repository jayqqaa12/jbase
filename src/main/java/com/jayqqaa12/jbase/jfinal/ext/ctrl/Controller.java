package com.jayqqaa12.jbase.jfinal.ext.ctrl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jayqqaa12.jbase.jfinal.ext.NullParamException;
import com.jayqqaa12.jbase.util.Validate;
import com.jayqqaa12.model.json.Form;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;


/**
 * 
 * 本工具（jbase）的限制
 * 
 * 由于是根据做的项目而慢慢演变的 
 * 所以并不适合所有项目 使用 
 * 
 * 做为本人私人使用 架构比较简陋 bug也挺多  仅供学习 
 * 
 * 
 * @author 12
 *
 * @param <T>
 */
public class Controller<T> extends com.jfinal.core.Controller {

	ControllerBind controll;

	public Controller() {
		controll = this.getClass().getAnnotation(ControllerBind.class);
	}
	
	
	public String getParaNotNull(String name)  {
		String str= super.getPara(name);
		if(str ==null || Validate.isEmpty(str)) throw new NullParamException();
		return str;
	}
	
	/**
	 * 必需不能为null 的参数用这个 方法获取
	 * @param name
	 * @return
	 */
	public Integer getParaToIntNotNull(String name)  {
		Integer str= super.getParaToInt(name);
		if(str ==null ) throw new NullParamException();
		return str;
	}
 
	/**
	 * 必需不能为null 的参数用这个 方法获取
	 * @param name
	 * @return
	 */
	public Long getParaToLongNotNull(String name)  {
		Long str= super.getParaToLong(name);
		if(str ==null  ) throw new NullParamException();
		return str;
	}
	/**
	 * 必需不能为null 的参数用这个 方法获取
	 * @param name
	 * @return
	 */
	public Boolean getParaToBooleanNotNull(String name)  {
		Boolean str= super.getParaToBoolean(name);
		if(str ==null  ) throw new NullParamException();
		return str;
	}
	/**
	 * 必需不能为null 的参数用这个 方法获取
	 * @param name
	 * @return
	 */
	public Date getParaToDateNotNull(String name) {
		Date str= super.getParaToDate(name);
		if(str ==null  ) throw new NullParamException();
		return str;
	}
 

 

	/**
	 * 通过传递 to参数来自动返回　HTML 页面 viewpath 需要设置 
	 * 
	 */
	public void rh() {
		String viewpath = "";
		if (controll != null) viewpath = controll.viewPath();
		String to = getPara("to");
		keepPara();
		if (!Validate.isEmpty(to)) render(viewpath + "/" + to + ".html");
	}

	/***
	 * 默认读取 注解来 转发到 约定的 视图
	 * 
	 */
	public void index() {
		if (controll != null) {
			String viewpath = controll.viewPath();
			setAttr("VIEW_PATH", viewpath);
			render(viewpath + "/index.html");
		}
	}

	public T getModel() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class modelClass = (Class) pt.getActualTypeArguments()[0];

		return (T) super.getModel(modelClass);
	}

	public void renderExcel(List<?> data, String fileName, String[] coulms, String[] headers) {

		PoiRender excel = PoiRender.me(data);
		excel.fileName(fileName);
		excel.headers(headers);
		excel.columns(coulms);
		excel.cellWidth(5000);
		render(excel);
	}


   
	public void delete() {
	}

	public void list()   {
	}

	public void saveOrUpdate()   {
	}

	public void add()  {
	}

	public void edit()   {
	}

	/***
	 * 通常用来组装 serach form
	 * 
	 * tableName 用来 过滤多表
	 * 
	 * 这是常用的几种
	 * 
	 * @return
	 */
	public Form getFrom(String tableName) {

		return Form.getForm(tableName, this, "date", "dateStart", "dateEnd", "name", "title", "des", "msg", "url",
				"icon", "text", "pwd", "status", "type", "createdateStart", "createdateEnd", "modifydateStart",
				"modifydateEnd", "operation");
	}

 
	public void renderTop(String url) {

		renderHtml("<html><script> window.open('" + url + "','_top') </script></html>");

	}

 
	public void renderGson(Object obj) {

		renderJson(new Gson().toJson(obj));
	}

 

}
