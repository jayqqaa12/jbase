package com.jayqqaa12.jbase.jfinal.ext.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.google.common.collect.Lists;
import com.jayqqaa12.jbase.jfinal.ext.exception.NullModelException;
import com.jayqqaa12.jbase.util.Sec;
import com.jayqqaa12.jbase.util.Txt;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

/***
 * 
 * 不支持 联合主键 因为不建议使用
 * 
 * @author 12
 * 
 *         升级兼容多种数据库
 *
 * @param <M>
 */
public class Model<M extends com.jfinal.plugin.activerecord.Model<M>> extends com.jfinal.plugin.activerecord.Model<M> {

	private static final long serialVersionUID = 8924183967602127690L;

	private Class<? extends com.jfinal.plugin.activerecord.Model<M>> clazz;

	public String TABLENAME;

	public Model() {

		Type genericSuperclass = getClass().getGenericSuperclass();

		try {

			clazz = (Class<? extends com.jfinal.plugin.activerecord.Model<M>>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
			loadTableName();
		} catch (Exception e) {
			throw new RuntimeException(" Can't new Model must new  extends sub class ");
		}
	}

	private void loadTableName() {

		if (clazz != null && TableMapping.me().getTable(clazz) != null) TABLENAME = TableMapping.me().getTable(clazz).getName();
	}

	// ///////////////////////////////////////////////////////////////

	public void deletePastData(String key) {

		loadTableName();
		Db.update(" delete from " + TABLENAME + " where " + key + " <curdate() - interval 1 month ");
	}

	/**
	 * 更新 指定 条件
	 * 
	 */
	public boolean updateByWhere(String key, Object value, String w, Object... params) {
		loadTableName();
		Object[] p = ArrayUtils.addAll(new Object[] { value }, params);

		return Db.update("update " + TABLENAME + " set " + key + "=? " + w, p) > 0;
	}

	public boolean update(String key, Object value, Object id) {
		loadTableName();
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];

		if (idKey == null) throw new ActiveRecordException("You can't update model without Primary Key.");

		return Db.update("update " + TABLENAME + " set " + key + "=? where " + idKey + " =?", value, id) > 0;
	}

	public boolean updateAddOneById(String key, Object id) {
		loadTableName();
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];
		if (idKey == null) throw new ActiveRecordException("You can't update model without Primary Key.");

