package com.jayqqaa12.jbase.spring.boot.config;

import com.jayqqaa12.jbase.spring.boot.base.JmsJsonConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;


@Configuration
public class JmsConfig {

    public static final String LISTENER_TOPIC = "jmsTopicListener";
    public static final String LISTENER_QUEUE = "jmsQueueListener";

    @Autowired
    ConnectionFactory connectionFactory;

    @Bean
    public JmsJsonConvertor jmsJsonConvertor() {
        return new JmsJsonConvertor();
    }



    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate=  new JmsTemplate();
        jmsTemplate.setMessageConverter(jmsJsonConvertor());
        jmsTemplate.setConnectionFactory(connectionFactory);
        return jmsTemplate;
    }





    @Bean("jmsTopicListener")
    public DefaultJmsListenerContainerFactory jmsTopicListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        factory.setMessageConverter(jmsJsonConvertor());
        return factory;
    }


    @Bean("jmsQueueListener")
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        factory.setMessageConverter(jmsJsonConvertor());
        return factory;
    }

}

