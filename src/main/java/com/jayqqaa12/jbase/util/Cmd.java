package com.jayqqaa12.jbase.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/***
 * win 32 命令
 * 
 * @author 12
 *
 */
public class Cmd {

 

	public static void killPort(int port) {
		try {
			
			Process listprocess = Runtime.getRuntime().exec("cmd.exe /c netstat -ano|findstr  " + port);
			InputStream is = listprocess.getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			String str = null;
			while ((str = r.readLine()) != null) {
				Matcher matcher = Pattern.compile("([\\d]*)$").matcher(str);
				while (matcher.find()) {
					if (matcher.groupCount() >= 1) {
						String pid = matcher.group(0);
						Runtime.getRuntime().exec("cmd.exe /c taskkill /f /pid " + pid);
					}
				}
			}
			System.out.println("kill port success ");
			
		} catch (Exception e) {
			System.out.println("kill port  fail");
			e.printStackTrace();
		}
	}

}
