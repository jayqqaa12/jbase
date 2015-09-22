package com.jayqqaa12.jbase.jfinal.ext.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jayqqaa12.jbase.util.L;
import com.jayqqaa12.jbase.util.Txt;
import com.jayqqaa12.jbase.util.Validate;
import com.jayqqaa12.model.easyui.Form;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

/***
 * 部分 方法 只能用于mysql  
 * @author 12
 *
 * @param <M>
 */
public class Model<M extends com.jfinal.plugin.activerecord.Model<M>> extends com.jfinal.plugin.activerecord.Model<M>
{

	private static final long serialVersionUID = 8924183967602127690L;

	/***
	 * 用来当 缓存名字 也用来 生成 简单sql
	 */

	public String tableName;

	/***
	 * 反射获取 注解获得 tablename
	 */
	public Model()
	{
		if (tableName == null)
		{
			TableBind table = this.getClass().getAnnotation(TableBind.class);
			if (table != null) tableName = table.tableName();
		}

	}

	@Override
	public M set(String attr, Object value)
	{
		if(value instanceof String&&Validate.isEmpty((String)value))  return (M)this;
		
		if (value != null) return super.set(attr, value);
		else  return (M)this;
	}

	public boolean update(String key, Object value, Object id)
	{

		return Db.update("update " + tableName + " set " + key + "=? where id =?", value, id) > 0;
	}
	
	
	/**
	 * 更新 指定 条件 非 id
	 * 
	 * @param key
	 * @param value
	 * @param w
	 * @param v
	 * @return
	 */
	public boolean updateByWhere(String key, Object value,String w, Object params)
	{

		return Db.update("update " + tableName + " set " + key + "=? "+w , value, params) > 0;
	}
	
	
	
	public boolean updateAddOneById(String key,Object id) {
		
		return Db.update("update " + tableName + " set " + key + " =" + key + "+1 where id =?",id)>0;
	}
	
	public boolean updateSubOneById(String key,Object id) {
		
		return Db.update("update " + tableName + " set " + key + " =" + key + "-1 where id =?",id)>0;
	}

	public M findByName(String name)
	{
		return findFirstByWhere( " where name =? ", name);
	}
	
	public M  findFirstByUid(Object id){
		
		return findFirstByWhere("where user_id=?", id);
	}
	

	public boolean checkNameExist(String name)
	{

		return findFirst("select * from " + tableName + " where name ='" + name + "'") != null;

	}

	/***
	 * if empty remove the attr
	 * 
	 * @param attr
	 */
	public Model<M> emptyRemove(String attr)
	{
		if (get(attr) == null) remove(attr);

		return this;
	}

	public Model<M> emptyZreo(String attr)
	{
		if (get(attr) == null) set(attr, 0);
		return this;
	}

	/***
	 * 删除自己的同时 删除 所有 子节点 属性名 必需为pid
	 * 
	 * @param para
	 * @return
	 */
	public boolean deleteByIdAndPid(Object id)
	{
		boolean result = deleteById(id);

		List<Model> list = (List<Model>) list("where pid=?", id);

		for (Model m : list)
		{
			deleteByIdAndPid(m.getId());

			Db.update("delete from " + tableName + " where pid=? ", id);
		}

		return result;
	}

	public boolean deleteById(String key, Object value)
	{

		return Db.deleteById(tableName, key, value);

	}

	/***
	 * ids 必需为 连续的 1，2，3 这样子
	 * 
	 * @param ids
	 */
	public boolean batchDelete(String ids)
	{
		if (Validate.isEmpty(ids)) return false;
		return Db.update("delete from " + tableName + " where id in (" + ids + ")") > 0;

	}

	/**
	 * 根据 id 判断的
	 */
	public boolean saveOrUpdate()
	{
		if (getId() != null)return update();
		else return save();
	}

