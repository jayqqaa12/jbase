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
package com.jfinal.plugin.zbus.sender;

import java.io.IOException;

/**  
 * @ClassName: Sender  
 * @Description: 发送对象到MQ／topic  
 * @author 李飞 (lifei@wellbole.com)   
 * @date 2015年8月11日 上午1:20:27
 * @since V1.0.0  
 */
public interface Sender<T> {
	/**
	 * @Title: sendSync
	 * @Description: 发送对象到MQ／topic（同步方式）
	 * @param obj
	 *            发送对象
	 * @throws IOException
	 * @since V1.0.0
	 */
	public void sendSync(T obj) throws IOException, InterruptedException;
	
	/**
	 * @Title: sendAsync  
	 * @Description: 发送对象到MQ／topic（异步方式）
	 * @param obj
	 *            发送对象
	 * @throws IOException
	 * @since V1.0.0
	 */
	public void sendAsync(T obj) throws IOException;
	
}
