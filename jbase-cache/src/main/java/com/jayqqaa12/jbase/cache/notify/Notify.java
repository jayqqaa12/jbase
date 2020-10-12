package com.jayqqaa12.jbase.cache.notify;

public interface Notify {


  /**
   *
   * 广播主动删除的cache 
   *
   */
  void send(Command command);

  /**
   *
   * 接受通知删除本地cache
   *
   */
  void receive(Command command);




}
