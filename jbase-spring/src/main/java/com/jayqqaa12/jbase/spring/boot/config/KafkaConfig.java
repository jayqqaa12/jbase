package com.jayqqaa12.jbase.spring.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;

/**
 * @author wangshuai
 */
@Configuration
@ConditionalOnClass(KafkaTemplate.class)
public class KafkaConfig {

  @Bean
  public ConsumerAwareListenerErrorHandler consumerErrorHandler() {
    return (message, e, consumer) -> {
      return null;
    };
  }


}
