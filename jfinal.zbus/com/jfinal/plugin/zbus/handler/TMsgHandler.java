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
package com.jfinal.plugin.zbus.handler;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.zbus.net.core.Session;
import org.zbus.net.http.Message;
import org.zbus.net.http.Message.MessageHandler;

import com.jfinal.plugin.zbus.coder.Coder;
import com.jfinal.plugin.zbus.coder.JsonCoder;

/**
 * @ClassName: TMsgHandler
 * @Description: 泛型消息回调接口（自动转型）
 * @author 李飞 (lifei@wellbole.com)
 * @date 2015年8月2日 上午1:27:46
 * @since V1.0.0
 */
public abstract class TMsgHandler<T> implements MessageHandler {

	/**
	 * 范型类型
	 */
	private final Class<?> tClass;
	
	/**
	 * 编码解码器
	 */
	private final Coder coder = new JsonCoder();

	/**
	 * <p>
	 * Title: TMsgHandler
	 * </p>
	 * <p>
	 * Description: 构造函数
	 * </p>
	 * 
	 * @since V1.0.0
	 */
	public TMsgHandler() {
		tClass = getSuperClassGenricType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void handle(Message msg, Session session) throws IOException {
		Object obj;
		try {
			obj = coder.decode(tClass, msg);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		this.handle((T) obj);
	}

	/**
	 * @Title: handle
	 * @Description: 消费者收到消息后的处理函数，子类需实现此方法
	 * @param msg
	 *            收到的消息
	 * @since V1.0.0
	 */
	public abstract void handle(T msg);

	@SuppressWarnings("rawtypes")
	private Class getSuperClassGenricType() {
		Class<?> clazz = getClass();
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			throw new RuntimeException(clazz.getSimpleName() + "'s superclass not ParameterizedType");
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (!(params[0] instanceof Class)) {
			throw new RuntimeException(
					clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
		}
		return (Class<?>) params[0];
	}
}
