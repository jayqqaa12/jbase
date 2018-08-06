package com.jayqqaa12.jbase.util.http;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class HeaderKit {

    private static final String UA = "user-agent";
    private static final String AL = "accept-language";
    private static final String AE = "accept-encoding";


    private static final String HOST = "host";
    private static final String XFH = "x-forwarded-host";
    private static final String XFPF = "x-forwarded-prefix";
    private static final String XFPO = "x-forwarded-proto";
    private static final String XFPT = "x-forwarded-port";
    private static final String XFF = "x-forwarded-for";


    public static String getUserAgent(HttpServletRequest request) {

        return getHeader(request, UA);
    }


    public static String getAcceptLanguage(HttpServletRequest req) {

        return getHeader(req, AL);
    }


    public static String getAcceptEncoding(HttpServletRequest req) {

        return getHeader(req, AE);
    }

    public static String getProto(HttpServletRequest req) {

        String proto = getHeader(req, XFPO);

        if (StringUtils.isEmpty(proto)) {
            return req.isSecure() ? "https" : "http";
        }
        return proto;
    }

    public static String getHost(HttpServletRequest req) {
        String host = getHeader(req, XFH);

        if (StringUtils.isEmpty(host)) {
            return getHeader(req, HOST);
        }
        return host;
    }

    public static String getPrefix(HttpServletRequest req) {

        return getHeader(req, XFPF);
    }

    public static String getClientPort(HttpServletRequest req) {

        return getHeader(req, XFPT);
    }


    public static String getClientIp(HttpServletRequest req) {
        String ip = req.getHeader(XFF);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }
        if (ip != null && ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        if (ip != null && ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }


    public static String getHeader(HttpServletRequest req, String header) {

        header = header.toUpperCase();
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            if (header.equals(key.toLowerCase())) {
                String v = req.getHeader(key);
                if (StringUtils.isNotEmpty(v) && "unknown".equalsIgnoreCase(v)) {
                    return v;
                }
            }
        }
        return null;

    }
}
