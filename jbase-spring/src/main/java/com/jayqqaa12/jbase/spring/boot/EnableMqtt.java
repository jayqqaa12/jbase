package com.jayqqaa12.jbase.spring.boot;


import com.jayqqaa12.jbase.spring.boot.mqtt.config.MqttInboundConfiguration;
import com.jayqqaa12.jbase.spring.boot.mqtt.config.MqttOutboundConfiguration;
import com.jayqqaa12.jbase.spring.boot.mqtt.config.MqttProperties;
import com.jayqqaa12.jbase.spring.boot.mqtt.handler.MqttReceiver;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.IntegrationComponentScan;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MqttProperties.class,
        MqttInboundConfiguration.class,
        MqttOutboundConfiguration.class,
        MqttReceiver.class
})
@IntegrationComponentScan("com.jayqqaa12.jbase.spring.boot.mqtt")
public @interface EnableMqtt {

}
