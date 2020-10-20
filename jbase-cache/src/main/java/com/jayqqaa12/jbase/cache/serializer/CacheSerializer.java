package com.jayqqaa12.jbase.cache.serializer;

import com.jayqqaa12.jbase.cache.core.CacheObject;

import java.io.IOException;


public interface CacheSerializer {

    byte[] serialize(Object obj)  ;

    Object deserialize(byte[] bytes)  ;

}
