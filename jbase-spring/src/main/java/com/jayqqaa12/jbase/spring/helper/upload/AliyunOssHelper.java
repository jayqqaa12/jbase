package com.jayqqaa12.jbase.spring.helper.upload;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yhj on 17/6/8.
 */
public class AliyunOssHelper {

    @Value("${config.aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${config.aliyun.oss.url}")
    private String url;

    @Value("${config.aliyun.oss.pkg:upload}")
    private String pkg;

    @Value("${config.aliyun.oss.bufferSize:2014}")
    private int bufferSize;

    @Value("${config.aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${config.aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${config.aliyun.accessKeySecret}")
    private String accessKeySecret;


    public String saveInputStreamToAli(String objectkey, InputStream is) {

        return saveInputStreamToAli(is, getContextType(objectkey), null, getStoreKey(objectkey));
    }

    public String saveInputStreamToAli(String objectkey, String context) {

        ByteArrayInputStream is = new ByteArrayInputStream(context.getBytes());

        return saveInputStreamToAli(is, getContextType(objectkey), "utf-8", getStoreKey(objectkey));
    }


    public String saveInputStreamToAli(String objectkey, MultipartFile multipartFile) {

        try {
            return saveInputStreamToAli(multipartFile.getInputStream(), multipartFile.getContentType(), null, getStoreKey(objectkey));
        } catch (IOException e) {
            throw new RuntimeException("上传OSS异常", e);
        }
    }


    /**
     * @param objectkey
     * @param file
     * @return
     */
    public String saveInputStreamToAli(String objectkey, File file) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);


        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentType(getContextType(objectkey));
        String storeKey = getStoreKey(objectkey);
        try {
            ossClient.putObject(bucketName, storeKey, file, objectMeta);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return "https://" + bucketName + url + storeKey;
    }


    /**
     * @param input
     * @param contentType
     * @param storeKey
     * @return
     */
    public String saveInputStreamToAli(InputStream input, String contentType, String contentEncoding, String storeKey) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata objectMeta = new ObjectMetadata();
        // 判断上传类型，多的可根据自己需求来判定
        objectMeta.setContentType(contentType);

        if (contentEncoding != null) {
            objectMeta.setContentEncoding(contentEncoding);
        }

        try {
            ossClient.putObject(bucketName, storeKey, input, objectMeta);
        } finally {
            ossClient.shutdown();
        }
        return "https://" + bucketName + url + storeKey;
    }


    private String getStoreKey(String objectkey) {

        return pkg.endsWith("/") ? pkg + objectkey : pkg + "/" + objectkey;
    }


    private String getContextType(String objectkey) {

        return OssContextType.getContextType(objectkey);
    }

}
