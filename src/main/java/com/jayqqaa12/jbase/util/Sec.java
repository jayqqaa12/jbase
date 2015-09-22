package com.jayqqaa12.jbase.util;


import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 字符串常用加密/解密工具类.
 *
 *RSA 参考 RSA
 */
public class Sec {

    /**
     * AES算法密钥生成器.
     * @return 生成的密钥 它是一个32个字符的16进制字符串.
     */
    @SuppressWarnings("unused")
    public static String AESKey() {
        try {
            // Get the KeyGenerator
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); // 192 and 256 bits may not be available
            // Generate the secret key specs.
            SecretKey key = keyGenerator.generateKey();
            byte[] raw = key.getEncoded();
            return byteArr2HexStr(raw);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 使用AES算法解密字符串. 
     * AES加密算法（美国国家标准局倡导的AES即将作为新标准取代DES）
     * @param encrypted 要解密的字符串
     * @param rawKey 密钥字符串, 要求为一个32位(或64位，或128位)的16进制数的字符串,否则会出错.
     * 可以使用{@link #AESKey()}方法生成一个密钥,
     * @return 解密之后的字符串
     * @see #AESEncrypt(String, String)
     */
    public static String AESDecrypt(String encrypted, String rawKey){
        byte[] tmp = hexStr2ByteArr(encrypted);
        byte[] key = hexStr2ByteArr(rawKey);
        try {
            SecretKeySpec sks = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            byte[] decrypted = cipher.doFinal(tmp);
            return new String(decrypted);
        } catch (Exception e) {
        	return null;
        }
    }

    /**
     * 使用AES算法加密字符串.
     * @param message 要加密的字符串.
     * @param rawKey 密钥字符串, 要求为一个32位(或64位，或128位)的16进制数的字符串,否则会出错.
     * 可以使用{@link #AESKey()}方法生成一个密钥,
     * @return 加密之后的字符串
     * @see #AESDecrypt(String, String)
     */
    public static String AESEncrypt(String message, String rawKey){
        byte[] key = hexStr2ByteArr(rawKey);
        try {
            SecretKeySpec sks = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            byte[] encrypted = cipher.doFinal(message.getBytes());
            return byteArr2HexStr(encrypted);
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }

    /**
     * 使用DES算法解密字符串. 
     * @param encrypted 要解密的字符串.
     * @param rawKey 密钥字符串, 可以为任意字符, 但最长不得超过8个字符(如最超过，后面的字符会被丢弃).
     * @return 解密之后的字符串.
     * @see #DESEncrypt(String, String)
     */
    public static String DESDecrypt(String encrypted, String rawKey){
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        byte[] arrBTmp = rawKey.getBytes();
        byte[] arrB = new byte[8]; // 创建一个空的8位字节数组（默认值为0）
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) // 将原始字节数组转换为8位
            arrB[i] = arrBTmp[i];
        try {
            Key key = new SecretKeySpec(arrB, "DES");// 生成密钥
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(hexStr2ByteArr(encrypted)));
        } catch (Exception e) {
        	
        	return null;
        }
    }

    /**
     * 使用DES算法加密字符串. 
     * @param message 要加密的字符串.
     * @param rawKey 密钥字符串,  可以为任意字符, 但最长不得超过8个字符(如最超过，后面的字符会被丢弃).
     * @return 加密之后的字符串.
     * @see #DESDecrypt(String, String)
     */
    public static String DESEncrypt(String message, String rawKey) {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        byte[] arrBTmp = rawKey.getBytes();
        byte[] arrB = new byte[8]; // 创建一个空的8位字节数组（默认值为0）
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) // 将原始字节数组转换为8位
            arrB[i] = arrBTmp[i];
        try {
            Key key = new SecretKeySpec(arrB, "DES");// 生成密钥
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return byteArr2HexStr(cipher.doFinal(message.getBytes()));
        } catch (Exception e) {

        	return null;
        }
    }
    
    
    public static String md5(String message){
    	
    	return MD5Encrypt( MD5Encrypt(message, 32), 32);
    }
    
    

    /**
     * 使用MD5算法加密字符串. 
     * @param message 要加密的字符串.
     * @param length 指定返回加密后字符串长度，其值必须是16或者32. 
     * @return 加密之后的字符串
     */
    public static String MD5Encrypt(String message, int length){
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(message.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            String str32=buf.toString().toUpperCase();
            if(length==32) return str32;
            else if (length==16) return str32.substring(8,24);
            else throw new  Exception( "mod5 encrypt fail");
        } catch (Exception e) {
        	return null;
        }
    }

    /**
     * Turns array of bytes into string
     * @param buf Array of bytes to convert to hex string
     * @return Generated hex string
     */
    private static String byteArr2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)  sb.append("0");

            sb.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] buf)互为可逆的转换过程
     * @param src 需要转换的字符串
     * @return 转换后的byte数组
     */
    private static byte[] hexStr2ByteArr(String src) {
        if (src.length() < 1) {
            return null;
        }
        byte[] encrypted = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);

            encrypted[i] = (byte) (high * 16 + low);
        }
        return encrypted;
    }

    private Sec(){}
    
    
    public static void main(String[] args)
	{
    	System.out.println(md5("231566qq"));
		
	}
    

}
