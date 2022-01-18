package com.jayqqaa12.jbase.spring.mqtt.config;


import lombok.Data;

@Data
public class MqttProp {
    private String urls;
    private String username;
    private String password;
    private String clientId;
    private String topics;
    private Integer qos;

}

