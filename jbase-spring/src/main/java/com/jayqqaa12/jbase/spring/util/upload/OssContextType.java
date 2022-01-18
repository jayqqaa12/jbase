package com.jayqqaa12.jbase.spring.util.upload;

import java.util.HashMap;

/**
 * Created by yhj on 17/6/8.
 */
public class OssContextType {

    private static HashMap<String, String> map = new HashMap<>();

    public static String getContextType(String fileName) {

        String suffix = fileName.substring(fileName.lastIndexOf("."));


        String type = map.get(suffix);

        if (type == null) {
            return "application/octet-stream";
        }
        return type;
    }


    static {
        map.put(".avi", "video/avi");
        map.put(".biz", "text/xml");
        map.put(".doc", "application/msword");
        map.put(".htm", "text/html");
        map.put(".html", "text/html");
        map.put(".java", "java/*");
        map.put(".jpeg", "image/jpeg");
        map.put(".jpg", "image/jpeg");
        map.put(".js", "application/x-javascript");
        map.put(".jsp", "text/html");
        map.put(".mp2v", "video/mpeg");
        map.put(".mp3", "audio/mp3");
        map.put(".mp4", "video/mpeg4");
        map.put(".mpeg", "video/mpg");
        map.put(".mpg", "video/mpg");
        map.put(".mpv", "video/mpg");
        map.put(".mpv2", "video/mpeg");
        map.put(".pdf", "application/pdf");
        map.put(".png", "image/png");
        map.put(".ppt", "application/x-ppt");
        map.put(".rm", "application/vnd.rn-realmedia");
        map.put(".rmvb", "application/vnd.rn-realmedia-vbr");
        map.put(".svg", "text/xml");
        map.put(".swf", "application/x-shockwave-flash");
        map.put(".wav", "audio/wav");
        map.put(".xhtml", "text/html");
    }
}
