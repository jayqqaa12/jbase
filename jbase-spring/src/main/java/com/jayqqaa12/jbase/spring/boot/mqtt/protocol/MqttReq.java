package com.jayqqaa12.jbase.spring.boot.mqtt.protocol;

import lombok.Data;

@Data
public class MqttReq<T> {

    /**
     * 请求序列 客户端传递用来标示 返回的消息是哪次请求
     */
    private String reqNo;

    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * 当前接口的版本号
     */
    private String  version;

    /**
     * 一般跟业务进行绑定
     * device-xx
     * android-xx
     * ios-xx
     */
    private String  clientId;

    /**
     * 设备唯一标示
     */
    private String deviceId;


    private T data;

    

}
