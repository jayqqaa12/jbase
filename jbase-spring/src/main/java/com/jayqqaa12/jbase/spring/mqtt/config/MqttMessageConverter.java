package com.jayqqaa12.jbase.spring.mqtt.config;

import com.alibaba.fastjson.JSON;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConversionException;

import java.nio.charset.Charset;

public class MqttMessageConverter extends DefaultPahoMessageConverter {

    protected byte[] messageToMqttBytes(Message<?> message) {
        Object payload = message.getPayload();

        if (!(payload instanceof byte[] || payload instanceof String)) {
            payload= JSON.toJSONString(payload);
        }

        byte[] payloadBytes;
        if (payload instanceof String) {
            try {
                payloadBytes = ((String) payload).getBytes(Charset.forName("UTF-8"));
            }
            catch (Exception e) {
                throw new MessageConversionException("failed to convert Message to object", e);
            }
        }
        else {
            payloadBytes = (byte[]) payload;
        }
        return payloadBytes;
    }

}
