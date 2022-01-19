package com.jayqqaa12.jbase.spring.aliyun;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.Base64;

/**
 * Created by yhj on 17/6/8.
 */
public class ImageHelper {


    @Autowired
    private AliyunOssHelper ossHelper;


    private final String default_suffix = ".png";

    //图片处理方法
    public String dataPic(String pic, String prefix, String suffix) {

        if (StringUtils.isEmpty(pic)) {
            return null;
        }
        byte[] bs = Base64.getDecoder().decode(pic);
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bs);
        String portrait = null;
        String objectKey = getObjectKey(prefix, suffix);
        try {
            portrait = ossHelper.saveInputStreamToAli(objectKey, byteInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return portrait;

    }


    private String getObjectKey(String prefix, String suffix) {
        Long time = System.currentTimeMillis();
        String objectKey = prefix + time + getSuffix(suffix);
        return objectKey;
    }


    public String getSuffix(String suffix) {

        if (StringUtils.isEmpty(suffix)) {
            return default_suffix;
        } else {

            return suffix.startsWith(".") ? suffix : "." + suffix;

        }
    }

}
