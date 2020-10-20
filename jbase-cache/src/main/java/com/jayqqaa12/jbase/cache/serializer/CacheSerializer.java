package com.jayqqaa12.jbase.cache.serializer;

import com.jayqqaa12.jbase.cache.core.CacheObject;

import java.io.IOException;


public interface CacheSerializer {

    byte[] serialize(CacheObject obj) throws IOException;

    CacheObject deserialize(byte[] bytes) throws IOException;

}
