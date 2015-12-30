package com.jayqqaa12.jbase.jfinal.ext.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jayqqaa12.jbase.util.Sec;
import com.jayqqaa12.jbase.util.Txt;
import com.jayqqaa12.jbase.util.Validate;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

/***
 * 
 * 不支持 联合主键  因为不建议使用
 * 
 * @author 12
 * 
 * 升级兼容多种数据库
 *
 * @param <M>
 */
public class Model<M extends com.jfinal.plugin.activerecord.Model<M>> extends com.jfinal.plugin.activerecord.Model<M> {

	private static final long serialVersionUID = 8924183967602127690L;

	/***
	 * 用来当 缓存名字 也用来 生成 简单sql
	 */
	public String tableName;

	private Class<? extends com.jfinal.plugin.activerecord.Model<M>> clazz;

	/***
	 * 反射获取 注解获得 tablename
	 */
	public Model() {
		if (tableName == null) {
			TableBind table = this.getClass().getAnnotation(TableBind.class);
			if (table != null) tableName = table.tableName();
			
			Type genericSuperclass = getClass().getGenericSuperclass();
			
			try{
				
			clazz = (Class<? extends com.jfinal.plugin.activerecord.Model<M>>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
			}
			catch(Exception e){
				throw new RuntimeException(" Can't new Model must new  extends sub class ");
			}
		}
	}

	// ///////////////////////////////////////////////////////////////

	/**
	 * 更新 指定 条件
	 * 
	 */
	public boolean updateByWhere(String key, Object value, String w, Object params) {
	
		
		return Db.update("update " + tableName + " set " + key + "=? " + w, value, params) > 0;
	}

	public boolean update(String key, Object value, Object id) {
		
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];
		
		 if (idKey == null)
           throw new ActiveRecordException("You can't update model without Primary Key.");
		
		return Db.update("update " + tableName + " set " + key + "=? where "+idKey+" =?", value, id) > 0;
	}

	public boolean updateAddOneById(String key, Object id) {
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];
		 if (idKey == null)
	           throw new ActiveRecordException("You can't update model without Primary Key.");
			
