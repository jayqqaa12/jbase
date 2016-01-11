package com.jayqqaa12.jbase.jfinal.auto;

import java.util.List;

import com.jayqqaa12.jbase.jfinal.ext.model.Model;
import com.jfinal.plugin.activerecord.Page;

public class BaseService<M extends Model> {

	protected Model dao;
	

	protected BaseService setDao(Model dao){
		this.dao=dao;
		return this;
	}
	

	public Page<M> findAll(int p, int c) {
		return dao.findAll(p, c);
	}

	public M findById(Object id) {

		return (M) dao.findById(id);
	}

	public List<M> findAll() {
		return dao.findAll();
	}

	public boolean update(M m) {

		return m.update();
	}

	public boolean save(M m) {
		return m.save();
	}

	public boolean deleteAll() {
		return dao.deleteAll() > 0;
	}

	public Long getAllCount() {
		return dao.getAllCount();
	}

	public List<M> findAllByCache() {

		return dao.findAllByCache();
	}

}
