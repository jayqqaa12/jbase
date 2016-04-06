package com.jayqqaa12.jbase.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;

import javax.crypto.Cipher;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RSA
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RSA.class);

	private static final String ALGORITHOM = "RSA";
	private static final String RSA_PAIR_FILENAME = "/__RSA_PAIR.txt";
	private static final int KEY_SIZE = 1024;
	private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();

	private static KeyPairGenerator keyPairGen = null;
	private static KeyFactory keyFactory = null;

	private static KeyPair oneKeyPair = null;

	private static File rsaPairFile = null;
	
	

	static
	{
		try
		{
			keyPairGen = KeyPairGenerator.getInstance("RSA", DEFAULT_PROVIDER);
			keyFactory = KeyFactory.getInstance("RSA", DEFAULT_PROVIDER);
		} catch (NoSuchAlgorithmException ex)
		{
			LOGGER.error(ex.getMessage());
		}
		rsaPairFile = new File(getRSAPairFilePath());
	}

	/***
	 * 
	 * 返回 解码后的 username pwd
	 * 
	 * add by 12
	 * 
	 * @param key
	 * @return
	 */
	public static String[] decryptUsernameAndPwd(String key)
	{

		key = RSA.decryptStringByJs(key);

		try
		{
			String username = key.substring(key.indexOf("=") + 1, key.indexOf("&"));
			String pwd = key.substring(key.lastIndexOf("=") + 1, key.length());
			return new String[] { username, pwd };
		} catch (Exception e)
		{
			return null;
		}

	}

	private static synchronized KeyPair generateKeyPair()
	{
		try
		{
			keyPairGen.initialize(1024, new SecureRandom(DateFormatUtils.format(new Date(), "yyyyMMdd").getBytes()));
			oneKeyPair = keyPairGen.generateKeyPair();
			saveKeyPair(oneKeyPair);
			return oneKeyPair;
		} catch (InvalidParameterException ex)
		{
			LOGGER.error("KeyPairGenerator does not support a key length of 1024.", ex);
		} catch (NullPointerException ex)
		{
			LOGGER.error("RSAUtils#KEY_PAIR_GEN is null, can not generate KeyPairGenerator instance.", ex);
		}
		return null;
	}

	private static String getRSAPairFilePath()
	{
		String urlPath = RSA.class.getResource("/").getPath();
		String str = new File(urlPath).getParent() + "/__RSA_PAIR.txt";

		urlPath = null;

		return str;
	}

	private static boolean isCreateKeyPairFile()
	{
		boolean createNewKeyPair = false;
		if ((!rsaPairFile.exists()) || (rsaPairFile.isDirectory()))
		{
			createNewKeyPair = true;
		}
		return createNewKeyPair;
	}

	private static void saveKeyPair(KeyPair keyPair)
	{
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try
		{
			fos = FileUtils.openOutputStream(rsaPairFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(keyPair);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(fos);
		}

		fos = null;
		oos = null;
	}

	public static KeyPair getKeyPair()
	{
		if (isCreateKeyPairFile()) { return generateKeyPair(); }
		if (oneKeyPair != null) { return oneKeyPair; }
		return readKeyPair();
	}

	private static KeyPair readKeyPair()
	{
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try
		{
			fis = FileUtils.openInputStream(rsaPairFile);
			ois = new ObjectInputStream(fis);
			oneKeyPair = (KeyPair) ois.readObject();
			return oneKeyPair;
		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{
			IOUtils.closeQuietly(ois);
			IOUtils.closeQuietly(fis);
		}

		fis = null;
		ois = null;
		return null;
	}

	public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent)
	{
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
		try
		{
			return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
		} catch (InvalidKeySpecException ex)
		{
			LOGGER.error("RSAPublicKeySpec is unavailable.", ex);
		} catch (NullPointerException ex)
		{
			LOGGER.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
		}
		publicKeySpec = null;
		modulus = (byte[]) null;
		publicExponent = (byte[]) null;
		return null;
	}

	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent)
	{
		RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
		try
		{
			return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
		} catch (InvalidKeySpecException ex)
		{
			LOGGER.error("RSAPrivateKeySpec is unavailable.", ex);
		} catch (NullPointerException ex)
		{
			LOGGER.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
		}
		privateKeySpec = null;
		modulus = (byte[]) null;
		privateExponent = (byte[]) null;
		return null;
	}

	public static RSAPrivateKey getRSAPrivateKey(String hexModulus, String hexPrivateExponent)
	{
		if ((StringUtils.isBlank(hexModulus)) || (StringUtils.isBlank(hexPrivateExponent)))
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("hexModulus and hexPrivateExponent cannot be empty. RSAPrivateKey value is null to return.");
			}
			return null;
		}
		byte[] modulus = (byte[]) null;
		byte[] privateExponent = (byte[]) null;
		try
		{
			modulus = Hex.decodeHex(hexModulus.toCharArray());
			privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());
		} catch (DecoderException ex)
		{
			LOGGER.error("hexModulus or hexPrivateExponent value is invalid. return null(RSAPrivateKey).");
		}
		if ((modulus != null) && (privateExponent != null)) { return generateRSAPrivateKey(modulus, privateExponent); }
		return null;
	}

	public static RSAPublicKey getRSAPublidKey(String hexModulus, String hexPublicExponent)
	{
		if ((StringUtils.isBlank(hexModulus)) || (StringUtils.isBlank(hexPublicExponent)))
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("hexModulus and hexPublicExponent cannot be empty. return null(RSAPublicKey).");
			}
			return null;
		}
		byte[] modulus = (byte[]) null;
		byte[] publicExponent = (byte[]) null;
		try
		{
			modulus = Hex.decodeHex(hexModulus.toCharArray());
			publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());
		} catch (DecoderException ex)
		{
			LOGGER.error("hexModulus or hexPublicExponent value is invalid. return null(RSAPublicKey).");
		}
		if ((modulus != null) && (publicExponent != null)) { return generateRSAPublicKey(modulus, publicExponent); }
		return null;
	}

	public static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception
	{
		Cipher ci = Cipher.getInstance("RSA", DEFAULT_PROVIDER);
		ci.init(1, publicKey);
		return ci.doFinal(data);
	}

	public static byte[] decrypt(PrivateKey privateKey, byte[] data) throws Exception
	{
		Cipher ci = Cipher.getInstance("RSA", DEFAULT_PROVIDER);
		ci.init(2, privateKey);
		return ci.doFinal(data);
	}

	public static String encryptString(PublicKey publicKey, String plaintext)
	{
		if ((publicKey == null) || (plaintext == null)) { return null; }
		byte[] data = plaintext.getBytes();
		try
		{
			byte[] en_data = encrypt(publicKey, data);
			return new String(Hex.encodeHex(en_data));
		} catch (Exception ex)
		{
			LOGGER.error(ex.getCause().getMessage());
		}
		return null;
	}

	public static String encryptString(String plaintext)
	{
		if (plaintext == null) { return null; }
		byte[] data = plaintext.getBytes();
		KeyPair keyPair = getKeyPair();
		try
		{
			byte[] en_data = encrypt((RSAPublicKey) keyPair.getPublic(), data);
			return new String(Hex.encodeHex(en_data));
		} catch (NullPointerException ex)
		{
			LOGGER.error("keyPair cannot be null.");
		} catch (Exception ex)
		{
			LOGGER.error(ex.getCause().getMessage());
		}
		return null;
	}

	public static String decryptString(PrivateKey privateKey, String encrypttext)
	{
		if ((privateKey == null) || (StringUtils.isBlank(encrypttext))) return null;
		try
		{
			byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
			byte[] data = decrypt(privateKey, en_data);
			return new String(data);
		} catch (Exception ex)
		{
			LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s", encrypttext, ex.getCause().getMessage()  ));
		}
		return null;
	}

	public static String decryptString(String encrypttext)
	{
		if (StringUtils.isBlank(encrypttext)) { return null; }
		KeyPair keyPair = getKeyPair();
		try
		{
			byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
			byte[] data = decrypt((RSAPrivateKey) keyPair.getPrivate(), en_data);
			return new String(data);
		} catch (NullPointerException ex)
		{
			LOGGER.error("keyPair cannot be null.");
		} catch (Exception ex)
		{
			LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s",  encrypttext, ex.getMessage()   ));
		}
		return null;
	}

	public static String decryptStringByJs(String encrypttext)
	{
		String text = decryptString(encrypttext);
		if (text == null) { return null; }
		return StringUtils.reverse(text);
	}

	public static RSAPublicKey getDefaultPublicKey()
	{
		KeyPair keyPair = getKeyPair();
		if (keyPair != null) { return (RSAPublicKey) keyPair.getPublic(); }
		return null;
	}

	public static RSAPrivateKey getDefaultPrivateKey()
	{
		KeyPair keyPair = getKeyPair();
		if (keyPair != null) { return (RSAPrivateKey) keyPair.getPrivate(); }
		return null;
	}
}
