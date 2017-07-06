package com.zly.apispider.encrypt;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import sun.misc.*;//几百年前的类库了Java8删除了

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;



public class Encrypter {
	private static Logger logger = Logger.getLogger(Encrypter.class);
	private static Encrypter encryter = null;
	private Encrypter() {
	}
	public static Encrypter getInstance() {
		return encryter!=null?encryter:new Encrypter();
	}
	public String[] encryptedRequest(String data) {
		String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b72" + 
		        "5152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbd" + 
		        "a92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe48" + 
		        "75d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";
		String nonce = "0CoJUm6Qyw8W8jud";
		String pubKey = "010001";
		String secKey = createSecretKey(16);
		data = aes_encrypt(aes_encrypt(data, nonce),secKey);
		String encryptKey = rsa_encrypt(secKey, pubKey, modulus);
		String[] postParams = new String[2];
		postParams[0] = data;
		postParams[1] = encryptKey;
		return postParams;
	}
	 private String aes_encrypt(String text, String sKey) {  
	        Cipher cipher = null;
			try {
				cipher = Cipher.getInstance("AES/CBC/NoPadding");
			} catch (NoSuchAlgorithmException e) {
				logger.error(e);
			} catch (NoSuchPaddingException e) {
				logger.error(e);
			}  
	        byte[] raw = sKey.getBytes();
	        String ivParameter = "0102030405060708";
	        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");//生成密匙 
	        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度  
	        try {
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			} catch (InvalidKeyException e) {
				logger.error(e);
			} catch (InvalidAlgorithmParameterException e) {
				logger.error(e);
			}
	        int pad = 16 - text.length()%16;
	        for(int i=0;i<pad;i++) {
	        	text = text + String.valueOf((char)pad);
	        }//我们按照网易云的方式填充
	        byte[] encrypted = null;
			try {
				encrypted = cipher.doFinal(text.getBytes());
			} catch (IllegalBlockSizeException e) {
				logger.error(e);
			} catch (BadPaddingException e) {
				logger.error(e);
			}  
	        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。  
	} 
	private String createSecretKey(int num) {
			String s = "012345679abcdef";
			char[] choice = s.toCharArray();
			StringBuilder builder = new StringBuilder();
			for(int i=0; i<num; i++) {
				int index = (int) Math.floor(Math.random()*choice.length);
				builder.append(choice[index]);
			}
			return builder.toString();
	}
	private String rsa_encrypt(String secKey,String pubkey, String modulus) {
		char[] keyArray = secKey.toCharArray();
		StringBuilder builder = new StringBuilder();
		for(int i=keyArray.length-1;i>=0;i--) {
			builder.append(keyArray[i]);
		}
		String reservedKey = builder.toString();//倒序排列secKey
		BigInteger result = expmod(string2Hex(reservedKey), new BigInteger(pubkey,16), new BigInteger(modulus, 16));		
		return zfill(String.format("%x", result),256);
	}
	private BigInteger string2Hex(String s) {
		byte[] b = s.getBytes();
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<b.length;i++) {
			builder.append(Integer.toHexString(b[i]));
		}
		return new BigInteger(builder.toString(),16);
	}
	private BigInteger expmod(BigInteger base, BigInteger exp, BigInteger mod) {//base^exp%mod
		return base.modPow(exp, mod);
	}
	private String zfill(String s,int num) { 
		while(s.length()<num) {
			s = "0" + s;
		}
		return s;
	}
}
