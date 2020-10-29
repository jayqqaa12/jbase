package com.jayqqaa12.jbase.cache.notify.kafka;

import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.core.JbaseCache;
import com.jayqqaa12.jbase.cache.notify.Command;
import com.jayqqaa12.jbase.cache.notify.Notify;
import com.jayqqaa12.jbase.cache.serializer.CacheSerializer;
import com.jayqqaa12.jbase.cache.util.UniqueKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteBufferDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 12
 */
@Slf4j
public class KafkaNotify implements Notify, Runnable {

  private KafkaProducer<String, byte[]> producer;
  private KafkaConsumer<String, byte[]> consumer;

  private String topic;
  private CacheSerializer cacheSerializer;
  private boolean running;
  private ExecutorService executorService;
  private JbaseCache cache;


  @Override
  public void init(CacheConfig cacheConfig, JbaseCache cache)
      throws Exception {
    this.topic = cacheConfig.getNotifyConfig().getNotifyTopic();
    this.cacheSerializer = (CacheSerializer) Class
        .forName(cacheConfig.getCacheSerializerClass()).newInstance();
    this.cache=cache;

    Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, cacheConfig.getNotifyConfig().getHost());
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteBufferDeserializer.class);
    configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    configs.put(ConsumerConfig.GROUP_ID_CONFIG,
        cacheConfig.getNotifyConfig().getGroupId() + "-" + UniqueKit.JVM_PID);

    consumer = new KafkaConsumer<>(configs);
    consumer.subscribe(Arrays.asList(topic));
    producer = new KafkaProducer<>(configs);

    this.running = true;
    this.executorService = new ThreadPoolExecutor(1, 1,
        0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1));
    this.executorService.execute(this);

  }

  @Override
  public void stop() {
    this.running = false;
    executorService.shutdown();
    producer.close();
    consumer.close();

  }


  @Override
  public void send(Command command) {
    ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic,
        cacheSerializer.serialize(command));
    producer.send(record, (RecordMetadata metadata, Exception exception) -> {
      if (exception != null) {
        log.error("kafka send notify  error  key={}@{}", command.getRegion(), command.getKeys(),
            exception);
      }
    });

    log.debug("kafka send notify key={}@{}", command.getRegion(), command.getKeys());
  }


  @Override
  public void run() {

    while (running) {
      final ConsumerRecords<String, byte[]> records = consumer.poll(1_000);
      records.forEach((record) -> {
        Command command = (Command) cacheSerializer.deserialize(record.value());

        log.debug("kafka receive notify  command {} ", command);
        cache.handlerCommand(command);

      });

    }

  }




}