	public boolean pidIsChild(Object id, Integer pid)
	{
		boolean result = false;
		if (pid != null)
		{
			List<Model> list = (List<Model>) list(" where  pid =?  ", id);

			if (list.size() == 0) result = false;

			for (Model r : list)
			{
				if (pid.equals(r.getId()))
				{
					result = true;
					return result;
				}
				else
				{
					if (pidIsChild(r.getId(), pid))
					{
						result = true;
						L.i("result =" + result);

						return result;
					}
				}

			}
		}

		return result;

	}

	public boolean isFind(String sql)
	{

		return find(sql).size() > 0;
	}

	public M findFirstByWhere(String where, Object... params)
	{
		List<M> list = list(where, params);
		if (list.size() > 0) return list.get(0);
		else return null;
	}

	public boolean isFindByWhere(String where, Object... params)
	{

		return list(where, params).size() > 0;
	}

	/***
	 * 返回全部的数据 比较方便 但不灵活
	 * 
	 * @return
	 */
	public List<M> list()
	{

		return find(" select *from " + tableName);
	}

	public List<M> listOrderBySeq()
	{

		return list(" order by seq");
	}

	public List<M> listOrderBySeq(String where, Object... params)
	{

		return list(where + " order by seq", params);
	}

	public List<M> list(String sql, Form f)
	{

		return find(sql + f.getWhere());
	}

	/***
	 * 返回全部的数据 比较方便 但不灵活
	 * 
	 * @return
	 */
	public List<M> list(String where)
	{

		return find(" select *from " + tableName + " " + where);
	}

	public List<M> listByCache()
	{
		return findByCache(" select *from " + tableName);
	}

	public List<M> listByCache(String key, String where, Object... params)
	{
		return findByCache(key, " select *from " + tableName + " " + where, params);
	}

	public List<M> listByCache(String where)
	{
		return findByCache(" select *from " + tableName + " " + where);
	}

	/***
	 * 
	 * @return
	 */
	public List<M> list(String where, Object... params)
	{

		return find(" select *from " + tableName + " " + where, params);
	}

	/***
	 * 返回全部的数据 比较方便 但不灵活
	 * 
	 * @return
	 */
	public List<M> list(int limit)
	{

		return find(" select *from " + tableName + " limit " + limit);
	}

	public List<M> listOrderLimit(int limit, String order)
	{

		return find(" select *from " + tableName + "  order by " + order + " limit " + limit);
	}

	/***
	 * 返回全部的数据 比较方便 但不灵活
	 * 
	 * @return
	 */
	public List<M> list(int page, int size)
	{

		if (page < 1) page = 1;
		return find(" select *from " + tableName + " limit " + (page - 1) * size + "," + size);
	}
	
	public List<M> listByWhereLimit(String where ,int page, int size,Object ... param)
	{
		if (page < 1) page = 1;
		return find("select * from  "+tableName+" " +where +" limit " + (page - 1) * size + "," + size,param);
	}

	public M findByIdCache(Object id)
	{
		String sql = "select * from " + tableName + " where id =" + id;
		M m = CacheKit.get(tableName, sql);
		if (m == null)
		{
			m = super.findById(id);
			CacheKit.put(tableName, sql, m);
		}

		return m;
	}

	public int deleteAll()
	{

		return Db.update(" delete from " + tableName + " ");
	}

	/**
	 * 自动以sql 作为 key
	 * 
	 * @param sql
	 * @return
	 */
	public List<M> findByCache(String sql)
	{

		return super.findByCache(tableName, sql, sql);
	}

	public List<M> findByCache(String key, String sql, Object... params)
	{
		return super.findByCache(tableName, key, sql, params);
	}

	public M findFirstByCache(String key, String sql, Object... params)
	{
		List<M> list = super.findByCache(tableName, key, sql, params);
		if (list.size() > 0) return list.get(0);
		else return null;
	}
	
	
	public M findFirstWhereByCache(String key, String where, Object... params)
	{
		String sql = "select * from " + tableName +" "+  where ;
		List<M> list = super.findByCache(tableName, key, sql, params);
		if (list.size() > 0) return list.get(0);
		else return null;
	}
	

