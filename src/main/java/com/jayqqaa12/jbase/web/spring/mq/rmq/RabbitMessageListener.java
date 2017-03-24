package com.jayqqaa12.jbase.web.spring.mq.rmq;

import com.jayqqaa12.jbase.web.spring.mq.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;

import java.io.UnsupportedEncodingException;

/**
 * Created by yhj on 17/3/22.
 */
public class RabbitMessageListener implements MessageListener {

    public static final Logger logger = LoggerFactory.getLogger(RabbitMessageListener.class);

    private Listener listener;
    private Queue dest;

    public RabbitMessageListener(Queue dest, Listener listener) {
        this.listener = listener;
        this.dest = dest;
    }

    @Override
    public void onMessage(Message message) {

        byte[] body = message.getBody();

        MessageProperties messageProperties = message.getMessageProperties();

        try {
            handleString(new String(body, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("转换异常:", e);
        }
    }

    private void handleString(String str) {
        listener.onMessage(str);
    }

}
