package com.jayqqaa12.test;


import java.util.Map;

import com.google.gson.Gson;
import com.jayqqaa12.jbase.jfinal.ext.exception.JbaseErrorCodeException;
import com.jayqqaa12.model.json.SendJson;
import com.jfinal.kit.LogKit;

public class TestKit {

	public static Map<String, Object> parseData(String rst) {

		SendJson json = new Gson().fromJson(rst, SendJson.class);
		return json.data;
	}

	public static int parse(String result) {
		SendJson json = new Gson().fromJson(result, SendJson.class);
		return json.code;
	}
	
	
	/**
	 * 默认断言 200
	 * @param rst
	 * @throws Exception 
	 * @throws ServiceErrorException
	 */
	public static void assertError(String rst)     {
		assertError(rst,200);
	}
	
	
	


	public static void assertError(String rst,int code)    {

		LogKit.error(rst);

		if (rst == null) throw new  RuntimeException("ERROR SERVICE RESULT IS NULL");

		if (parse(rst) != code) throw new JbaseErrorCodeException(code, "ASSERT FAIL ERROR  CODE =" + parse(rst));

	}

}
