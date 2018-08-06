package com.jayqqaa12.jbase.spring.boot.config;

import com.jayqqaa12.jbase.spring.boot.base.FastAmqpConverter;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RabbitTemplate.class)
@EnableRabbit
public class RabbitConfig {

    @Bean
    public FastAmqpConverter fastAmqpConverter() {
        return new FastAmqpConverter();
    }

    //
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//
//
//        RabbitTemplate rabbitTemplate = new RabbitTemplate();
//
//        rabbitTemplate.setMessageConverter(fastAmqpConverter());
//        rabbitTemplate.setConnectionFactory(connectionFactory);
//
//        return rabbitTemplate;
//    }
//
//
//    public SimpleRabbitListenerContainerFactory containerFactory(){
//
//
//    }


}
