package com.jayqqaa12.jbase.spring.boot.mqtt.config;

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
        factory.setServerURIs(mqttProperties.getInbound().getUrls());
        factory.setPersistence(new MemoryPersistence());
        factory.setUserName(mqttProperties.getOutbound().getUsername());
        factory.setPassword(mqttProperties.getOutbound().getPassword());

        String[] inboundTopics = mqttProperties.getInbound().getTopics().split(",");

        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                        mqttProperties.getInbound().getClientId(),factory, inboundTopics);

        DefaultPahoMessageConverter converter= new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(true);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(converter);
        adapter.setQos(mqttProperties.getInbound().getQos());
        adapter.setOutputChannel(mqttInputChannel());

        return adapter;
    }


}
