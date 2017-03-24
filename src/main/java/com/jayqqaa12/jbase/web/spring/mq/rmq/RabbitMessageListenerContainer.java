package com.jayqqaa12.jbase.web.spring.mq.rmq;

import com.jayqqaa12.jbase.web.spring.mq.Consumser;
import com.jayqqaa12.jbase.web.spring.mq.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yhj on 17/3/22.
 */
public class RabbitMessageListenerContainer implements BeanFactoryAware, InitializingBean {
    private Logger logger = LoggerFactory.getLogger(getClass());
    BeanFactory factory;
    ConnectionFactory connectionFactory;
    Map<Queue, Listener> msgListeners = new HashMap<Queue, Listener>();

    public void setMsgListeners(Map<Queue, Listener> msgListeners) {
        this.msgListeners = msgListeners;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 配置上下文加载好之后，加载jms消息侦听容器
        for (Iterator iterator = msgListeners.keySet().iterator(); iterator.hasNext(); ) {
            Queue dest = (Queue) iterator.next();
            BeanDefinitionBuilder builder = BeanDefinitionBuilder
                    .genericBeanDefinition(SimpleMessageListenerContainer.class);
            builder.setLazyInit(false);
            builder.addPropertyValue("connectionFactory", connectionFactory);
            builder.addPropertyValue("queues", dest);
            Listener listener = msgListeners.get(dest);
            Consumser consumer = listener.getClass().getAnnotation(Consumser.class);
            int worker = consumer == null || consumer.value() < 1 ? 1 : consumer.value();
            builder.addPropertyValue("messageListener", new RabbitMessageListener(dest, listener));
//			builder.addPropertyValue("maxConcurrentConsumers", worker);
            builder.addPropertyValue("concurrentConsumers", worker);
            DefaultListableBeanFactory fac = (DefaultListableBeanFactory) factory;
            String beanName = "consumer-" + dest.toString();
            fac.registerBeanDefinition(beanName, builder.getRawBeanDefinition());
            fac.getBean(beanName);
            logger.info("init a message listener {} for destination {}", listener.getClass(), dest);
        }
    }
}