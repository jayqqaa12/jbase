package com.jayqqaa12.jbase.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 请 使用 jfinal httpkit
 * @author 12
 *
 */
@Deprecated
public class Http
{
	private String cookie = "";
	private String charset = "utf8";

	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	public String getCookie()
	{
		return this.cookie;
	}

	public String get(String url)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			URL _url = new URL(url);

			if (url.toLowerCase().contains("https://"))
			{
				HttpsHandler httpsH = new HttpsHandler();
				httpsH.trustAllHttpsCertificates();
				HostnameVerifier hv = new HostnameVerifier()
				{
					public boolean verify(String urlHostName, SSLSession session)
					{
						return true;
					}
				};
				HttpsURLConnection.setDefaultHostnameVerifier(hv);
				HttpsURLConnection sconn = (HttpsURLConnection) _url.openConnection();
				sconn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");
				sconn.setReadTimeout(3000);
				sconn.setRequestProperty("Cookie", this.cookie);

				BufferedReader br = new BufferedReader(new InputStreamReader(sconn.getInputStream(), this.charset));
				String temp = null;
				while ((temp = br.readLine()) != null)
				{
					sb.append(temp);
				}
				br.close();
				String cookie = sconn.getHeaderField("Set-Cookie");
				if (cookie != null)
				{
					this.cookie = cookie;
				}
			}
			else
			{
				HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
				conn.setRequestProperty("Cookie", this.cookie);
				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");

				conn.setRequestProperty("merchantid", "7b3852b4-25ff-48eb-8657-656af7bc0573");
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), this.charset));
				String temp = null;
				while ((temp = br.readLine()) != null)
				{
					sb.append(temp);
				}
				br.close();
				String cookie = conn.getHeaderField("Set-Cookie");
				if (cookie != null)
				{
					this.cookie = cookie;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}

 
	public String post(String url, String data)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			URL _url = new URL(url);
			if (url.toLowerCase().contains("https://"))
			{
				HttpsHandler httpsH = new HttpsHandler();
				httpsH.trustAllHttpsCertificates();
				HostnameVerifier hv = new HostnameVerifier()
				{
					public boolean verify(String urlHostName, SSLSession session)
					{
						return true;
					}
				};
				HttpsURLConnection.setDefaultHostnameVerifier(hv);
				HttpsURLConnection sconn = (HttpsURLConnection) _url.openConnection();
				
				sconn.setRequestMethod("POST");
				sconn.setDoInput(true);
				sconn.setDoOutput(true);
				sconn.setRequestProperty("Cookie", this.cookie);
				sconn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sconn.getOutputStream(), this.charset));
				bw.write(data);
				bw.flush();
				BufferedReader br = new BufferedReader(new InputStreamReader(sconn.getInputStream(), this.charset));
				String temp = null;
				while ((temp = br.readLine()) != null)
				{
					sb.append(temp);
					sb.append("\n");
				}
				br.close();
				String cookie = sconn.getHeaderField("Set-Cookie");
				if (cookie != null)
				{
					this.cookie = cookie;
				}
			}
			else
			{
				HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");

				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestProperty("Cookie", this.cookie);
				conn.setRequestMethod("POST");
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), this.charset));
				bw.write(data);
				bw.flush();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), this.charset));
				String temp = null;
				while ((temp = br.readLine()) != null)
				{
					sb.append(temp);
					sb.append("\n");
				}
				br.close();
				bw.close();
				String cookie = conn.getHeaderField("Set-Cookie");
				if (cookie != null)
				{
					this.cookie = cookie;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();

		}
		return sb.toString();
	}

	public byte[] file(String url)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			URL _url = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
			conn.setRequestProperty("Cookie", this.cookie);
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = bis.read(b)) != -1)
			{
				bos.write(b, 0, len);
				bos.flush();
			}
			bos.close();
			bis.close();
			String cookie = conn.getHeaderField("Set-Cookie");
			if (cookie != null)
			{
				this.cookie = cookie;
			}
		} catch (Exception e)
		{
			return null;
		}
		return bos.toByteArray();
	}

	private class HttpsHandler
	{
		private HttpsHandler()
		{}

		public void trustAllHttpsCertificates() throws Exception
		{
			TrustManager[] tm_array = new TrustManager[1];
			TrustManager tm = new MyTrustManager();
			tm_array[0] = tm;
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, tm_array, null);
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		}

		private class MyTrustManager implements TrustManager, X509TrustManager
		{
			private MyTrustManager()
			{}

			public X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}

			public boolean isServerTrusted(X509Certificate[] certs)
			{
				return true;
			}

			public boolean isClientTrusted(X509Certificate[] certs)
			{
				return true;
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException
			{}

			public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException
			{}
		}
	}
}
