package com.jayqqaa12.jbase.spring.mqtt.handler;


import org.springframework.messaging.MessageHeaders;

public interface MqttHandler<T> {

    void handler(T payload, MessageHeaders headers);

}
