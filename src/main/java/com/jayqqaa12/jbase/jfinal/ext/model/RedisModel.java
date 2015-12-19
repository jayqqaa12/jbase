package com.jayqqaa12.jbase.jfinal.ext.model;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfinal.ext.plugin.redis.JedisKit;

/**
 * 
 * 使用redis 作为缓存的 model
 * 
 * @author 12
 *
 */
public class RedisModel<M extends com.jayqqaa12.jbase.jfinal.ext.model.Model<M>> extends
		com.jayqqaa12.jbase.jfinal.ext.model.Model<M> {
	
//	static final Object[] NULL_PARA_ARRAY = new Object[0];

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
	
	
	
//	@Override
//	public List<M> findByCache(String cacheName, Object key, String sql) {
//		return findByCache(cacheName, key, sql, NULL_PARA_ARRAY);
//	}

//	@Override
//	public M findFirstByCache(String cacheName, Object key, String sql) {
//
//		return findFirstByCache(cacheName, key, sql, NULL_PARA_ARRAY);
//	}
//
//	@Override
//	public M findFirstWhereByCache(String key, String where, Object... params) {
//		String sql = "select * from " + tableName + " " + where;
//		return findFirstByCache(tableName, key, sql, params);
//	}
//
//	@Override
//	public M findByIdCache(Object id) {
//
//		return findFirstByCache(tableName, id, "select * from " + tableName + " where id =?", id);
//
//	}
//
//	@Override
//	public List<M> findByCache(String sql) {
//		return findByCache(tableName, sql, sql);
//	}
//
//	@Override
//	public List<M> findByCache(String key, String sql, Object... params) {
//		return findByCache(tableName, key, sql, params);
//	}
//
//	@Override
//	public List<M> listByCache() {
//		return findByCache(" select *from " + tableName);
//	}
//
//	@Override
//	public List<M> listByCache(String key, String where, Object... params) {
//
//		return findByCache(key, " select *from " + tableName + " " + where, params);
//	}
//
//	@Override
//	public List<M> listByCache(String where) {
//		return findByCache(" select *from " + tableName + " " + where);
//	}

}
