package com.jayqqaa12.j2cache.serializer;

import net.sf.ehcache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;


public class SerializationUtils {

    private final static Logger log = LoggerFactory.getLogger(SerializationUtils.class);
    private static CacheSerializer serializer;


    static {
        String ser = "json";
        if (ser == null || "".equals(ser.trim()))
            serializer = new JavaCacheSerializer();
        else {
            if (ser.equals("java")) {
                serializer = new JavaCacheSerializer();
            } else if (ser.equals("fst")) {
                serializer = new FSTCacheSerializer();
            }  else if (ser.equals("json")) {
                serializer = new JsonCacheSerializer();
            } else {
                try {
                    serializer = (CacheSerializer) Class.forName(ser).newInstance();
                } catch (Exception e) {
                    throw new CacheException("Cannot initialize SerializerClass named [" + ser + ']', e);
                }
            }
        }
        log.info("Using SerializerClass -> [" + serializer.name() + ":" + serializer.getClass().getName() + ']');
    }

    public static byte[] serialize(Object obj) throws IOException {
            return serializer.serialize(obj);

    }

    public static Serializable deserialize(byte[] bytes) throws IOException {
            return (Serializable) serializer.deserialize(bytes);
    }

}
