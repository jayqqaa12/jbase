package com.jayqqaa12.model.easyui;

import java.util.Enumeration;

import com.jayqqaa12.jbase.util.Validate;
import com.jfinal.core.Controller;

/***
 * 
 * 基于约定 其他人不要使用
 * 
 * 可用来生成 where
 * 
 * @author 12
 * 
 */
public class Form extends com.jayqqaa12.model.json.Form
{

	public Form()
	{
		fuzzySerach.add("name");
		fuzzySerach.add("title");

		integerValue.add("status");
		integerValue.add("type");
		integerValue.add("operation");

	}

	public Form(String tableName)
	{
		this();
		if (!Validate.isEmpty(tableName)) this.tableName = tableName;
	}
	


	public static Form getForm(String tableName, Controller c, String... params)
	{
		Form form = new Form(tableName);

		for (String key : params)
		{
			form.fromMap.put(key, c.getPara(key, null));
		}

		Enumeration<String> names = c.getParaNames();

		while (names.hasMoreElements())
		{
			String name = names.nextElement();
			if (name.contains(".")) form.addFrom(form, name, c.getPara(name, null));
			else if(name.contains("-")) form.addFrom(form, name, c.getPara(name,null));
		}

		return form;
	}


	public String getWhereAndSort(DataGrid dg)
	{
		if (getWhere().contains("date")) dg.sortOrder = "asc";

		return getWhere() + sort(dg.sortName, dg.sortOrder);

	}

	/***
	 * 设置自己的where
	 * 
	 * @param dg
	 * @param where
	 * @return
	 */
	public String getWhereAndLimit(DataGrid dg, String where)
	{
		int page = dg.page;
		if (page > 0) page -= 1;
		return where + limit(page, dg.total);
	}

	public String getWhereAndLimit(DataGrid dg)
	{
		int page = dg.page;
		return getWhereAndSort(dg) + limit(page, dg.total);
	}

	
	public String sort(String sortName, String sortOrder)
	{
		if (Validate.isEmpty(sortName)) return "";
		else return " order by " + sortName + " " + sortOrder;
	}

	public String limit(int page, int size)
	{
		if (page > 0) page -= 1;
		return " limit " + size * page + "," + size;
	}


}
