package com.jayqqaa12.jbase.spring.util;


import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by framework on 2014/11/7.
 */
public class StringUtil extends StringUtils {

    public static final String EMPTY = "";

    public static String trimToEmpty(String str) {
        return isEmpty(str) ? EMPTY : str.trim();
    }

    public static String trimToNull(String str) {
        return isEmpty(str) ? null : str.trim();
    }

    public static String trimToDefault(String str, String defaultStr) {

        return isEmpty(str) ? defaultStr : str;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(String str) {

        return str == null || str.length() == 0;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }


    /**
     * 首字母修改为小写
     *
     * @param str
     * @return
     */
    public static String firstCharToLowerCase(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            char[] arr = str.toCharArray();
            arr[0] += ('a' - 'A');
            return new String(arr);
        }
        return str;
    }

    /**
     * 首字母修改为大写
     *
     * @param str
     * @return
     */
    public static String firstCharToUpperCase(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            char[] arr = str.toCharArray();
            arr[0] -= ('a' - 'A');
            return new String(arr);
        }
        return str;
    }

    public static String array2String(String[] strs, String delimiter) {
        if (strs == null || strs.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (String s : strs) {
            sb.append(delimiter).append(s);
        }
        return sb.substring(delimiter.length());
    }

    public static String[] tokenizeToStringArray(String str) {
        return tokenizeToStringArray(str, ";,", true, true);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return new String[0];
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List tokens = new ArrayList();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    public static String[] toStringArray(Collection collection) {
        if (collection == null) {
            return null;
        }
        return (String[]) collection.toArray(new String[collection.size()]);
    }

    /**
     * 去掉下划线转换为大写
     */
    public static String camelToUnderline(String s) {
        if (s == null || "".equals(s.trim())) {
            return "";
        }
        int len = s.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            if (Character.isUpperCase(s.charAt(i)) && i > 0) {
                sb.append("_").append(Character.toLowerCase(s.charAt(i)));
            } else {
                sb.append(Character.toLowerCase(s.charAt(i)));
            }
        }
        return sb.toString();
    }

    /**
     * 去掉下划线转换为大写
     */
    public static String camelToUppercase(String s) {
        if (s == null || "".equals(s.trim())) {
            return "";
        }
        int len = s.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == '_' && i > 0 && i < len) {
                sb.append(Character.toUpperCase(s.charAt(++i)));
            } else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }


    public static Integer[] split2Int(String strs) {

        if (isEmpty(strs)) {
            return new Integer[0];
        }

        String[] splits = tokenizeToStringArray(strs, ";,");
        Integer[] result = new Integer[splits.length];

        for (int i = 0; i < splits.length; i++) {
            if (isNumeric(splits[i])) {
                result[i] = Integer.parseInt(splits[i]);
            }
        }
        return result;
    }

    public static Double[] split2Double(String strs) {

        if (isEmpty(strs)) {
            return new Double[0];
        }

        String[] splits = tokenizeToStringArray(strs, ";,");
        Double[] result = new Double[splits.length];

        for (int i = 0; i < splits.length; i++) {
            if (isNumeric(splits[i])) {
                result[i] = Double.parseDouble(splits[i]);
            }
        }
        return result;
    }


}
