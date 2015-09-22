package com.jayqqaa12.jbase.util;


import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * JDiy实用函数工具类.
 *
 * @author 子秋(ziquee)  http://www.jdiy.org
 */
public class Fn {
    /**
     * 时间间隔枚举.
     * 此枚举供{@link Fn#dateadd(Fn.interval, int, java.util.Date)}和
     * {@link Fn#datediff(Fn.interval, java.util.Date, java.util.Date)}方法的参数调用.
     */
    public static enum interval {
        /**
         * 代表年份.
         */
        Y,
        /**
         * 代表月份.
         */
        M,
        /**
         * 代表日期天
         */
        D,
        /**
         * 代表小时
         */
        h,
        /**
         * 代表分钟
         */
        m,
        /**
         * 代表秒
         */
        s,
        /**
         * 代表周(星期)
         */
        w
    }

    /**
     * 检测某个数组对象的所有下标元素中是否包含另一个对象.
     *
     * @param arrObj 被检测的数组对象.
     * @param theObj 要检测的对象.
     * @return true | false 如果arrObj的下标元素中包含theObj,将返回true, 否则返回false.
     * @see #containsIgnoreCase(String[], String)
     */
    public static boolean contains(Object arrObj[], Object theObj) {
        return indexOf(arrObj, theObj) != -1;
    }

    /**
     * 以不区分大小写的方式检测某个字符串数组元素中是否包含另一个字符串.
     *
     * @param arr 被检测的字符串数组.
     * @param str 要检测的字符串.
     * @return true | false 如果arr的下标元素中包含str（不区分大小写）,将返回true, 否则返回false.
     * @see #contains(Object[], Object)
     */
    public static boolean containsIgnoreCase(String arr[], String str) {
        if (arr == null) return false;
        for (String anArr : arr) {
            if (anArr != null && anArr.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测某个数组的所有元素是否包含另一个对象,并返回该对象在此数组中首次出现的位置.
     *
     * @param arrObj 被检测的数组对象.
     * @param theObj 要检测的对象.
     * @return int 如果theObj不在arrObj数组中出现，将返回-1.
     */
    public static int indexOf(Object arrObj[], Object theObj) {
        if (arrObj == null) return -1;
        for (int i = 0; i < arrObj.length; i++)
            if (arrObj[i] != null && arrObj[i].equals(theObj))
                return i;
        return -1;
    }

    /**
     * 返回添加指定间隔之后的日期时间. 实现类似MSSQL数据库dateadd函数的功能.
     *
     * @param interval 要添加的间隔的类型，支持以下几种类型：<br />　　Y（年） M（月） D（日） h（时） m（分） s（秒） w(周)
     * @param number   要添加的间隔数. 如果为正数，将返回参数datetime之后的日期时间；如果为负，将返回datetime之前的日期时间；如果为零，返回datetime本身.
     * @param datetime 指定日期时间.
     * @return 添加指定间隔之后的日期时间
     */
    @SuppressWarnings("unused")
    public static Date dateadd(interval interval, int number, Date datetime) {
        if (number == 0) return datetime;
        Calendar cd1 = Calendar.getInstance();
        if (datetime != null) cd1.setTime(datetime);
        int X;
        switch (interval) {
            case Y:
                X = Calendar.YEAR;
                break;
            case M:
                X = Calendar.MONTH;
                break;
            case D:
                X = Calendar.DAY_OF_YEAR;
                break;
            case h:
                X = Calendar.HOUR;
                break;
            case m:
                X = Calendar.MINUTE;
                break;
            case s:
                X = Calendar.SECOND;
                break;
            case w:
                X = Calendar.WEEK_OF_YEAR;
                break;
            default:
                X = Calendar.DAY_OF_YEAR;
        }
        cd1.set(X, cd1.get(X) + number);
        return cd1.getTime();
    }

    /**
     * 返回两个日期时间之间的间隔.
     * 实现类似MSSQL数据库datediff函数的功能.
     * <br />如果date1早于date2，将返回正数；如果date1晚于date2, 将返回负数；如果相等，将返回0.
     *
     * @param interval  为返回的间隔的类型. 有以下几种类型：<br />　　Y（年） M（月） D（日） h（时） m（分） s（秒） <br/>
     *                  　　例如：传入D代表查询两个日期之间相距多少天；传入h代表两个日期之间相隔多少小时.
     * @param datetime1 日期时间1.
     * @param datetime2 日期时间2.
     * @return 两个日期时间之间的间隔
     */
    public static int datediff(interval interval, Date datetime1, Date datetime2) {
        Calendar cd1 = Calendar.getInstance(), cd2 = Calendar.getInstance();
        cd1.setTime(datetime1);
        cd2.setTime(datetime2);
        int Y1 = cd1.get(Calendar.YEAR), Y2 = cd2.get(Calendar.YEAR);
        int M1 = cd1.get(Calendar.MONTH), M2 = cd2.get(Calendar.MONTH);
        int D1 = cd1.get(Calendar.DAY_OF_YEAR), D2 = cd2.get(Calendar.DAY_OF_YEAR);
        int h1 = cd1.get(Calendar.HOUR_OF_DAY), h2 = cd2.get(Calendar.HOUR_OF_DAY);
        int m1 = cd1.get(Calendar.MINUTE), m2 = cd2.get(Calendar.MINUTE);
        int s1 = cd1.get(Calendar.SECOND), s2 = cd2.get(Calendar.SECOND);

        if (interval == Fn.interval.Y) { //year
            return Y2 - Y1;
        } else if (interval == Fn.interval.w) { //week
            if (cd1.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                cd1.add(Calendar.DAY_OF_YEAR, 8 - cd1.get(Calendar.DAY_OF_WEEK));
            if (cd2.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                cd2.add(Calendar.DAY_OF_YEAR, 8 - cd2.get(Calendar.DAY_OF_WEEK));
            return datediff(Fn.interval.D, cd1.getTime(), cd2.getTime()) / 7;
        }

        int stepM = 0, stepD = 0;
        if (Y1 == Y2) {
            stepM = M2 - M1;
            stepD = D2 - D1;
        } else if (Y1 < Y2) {
            for (int i = Y1; i <= Y2; i++) {
                int daysOfYear = ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0) ? 366 : 365;
                if (i == Y1) {
                    stepM += 12 - M1;
                    stepD += daysOfYear - D1;
                } else if (i == Y2) {
                    stepM += M2;
                    stepD += D2;
                } else {
                    stepM += 12;
                    stepD += daysOfYear;
                }
            }
        } else {
            for (int i = Y1; i >= Y2; i--) {
                int daysOfYear = ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0) ? 366 : 365;
                if (i == Y1) {
                    stepM -= M1;
                    stepD -= D1;
                } else if (i == Y2) {
                    stepM -= 12 - M2;
                    stepD -= daysOfYear - D2;
                } else {
                    stepM -= 12;
                    stepD -= daysOfYear;
                }
            }
        }
        int steph = stepD * 12 + h2 - h1;
        int stepm = steph * 60 + m2 - m1;
        int steps = stepm * 60 + s2 - s1;
        switch (interval) {
            case M:
                return stepM;
            case D:
                return stepD;
            case h:
                return steph;
            case m:
                return stepm;
            case s:
                return steps;
            default:
                return 0;
        }
    }
    
    
    public static void rename(File f, String dir){
    	f.renameTo(new File(dir));
    }

    private Fn() {
    }
}
