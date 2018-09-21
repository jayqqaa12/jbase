package com.jayqqaa12.jbase.spring.boot.mqtt.handler;


import org.springframework.messaging.MessageHeaders;

public interface MqttHandler<T> {

    void handler(T payload, MessageHeaders headers);

}
