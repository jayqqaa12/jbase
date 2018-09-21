package com.jayqqaa12.jbase.spring.boot.mqtt.handler;

import com.alibaba.fastjson.JSON;
import com.jayqqaa12.jbase.spring.boot.mqtt.protocol.MqttReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Component
public class MqttReceiver {

    @Autowired
    List<MqttHandler> handlerList;


    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return (message) -> {
            String topic = (String) message.getHeaders().get(MqttHeaders.TOPIC);

            byte[] payload = (byte[]) message.getPayload();

            log.info("receiver byte len {}", payload.length);

            try {

                for (MqttHandler mqttHandler : handlerList) {
                    TopicMapping topicMapping = mqttHandler.getClass().getAnnotation(TopicMapping.class);
                    if (topicMapping == null) throw new RuntimeException("MqttHandler must use @TopicMapping ");

                    Type type = mqttHandler.getClass().getGenericInterfaces()[0];

                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type[] types = parameterizedType.getActualTypeArguments();

                    Type rawType = types[0];
                    if (rawType instanceof ParameterizedType) {
                        rawType = ((ParameterizedType) rawType).getRawType();
                    }
                    Class clazz = (Class) rawType;

                    // 这里要判断一下 topic
                    if (isContainsTopic(topic, topicMapping.value())) {
                        Object data = payload;
                        if (clazz.isAssignableFrom(String.class))
                            data = new String(payload, Charset.defaultCharset());
                        else if (clazz.isAssignableFrom(MqttReq.class)) {
                            data = JSON.parseObject(payload, types[0]);
                        }

                        mqttHandler.handler(data, message.getHeaders());
                    }
                }
            } catch (Exception e) {
                log.info("mqtt handler error {}", e);
            }
        };
    }


    private static boolean isContainsTopic(String t1, String t2) {
        if (t1.equals(t2)) return true;
        int index = t1.split("/").length;
        String[] arrays = t2.split("/");

        //匹配下一级
        if (t2.contains("+") && t1.startsWith(t2.replaceAll("\\+", ""))) {

            if (arrays.length > index) return false;

            return true;
            //匹配剩下的所有
        } else if (t2.contains("#") && t1.startsWith(t2.replaceAll("#", ""))) {

            return true;
        }

        return false;
    }


}
