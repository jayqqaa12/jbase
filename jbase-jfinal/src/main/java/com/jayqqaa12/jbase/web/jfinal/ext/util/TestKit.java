package com.jayqqaa12.jbase.web.jfinal.ext.util;

import com.alibaba.fastjson.JSON;
import com.jayqqaa12.jbase.exception.JbaseErrorCodeException;
import com.jayqqaa12.jbase.web.jfinal.ext.model.vo.SendJson;

import java.util.Map;

public class TestKit {

	public static Map<String, Object> parseData(String rst) {

		SendJson json = JSON.parseObject(rst, SendJson.class);
		return json.data;
	}

	public static int parse(String result) {
		SendJson json = JSON.parseObject(result, SendJson.class);
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
