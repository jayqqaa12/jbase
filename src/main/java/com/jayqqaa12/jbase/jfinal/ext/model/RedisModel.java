package com.jayqqaa12.jbase.jfinal.ext.model;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfinal.ext.plugin.redis.JedisKit;

/**
 * 
 * 使用redis 作为缓存的 model
 * 
 * 此类违反  里氏替换 原则  不应修改父类原有功能 
 * 
 * 更好的办法为 实现一个抽象接口 来实现不同策略   
 * 
 * 
 * @author 12
 *
 */
public class RedisModel<M extends com.jayqqaa12.jbase.jfinal.ext.model.Model<M>> extends
		com.jayqqaa12.jbase.jfinal.ext.model.Model<M> {

	private static final long serialVersionUID = 7715592315874069631L;

	private static int DEFALT_CACHE_TIME = 60 * 5;
  
	public static void setCacheTime(int seconds) {
		DEFALT_CACHE_TIME = seconds;
	}


	/**
	 *key 需要为 能转化为key
	 * 
	 */
	@Override
	public List<M> findByCache(String cacheName, Object key, String sql, Object... paras) {

		String k = keyToString(key);

		List<M> result = null;
		String json = JedisKit.get(k);
		if (json != null) result = new Gson().fromJson(json, new TypeToken<List<M>>() {}.getType());
		if (result == null) {
			result = find(sql, paras);
			JedisKit.set(k, new Gson().toJson(result), DEFALT_CACHE_TIME);
		}
		return result;
	}

	/**
	 * key 需要为 能转化为key
	 * 
	 */
	@Override
	public M findFirstByCache(String cacheName, Object key, String sql, Object... paras) {

		String k = keyToString(key);

		M result = null;
		if (k != null) JedisKit.get(k);

		if (result == null) {
			result = findFirst(sql, paras);
			JedisKit.set(k, result, DEFALT_CACHE_TIME);
		}
		return result;
	}

	private String keyToString(Object key) {
		String k = null;

		if (key instanceof String) k = (String) key;
		if (key instanceof Integer) k = key + "";
		if (key instanceof Long) k = key + "";

		return k;
	}
	
	
	
 

}
