package com.jayqqaa12.jbase.spring.serialize;

import com.alibaba.fastjson2.JSON;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.JSONWriter.Feature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class FastJsonRedisJsonSerializer<T> implements RedisSerializer<T> {


	private static final JSONWriter.Feature[] features = {
			JSONWriter.Feature.WriteEnumUsingToString,
			JSONWriter.Feature.WriteClassName };


	@Override
	public byte[] serialize(Object t) throws SerializationException {
		return JSON.toJSONBytes(t, features);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null)
			return null;
		return (T) JSON.parse(bytes,JSONReader.Feature.SupportAutoType);
	}


}
