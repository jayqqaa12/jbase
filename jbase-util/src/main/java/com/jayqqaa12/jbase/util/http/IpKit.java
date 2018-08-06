package com.jayqqaa12.jbase.util.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayqqaa12.jbase.util.HttpKit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * IP工具类
 */
public class IpKit {

    private static final Logger LOG = LoggerFactory.getLogger(IpKit.class);
    /**
     * 获取登录用户的IP地址
     * <p>
     * 对手机无效
     *
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        if (ip != null && ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    public static String getIpByMobile(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (ip != null && (ip.equals("127.0.0.1") || ip.startsWith("192") || ip.startsWith("100") || ip.startsWith("0:0:")))
            ip = getIp(request);

        return ip;
    }

    public static JSONObject getIp(String ip) {
        String result = HttpKit.get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=model&ip=" + ip);
        JSONObject obj = new JSONObject();
        try {
            obj = (JSONObject) JSON.parse(result);
        } catch (Exception e) {
        }

        return obj;

    }



    public static String getIpAddr() {

        String ip = HttpKit.get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=model");
        String info = "";

        JSONObject obj = (JSONObject) JSON.parse(ip);
        info += obj.getString("province") + " ";
        info += obj.getString("city") + " ";
        info += obj.getString("isp");

        return info;
    }

    public static String getIpCity() {
        String ip = HttpKit.get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=model&ip=");
        String info = "";
        JSONObject obj = (JSONObject) JSON.parse(ip);
        info += obj.getString("city");

        return info;
    }

    public static String getWeather(String location) {

        String url = "http://api.map.baidu.com/telematics/v3/weather?location=" + location
                + "&output=model&ak=640f3985a6437dad8135dae98d775a09";
        String ip = HttpKit.get(url);

        String info = "";

        JSONObject obj = (JSONObject) JSON.parse(ip);

        if ("success".equals(obj.getString("status"))) {
            JSONObject data = obj.getJSONArray("results").getJSONObject(0);

            data = data.getJSONArray("weather_data").getJSONObject(0);
            info += info += location + "天气  ";
            info += data.getString("weather") + " ";
            info += data.getString("wind") + " ";
            info += data.getString("temperature") + " ";
        }

        return info;
    }


    /**
     * 本机外网ip  可能会过期 注意
     *
     * @return
     */
    public static String getEth1InetIP() {
        InputStream ins = null;
        try {
            URL url = new URL("http://1212.ip138.com/ic.asp");
            URLConnection con = url.openConnection();
            ins = con.getInputStream();
            InputStreamReader isReader = new InputStreamReader(ins, "GB2312");
            BufferedReader bReader = new BufferedReader(isReader);
            StringBuffer webContent = new StringBuffer();
            String str = null;
            while ((str = bReader.readLine()) != null) {
                webContent.append(str);
            }
            int start = webContent.indexOf("[") + 1;
            int end = webContent.indexOf("]");
            return webContent.substring(start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return "";
    }

    public static long strToLong(String strip) {
        long[] ip = new long[4];
        int position1 = strip.indexOf(".");
        int position2 = strip.indexOf(".", position1 + 1);
        int position3 = strip.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(strip.substring(0, position1));
        ip[1] = Long.parseLong(strip.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strip.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strip.substring(position3 + 1));
        return ((ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3]);

    }


    public static boolean isInnerIP(String ipAddress) {
        if (StringUtils.isBlank(ipAddress))
            return false;
        if (!isValid(ipAddress))
            return false;
        long ipNum = getIpNum(ipAddress);
        /**
         * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
         * 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
         **/
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        return isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
                || ipAddress.startsWith("127.");
    }

    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
        return ipNum;
    }




    public static boolean isValid(String var0) {
        return isValidIPv4(var0) || isValidIPv6(var0);
    }



    public static boolean isValidIPv4(String var0) {
        if(var0.length() == 0) {
            return false;
        } else {
            int var2 = 0;
            String var3 = var0 + ".";
            int var5 = 0;

            while(true) {
                int var4;
                if(var5 < var3.length() && (var4 = var3.indexOf(46, var5)) > var5) {
                    if(var2 == 4) {
                        return false;
                    }

                    int var1;
                    try {
                        var1 = Integer.parseInt(var3.substring(var5, var4));
                    } catch (NumberFormatException var7) {
                        return false;
                    }

                    if(var1 >= 0 && var1 <= 255) {
                        var5 = var4 + 1;
                        ++var2;
                        continue;
                    }

                    return false;
                }

                return var2 == 4;
            }
        }
    }

    public static boolean isValidIPv6(String var0) {
        if(var0.length() == 0) {
            return false;
        } else {
            int var2 = 0;
            String var3 = var0 + ":";
            boolean var4 = false;

            int var5;
            for(int var6 = 0; var6 < var3.length() && (var5 = var3.indexOf(58, var6)) >= var6; ++var2) {
                if(var2 == 8) {
                    return false;
                }

                if(var6 != var5) {
                    String var7 = var3.substring(var6, var5);
                    if(var5 == var3.length() - 1 && var7.indexOf(46) > 0) {
                        if(!isValidIPv4(var7)) {
                            return false;
                        }

                        ++var2;
                    } else {
                        int var1;
                        try {
                            var1 = Integer.parseInt(var3.substring(var6, var5), 16);
                        } catch (NumberFormatException var9) {
                            return false;
                        }

                        if(var1 < 0 || var1 > '\uffff') {
                            return false;
                        }
                    }
                } else {
                    if(var5 != 1 && var5 != var3.length() - 1 && var4) {
                        return false;
                    }

                    var4 = true;
                }

                var6 = var5 + 1;
            }

            return var2 == 8 || var4;
        }
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }
    /**
     * linux 使用
     * 要确保支持 ip 命令
     *
     * @return
     */
    public static String getEth0InetIP() {
        String cmd = " ip addr ls eth0 |grep -E -o '([0-9]{1,3}[\\.]){3}[0-9]{1,3}' ";

        String[] sh = new String[]{"/bin/sh", "-c", cmd};

        String ip = "";
        try {
            Process p = Runtime.getRuntime().exec(sh);
            ip = new DataInputStream(p.getInputStream()).readLine();

            if (p.waitFor() ==1) {
                LOG.error("get eth0 ip error ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ip;
    }
}
