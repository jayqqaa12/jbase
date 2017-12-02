package com.jayqqaa12.jbase.spring.boot.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class RedisJsonSerializer<T> implements RedisSerializer<T> {


	private static final SerializerFeature[] features = {
			SerializerFeature.WriteEnumUsingToString,
			SerializerFeature.SortField, SerializerFeature.SkipTransientField,
			SerializerFeature.WriteClassName };

	private static final Feature[] DEFAULT_PARSER_FEATURE = {
			Feature.AutoCloseSource, Feature.InternFieldNames,
			Feature.AllowUnQuotedFieldNames, Feature.AllowSingleQuotes,
			Feature.AllowArbitraryCommas, Feature.SortFeidFastMatch,
			Feature.IgnoreNotMatch };

	@Override
	public byte[] serialize(Object t) throws SerializationException {
		return JSON.toJSONBytes(t, features);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null)
			return null;
		return (T) JSON.parse(bytes, DEFAULT_PARSER_FEATURE);
	}
	

}
