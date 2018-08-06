package com.jayqqaa12.jbase.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularKit {
    // 姓名
    public static boolean checkNameMatch(String realName) {
        String reg = "^[\u4e00-\u9fa5]{2,25}$";
        return Pattern.compile(reg).matcher(realName).matches();
    }

    // 身份证
    public static boolean checkIdCardMatch(String idCard) {
        String reg = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)" +
                "|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        return Pattern.compile(reg).matcher(idCard).matches();
    }

    // 银行卡号
    public static boolean checkBankCardMatch(String bankCard) {
        String reg = "^\\d{16,19}$";
        return Pattern.compile(reg).matcher(bankCard).matches();
    }

    // 虚拟手机号
    public static boolean fictitiousPhone(String phone) {
        String reg = "^\\d{11}$";
        return Pattern.compile(reg).matcher(phone).matches();
    }
    //手机号
    public static boolean checkPhone(String phone) {
        String reg = "^1(3|4|5|7|8)\\d{9}$";
        return Pattern.compile(reg).matcher(phone).matches();
    }

    // 昵称
    public static boolean checkNickName(String userName) {
        String reg = "^[a-zA-Z0-9\u4e00-\u9fa5]{1,8}$";
        return Pattern.compile(reg).matcher(userName).matches();
    }

    // QQ
    public static boolean checkQQ(String qq) {
        String reg = "^[1-9][0-9]{4,9}$";
        return Pattern.compile(reg).matcher(qq).matches();
    }

    // 邮箱
    public static boolean checkEmail(String email) {
        String reg = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        return Pattern.compile(reg).matcher(email).matches();
    }


    public static int getDigitalForString(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return Integer.valueOf(m.replaceAll("").trim());
    }
    // 微信
    public static boolean checkWechat(String wechat) {
        String judge = "^[a-zA-Z-1-9]{1}[-_a-zA-Z0-9]{5,19}+$";
        Pattern pat = Pattern.compile(judge);
        Matcher mat = pat.matcher(wechat);
        return mat.matches();
    }
}
