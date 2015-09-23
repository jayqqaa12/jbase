package com.jayqqaa12.jbase.sdk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

/**
 * aliyun oss 工具 请 初始化设置好
 * 
 * 
 * @author 12
 *
 */
public class OSSKit {

	private static OSSClient client;
	private static String buketName;

	public static void init(String endponit, String accessId, String accessKey, String bucketName) {
		client = new OSSClient(endponit, accessId, accessKey);
		buketName = bucketName;

	}

	public OSSClient getClient() {
		if (client == null) throw new OSSException("OssKit must init before use");
		return client;
	}

	private static void downloadFile(String key, String filename) throws OSSException, ClientException {
		if (client == null) throw new OSSException("OssKit must init before use");

		client.getObject(new GetObjectRequest(buketName, key), new File(filename));
	}

	
	public void deleteFile(String key) {
		if (client == null) throw new OSSException("OssKit must init before use");

		client.deleteObject(buketName, key);
	}

	// 上传文件
	public static String uploadFile(String fileName, String path) throws OSSException, ClientException,
			FileNotFoundException {
		if (client == null) throw new OSSException("OssKit must init before use");

		File file = new File(path);

		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.length());
		InputStream input = new FileInputStream(file);

		client.putObject(buketName, fileName, input, objectMeta);

		return getUrl(fileName);
	}

	/**
	 * 获取到 url 通过 key
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getUrl(String fileName) {
		if (client == null) throw new OSSException("OssKit must init before use");
		return client.generatePresignedUrl(buketName, fileName, new Date(2020, 1, 1)).toString().split("\\?")[0];
	}

}