		return Db.update("update " + TABLENAME + " set " + key + " =" + key + "+1 where " + idKey + " =?", id) > 0;
	}

	public boolean updateSubOneById(String key, Object id) {
		loadTableName();
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];
		if (idKey == null) throw new ActiveRecordException("You can't update model without Primary Key.");

		return Db.update("update " + TABLENAME + " set " + key + " =" + key + "-1 where " + idKey + " =?", id) > 0;
	}

	/***
	 * ids 必需为 连续的 1，2，3 这样子
	 * 
	 * @param ids
	 */
	public boolean batchDelete(String ids) {
		if (StrKit.isBlank(ids)) return false;
		loadTableName();

		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];

		if (idKey == null) throw new ActiveRecordException("You can't update model without Primary Key.");

		return Db.update("delete from " + TABLENAME + " where " + idKey + " in (" + ids + ")") > 0;
	}

	/**
	 * 不支持 联合主键
	 */
	public boolean deleteById(Object id) {
		loadTableName();
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];

		if (idKey == null) throw new ActiveRecordException("You can't update model without Primary Key.");

		return Db.deleteById(TABLENAME, idKey, id);

	}

	public M findByIdNotNull(Object idValue) {
		M m = findById(idValue);
		if (m == null) throw new NullModelException();
		return m;
	}

	public boolean deleteByWhere(String where, Object... param) {
		loadTableName();
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];

		if (idKey == null) throw new ActiveRecordException("You can't update model without Primary Key.");

		return Db.update("delete from " + TABLENAME + " " + where, param) > 0;
	}

	public M findByIdCache(Object id) {
		loadTableName();
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];
		if (idKey == null) throw new ActiveRecordException("You can't update model without Primary Key.");

		return super.findFirstByCache(TABLENAME, id, "select * from " + TABLENAME + " where " + idKey + " =?", id);
	}

	public M findFirstByWhere(String where, Object... params) {

		List<M> list = findAllByWhere(where, params);

		if (list != null && list.size() > 0) return list.get(0);

		else return null;
	}

	public M findFirstByWhereNotNull(String where, Object... params) {

		List<M> list = findAllByWhere(where, params);

		if (list != null && list.size() > 0) return list.get(0);

		else throw new NullModelException();
	}

	public boolean isFindByWhere(String where, Object... params) {
		loadTableName();
		String idKey = TableMapping.me().getTable(clazz).getPrimaryKey()[0];
		if (idKey == null) throw new ActiveRecordException("You can't update model without Primary Key.");

		return find(" select " + idKey + " from " + TABLENAME + " " + where, params).size() > 0;
	}

	/***
	 * 返回全部的数据
	 * 
	 * @return
	 */
	public List<M> findAll() {
		loadTableName();
		return find(" select *from " + TABLENAME);
	}

	/***
	 * 返回全部的数据 可设置where
	 * 
	 * @return
	 */
	public List<M> findAllByWhere(String where) {
		loadTableName();
		return find(" select *from " + TABLENAME + " " + where);
	}

	/***
	 * 返回全部的数据 可设置where
	 * 
	 * @return
	 */
	public List<M> findAllByWhere(String where, Object... params) {
		loadTableName();
		return find(" select *from " + TABLENAME + " " + where, params);
	}

	public List<M> findAllByCache() {
		loadTableName();
		return findByCache(" select *from " + TABLENAME);
	}

	public List<M> findAllByCache(String key, String where, Object... params) {
		loadTableName();
		return findByCache(key, " select *from " + TABLENAME + " " + where, params);
	}

	public List<M> findAllByCache(String where) {
		loadTableName();
		return findByCache(" select *from " + TABLENAME + " " + where);
	}

	public int deleteAll() {
		loadTableName();
		return Db.update(" delete from " + TABLENAME + " ");
	}

	/**
	 * 自动以sql 作为 key
	 * 
	 * @param sql
	 * @return
	 */
	public List<M> findByCache(String sql) {
		loadTableName();
		return super.findByCache(TABLENAME, Sec.md5(sql), sql);
	}

	public List<M> findByCache(String key, String sql, Object... params) {
		loadTableName();
		return super.findByCache(TABLENAME, key, sql, params);
	}

	public M findFirstByWhereCache(String key, String where, Object... params) {
		loadTableName();
		String sql = "select * from " + TABLENAME + " " + where;
		return super.findFirstByCache(TABLENAME, key, sql, params);
	}

	public long getAllCount() {
		return getCount(" ");
	}

	public long getCount(String sql) {

		return getCount(sql, new Object[] {});
	}

	/**
	 * 可以是 where
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public long getCount(String sql, Object... params) {
		loadTableName();
		if (sql.contains("select")) sql = Txt.split(sql.toLowerCase(), "from")[1];
		if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];
		return findFirst(" select count(*) as c from " + TABLENAME + " " + sql, params).getLong("c");
	}

	public Page<M> findAll(int page, int size) {
		loadTableName();
		return paginate(page, size, "select *", "from " + TABLENAME);
	}

	public Page<M> findAll(int page, int size, String sql, Object... param) {

		return paginate(page, size, sql, param);
	}

	/**
	 * 不需要加 limit
	 * 
	 * @param where
	 * @param page
	 * @param size
	 * @param param
	 * @return
	 */
	public Page<M> findAllByWhere(int page, int size, String where, Object... param) {
		loadTableName();
		return paginate(page, size, "select * from " + TABLENAME + " " + where, param);
	}

	public Page<M> findAllByCache(int page, int size) {
		loadTableName();
		String key = Sec.md5(TABLENAME + "findAllByCache" + page + "" + size);

		return paginateByCache(TABLENAME, key, page, size, "select * from " + TABLENAME);
	}

	/**
	 * 不需要加 limit
	 * 
	 * @param where
	 * @param page
	 * @param size
	 * @param param
	 * @return
	 */
	public Page<M> findAllByCache(String where, int page, int size, Object... param) {
		loadTableName();
		String key = Sec.md5(TABLENAME + "findAllByWhereCache" + page + "" + size);

		return paginateByCache(TABLENAME, key, page, size, "select *from " + TABLENAME + " " + where, param);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

		if (value instanceof Model) this.put(key, ((Model<?>) value).getAttrs());
		if (value instanceof Record) this.put(key, ((Record) value).getColumns());
		if (value instanceof Page) value = ((Page<?>) value).getList();

		if (value instanceof List) {
			List models = Lists.newArrayList();
			for (Object obj : (List) value) {
				if (obj instanceof Model) {
					models.add(((Model<?>) obj).getAttrs());
				}
				if (obj instanceof Record) {
					models.add(((Record) obj).getColumns());
				}

			}
			this.put(key, models);
		}

		return (M) this;
	}

	/**
	 * @param key
	 * @return
	 */
	public boolean isNull(String key) {
		return getAttrs().get(key) == null;
	}

	/**
	 * @param key
	 * @return
	 */
	public boolean notNull(String key) {
		return getAttrs().get(key) != null;
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

	private M setDate(String date) {
		return this.set(date, new Timestamp(System.currentTimeMillis()));
	}

	public static String sql(String key) {

		return SqlKit.sql(key);
	}

}
