// Copyright 2020 Leyantech Ltd. All Rights Reserved.

package com.jayqqaa12.jbase.cache.notify.rocketmq;

import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.core.JbaseCache;
import com.jayqqaa12.jbase.cache.notify.Command;
import com.jayqqaa12.jbase.cache.notify.Notify;
import com.jayqqaa12.jbase.cache.serializer.CacheSerializer;
import com.jayqqaa12.jbase.cache.util.CacheException;
import com.jayqqaa12.jbase.cache.util.UniqueKit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * @author 12, {@literal <shuai.wang@leyantech.com>}
 * @date 2020-10-29.
 */
@Slf4j
public class RocketMqNotify implements Notify, MessageListenerConcurrently {

  private JbaseCache cache;
  private String topic;
  private CacheSerializer cacheSerializer;
  private DefaultMQProducer producer;
  private DefaultMQPushConsumer consumer;

  @Override
  public void init(CacheConfig cacheConfig, JbaseCache cache)
      throws ClassNotFoundException, Exception {
    this.cache = cache;
    this.topic = cacheConfig.getNotifyConfig().getNotifyTopic();
    this.cacheSerializer = (CacheSerializer) Class
        .forName(cacheConfig.getCacheSerializerClass()).newInstance();

    this.producer = new DefaultMQProducer(
        cacheConfig.getNotifyConfig().getGroupId());
    producer.setNamesrvAddr(cacheConfig.getNotifyConfig().getHost());
    producer.start();

    this.consumer = new DefaultMQPushConsumer(
        cacheConfig.getNotifyConfig().getGroupId() + "-" + UniqueKit.JVM_PID);
    consumer.setNamesrvAddr(cacheConfig.getNotifyConfig().getHost());
    consumer.subscribe(topic, "*");

    consumer.setMessageListener(this);
    consumer.start();

  }

  @Override
  public void send(Command command) throws CacheException {

    try {

      producer.send(new Message(topic, cacheSerializer.serialize(command)), new SendCallback() {
        @Override
        public void onSuccess(SendResult sendResult) {
          log.debug("rocketmq send notify key={}@{}", command.getRegion(), command.getKeys());
        }

        @Override
        public void onException(Throwable throwable) {
          log.error("rocketmq send notify error key={}@{}", command.getRegion(), command.getKeys(),
              throwable);
        }
      });
    } catch (Exception e) {
      log.error("rocketmq send notify error key={}@{}", command.getRegion(), command.getKeys(), e);
      throw new CacheException(e);
    }

  }

  @Override
  public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
      ConsumeConcurrentlyContext context) {
    for (MessageExt msg : msgs) {
      Command command = (Command) cacheSerializer.deserialize(msg.getBody());
      log.debug("kafka receive notify  command {} ", command);
      cache.handlerCommand(command);
    }
    // 消息消费成功
    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
  }

  @Override
  public void stop() {

    producer.shutdown();
    consumer.shutdown();

  }


}
