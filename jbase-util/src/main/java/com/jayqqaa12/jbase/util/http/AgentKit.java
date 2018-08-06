package com.jayqqaa12.jbase.util.http;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * @author Boyce 上午11:33:04
 */
public class AgentKit {


    private static final String ua = "user-agent";


    public static final Pattern androidKeyword = Pattern.compile("(Android|linux)", Pattern.CASE_INSENSITIVE);

    public static final Pattern iosKeyword = Pattern.compile("(iPhone|iPod|iPad|iOs)", Pattern.CASE_INSENSITIVE);

    public static final Pattern appleKeyword = Pattern.compile("(iPhone|iPod|iPad|ios|Macintosh|Mac OS X)", Pattern.CASE_INSENSITIVE);

    public static final Pattern pcKeyword = Pattern.compile("(Windows|Macintosh|Mac OS X)", Pattern.CASE_INSENSITIVE);

    public static boolean isAndroid(HttpServletRequest request) {


        return checkUserAgent(request,androidKeyword);
    }

    public static boolean isIphone(HttpServletRequest request) {

        return checkUserAgent(request,iosKeyword);
    }

    public static boolean isApple(HttpServletRequest request) {

        return checkUserAgent(request,appleKeyword);
    }

    public static boolean isPc(HttpServletRequest request) {

        return checkUserAgent(request,pcKeyword);
    }

    public static boolean checkUserAgent(HttpServletRequest request ,Pattern keyword ){

        String userAgent = getUserAgent(request);

        return keyword.matcher(userAgent).find();
    }

    public static String getUserAgent(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            if (ua.equals(key.toLowerCase())) {
                return request.getHeader(key);
            }
        }
        return null;
    }
}
