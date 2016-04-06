package com.jayqqaa12.jbase.util;

import static java.io.File.separator;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Properties;

import com.jfinal.kit.LogKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件系统操作工具类.
 */
@SuppressWarnings({ "unused", "ResultOfMethodCallIgnored" })
public final class Fs {

	private Fs() {
	}

	public String uploadFileWithHttpUrlConnection(InputStream inputStream, String fileName, String uploadUrl)
			throws Exception {
		/* 边界、分隔 */
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		URL url = new URL(uploadUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setRequestMethod("POST");
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
		DataOutputStream ds = new DataOutputStream(con.getOutputStream());
		ds.writeBytes(twoHyphens + boundary + end);
		ds.writeBytes("Content-Disposition: form-data; " + "name=\"file\";filename=\"" + fileName + "\" " + end);
		ds.writeBytes(end);
		/* 文件较大时一定要设置缓冲区，防止内存溢出 */
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int length = -1;
		/* 循环将流输出 */
		while ((length = inputStream.read(buffer)) != -1) {
			ds.write(buffer, 0, length);
		}
		ds.writeBytes(end);
		ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
		inputStream.close();
		ds.flush();
		/* 读取响应 */
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuffer stringBuffer = new StringBuffer();
		String str = "";
		while ((str = br.readLine()) != null) {
			stringBuffer.append(str);
		}
		return stringBuffer.toString();
	}

	public static byte[] getBytesFromFile(File f) {
		if (f == null) {
			return new byte[0];
		}

		FileInputStream stream =null;
		try {
			  stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			LogKit.error(e.getMessage(),e);
		}
		finally {
			if(stream!=null) try {
				stream.close();
			} catch (IOException e) {
				LogKit.error(e.getMessage(),e);
			}
		}

		return new byte[0];
	}

	public static void down(File f, String url) {
		byte[] buffer = new byte[8 * 1024];
		URL u;
		URLConnection connection = null;
		try {
			u = new URL(url);
			connection = u.openConnection();

		} catch (Exception e) {
			LogKit.error(e.getMessage(), e);
			return;
		}
		connection.setReadTimeout(100000);
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			f.createNewFile();
			is = connection.getInputStream();
			fos = new FileOutputStream(f);
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}

		} catch (Exception e) {
			f.delete();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	final static List<String> getAllFiles(File dir, List<String> list) {

		try {
			File[] fs = dir.listFiles();
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					getAllFiles(fs[i], list);
				} else list.add(fs[i].getAbsolutePath());
			}
		} catch (Exception e) {}

