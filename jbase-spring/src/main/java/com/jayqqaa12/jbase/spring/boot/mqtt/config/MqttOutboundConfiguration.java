package com.jayqqaa12.jbase.spring.boot.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.concurrent.ThreadLocalRandom;

@Configuration
@ConditionalOnBean(MqttProperties.class)
public class MqttOutboundConfiguration {

  @Autowired
  private MqttProperties mqttProperties;

  @Bean
  @ServiceActivator(inputChannel = "mqttOutboundChannel")
  public MessageHandler mqttOutbound() {

    String[] serverURIs = mqttProperties.getOutbound().getUrls().split(",");
    DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
    factory.setPersistence(new MemoryPersistence());

    //最大缓存消息，设置太小会导致同一时间不能发送太多消息
    factory.getConnectionOptions().setMaxInflight(1000000);

    MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
    mqttConnectOptions.setServerURIs(serverURIs);
    mqttConnectOptions.setUserName(mqttProperties.getOutbound().getUsername());
    mqttConnectOptions.setPassword(mqttProperties.getOutbound().getPassword().toCharArray());
    mqttConnectOptions.setCleanSession(true);
    factory.setConnectionOptions(mqttConnectOptions);

    String clientId =
        mqttProperties.getOutbound().getClientId() + ThreadLocalRandom.current().nextInt(1000);

    MqttPahoMessageHandler messageHandler =
        new MqttPahoMessageHandler(clientId, factory);

    messageHandler.setAsync(true);
    messageHandler.setDefaultQos(mqttProperties.getOutbound().getQos());
    messageHandler.setDefaultRetained(false);

    messageHandler.setConverter(new MqttMessageConverter());

    return messageHandler;
  }

  @Bean
  public MessageChannel mqttOutboundChannel() {
    return new DirectChannel();
  }
}

