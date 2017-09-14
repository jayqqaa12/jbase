package com.jayqqaa12.jbase.util.sdk;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

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
	private static String imgDomainUrl; // 图片处理 绑定的域名
	private static String domainUrl; // 绑定的域名 注意 要支持 流媒体 必需要绑定域名
	private static String endponit;

	public static void init(String endponit, String accessId, String accessKey, String bucketName) {
		client = new OSSClient(endponit, accessId, accessKey);
		OSSKit.endponit = endponit;
		OSSKit.buketName = bucketName;
	}

	public static void init(String endponit, String accessId, String accessKey, String bucketName, String imgUrl,
			String domainUrl) {
		client = new OSSClient(endponit, accessId, accessKey);

		OSSKit.endponit = endponit;
		OSSKit.buketName = bucketName;
		OSSKit.imgDomainUrl = imgUrl;
		OSSKit.domainUrl = domainUrl;
	}

	public OSSClient getClient() {
		if (client == null) throw new OSSException("OssKit must init before use");
		return client;
	}

	public static void downloadFile(String key, String filename) throws OSSException, ClientException {
		if (client == null) throw new OSSException("OssKit must init before use");

		client.getObject(new GetObjectRequest(buketName, key), new File(filename));
	}

	public void deleteFile(String key) {
		if (client == null) throw new OSSException("OssKit must init before use");

		client.deleteObject(buketName, key);
	}

	public static String uploadFile(String fileName, String path) throws OSSException, ClientException,
			FileNotFoundException {

		return uploadFile(fileName, path, false, null);
	}

	public static String uploadFile(String fileName, String path, boolean isImg) throws OSSException, ClientException,
			FileNotFoundException {

		return uploadFile(fileName, path, isImg, null);
	}

	// 上传文件
	public static String uploadFile(String fileName, String path, boolean img, String contentType) throws OSSException,
			ClientException, FileNotFoundException {
		if (client == null) throw new OSSException("OssKit must init before use");

		File file = new File(path);

		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.length());
		if (contentType != null) objectMeta.setContentType(contentType);

		InputStream input = new FileInputStream(file);

		client.putObject(buketName, fileName, input, objectMeta);

		if (img) return getImgUrl(fileName);
		else return getUrl(fileName);
	}

	private static String getImgUrl(String fileName) {
		if (imgDomainUrl == null) throw new OSSException("OssKit must init set img domain Url");
		String url = getUrl(fileName);

		if (url != null) url = url.replace(buketName + ".", "").replace(endponit, imgDomainUrl);
		if (url != null) url = url.replace(domainUrl, imgDomainUrl);

		return url;
	}

	/**
	 * 获取到 url 通过 key
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getUrl(String fileName) {
		if (client == null) throw new OSSException("OssKit must init before use");

		String url = client.generatePresignedUrl(buketName, fileName, new Date(2020, 1, 1)).toString().split("\\?")[0];

		if (domainUrl != null)url= url.replace(buketName + ".", "").replace(endponit, domainUrl);

		return url;
	}

}
