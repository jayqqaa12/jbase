package com.jayqqaa12.jbase.spring.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

import java.util.concurrent.ThreadLocalRandom;

@Configuration
@ConditionalOnBean(MqttProperties.class)
public class MqttInboundConfiguration {

  @Autowired
  private MqttProperties mqttProperties;

  @Bean
  public MessageChannel mqttInputChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageProducer inbound() {

    DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
    factory.setPersistence(new MemoryPersistence());

    MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
    mqttConnectOptions.setServerURIs(new String[]{mqttProperties.getInbound().getUrls()});
    mqttConnectOptions.setUserName(mqttProperties.getInbound().getUsername());
    mqttConnectOptions.setPassword(mqttProperties.getInbound().getPassword().toCharArray());

    factory.setConnectionOptions(mqttConnectOptions);

    //
//        Properties sslClientProps = new Properties();
//        sslClientProps.setProperty(SSLSocketFactoryFactory.SSLPROTOCOL, mqttProperties.getProtocol());
//        sslClientProps.setProperty(SSLSocketFactoryFactory.KEYSTORE, mqttProperties.getKeyStore());
//        sslClientProps.setProperty(SSLSocketFactoryFactory.KEYSTOREPWD, mqttProperties.getKeyStorePassword());
//        sslClientProps.setProperty(SSLSocketFactoryFactory.KEYSTORETYPE, mqttProperties.getKeyStoreType());
//        sslClientProps.setProperty(SSLSocketFactoryFactory.TRUSTSTORE, mqttProperties.getTrustStore());
//        sslClientProps.setProperty(SSLSocketFactoryFactory.TRUSTSTOREPWD, mqttProperties.getTrustStorePassword());
//        sslClientProps.setProperty(SSLSocketFactoryFactory.TRUSTSTORETYPE, mqttProperties.getTrustStoreType());
//
//        factory.setSslProperties(sslClientProps);

    String[] inboundTopics = mqttProperties.getInbound().getTopics().split(",");

    String clientId =
        mqttProperties.getInbound().getClientId() + ThreadLocalRandom.current().nextInt(1000);

    MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
        clientId, factory, inboundTopics);

    DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
    converter.setPayloadAsBytes(true);
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(converter);
    adapter.setQos(mqttProperties.getInbound().getQos());
    adapter.setOutputChannel(mqttInputChannel());

    return adapter;
  }


}
