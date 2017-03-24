package com.jayqqaa12.jbase.web.spring.mq;

import com.jayqqaa12.jbase.web.spring.mq.jms.JmsContainer;

/**
 * 
 *
 * 描述:jms消息接收处理器
 *
 * @author boyce
 * @created 2015年5月16日 下午4:34:03
 * @since v1.0.0
 */
public interface Listener {
	/**
	 * 
	 * 
	 * 描述:{@link JmsContainer}提供了关于destination与本Listener的绑定关系，
	 * {@link JmsContainer}本身接管了jms的消息确认。 如果本方法出现异常，或者返回值为false，则
	 * {@link JmsContainer}认为程序主动或者被动的表明关于message的处理是失败的，需要 回滚处理。
	 *
	 * @author boyce
	 * @created 2015年5月16日 下午4:34:33
	 * @since v1.0.0
	 * @param message
	 * @return
	 * @return boolean
	 */
	public void onMessage(String message);
}
