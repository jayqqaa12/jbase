package com.jayqqaa12.jbase.util;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;

/**
 * IP工具类
 * 
 * 
 */
public class IpUtil {

	/**
	 * 获取登录用户的IP地址
	 * 
	 * 对手机无效
	 * 
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		if (ip.split(",").length > 1) {
			ip = ip.split(",")[0];
		}
		return ip;
	}

	public static String getIpByMobile(HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		if (ip != null && (ip.equals("127.0.0.1") || ip.startsWith("192")||ip.startsWith("0:0:"))) ip = getIp(request);

		return ip;
	}

	public static JSONObject getIp(String ip) {
		String result = HttpKit.get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ip);
		JSONObject obj =  new JSONObject();
		try {
			obj = (JSONObject) JSON.parse(result);
		} catch (Exception e) {}
		
		return obj;

	}

	public static String getIpAddr() {

		String ip =  HttpKit.get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json");
		String info = "";

		JSONObject obj = (JSONObject) JSON.parse(ip);
		info += obj.getString("province") + " ";
		info += obj.getString("city") + " ";
		info += obj.getString("isp");

		return info;
	}

	public static String getIpCity() {
		String ip =  HttpKit.get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=");
		String info = "";
		JSONObject obj = (JSONObject) JSON.parse(ip);
		info += obj.getString("city");

		return info;
	}

	public static String getWeather(String location) {

		String url = "http://api.map.baidu.com/telematics/v3/weather?location=" + location
				+ "&output=json&ak=640f3985a6437dad8135dae98d775a09";
		String ip =  HttpKit.get(url);

		String info = "";

		JSONObject obj = (JSONObject) JSON.parse(ip);

		if ("success".equals(obj.getString("status"))) {
			JSONObject data = obj.getJSONArray("results").getJSONObject(0);

			data = data.getJSONArray("weather_data").getJSONObject(0);
			info += info += location + "天气  ";
			info += data.getString("weather") + " ";
			info += data.getString("wind") + " ";
			info += data.getString("temperature") + " ";
		}

		return info;
	}

	public static long strToLong(String strip) {
		long[] ip = new long[4];
		int position1 = strip.indexOf(".");
		int position2 = strip.indexOf(".", position1 + 1);
		int position3 = strip.indexOf(".", position2 + 1);
		ip[0] = Long.parseLong(strip.substring(0, position1));
		ip[1] = Long.parseLong(strip.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strip.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strip.substring(position3 + 1));
		return ((ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3]);

	}
}
