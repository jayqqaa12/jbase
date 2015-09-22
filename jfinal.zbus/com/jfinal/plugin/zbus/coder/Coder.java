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

import org.zbus.net.http.Message;

/**  
 * @ClassName: Coder  
 * @Description: 编码解码器  
 * @author 李飞 (lifei@wellbole.com)   
 * @date 2015年8月11日 上午1:41:14
 * @since V1.0.0  
 */
public interface Coder {
	/**
	 * @Title: encode  
	 * @Description: 将一个Object对象转化为一个Message对象  
	 * @param obj Object对象
	 * @return Message消息
	 * @since V1.0.0
	 */
	public Message encode(Object obj);
	
	/**
	 * @Title: decode  
	 * @Description: 将一个Message对象依据类型转化成一个Object对象  
	 * @param tClass 类型
	 * @param msg Message消息
	 * @return Object对象
	 * @throws Exception 
	 * @since V1.0.0
	 */
	public Object decode(Class<?> tClass, Message msg ) throws Exception;
}
