/**
 *
 */
package com.jayqqaa12.j2cache.serializer;

import net.sf.ehcache.CacheException;

import java.io.*;

/**
 * 标准的 Java 序列化
 */
public class JavaCacheSerializer implements CacheSerializer {

    @Override
    public String name() {
        return "java";
    }

    @Override
    public byte[] serialize(Object obj) throws IOException {
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            return baos.toByteArray();
        } finally {
            if (oos != null)
                try {
                    oos.close();
                } catch (IOException e) {
                }
        }
    }

    @Override
    public Object deserialize(byte[] bits) throws IOException {
        if (bits == null || bits.length == 0)
            return null;
        ObjectInputStream ois = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bits);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new CacheException(e);
        } finally {
            if (ois != null)
                try {
                    ois.close();
                } catch (IOException e) {
                }
        }
    }

}
