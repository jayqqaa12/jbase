package com.jayqqaa12.jbase.jfinal.ext.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class Db extends com.jfinal.plugin.activerecord.Db
{

	public static List<Record> list(String tableName)
	{
		return  find(" select * from " + tableName);
	}

	public static List<Record> list(String tableName, String where)
	{
		return  find(" select * from " + tableName +" "+where);
	}
	

	public static List<Record> listByCache(String tableName)
	{
		String sql =" select * from " + tableName;
		
		return  findByCache(tableName,sql,sql);
	}
	
	public static List<Record> listByCache(String tableName, String where)
	{
		String sql =" select * from " + tableName +" "+where;
		
		return  findByCache(tableName,sql,sql);
	}
}
