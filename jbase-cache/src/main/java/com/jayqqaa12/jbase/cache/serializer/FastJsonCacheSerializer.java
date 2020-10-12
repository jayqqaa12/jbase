package com.jayqqaa12.jbase.cache.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jayqqaa12.jbase.cache.core.CacheObject;

public class FastJsonCacheSerializer implements CacheSerializer {

  private static final SerializerFeature[] features = {
      SerializerFeature.WriteEnumUsingToString,
      SerializerFeature.SortField,
      SerializerFeature.SkipTransientField,
      SerializerFeature.WriteClassName

  };

  private static final Feature[] DEFAULT_PARSER_FEATURE = {
      Feature.SupportAutoType,
      Feature.AutoCloseSource, Feature.InternFieldNames,
      Feature.AllowUnQuotedFieldNames, Feature.AllowSingleQuotes,
      Feature.AllowArbitraryCommas, Feature.SortFeidFastMatch,
      Feature.IgnoreNotMatch
  };

  @Override
  public byte[] serialize(Object t) {

    return JSON.toJSONBytes(t, features);
  }

  @Override
  public CacheObject deserialize(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
    return (CacheObject) JSON.parse(bytes, DEFAULT_PARSER_FEATURE);
  }
}
