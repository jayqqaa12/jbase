package com.jayqqaa12.jbase.jfinal.ext.ctrl;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.jayqqaa12.jbase.jfinal.ext.exception.ErrorCode;
import com.jayqqaa12.jbase.jfinal.ext.exception.NullParamException;
import com.jayqqaa12.model.json.Form;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;


/**
 * 
 * 做为本人私人使用 架构比较简陋 bug也挺多  仅供学习 
 * 
 * 2013-8-12 
 * 
 * 2015-12-23 重新修订
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
		if(str ==null || StrKit.isBlank(str)) return throwNullParam(name);
		return str;
	}

	private String throwNullParam(String name) {

		throw new NullParamException(ErrorCode.NULL_PARAM_ERROR.code,
				ErrorCode.NULL_PARAM_ERROR.msg+" "+name);
	}

	/**
	 * 必需不能为null 的参数用这个 方法获取
	 */
	public Integer getParaToIntNotNull(String name)  {
		Integer str= super.getParaToInt(name);
		if(str ==null ) throwNullParam(name);
		return str;
	}
 
	/**
	 * 必需不能为null 的参数用这个 方法获取
	 */
	public Long getParaToLongNotNull(String name)  {
		Long str= super.getParaToLong(name);
		if(str ==null  ) throwNullParam(name);
		return str;
	}
	/**
	 * 必需不能为null 的参数用这个 方法获取
	 */
	public Boolean getParaToBooleanNotNull(String name)  {
		Boolean str= super.getParaToBoolean(name);
		if(str ==null  ) throwNullParam(name);
		return str;
	}
	/**
	 * 必需不能为null 的参数用这个 方法获取
	 */
	public Date getParaToDateNotNull(String name) {
		Date str= super.getParaToDate(name);
		if(str ==null  ) throwNullParam(name);
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
		if (StrKit.notBlank(to)) render(viewpath + "/" + to + ".html");
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
	
	
 
	/**
	 * 这个方法跟jfinal的 不一样
	 * 
	 * 不同之处 如果 数据库里没有这个字段 就不会放入 model
	 * 
	 */
	@Override
	public <M> M getModel(Class<M> modelClass) {
		return (M)com.jayqqaa12.jbase.jfinal.ext.ctrl.Injector.injectModel(modelClass, getRequest(), false);
	}

	/**
	 * 这个方法跟jfinal的 不一样
	 * 
	 * 不同之处 如果 数据库里没有这个字段 就不会放入 model
	 * 
	 */
	@Override
	public <M> M getModel(Class<M> modelClass, String modelName) {
		return (M)com.jayqqaa12.jbase.jfinal.ext.ctrl.Injector.injectModel(modelClass, modelName, getRequest(), false);
	}
	
	
	@Override
	public <M> M getModel(Class<M> modelClass, boolean skipConvertError) {
		return (M)com.jayqqaa12.jbase.jfinal.ext.ctrl.Injector.injectModel(modelClass, getRequest(), skipConvertError);
	}

	public T getModel() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class  modelClass = (Class ) pt.getActualTypeArguments()[0];

		return (T) this.getModel(modelClass);
	}
	
	public T getNotPreModel() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class  modelClass = (Class ) pt.getActualTypeArguments()[0];
		return (T) this.getModel(modelClass, "");
	}

	public void renderExcel(List<?> data, String fileName, String[] coulms, String[] headers) {

		PoiRender excel = PoiRender.me(data);
		excel.fileName(fileName);
		excel.headers(headers);
		excel.columns(coulms);
		excel.cellWidth(5000);
		render(excel);
	}




	/***
	 * 通常用来组装 serach form
	 * 
	 * tableName 用来 过滤多表
	 * 
	 * 这是常用的几种
	 * 
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




	/**
	 * 当前页 p
	 * 默认值1
	 * 基于约定
	 */
	protected  int p(){
		return getParaToInt("p", 1);
	}

	/**
	 * 页数 c
	 * 默认值10
	 * 基于约定
	 */
	protected  int c(){
		return getParaToInt("c", 10);
	}



}
