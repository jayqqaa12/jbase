package com.jayqqaa12.jbase.spring.boot.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.jayqqaa12.j2cache.core.J2Cache;
import com.jayqqaa12.jbase.spring.boot.base.JmsMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Created by 12 on 2017/11/20.
 * <p>
 * 依赖j2cache
 */
public class JmsJsonConvertor implements MessageConverter {

    public static final String JMS_CACHE_KEY = "JMS_MSG:";

    @Autowired
    J2Cache j2cache;

    @Override
    public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {
        return session.createTextMessage(JSON.toJSONString(new JmsMsg().setData(o).setMsgId(IdWorker.getId()), SerializerFeature.WriteClassName));
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        if (message instanceof TextMessage) {
            String text = ((TextMessage) message).getText();
            JmsMsg jmsMsg = null;
            try {
                 jmsMsg = JSON.parseObject(text, JmsMsg.class);
            }catch (Exception e){
                return text;
            }

            Long msgId = jmsMsg.getMsgId();

            // 不符合标准的jms 可能是旧系统的直接返回
            if (msgId == null) return text;

            //判断一下如果 msg id 存在就返回null
            if (j2cache.get1(JMS_CACHE_KEY , msgId) == null) {
                j2cache.set1n(JMS_CACHE_KEY , msgId, "", 60*60);
                return jmsMsg.getData();
            } else throw new MessageConversionException("MSG id exist" + msgId + "  filter");
        }

        return message;
    }
}
