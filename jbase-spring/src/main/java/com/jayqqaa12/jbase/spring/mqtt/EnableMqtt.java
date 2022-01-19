package com.jayqqaa12.jbase.spring.mqtt;


import com.jayqqaa12.jbase.spring.mqtt.config.MqttInboundConfiguration;
import com.jayqqaa12.jbase.spring.mqtt.config.MqttOutboundConfiguration;
import com.jayqqaa12.jbase.spring.mqtt.config.MqttProperties;
import com.jayqqaa12.jbase.spring.mqtt.handler.MqttReceiver;
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
@IntegrationComponentScan("com.jayqqaa12.jbase.spring.mqtt")
public @interface EnableMqtt {

}
