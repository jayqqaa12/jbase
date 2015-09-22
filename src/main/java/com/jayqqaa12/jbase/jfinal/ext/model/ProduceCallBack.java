package com.jayqqaa12.jbase.jfinal.ext.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.ICallback;

/***
 * 这个 sql 直接 拼接吧 省事
 * @author 12
 *
 */
public class ProduceCallBack implements ICallback
{
	public String produceSql = null;
	
	public  Object[] objs;

	public ProduceCallBack(String sql)
	{
		this.produceSql = sql;
	}
	
	public ProduceCallBack(String sql,Object ...obj)
	{
		this.produceSql = sql;
		this.objs = obj;
	}

	public Object call(Connection conn) throws SQLException
	{
		CallableStatement proc = null;
		try
		{
			proc = conn.prepareCall("{ call " + produceSql + "}");
			if(objs!=null){
				
				
				int i =1;
				for(Object obj :objs)
				{
					if(obj instanceof Integer){
						proc.setInt(i++, (Integer) obj);
					}
					
					if(obj instanceof Float){
						proc.setFloat(i++, (Float) obj);
					}
					if(obj instanceof Double){
						proc.setDouble(i++, (Double) obj);
					}
					if(obj instanceof String){
						proc.setString(i++, (String) obj);
					}
					if(obj instanceof Byte){
						proc.setByte(i++, (Byte) obj);
					}
					if(obj instanceof Boolean){
						proc.setBoolean(i++, (Boolean) obj);
					}
					if(obj instanceof Long){
						proc.setLong(i++, (Long) obj);
					}
					if(obj instanceof  Timestamp){
						proc.setTimestamp(i++,   (Timestamp) obj);
					}
					
					if(obj ==null) {
						proc.setNull(i++, java.sql.Types.NULL);;
					}
					
				}
				
			}
			
			
			
			proc.execute();
		} finally
		{
			DbKit.getConfig().close(proc, conn);
		}
		return proc;
	}

 

	 
}
