package com.jayqqaa12.jbase.jfinal.ext.ctrl;


import com.jayqqaa12.model.easyui.DataGrid;
import com.jayqqaa12.model.easyui.Form;

public class EasyuiController<T> extends Controller<T>
{
	
	public DataGrid<T> getDataGrid()
	{
		DataGrid<T> dg = new DataGrid<T>();
		dg.sortName = getPara("sort", "");
		dg.sortOrder = getPara("order", "");
		dg.page = getParaToInt("page", 1);
		dg.total = getParaToInt("rows", 15);

		return dg;
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

		return (Form) com.jayqqaa12.model.easyui.Form.getForm(tableName, this, "date", "dateStart", "dateEnd", "name", "title", "des", "msg", "url",
				"icon", "text", "pwd", "status", "type", "createdateStart", "createdateEnd", "modifydateStart",
				"modifydateEnd", "operation");
	}


}
