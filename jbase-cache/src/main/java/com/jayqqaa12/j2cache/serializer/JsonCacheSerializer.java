package com.jayqqaa12.j2cache.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonCacheSerializer implements CacheSerializer  {
	@Override
	public String name() {
		return "json";
	}


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
	public byte[] serialize(Object t)  {
		return JSON.toJSONBytes(t, features);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object deserialize(byte[] bytes)  {
		if (bytes == null)
			return null;
		return   JSON.parse(bytes, DEFAULT_PARSER_FEATURE);
	}
}
