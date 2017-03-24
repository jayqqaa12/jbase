package com.jayqqaa12.jbase.util.android;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.jfinal.kit.LogKit;

public class ApkKit {

	private static  String aaptPath = "/root/android-sdk-linux/build-tools/19.0.1/aapt";

	public static void setAaptPath(String path) {
		aaptPath = path;
	}

	public static ApkInfo getApkInfo(String apkPath) throws Exception {
		ApkUtil apk = new ApkUtil();
		if (aaptPath != null) apk.setmAaptPath(aaptPath);

		return  apk.getApkInfo(apkPath);
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
	private static ZipFile extractFileFromApk(String apkpath, String fileName) {
		try {
			ZipFile zFile = new ZipFile(apkpath);
			ZipEntry entry = zFile.getEntry(fileName);
			entry.getComment();
			entry.getCompressedSize();
			entry.getCrc();
			entry.isDirectory();
			entry.getSize();
			entry.getMethod();
			return zFile;
		} catch (IOException e) {
			LogKit.error(e.getMessage(), e);
		}

		return null;
	}
	
	public static void extractFileFromApk(String apkpath, String fileName, String outputPath) throws IOException {
		ZipFile zFile  = extractFileFromApk(apkpath, fileName);

		InputStream is= zFile.getInputStream(zFile.getEntry(fileName));
		
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
