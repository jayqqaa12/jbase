package com.jayqqaa12.jbase.util.android;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.sinaapp.msdxblog.apkUtil.ApkInfo;
import com.sinaapp.msdxblog.apkUtil.ApkUtil;

public class ApkKit {

	public static String AAPT_PATH = "/root/android-sdk-linux/build-tools/19.0.1/aapt";

	public static void setAaptPath(String path) {
		AAPT_PATH = path;
	}

	public static ApkInfo getApkInfo(String apkPath) throws Exception {
		ApkInfo apkInfo = null;
		ApkUtil apk = new ApkUtil();
		if (AAPT_PATH != null) apk.setmAaptPath(AAPT_PATH);
		apkInfo = apk.getApkInfo(apkPath);

		return apkInfo;
	}

	/***
	 * 
	 * @param apkPath
	 * @param iconPath
	 *            导出的图标 地址
	 * @return
	 * @throws Exception
	 */
	public static ApkInfo getApkInfo(String apkPath, String iconPath) throws Exception {
		ApkInfo apkInfo = getApkInfo(apkPath);
		if (apkInfo != null) {
			 extractFileFromApk(apkPath, apkInfo.getApplicationIcon(), iconPath);
		}
		return apkInfo;
	}
	
	
	/**
	 * 从指定的apk文件里获取指定file的流
	 * @param apkpath
	 * @param fileName
	 * @return
	 */
	public static InputStream extractFileFromApk(String apkpath, String fileName) {
		try {
			ZipFile zFile = new ZipFile(apkpath);
			ZipEntry entry = zFile.getEntry(fileName);
			entry.getComment();
			entry.getCompressedSize();
			entry.getCrc();
			entry.isDirectory();
			entry.getSize();
			entry.getMethod();
			InputStream stream = zFile.getInputStream(entry);
			return stream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void extractFileFromApk(String apkpath, String fileName, String outputPath) throws Exception {
		InputStream is = extractFileFromApk(apkpath, fileName);
		
		File file = new File(outputPath);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file), 1024);
		byte[] b = new byte[1024];
		BufferedInputStream bis = new BufferedInputStream(is, 1024);
		while(bis.read(b) != -1){
			bos.write(b);
		}
		bos.flush();
		is.close();
		bis.close();
		bos.close();
	}

}
