package com.sinaapp.msdxblog.apkUtil.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.sinaapp.msdxblog.apkUtil.Version;
import com.sinaapp.msdxblog.apkUtil.entity.ApkInfo;
import com.sinaapp.msdxblog.apkUtil.entity.ImpliedFeature;

public class ApkUtil
{
	public static final String VERSION_CODE = "versionCode";
	public static final String VERSION_NAME = "versionName";
	public static final String SDK_VERSION = "sdkVersion";
	public static final String TARGET_SDK_VERSION = "targetSdkVersion";
	public static final String USES_PERMISSION = "uses-permission";
	public static final String APPLICATION_LABEL = "application-label";
	public static final String APPLICATION_ICON = "application-icon";
	public static final String USES_FEATURE = "uses-feature";
	public static final String USES_IMPLIED_FEATURE = "uses-implied-feature";
	public static final String SUPPORTS_SCREENS = "supports-screens";
	public static final String SUPPORTS_ANY_DENSITY = "supports-any-density";
	public static final String DENSITIES = "densities";
	public static final String PACKAGE = "package";
	public static final String APPLICATION = "application:";
	public static final String LAUNCHABLE_ACTIVITY = "launchable-activity";
	private ProcessBuilder mBuilder;
	private static final String SPLIT_REGEX = "(: )|(=')|(' )|'";
	private static final String FEATURE_SPLIT_REGEX = "(:')|(',')|'";
	private String mAaptPath = "lib/aapt";

	public ApkUtil()
	{
		this.mBuilder = new ProcessBuilder(new String[0]);
		this.mBuilder.redirectErrorStream(true);
	}

	public ApkInfo getApkInfo(String apkPath) throws Exception
	{
		Process process = this.mBuilder.command(new String[] { this.mAaptPath, "d", "badging", apkPath }).start();
		InputStream is = null;
		is = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf8"));
		String tmp = br.readLine();
		try
		{
			if ((tmp == null) || (!tmp.startsWith("package"))) { throw new Exception("参数不正确，无法正常解析APK包。输出结果为:\n" + tmp + "..."); }
			ApkInfo apkInfo = new ApkInfo();
			do
				setApkInfoProperty(apkInfo, tmp);
			while ((tmp = br.readLine()) != null);
			return apkInfo;
		} catch (Exception e)
		{} finally
		{
			process.destroy();
			closeIO(is);
			closeIO(br);
		}
		
		return null;
	}

	private void setApkInfoProperty(ApkInfo apkInfo, String source)
	{
		if (source.startsWith("package"))
		{
			splitPackageInfo(apkInfo, source);
		}
		else if (source.startsWith("launchable-activity"))
		{
			apkInfo.setLaunchableActivity(getPropertyInQuote(source));
		}
		else if (source.startsWith("sdkVersion"))
		{
			apkInfo.setSdkVersion(getPropertyInQuote(source));
		}
		else if (source.startsWith("targetSdkVersion"))
		{
			apkInfo.setTargetSdkVersion(getPropertyInQuote(source));
		}
		else if (source.startsWith("uses-permission"))
		{
			apkInfo.addToUsesPermissions(getPropertyInQuote(source));
		}
		else if (source.startsWith("application-label"))
		{
			apkInfo.setApplicationLable(getPropertyInQuote(source));
		}
		else if (source.startsWith("application-icon"))
		{
			apkInfo.addToApplicationIcons(getKeyBeforeColon(source), getPropertyInQuote(source));
		}
		else if (source.startsWith("application:"))
		{
			String[] rs = source.split("( icon=')|'");
			apkInfo.setApplicationIcon(rs[(rs.length - 1)]);
		}
		else if (source.startsWith("uses-feature"))
		{
			apkInfo.addToFeatures(getPropertyInQuote(source));
		}
		else if (source.startsWith("uses-implied-feature"))
		{
			apkInfo.addToImpliedFeatures(getFeature(source));
		}
	}

	private ImpliedFeature getFeature(String source)
	{
		String[] result = source.split("(:')|(',')|'");
		ImpliedFeature impliedFeature = new ImpliedFeature(result[1], result[2]);
		return impliedFeature;
	}

	private String getPropertyInQuote(String source)
	{
		int index = source.indexOf("'") + 1;
		return source.substring(index, source.indexOf('\'', index));
	}

	private String getKeyBeforeColon(String source)
	{
		return source.substring(0, source.indexOf(':'));
	}

	private void splitPackageInfo(ApkInfo apkInfo, String packageSource)
	{
		String[] packageInfo = packageSource.split("(: )|(=')|(' )|'");
		apkInfo.setPackageName(packageInfo[2]);
		apkInfo.setVersionCode(packageInfo[4]);
		apkInfo.setVersionName(packageInfo[6]);
	}

	private final void closeIO(Closeable c)
	{
		if (c == null) return;
		try
		{
			c.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		try
		{
			String demo = "D:/works/app/ApplicationAssistant.apk";
			if (args.length > 0)
			{
				if ((args[0].equals("-version")) || (args[0].equals("-v")))
				{
					System.out.println("ApkUtil   -by Geek_Soledad");
					System.out.println("Version:" + Version.getVersion());
					return;
				}
				demo = args[0];
			}
			ApkInfo apkInfo = new ApkUtil().getApkInfo(demo);
			System.out.println(apkInfo);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getmAaptPath()
	{
		return this.mAaptPath;
	}

	public void setmAaptPath(String mAaptPath)
	{
		this.mAaptPath = mAaptPath;
	}
}
