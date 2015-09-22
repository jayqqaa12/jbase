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
package com.jfinal.plugin.zbus;

/**
 * @ClassName: Topic  
 * @Description: Topic主题对象  
 * @author 李飞 (lifei@wellbole.com)   
 * @date 2015年8月20日 下午11:10:15
 * @since V1.0.0
 */
public final class Topic {
	private final String mqName;
	private final String topicName;
	public Topic(String mqName, String topicName) {
		this.mqName = mqName;
		this.topicName = topicName;
	}
	public final String getMqName() {
		return mqName;
	}
	public final String getTopicName() {
		return topicName;
	}
}
