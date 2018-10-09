package com.jayqqaa12.jbase.spring.boot.mqtt.protocol;

import com.jayqqaa12.jbase.spring.mvc.RespCode;
import lombok.Data;

@Data
public class MqttResp {
    /**
     * 请求序列 客户端传递用来标示 返回的消息是哪次请求
     */
    private String reqNo;

    /**
     * 返回码
     * 
     */
    private int code= RespCode.SUCCESS;
    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 提示信息
     */
    private String  msg;

    private Object data;


}
