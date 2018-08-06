package com.jayqqaa12.jbase.util.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieKit {
    private static int maxAge = -1;

    private static String domain = null;

    public static void addCookie(String name, Object value, HttpServletResponse resp, HttpServletRequest req) {

        addCookie(resp, name, value, maxAge, domain, getPath(req));
    }


    public static void addCookie(HttpServletResponse resp, String name, Object value, int maxAge, String domain, String path) {
        if (value == null) {
            throw new RuntimeException("name[" + name + "] value is null");
        }
        if (resp != null) {
            Cookie cookie = new Cookie(name, String.valueOf(value));

            if (maxAge > 0) {
                cookie.setMaxAge(maxAge);
            }
            if (domain != null) {
                cookie.setDomain(domain);
            }
            if (path != null) {
                cookie.setPath(path);
            }
            resp.addCookie(cookie);
        } else {
            throw new RuntimeException("http response is null");
        }
    }

    public static Cookie getCookie(HttpServletRequest req, String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getCookieAttr(String name, HttpServletRequest req) {
        if (req == null) {
            throw new RuntimeException("http request is null");
        }
        if (req != null) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    if (cookie.getName().equals(name)) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return null;
    }

    private static String getPath(HttpServletRequest request) {
        String path = request.getContextPath();
        return (path == null || path.length() == 0) ? "/" : path;
    }

}

