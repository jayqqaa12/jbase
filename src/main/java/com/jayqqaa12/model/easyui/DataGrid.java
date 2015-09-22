package com.jayqqaa12.model.easyui;

import java.util.ArrayList;
import java.util.List;

import com.jayqqaa12.jbase.util.Validate;

/**
 * EasyUI DataGrid模型
 * 
 * @author 孙宇
 * 
 */
public class DataGrid<T> implements java.io.Serializable
{

	public int total = 0;
	public int page;
	public String sortName = "";
	public String sortOrder = "";
	public List<T> rows = new ArrayList<T>();
	
	
	

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public List<T> getRows()
	{
		return rows;
	}

	public void setRows(List<T> rows)
	{
		this.rows = rows;
	}

}
