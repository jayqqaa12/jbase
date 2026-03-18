package com.jayqqaa12.jbase.spring.serialize;

import com.google.gson.Gson;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class GsonRedisJsonSerializer<T> implements RedisSerializer<T> {

  private Gson gson = new Gson();

  @Override
  public byte[] serialize(Object t) throws SerializationException {
    return gson.toJson(t).getBytes();
  }

  @Override
  public T deserialize(byte[] bytes) throws SerializationException {
    if (bytes == null) {
      return null;
    }
    return (T) gson.fromJson(new String(bytes), Object.class);
  }



}
