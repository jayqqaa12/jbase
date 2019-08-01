//package com.jayqqaa12.jbase.spring.boot.base;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.support.converter.AbstractJsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConversionException;
//
//import java.io.IOException;
//
//
//public class FastAmqpConverter extends AbstractJsonMessageConverter {
//
//    private Logger logger = LoggerFactory.getLogger(FastAmqpConverter.class);
//
//    public FastAmqpConverter() {
//        this.setClassMapper(new FastAmqpClassMapper());
//    }
//
//    @Override
//    public Object fromMessage(Message message) throws MessageConversionException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("start convert message 2 object for:" + message.toString());
//        }
//        try {
//            String body = new String(message.getBody(), getDefaultCharset());
//
//            Class clazz = getClassMapper().toClass(message.getMessageProperties());
//
//            Object obj = JSONObject.parseObject(body, clazz);
//
//            if (logger.isDebugEnabled()) {
//                logger.debug("end convert message  2 object and result :" + obj);
//            }
//
//            return obj;
//        } catch (Exception e) {
//            throw new MessageConversionException("Failed to convert Message content", e);
//        }
//    }
//
//
//    @Override
//    protected Message createMessage(Object o, MessageProperties messageProperties)
//            throws MessageConversionException {
//        byte[] bytes = null;
//
//        if (logger.isDebugEnabled()) {
//            logger.debug("start convert object 2 message for:" + o.toString());
//        }
//
//        try {
//            String msg = JSON.toJSONString(o);
//            bytes = msg.getBytes(getDefaultCharset());
//        } catch (IOException e) {
//            throw new MessageConversionException("Failed to convert Message content", e);
//        }
//        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
//        messageProperties.setContentEncoding(getDefaultCharset());
//        messageProperties.setContentLength(bytes.length);
//
//        getClassMapper().fromClass(o.getClass(), messageProperties);
//
//        Message message = new Message(bytes, messageProperties);
//        if (logger.isDebugEnabled()) {
//            logger.debug("end convert object 2 message for:" + message);
//        }
//
//        return message;
//
//    }
//
//
//}
