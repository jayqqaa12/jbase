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

import org.zbus.mq.Producer;
import org.zbus.mq.Protocol.MqMode;
import org.zbus.net.http.Message;

import com.jfinal.plugin.zbus.ZbusPlugin;
import com.jfinal.plugin.zbus.coder.Coder;
import com.jfinal.plugin.zbus.coder.JsonCoder;

/**
 * @ClassName: MqSender
 * @Description: Mq泛型发送器
 * @author 李飞 (lifei@wellbole.com)
 * @date 2015年8月2日 下午6:46:51
 * @since V1.0.0
 */
public class MqSender<T> implements Sender<T>{
	/**
	 * 生产者
	 */
	private final Producer producer;
	
	/**
	 * 编码解码器
	 */
	private final Coder coder = new JsonCoder();

	/**
	 * <p>
	 * Title: Sender
	 * </p>
	 * <p>
	 * Description: 构建一个MQ发送器
	 * </p>
	 * 
	 * @param mq
	 *            MQ队列名
	 * @since V1.0.0
	 */
	public MqSender(String mq) {
		producer = new Producer(ZbusPlugin.getBroker(), mq, MqMode.MQ);
		try {
			producer.createMQ();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}
	}

	/**
	 * @Title: sendSync
	 * @Description: 发送对象到Mq(同步方式)
	 * @param obj
	 *            发送对象
	 * @throws IOException
	 * @throws InterruptedException 
	 * @since V1.0.0
	 */
	@Override
	public void sendSync(T obj) throws IOException, InterruptedException {
		Message msg = this.coder.encode(obj);
		producer.sendSync(msg);
	}
	
	/**
	 * @Title: sendAsync
	 * @Description: 发送对象到Mq(异步方式)
	 * @param obj
	 *            发送对象
	 * @throws IOException
	 * @since V1.0.0
	 */
	@Override
	public void sendAsync(T obj) throws IOException {
		Message msg = this.coder.encode(obj);
		producer.sendAsync(msg);
	}
}