	public boolean saveAndDate()
	{

		return this.setDate("date").save();
	}

	public boolean saveAndCreateDate()
	{
		this.setDate("createdate");
		return this.save();
	}

	public boolean updateAndModifyDate()
	{

		return this.setDate("modifydate").update();
	}

	public Map<String, Object> getAttrs()
	{
		return super.getAttrs();
	}

	public M setDate(String date)
	{
		return this.set(date, new Timestamp(System.currentTimeMillis()));

	}

	/***
	 * 把 model 转化为 list 找到其中的单个属性
	 * 
	 * @param sql
	 * @param attr
	 * @return
	 */
	public List<String> getAttr(String sql, String attr)
	{

		List<String> list = new ArrayList<String>();

		for (M t : find(sql))
		{

			list.add(t.getStr(attr));
		}
		return list;

	}

	/***
	 * 把 model 转化为 list 找到其中的单个属性
	 * 
	 * @param sql
	 * @param attr
	 * @return
	 */
	public List<String> getAttr(String sql, String attr, String... param)
	{

		List<String> list = new ArrayList<String>();

		for (M t : find(sql, param))
		{

			list.add(t.getStr(attr));
		}
		return list;

	}

	public long getAllCount()
	{

		return findFirst(" select count(*) as c from " + tableName).getLong("c");
	}

	public long getCountByWhere(String where)
	{
		return findFirst(" select count(*) as c from "+tableName +" " + where).getLong("c");
	}

	public long getCountByWhere(String where, Object... params)
	{
		return findFirst(" select count(*) as c from " + tableName + " " + where, params).getLong("c");
	}

	public long getCount(String sql)
	{
		sql = Txt.split(sql.toLowerCase(), "from")[1];
		if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];

		return findFirst(" select count(*) as c from " + sql).getLong("c");
	}

	public long getCount(String sql, Object... params)
	{
		sql = Txt.split(sql.toLowerCase(), "from")[1];
		if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];
		return findFirst(" select count(*) as c from " + sql, params).getLong("c");
	}

	public M putModel(String key, Object value)
	{

		if (value instanceof Model)
		{
			this.put(key, ((Model) value).getAttrs());
		}
		if (value instanceof Record)
		{
			this.put(key, ((Record) value).getColumns());
		}

		if (value instanceof List)
		{
			List models = new ArrayList();
			for (Object obj : (List) value)
			{
				if (obj instanceof Model)
				{
					models.add(((Model) obj).getAttrs());
				}
				if (obj instanceof Record)
				{
					models.add(((Record) obj).getColumns());
				}

			}
			this.put(key, models);
		}

		return (M) this;
	}

	/***
	 * 取值
	 * 
	 * @return
	 */
	public Long getCount()
	{

		return getLong("count");
	}

	public Object getId()
	{
		return get("id");
	}
	

	public Integer getPid()
	{

		return getInt("pid");
	}

	public Integer getType()
	{
		return getInt("type");
	}

	/***
	 * return getStr("name");
	 * 
	 * @return
	 */
	public String getName()
	{
		return getStr("name");
	}

	/***
	 * return getStr("pwd");
	 * 
	 * @return
	 */
	public String getPwd()
	{
		return getStr("pwd");
	}

	/***
	 * return getStr("des");
	 * 
	 * @return
	 */
	public String getDes()
	{

		return getStr("des");
	}

	/***
	 * return getStr("date");
	 * 
	 * @return
	 */
	public String getDate()
	{

		return getStr("date");
	}

	public String getCreateDate()
	{

		return getStr("createdate");

	}

	public String getModifyDate()
	{

		return getStr("modifydate");
	}

	public String getIcon()
	{
		return getStr("icon");
	}

	public String getIconCls()
	{
		return getStr("iconCls");
	}

	public Integer getStatus()
	{
		return getInt("status");
	}

	public static String sql(String key)
	{

		return SqlKit.sql(key);
	}

}
