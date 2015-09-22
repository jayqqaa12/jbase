/**
 * Copyright (c) 2015, 玛雅牛［李飞］ (lifei@wellbole.com).
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
package com.jfinal.plugin.zbus.coder;

import java.util.Map;

import org.zbus.net.http.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**  
 * @ClassName: JsonCoder  
 * @Description: 基于Json实现的编码解码器  
 * @author 李飞 (lifei@wellbole.com)   
 * @param <T>
 * @date 2015年8月11日 上午1:43:25
 * @since V1.0.0  
 */
public class JsonCoder implements Coder {

	@Override
	public Message encode(Object obj) {
		Message msg = new Message();
		if (obj instanceof Model) {
			Map<String, Object> map = com.jfinal.plugin.activerecord.CPI.getAttrs((Model<?>) obj);
			msg.setBody(JSON.toJSONString(map));
		} else if (obj instanceof Record) {
			Map<String, Object> map = ((Record) obj).getColumns();
			msg.setBody(JSON.toJSONString(map));
		} else {
			msg.setBody(JSON.toJSONString(obj));
		}
		return msg;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object decode(Class<?> tClass, Message msg ) throws Exception {
		String textMsg = msg.getBodyString();
		if (Model.class.isAssignableFrom(tClass)) {
			// JFinal内置Model类型
			JSONObject jsonObj = JSON.parseObject(textMsg);
			Model model = (Model)tClass.newInstance();
			model.setAttrs(jsonObj);
			return model;
		} else if (Record.class.isAssignableFrom(tClass)) {
			// JFinal内置Record类型
			JSONObject jsonObj = JSON.parseObject(textMsg);
			Record rec = (Record)tClass.newInstance();
			rec.setColumns(jsonObj);
			return rec;
		} else {
			// 其他类型
			Object obj = JSON.parseObject(textMsg, tClass);
			return obj;
		}
	}
}
