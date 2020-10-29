package com.jayqqaa12.jbase.cache.notify;

import com.jayqqaa12.jbase.cache.core.CacheConfig;
import com.jayqqaa12.jbase.cache.core.JbaseCache;
import com.jayqqaa12.jbase.cache.util.CacheException;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

public interface Notify {

  void init(CacheConfig cacheConfig, JbaseCache cache) throws ClassNotFoundException, Exception;


  /**
   *
   * 广播主动删除的cache 
   *
   */
  void send(Command command) throws CacheException;


  void stop();




}
