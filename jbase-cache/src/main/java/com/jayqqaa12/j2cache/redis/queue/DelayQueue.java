package com.jayqqaa12.j2cache.redis.queue;

public interface DelayQueue<T> {



    /**
     * @return 当消息已经被取出，等待确认的时间。等待确认时间过去后将被自动删除
     * @see #ack(String)
     */
    int getUnackTime();

    void setUnackTime(int time);

    boolean ack(String messageId);

    boolean setUnackTimeout(String messageId, long timeout);

    boolean setTimeout(String messageId, long timeout);

    Message get(String messageId);

    boolean contain(String  messageId);

    long size();

    void clear();

    boolean push(Message<T> message);
    /**
     * 开启消息队列监听
     */
    void listen();

    void close();
}
