package com.jayqqaa12.jbase.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSACryptKit {
	public static final Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
	static {
		Security.addProvider(provider);
	}
	public static KeyPair generateKeypair(int keyLength) throws Exception {
		try {
			KeyPairGenerator kpg;
			try {
				kpg = KeyPairGenerator.getInstance("RSA");
			} catch (Exception e) {
				kpg = KeyPairGenerator.getInstance("RSA", provider);
			}
			kpg.initialize(keyLength);
			KeyPair keyPair = kpg.generateKeyPair();
			System.out.println(new Base64().encodeAsString(keyPair.getPrivate().getEncoded()));
			System.out.println(new Base64().encodeAsString(keyPair.getPublic().getEncoded()));
			return keyPair;
		} catch (NoSuchAlgorithmException e1) {
			throw new RuntimeException("RSA algorithm not supported", e1);
		} catch (Exception e) {
			throw new Exception("other exceptions", e);
		}
	}
	
    /** 
     * <p> 
     * 公钥加密 
     * </p> 
     *  
     * @param data 源数据 
     * @param publicKey 公钥(BASE64编码) 
     * @return 
     * @throws Exception 
     */  
    public static String encryptByPublicKey(String data, String publicKey)  
            throws Exception {  
        
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(new Base64().decode(publicKey));  
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密  
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
        cipher.init(Cipher.ENCRYPT_MODE, publicK); 
    	byte[] code=cipher.doFinal(data.getBytes());
           
    	return new Base64().encodeAsString(code);
    } 
    
    
    /** *//** 
     * <P> 
     * 私钥解密 
     * </p> 
     *  
     * @param encryptedData 已加密数据 
     * @param privateKey 私钥(BASE64编码) 
     * @return 
     * @throws Exception 
     */  
    public static String decryptByPrivateKey(String encryptedData, String privateKey)  
            throws Exception {  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(new Base64().decode(privateKey));  
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        
       return  new String(cipher.doFinal(new Base64().decode(encryptedData)));
        
    } 
	
    /** 
     * <p> 
     * 用私钥对信息生成数字签名 
     * </p> 
     * 
     * @param encryptedData 已加密数据 
     * @param privateKey 私钥(BASE64编码) 
     * 
     * @return 
     * @throws Exception 
     */  
    public static String sign(byte[] encryptedData, String privateKey) throws Exception {  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(new Base64().decode(privateKey));  
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Signature signature = Signature.getInstance("SHA1WithRSA");  
        signature.initSign(privateK);  
        signature.update(encryptedData);  
        byte[] signed =  signature.sign();
    	return new Base64().encodeAsString(signed);
    } 
    

    /** 
     * <p> 
     * 校验数字签名 
     * </p> 
     * 
     * @param encryptedData 已加密数据 
     * @param publicKey 公钥(BASE64编码) 
     * @param sign 数字签名 
     * 
     * @return 
     * @throws Exception 
     * 
     */  
    public static boolean verify(byte[] encryptedData, String publicKey, String sign)  
            throws Exception {  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(new Base64().decode(publicKey));  
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
        PublicKey publicK = keyFactory.generatePublic(keySpec);  
        Signature signature = Signature.getInstance("SHA1WithRSA");  
        signature.initVerify(publicK);  
        signature.update(encryptedData);  
        return signature.verify(new Base64().decode(sign));  
    }  

	public static void main(String[] args) throws Exception {
		  
		String privateKey="MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAkXbYNqM5evxy8ooSB8Ts4vZDh0ysCuyKq9kS5XYmis7D9S/"
				+ "Cou7yhnxl920Eg0MAU30HZm9nGZoymDwsZuo84QIDAQABAkBD8hNEUjHDNLAsgjmxz1YnHYilZjbmU17irl6ZN/sA0qTOnyVApaddDgE"
				+ "Nq6tx6mmx9RDmtJFJMrORuaR/x+cBAiEA5Ycmj7QeGRhUoLnA+HaI/Lovk0O8z7s3SL95RPM7yqkCIQCiPbMheSUDBUZ19wSD9UMQ16HL"
				+ "AnBKpqP4F9+E/m67eQIgMZh3c5u22TNRrf0VPlrWlM1iVE7RsI1Cj9yXxpdMNykCIA9zMrjQUY79FJ2tPVfXpmBXOIgdnlXtkpXQqC+BD0"
				+ "h5AiEAka2mrBwlRHkQU88yzuIpmhTlitX8HJ5EDh8AoV5ncRw=";
		String publicKey="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJF22DajOXr8cvKKEgfE7OL2Q4dMrArsiqvZEuV2JorOw/UvwqLu8oZ8ZfdtBINDAFN9B2ZvZxmaMpg8LGbqPOECAwEAAQ==";
		generateKeypair(1024);
		// 构造PKCS8EncodedKeySpec对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(new Base64().decode(privateKey));
			// KEY_ALGORITHM 指定的加密算法
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			// 取私钥匙对象
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
			
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(new Base64().decode(publicKey));
			// 取私钥匙对象
			PublicKey pubkey = keyFactory.generatePublic(x509KeySpec);
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, priKey);
			byte[] code=cipher.doFinal("你 你好你好你好你好".getBytes());
			System.out.println(new Base64().encodeAsString(code));
			System.out.println("ZJMZ1T4Q0ksPNLE3G42LYdDfTGKwGerWb+w2d9yap4iD88N1ThEE72+bS9rwBbi8pjdPKfjhvdti3nlZdQ9KVw==");
		
			
			
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, pubkey);
			System.out.println(new String(cipher.doFinal(code)));;
			
			
			
			String pp="Pl7Ir11t72QqcyZhwAu31Bfs39w5Be2/qbefe5SP7adPZxYlZbNaLHh/qDUxVmEiJNmDyXnfoMpYlSs8YnNo9g==";
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			System.out.println(new String(cipher.doFinal(new Base64().decode(pp))));;
			
			
//			
//			Signature signature = Signature.getInstance("MD5withRSA");
//			signature.initSign(priKey);
//			signature.update("123".getBytes());
//			byte[] sign=signature.sign();
//			
//			signature = Signature.getInstance("MD5withRSA");
//			signature.initVerify(pubkey);
//			signature.update("123".getBytes());
//			System.out.println(signature.verify(sign));
	}
}
