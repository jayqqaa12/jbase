package com.jayqqaa12.jbase.spring.mapstruct;

import com.alibaba.fastjson2.JSON;
import org.mapstruct.Named;

import java.util.List;

/**
 * @author 12
 */
public interface BaseMapstructMapper {


  @Named("serializeJson")
  default String serializeJson(Object obj) {
    return JSON.toJSONString(obj);
  }

  @Named("deserializeJson")
  default Object deserializeJson(String json) {
    return JSON.parse(json);
  }


  @Named("deserializeStrArray")
  default List<String> deserializeStrArray(String json) {
    return deserializeJsonArray(json, String.class);
  }


  default <T> T deserializeJsonObj(String json, Class<T> clazz) {
    return JSON.parseObject(json, clazz);
  }

  default <T> List<T> deserializeJsonArray(String json, Class<T> clazz) {
    return JSON.parseArray(json, clazz);
  }


}