		return Db.update("update " + tableName + " set " + key + " =" + key + "+1 where "+idKey+" =?", id) > 0;
	}

	public boolean updateSubOneById(String key, Object id) {
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];
		 if (idKey == null)
	           throw new ActiveRecordException("You can't update model without Primary Key.");
			
		return Db.update("update " + tableName + " set " + key + " =" + key + "-1 where "+idKey+" =?", id) > 0;
	}

	
	
	/***
	 * ids 必需为 连续的 1，2，3 这样子
	 * 
	 * @param ids
	 */
	public boolean batchDelete(String ids) {
		if (Validate.isEmpty(ids)) return false;
		
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];
		
		 if (idKey == null)
	           throw new ActiveRecordException("You can't update model without Primary Key.");
			
		return Db.update("delete from " + tableName + " where "+idKey+" in (" + ids + ")") > 0;
	}
	
	
	/**
	 * 不支持 联合主键
	 */
	public boolean deleteById(  Object id) {

		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];
		
		 if (idKey == null)
	           throw new ActiveRecordException("You can't update model without Primary Key.");
	
		return Db.deleteById(tableName, idKey, id);

	}


	public M findByIdCache(Object id) {
		
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];
		 if (idKey == null)
	           throw new ActiveRecordException("You can't update model without Primary Key.");
			
		return super.findFirstByCache(tableName, id, "select * from " + tableName + " where "+idKey+" =?", id);
	}

	

	public M findFirstByWhere(String where, Object... params) {

		List<M> list = findAllByWhere(where, params);

		if (list != null && list.size() > 0) return list.get(0);

		else return null;
	}

	public boolean isFindByWhere(String where, Object... params) {

		return findAllByWhere(where, params).size() > 0;
	}

	/***
	 * 返回全部的数据  
	 * 
	 * @return
	 */
	public List<M> findAll() {

		return find(" select *from " + tableName);
	}


	/***
	 * 返回全部的数据   可设置where
	 * 
	 * @return
	 */
	public List<M> findAllByWhere(String where) {

		return find(" select *from " + tableName + " " + where);
	}
	
	
	/***
	 * 返回全部的数据   可设置where
	 * 
	 * @return
	 */
	public List<M> findAllByWhere(String where, Object... params) {

		return find(" select *from " + tableName + " " + where, params);
	}
 

	public List<M> findAllByCache() {
		return findByCache(" select *from " + tableName);
	}

	public List<M> findAllByCache(String key, String where, Object... params) {
		return findByCache(key, " select *from " + tableName + " " + where, params);
	}

	public List<M> findAllByCache(String where) {
		return findByCache(" select *from " + tableName + " " + where);
	}



	public int deleteAll() {

		return Db.update(" delete from " + tableName + " ");
	}

	/**
	 * 自动以sql 作为 key
	 * 
	 * @param sql
	 * @return
	 */
	public List<M> findByCache(String sql) {

		return super.findByCache(tableName, Sec.md5(sql), sql);
	}

	public List<M> findByCache(String key, String sql, Object... params) {
		return super.findByCache(tableName, key, sql, params);
	}

	public M findFirstByWhereCache(String key, String where, Object... params) {
		String sql = "select * from " + tableName + " " + where;
		return super.findFirstByCache(tableName, key, sql, params);
	}

	
	public long getAllCount() {
		return getCount(" ");
	}
	
	public long getCount(String sql) {
		
		return getCount(sql,new Object[]{});
	}
	/**
	 * 可以是 where
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public long getCount(String sql, Object... params) {

		if (sql.contains("select")) sql = Txt.split(sql.toLowerCase(), "from")[1];
		if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];
		return findFirst(" select count(*) as c from " + tableName + " " + sql, params).getLong("c");
	}


	
	public Page<M> findAll(int page, int size) {

		return paginate(page, size, "select *", "from "+tableName  );
	}

	/**
	 * 不需要加 limit  
	 * @param where
	 * @param page
	 * @param size
	 * @param param
	 * @return
	 */
	public Page<M> findAllByWhere(int page, int size,String where,  Object... param) {
		
		return paginate(page,size,"select * " , "from "+tableName + " " + where , param);
	}
	
	
	

	
	public Page<M> findAllByCache(int page, int size) {

		String key = Sec.md5( tableName +"findAllByCache"+page+""+size);
		
		return paginateByCache(tableName, key,page, size, "select *", "from "+tableName  );
	}

	/**
	 * 不需要加 limit  
	 * @param where
	 * @param page
	 * @param size
	 * @param param
	 * @return
	 */
	public Page<M> findAllByCache(String where, int page, int size, Object... param) {
		
		String key = Sec.md5( tableName +"findAllByWhereCache"+page+""+size);
		
		return paginateByCache(tableName, key, page,size,"select * " , "from "+tableName + " " + where , param);
	}
	
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@SuppressWarnings("unchecked")
	@Override
	public M set(String attr, Object value) {
		if (value instanceof String && Validate.isEmpty((String) value)) return (M) this;

		if (value != null) return super.set(attr, value);
		else return (M) this;
	}

	/***
	 * if empty remove the attr
	 * 
	 * @param attr
	 */
	public Model<M> emptyRemove(String attr) {
		if (get(attr) == null) remove(attr);

		return this;
	}

	public Model<M> emptyZreo(String attr) {
		if (get(attr) == null) set(attr, 0);
		return this;
	}



	/**
	 * 根据 id 判断的
	 */
	public boolean saveOrUpdate() {
		if (getId() != null) return update();
		else return save();
	}

	public M putModel(String key, Object value) {

		if (value instanceof Model) {
			this.put(key, ((Model) value).getAttrs());
		}
		if (value instanceof Record) {
			this.put(key, ((Record) value).getColumns());
		}

		if (value instanceof List) {
			List models = new ArrayList();
			for (Object obj : (List) value) {
				if (obj instanceof Model) {
					models.add(((Model) obj).getAttrs());
				}
				if (obj instanceof Record) {
					models.add(((Record) obj).getColumns());
				}

			}
			this.put(key, models);
		}

		return (M) this;
	}

	/***
	 * 
	 * id 为“id”时候用一下 联合主键就算了
	 * 
	 * @return
	 */
	public Object getId() {
		return get("id");
	}

	public boolean saveAndDate() {

		return this.setDate("date").save();
	}

	public boolean saveAndCreateDate() {
		this.setDate("createdate");
		return this.save();
	}

	public boolean updateAndModifyDate() {

		return this.setDate("modifydate").update();
	}

	public Map<String, Object> getAttrs() {
		return super.getAttrs();
	}

	public M setDate(String date) {
		return this.set(date, new Timestamp(System.currentTimeMillis()));

	}

	public static String sql(String key) {

		return SqlKit.sql(key);
	}

}
