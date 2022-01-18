package com.jayqqaa12.jbase.spring.mqtt.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "config.mqtt",name = "enabled",havingValue = "true")
@ConfigurationProperties(prefix = "config.mqtt")
@Configuration
public class MqttProperties {
    private MqttProp inbound;
    private MqttProp outbound;

    public MqttProp getInbound() {
        return inbound;
    }

    public void setInbound(MqttProp inbound) {
        this.inbound = inbound;
    }

    public MqttProp getOutbound() {
        return outbound;
    }
    public void setOutbound(MqttProp outbound) {
        this.outbound = outbound;
    }



}

 