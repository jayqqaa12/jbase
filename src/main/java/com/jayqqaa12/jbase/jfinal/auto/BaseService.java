/**
 * Copyright (c) 2011-2013, jayqqaa12 12shu (476335667@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayqqaa12.jbase.jfinal.auto;

import java.util.List;

import com.jayqqaa12.jbase.jfinal.ext.model.Model;
import com.jfinal.plugin.activerecord.Page;


/***
 * 与业务相关写在service 层
 * 
 * @author 12
 *
 * @param <M>
 */
public class BaseService<M extends Model> {

	protected M dao;
	

	protected BaseService<?> setDao(M dao){
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
	
	
	protected String sql(String key){
		return dao.sql(key);
	}

}
