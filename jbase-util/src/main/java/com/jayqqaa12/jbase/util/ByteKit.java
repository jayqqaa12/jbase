package com.jayqqaa12.jbase.util;

/**
 * 操作byte 获取位 修改位
 *
 * @author: 12
 * @create: 2018-10-24 14:24
 **/
public class ByteKit {

    /**
     * 获取byte 的制定位
     *
     * @param a
     * @param i
     * @return
     */
    public static int get(int a, int i) {

        if ((a & (1 << i)) == 0) {
            return 0;
        } else return 1;
    }


    //将 整数 num 的第 i 位的值 置为 1

    
    public static int set1(int num, int i) {
        return (num | (1 << i));
    }

    //将 整数 num 的第 i 位的值 置为 0

    public static int set0(int num, int i) {
        int mask = ~(1 << i);//000100
        return (num & (mask));//111011
    }



    public static int set(int num, int i,int value) {
        if(value>0)return set1(num,i);
        else return set0(num,i);
    }



}
