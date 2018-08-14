package com.jayqqaa12.j2cache.serializer;

import java.io.IOException;


public interface CacheSerializer {
    String name();

    byte[] serialize(Object obj) throws IOException;

    Object deserialize(byte[] bytes) throws IOException;

}
