package com.jayqqaa12.test.kit;


import java.util.Map;

import com.google.gson.Gson;
import com.jayqqaa12.model.json.SendJson;

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
	 * @throws ServiceErrorException
	 */
	public static void assertError(String rst) throws ServiceErrorException {
		assertError(rst,200);
	}
	
	
	


	public static void assertError(String rst,int code) throws ServiceErrorException {

		System.out.println(rst);

		if (rst == null) throw new ServiceErrorException("ERROR SERVICE RESULT IS NULL");

		if (parse(rst) != code) throw new ServiceErrorException("ASSERT FAIL ERROR  CODE =" + parse(rst));

	}

}