		return list;
	}

	/**
	 * 复制文件(或文件夹). 此方法会自动建立要复制到的文件(或文件夹)所在路径所需的上层目录.
	 *
	 * @param fromFile
	 *            原文件 如：new File("c:/a.txt")
	 * @param toFile
	 *            复制后文件 如：new File("f:/aaa/bbb/ccc/ddd/b.txt")
	 *            (上层目录不存在，将会自动创建它们)
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #copyAs(String, String, boolean)
	 * @see #copyTo(String, String, boolean)
	 * @see #copyTo(java.io.File, java.io.File, boolean)
	 */
	public static boolean copyAs(File fromFile, File toFile, boolean overwrite) {
		if (!fromFile.exists()) {
			return false;
		}
		if (fromFile.equals(toFile)) {
			return false;
		} else if (fromFile.isFile()) {
			return cpFile(fromFile, toFile, overwrite);
		} else {
			return cpDir(fromFile, toFile, overwrite);
		}
	}

	/**
	 * 复制文件(或文件夹). 此方法会自动建立要复制到的文件(或文件夹)所在路径所需的上层目录.
	 *
	 * @param fromFile
	 *            原文件路径 如：c:/a.txt
	 * @param toFile
	 *            复制后路径 如：f:/aaa/bbb/ccc/ddd/b.txt
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #copyAs(java.io.File, java.io.File, boolean)
	 * @see #copyTo(String, String, boolean)
	 * @see #copyTo(java.io.File, java.io.File, boolean)
	 */
	public static boolean copyAs(String fromFile, String toFile, boolean overwrite) {
		return copyAs(new File(fromFile), new File(toFile), overwrite);
	}

	/**
	 * 复制文件到某个路径下. 与{@link #copyAs(String, String, boolean)},
	 * {@link #copyAs(java.io.File, java.io.File, boolean)}方法不同的是，
	 * 此方法是将fromFile复制到toDir路径的下面，即toDir一定是一个目录。
	 *
	 * @param fromFile
	 *            要复制的文件（或文件夹）
	 * @param toDir
	 *            文件（或文件夹）将复制到此目录下。
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #copyTo(String, String, boolean)
	 * @see #copyAs(String, String, boolean)
	 * @see #copyAs(java.io.File, java.io.File, boolean)
	 */
	public static boolean copyTo(File fromFile, File toDir, boolean overwrite) {
		if (toDir.isFile()) {
			return false;
		}
		return copyAs(fromFile, new File(toDir.getPath() + separator + fromFile.getName()), overwrite);
	}

	/**
	 * 复制文件到某个路径下. 与{@link #copyAs(String, String, boolean)},
	 * {@link #copyAs(java.io.File, java.io.File, boolean)}方法不同的是，
	 * 此方法是将fromFile复制到toDir路径的下面，即toDir一定是一个目录。
	 *
	 * @param fromFile
	 *            要复制的文件（或文件夹）
	 * @param toDir
	 *            文件（或文件夹）将复制到此目录下。
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #copyTo(java.io.File, java.io.File, boolean)
	 * @see #copyAs(String, String, boolean)
	 * @see #copyAs(java.io.File, java.io.File, boolean)
	 */
	public static boolean copyTo(String fromFile, String toDir, boolean overwrite) {
		return copyTo(new File(fromFile), new File(toDir), overwrite);
	}

	/**
	 * 删除文件（或文件夹）. 如果file是一个文件夹，将删除此文件夹及其下的全部内容.
	 *
	 * @param file
	 *            要删除的文件（或文件夹）.
	 */
	public static void delAll(File file) {
		if (!file.exists()) return;
		File[] fs;
		if (file.isDirectory()) if ((fs = file.listFiles()) != null) for (File f : fs)
			delAll(f);
		file.delete();
	}

	/**
	 * 删除文件（或文件夹）. 如果file是一个文件夹，将删除此文件夹及其下的全部内容.
	 *
	 * @param file
	 *            要删除的文件（或文件夹）的绝对路径地址.
	 */
	public static void delAll(String file) {
		delAll(new File(file));
	}

	/**
	 * 删除空目录.
	 *
	 * @param dir
	 *            如果dir所表示的目录下面没有任何内容,则删除之.
	 * @see #delAll(java.io.File)
	 * @see #delEmpty(String)
	 */
	public synchronized static void delEmpty(File dir) {
		if (dir.isFile()) return;
		String[] s = dir.list();
		if (s == null || s.length < 1) dir.delete();// 取消了删除空子级目录，因为当子目录或文件过多时占资源
	}

	/**
	 * 删除空目录.
	 *
	 * @param path
	 *            目录的绝对路径，如果此目录下面没有任何内容，则删除之.
	 * @see #delEmpty(java.io.File)
	 * @see #delAll(String)
	 */
	public static void delEmpty(String path) {
		delEmpty(new File(path));
	}

	/**
	 * 清空目录. 此方法将删除目录下面的所有子文件夹及文件（当前的目录本身并不会删除）
	 *
	 * @param dir
	 *            要清空的目录.
	 * @see #empty(String)
	 */
	public static void empty(File dir) {
		File[] fs;
		if (dir.isDirectory()) if ((fs = dir.listFiles()) != null) for (File f : fs)
			delAll(f);
	}

	/**
	 * 清空目录. 此方法将删除目录下面的所有子文件夹及文件（当前的目录本身并不会删除）
	 *
	 * @param dir
	 *            要清空的目录的绝对路径地址.
	 * @see #empty(java.io.File)
	 * @since 这是JDiy-1.9 及后续版本新增的方法.
	 */
	public static void empty(String dir) {
		delEmpty(new File(dir));
	}

	/**
	 * 读取属性文件. 此方法提供属性文件的便捷访问.
	 *
	 * @param resource
	 *            属性文件的路径地址.（路径地址可以是本地物理路径地址，也可以是一个URL地址）
	 * @return Properties对象, 通过该对象获取属性变量值.
	 */
	public static Properties getProperties(String resource) {
		Properties properties = new Properties();
		try {
			properties.load(getResource(resource).openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 以classpath为相对根路径，获取资源.
	 *
	 * @param relativePath
	 *            相对路径地址，例如： a.txt - %classpath%/a.txt ./a.txt - 同上 /a.txt - 同上
	 *            ../a.txt - %classpath%的上级路径下的a.txt abc/a.txt-
	 *            %classpath%/abc/a.txt
	 * @return 该资源的URL对象，可以通过
	 *         <b>returnurl.getPath()</b>来获取该资源的真实物理路径(returnurl即此方法返回的URL对象)。
	 * @throws java.net.MalformedURLException
	 *             MalformedURLException
	 */
	public static URL getResource(String relativePath) throws MalformedURLException {
		if (relativePath == null) {
			relativePath = "./";
		} else if (relativePath.startsWith("/")) {
			relativePath = "." + relativePath;
		} else if (!relativePath.startsWith("./") && !relativePath.startsWith("../")) {
			relativePath = "./" + relativePath;
		}
		@SuppressWarnings("ALL")
		String classAbsPath = Fs.class.getClassLoader().getResource("").toString();
		String parentStr = relativePath.substring(0, relativePath.lastIndexOf("../") + 3);
		relativePath = relativePath.substring(relativePath.lastIndexOf("../") + 3);
		int containSum = Txt.containSum(parentStr, "../");
		for (int i = 0; i < containSum; i++)
			classAbsPath = classAbsPath.substring(0, classAbsPath.lastIndexOf("/", classAbsPath.length() - 2) + 1);

		return new URL(classAbsPath + relativePath);
	}

	/**
	 * 移动文件(或文件夹). 此方法会自动建立要移动到的文件(或文件夹)所在路径所需的上层目录.
	 *
	 * @param fromFile
	 *            原文件 如：new File("c:/a.txt")
	 * @param toFile
	 *            移动后文件 如：new File("f:/aaa/bbb/ccc/ddd/b.txt")
	 *            (上层目录不存在，将会自动创建它们)
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #moveAs(String, String, boolean)
	 * @see #moveTo(String, String, boolean)
	 * @see #moveTo(java.io.File, java.io.File, boolean)
	 */
	public static boolean moveAs(File fromFile, File toFile, boolean overwrite) {
		if (copyAs(fromFile, toFile, overwrite)) {
			delAll(fromFile);
			return true;
		}
		return false;
	}

	/**
	 * 移动文件(或文件夹). 此方法会自动建立要移动到的文件(或文件夹)所在路径所需的上层目录.
	 *
	 * @param fromFile
	 *            原文件路径 如：c:/a.txt
	 * @param toFile
	 *            移动后路径 如：f:/aaa/bbb/ccc/ddd/b.txt
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #moveAs(java.io.File, java.io.File, boolean)
	 * @see #moveTo(String, String, boolean)
	 * @see #moveTo(java.io.File, java.io.File, boolean)
	 */
	public static boolean moveAs(String fromFile, String toFile, boolean overwrite) {
		if (copyAs(fromFile, toFile, overwrite)) {
			delAll(fromFile);
			return true;
		}
		return false;
	}

	/**
	 * 移动文件到某个路径下. 与{@link #moveAs(String, String, boolean)},
	 * {@link #moveAs(java.io.File, java.io.File, boolean)}方法不同的是，
	 * 此方法是将fromFile移动到toDir路径的下面，即toDir一定是一个目录。
	 *
	 * @param fromFile
	 *            要移动的文件（或文件夹）
	 * @param toDir
	 *            文件（或文件夹）将移动到此目录下。
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #moveTo(String, String, boolean)
	 * @see #moveAs(String, String, boolean)
	 * @see #moveAs(java.io.File, java.io.File, boolean)
	 */
	public static boolean moveTo(File fromFile, File toDir, boolean overwrite) {
		if (copyTo(fromFile, toDir, overwrite)) {
			delAll(fromFile);
			return true;
		}
		return false;
	}

	/**
	 * 移动文件到某个路径下. 与{@link #moveAs(String, String, boolean)},
	 * {@link #moveAs(java.io.File, java.io.File, boolean)}方法不同的是，
	 * 此方法是将fromFile移动到toDir路径的下面，即toDir一定是一个目录。
	 *
	 * @param fromFile
	 *            要移动的文件（或文件夹）
	 * @param toDir
	 *            文件（或文件夹）将移动到此目录下。
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #moveTo(java.io.File, java.io.File, boolean)
	 * @see #moveAs(String, String, boolean)
	 * @see #moveAs(java.io.File, java.io.File, boolean)
	 */
	public static boolean moveTo(String fromFile, String toDir, boolean overwrite) {
		if (copyTo(fromFile, toDir, overwrite)) {
			delAll(fromFile);
			return true;
		}
		return false;
	}

	/**
	 * 读取文本文件的内容.
	 *
	 * @param file
	 *            要读取的文件. 可以为任意文本类型的文件，例如：txt, log, html, bat, sh, js, css等.
	 * @param encoding
	 *            文本文件的编码类型. 例如： utf-8(默认值), gb2312等等.
	 * @return 文本文件的内容.
	 * @throws IOException
	 * @see #readFile(java.io.File)
	 * @see #writeFile(java.io.File, String)
	 * @see #writeFile(java.io.File, String, String)
	 * @since 这是JDiy-1.9及后续版本新增的方法.
	 */
	public static String readFile(File file, String encoding) throws IOException {
		StringBuilder contents = new StringBuilder();
		BufferedReader reader = null;
		try {
			if (encoding == null || "".equals(encoding)) encoding = "utf-8";
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
			reader = new BufferedReader(read);
			String text;
			while ((text = reader.readLine()) != null) {
				contents.append(text).append(System.getProperty("line.separator"));
			}
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ignore) {}
		}
		return contents.toString();
	}

	/**
	 * 读取文本文件的内容.
	 * <p>
	 * 程序默认以utf-8的编码形式来读取文本文件的内容. 如果文本文件不是utf-8的编码，请使用
	 * {@link #readFile(java.io.File, String)}方法指定编码.
	 *
	 * @param file
	 *            要读取的文件. 可以为任意文本类型的文件，例如：txt, log, html, bat, sh, js, css等.
	 * @return 文本文件的内容.
	 * @throws IOException
	 * @see #readFile(java.io.File, String)
	 * @see #writeFile(java.io.File, String)
	 * @see #writeFile(java.io.File, String, String)
	 * @since 这是JDiy-1.9及后续版本新增的方法.
	 */
	public static String readFile(File file) throws IOException {
		return readFile(file, "utf-8");
	}

	/**
	 * 将文本内容保存为文件. 在保存文件时，(如果上层路径不存在).系统将自动创建其上层目录路径.
	 *
	 * @param file
	 *            要保存的文件.
	 * @param str
	 *            要保存的文本内容.
	 * @param encoding
	 *            指定文本文件编码方式. 例如：utf-8(默认值), gb2312 等等...
	 * @see #writeFile(java.io.File, String)
	 * @see #readFile(java.io.File)
	 * @see #readFile(java.io.File, String)
	 * @since 这是JDiy-1.9及后续版本新增的方法.
	 */
	public static void writeFile(File file, String str, String encoding) throws IOException {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, encoding);
			osw.write(str);
			osw.flush();
		} finally {
			try {
				if (null != osw) osw.close();
				if (null != fos) fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将文本内容保存为文件. 在保存文件时，(如果上层路径不存在).系统将自动创建其上层目录路径.
	 *
	 * @param file
	 *            要保存的文件.
	 * @param b
	 *            要保存的文本内容.
	 * @see #writeFile(java.io.File, String)
	 * @see #readFile(java.io.File)
	 * @see #readFile(java.io.File, String)
	 * @since 这是JDiy-1.9及后续版本新增的方法.
	 */
	public static void writeFile(File file, byte[] b) throws IOException {
		FileOutputStream fos = null;
		try {
			if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
			fos = new FileOutputStream(file);
			fos.write(b);
		} finally {
			try {
				if (null != fos) fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将文本内容保存为文件. 程序默认以utf-8的编码形式保存文本文件，如要使用其它编码方式保存， 请使用
	 * {@link #writeFile(java.io.File, String, String)}方法.
	 * 在保存文件时，(如果上层路径不存在).系统将自动创建其上层目录路径.
	 *
	 * @param file
	 *            要保存的文件.
	 * @param str
	 *            要保存的文本内容.
	 * @see #writeFile(java.io.File, String, String)
	 * @see #readFile(java.io.File)
	 * @see #readFile(java.io.File, String)
	 * @since 这是JDiy-1.9及后续版本新增的方法.
	 */
	public static void writeFile(File file, String str) throws IOException {
		writeFile(file, str, "utf-8");
	}

	/**
	 * 返回指定路径下的文件（夹）所占用的空间字节大小.
	 *
	 * @param file
	 *            文件（夹）
	 * @return 该路径下的文件(夹)所占用的空间字节大小.
	 * @see #size(String)
	 * @see #sizeStr(String)
	 */
	public static long size(File file) {
		if (!file.exists()) return 0L;
		if (file.isFile()) return file.length();

		File[] fs = file.listFiles();
		long sizes = 0L;
		if (fs != null) for (File f1 : fs)
			sizes += size(f1);

		return sizes;
	}

	/**
	 * 返回指定路径下的文件（夹）所占用的空间字节大小.
	 *
	 * @param path
	 *            文件（夹）的绝对地址
	 * @return 该路径下的文件(夹)所占用的空间字节大小.
	 * @see #size(java.io.File)
	 * @see #sizeStr(String)
	 */
	public static long size(String path) {
		return size(new File(path));
	}

	/**
	 * 根据文件大小自动格式化为以Byte,Kb, Mb, Gb等单位的字符串显示. 系统采用1024进位.
	 *
	 * @param size
	 *            文件（夹）的字节大小
	 * @return 返回该字节大小格式化后的Byte, Kb, Mb, Gb等单位的字符串
	 * @see #size(String)
	 */
	public static String sizeStr(long size) {
		double size0;
		String s = "Byte";
		if (size < 1024 * 1024 && size > 1024) {
			size0 = size / 1024.0;
			s = "KB";
		} else if (size < 1024 * 1000 * 1024 && size > 1024 * 1024) {
			size0 = size / (1024 * 1024.0);
			s = "MB";
		} else if (size < 1024L * 1024 * 1024 * 1024 && size > 1024 * 1024 * 1024) {
			size0 = size / (1024 * 1024 * 1024.0);
			s = "GB";
		} else {
			size0 = size;
		}
		size0 = Math.round(size0 * 100.0) / 100.0;
		String ss = String.valueOf(size0);
		if (ss.lastIndexOf(".") == ss.length() - 2) {
			ss += "0";
		}
		return ss + " " + s;
	}

	/**
	 * 获取文件（夹）的占用空间大小，并格式化为Byte,Kb, Mb, Gb等单位的字符串显示. 系统采用1024进位.
	 *
	 * @param file
	 *            要获取大小的文件(或文件夹).
	 * @return 返回该文字的字节大小字符串
	 * @see #size(String)
	 */
	public static String sizeStr(File file) {
		return sizeStr(size(file));
	}

	/**
	 * 获取文件（夹）的占用空间大小，并格式化为Byte,Kb, Mb, Gb等单位的字符串显示. 系统采用1024进位.
	 *
	 * @param path
	 *            要获取大小的文件(或文件夹)的绝对路径.
	 * @return 返回该文字的字节大小字符串
	 * @see #size(String)
	 */
	public static String sizeStr(String path) {
		return sizeStr(size(path));
	}

	private static boolean cpFile(File fromFile, File toFile, boolean overWrite) {
		if (!overWrite && toFile.exists()) {
			return false;
		}
		int byteread;
		toFile.getParentFile().mkdirs();
		try {
			InputStream inStream = new FileInputStream(fromFile.getPath()); // 读入原文件
			FileOutputStream fs = new FileOutputStream(toFile.getPath());
			byte[] buffer = new byte[1444];
			while ((byteread = inStream.read(buffer)) != -1)
				fs.write(buffer, 0, byteread);

			inStream.close();
			fs.close();
			return true;
		} catch (Exception ioe) {
			ioe.printStackTrace();
			return true;
		}

	}

	private static boolean cpDir(File fromFile, File toFile, boolean overWrite) {
		if (!overWrite && toFile.exists()) {
			return false;
		}
		toFile.mkdirs();
		File[] files = fromFile.listFiles();
		boolean isOk = true;
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					if (!cpFile(file, new File(toFile.getPath() + separator + file.getName()), true)) isOk = false;
				} else {
					if (!cpDir(file, new File(toFile.getPath() + separator + file.getName()), true)) isOk = false;
				}
			}
		}
		return isOk;
	}

	private static final Logger log = LoggerFactory.getLogger(Fs.class);
}
