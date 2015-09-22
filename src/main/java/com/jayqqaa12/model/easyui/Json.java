package com.jayqqaa12.model.easyui;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * JSON模型
 * 
 * 用户后台向前台返回的JSON对象
 * 
 * 
 */
@SuppressWarnings("serial")
public class Json implements java.io.Serializable {
	public boolean succ = true;
	public String msg = "";
	public Object obj = null;
	public Map data = new HashMap();

	public Json addKey(String string, Object modulus)
	{
		data.put(string, modulus);
		
		return this;
	}




}
