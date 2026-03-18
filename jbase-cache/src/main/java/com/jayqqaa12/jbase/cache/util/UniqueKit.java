package com.jayqqaa12.jbase.cache.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.UUID;

/**
 * Created by 12 on 2017/7/17.
 */
public class UniqueKit {

    public static final int JVM_PID = ProcessHandle.current().pid() != 0 ? (int) ProcessHandle.current().pid() : -1;

    public static final String MAC_ADDR = getLocalMac();


    private static String getLocalMac() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // 字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0" + str.toUpperCase());
                } else {
                    sb.append(str.toUpperCase());
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return UUID.randomUUID().toString();
        }
    }
}
