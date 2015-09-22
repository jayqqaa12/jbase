package com.jayqqaa12.jbase.jfinal.ext.model;

import java.util.List;

import com.jayqqaa12.model.easyui.DataGrid;
import com.jayqqaa12.model.easyui.Form;


public class EasyuiModel<M extends com.jayqqaa12.jbase.jfinal.ext.model.Model<M>> extends com.jayqqaa12.jbase.jfinal.ext.model.Model<M>
{
	private static final long serialVersionUID = -7162337934972053871L;
	/***
	 * 自定义sql
	 * 
	 * @param sql
	 * @param dg
	 * @param f
	 * @return
	 */
	public DataGrid<M> listByDataGrid(String sql, DataGrid<M> dg, Form f)
	{
		List<M> list = find(sql + f.getWhereAndLimit(dg));
		dg.rows = list;
		dg.total = (int) getCount(sql + f.getWhereAndSort(dg));

		return dg;
	}
	
	public List<M> list(DataGrid dg, Form f)
	{
		return list(f.getWhereAndLimit(dg));
	}


	/***
	 * 自定义sql
	 * 
	 * 这个方法无法 自动 获取 过滤 where 了
	 * 
	 * @param sql
	 * @param dg
	 * @param f
	 * @return
	 */
	public DataGrid<M> listByDataGrid(DataGrid<M> dg, Form f, String where, Object... params)
	{
		List<M> list = find(" select *from " + tableName + " " + where, params);
		dg.rows = list;
		dg.total = (int) getCount(" select *from " + tableName + " " + where, params);

		return dg;
	}

	/***
	 * 自定义sql
	 * 
	 * @param sql
	 * @param dg
	 * @param f
	 * @return
	 */
	public DataGrid<M> listByDataGridXml(String sql, DataGrid<M> dg, Form f)
	{
		List<M> list = find(sql(sql) + f.getWhereAndLimit(dg));
		dg.rows = list;
		dg.total = (int) getCount(sql(sql) + f.getWhereAndSort(dg));

		return dg;
	}

	/***
	 * 自定义sql
	 * 
	 * @param sql
	 * @param dg
	 * @param f
	 * @return
	 */
	public DataGrid<M> listByDataGridXml(String sql, DataGrid<M> dg, Form f, String where, Object... params)
	{

		List<M> list = find(sql(sql) + f.getWhere() + where + f.sort(dg.sortName, dg.sortOrder) + f.limit(dg.page, dg.total), params);
		dg.rows = list;
		dg.total = (int) getCount(sql(sql) + f.getWhere() + where, params);

		return dg;
	}

	/***
	 * 直接插 自动删除 最简单的sql
	 * 
	 * @param dg
	 * @param f
	 * @return
	 */
	public DataGrid<M> listByDataGrid(DataGrid<M> dg, Form f)
	{
		List<M> list = list(f.getWhereAndLimit(dg));
		dg.rows = list;
		dg.total = (int) getCount("select * from " + tableName + " " + f.getWhereAndSort(dg));

		return dg;
	}
}
