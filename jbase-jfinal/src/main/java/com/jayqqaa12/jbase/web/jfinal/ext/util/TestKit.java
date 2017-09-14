package com.jayqqaa12.jbase.web.jfinal.ext.util;

import java.util.Map;

import com.google.gson.Gson;
import com.jayqqaa12.jbase.exception.JbaseErrorCodeException;
import com.jayqqaa12.jbase.web.jfinal.ext.model.vo.SendJson;

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
	 */
	public static void assertError(String rst)     {
		assertError(rst,200);
	}


	/**
	 * 非500 即 正常
	 * @param rst
	 */
	public static void assertNotError(String rst) {

		System.out.println(rst);

		if (rst == null) throw new  RuntimeException("ERROR SERVICE RESULT IS NULL");


		if (parse(rst) == 500) throw new JbaseErrorCodeException(500, "ASSERT FAIL ERROR  CODE =" + parse(rst));

	}




	public static void assertError(String rst,int code)    {

		System.out.println(rst);

		if (rst == null) throw new  RuntimeException("ERROR SERVICE RESULT IS NULL");


		if (parse(rst) != code) throw new JbaseErrorCodeException(code, "ASSERT FAIL ERROR  CODE =" + parse(rst));

	}

}
