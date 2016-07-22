package ng.com.justjava.epayment.utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.*;
import org.apache.commons.codec.binary.Base64;


public class Cryptor {
	private static final String characterEncoding = "UTF-8";
	private static final String cipherTransformation = "AES/CBC/PKCS5Padding";
	private static final String aesEncryptionAlgorithm = "AES";
	
	public static String generateKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(aesEncryptionAlgorithm);
	    SecureRandom random = new SecureRandom(); // cryptograph. secure random 
	    keyGen.init(random); 
	    SecretKey secretKey = keyGen.generateKey();
	    
	    byte[] raw = secretKey.getEncoded();
	    
	    return Base64.encodeBase64String(raw);
	    
	}

	public static byte[] decrypt(byte[] cipherText, byte[] key, byte[] initialVector)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		
		Cipher cipher = Cipher.getInstance(cipherTransformation);
		SecretKeySpec secretKeySpecy = new SecretKeySpec(key,aesEncryptionAlgorithm);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
		cipherText = cipher.doFinal(cipherText);
		
		return cipherText;
	}

	public static byte[] encrypt(byte[] plainText, byte[] key, byte[] initialVector)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		
		Cipher cipher = Cipher.getInstance(cipherTransformation);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key,aesEncryptionAlgorithm);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		plainText = cipher.doFinal(plainText);
		
		return plainText;
	}

	private static byte[] getKeyBytes(String key) throws UnsupportedEncodingException {
		byte[] keyBytes = new byte[16];
		byte[] parameterKeyBytes = key.getBytes(characterEncoding);
		System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
		
		return keyBytes;
	}

	// / <summary>
	// / Encrypts plaintext using AES 128bit key and a Chain Block Cipher and
	// returns a base64 encoded string
	// / </summary>
	// / <param name="plainText">Plain text to encrypt</param>
	// / <param name="key">Secret key</param>
	// / <returns>Base64 encoded string</returns>
	public static String encrypt(String plainText, String key)
			throws UnsupportedEncodingException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {
		byte[] plainTextbytes = plainText.getBytes(characterEncoding);
		byte[] keyBytes = getKeyBytes(key);
		
		return Base64.encodeBase64String(encrypt(plainTextbytes, keyBytes, keyBytes));
	}

	// / <summary>
	// / Decrypts a base64 encoded string using the given key (AES 128bit key
	// and a Chain Block Cipher)
	// / </summary>
	// / <param name="encryptedText">Base64 Encoded String</param>
	// / <param name="key">Secret Key</param>
	// / <returns>Decrypted String</returns>
	public static String decrypt(String encryptedText, String key)
			throws KeyException, GeneralSecurityException,
			GeneralSecurityException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		byte[] cipheredBytes = Base64.decodeBase64(encryptedText);
		byte[] keyBytes = getKeyBytes(key);
		return new String(decrypt(cipheredBytes, keyBytes, keyBytes),
				characterEncoding);
	}
	
	public static String getCheckSum(String amount, String terminalId, String transactionId, String responseUrl, String secretKey) { 
        String checkSum = ""; 
        String combination = amount + terminalId + transactionId + responseUrl + secretKey; 
        try { 

            java.security.MessageDigest mdEnc = java.security.MessageDigest.getInstance("MD5"); // Encryption algorithm 
            mdEnc.update(combination.getBytes(), 0, combination.length()); 
            checkSum = new java.math.BigInteger(1, mdEnc.digest()).toString(16); 
   System.out.println("CHECKSUM is "+checkSum); 
        } catch (Exception e) { 
        } 
        return checkSum; 
    }
	public static void main(String[] args) throws Exception {
		String key = Cryptor.generateKey();//"KEd4gDNSDdMBxCGliZaC8w==" 
		String plain = "0012";//"FundGate 2.0";
		System.out.println(" key used ::::: " + key); 
		String encry = Cryptor.encrypt("0207", "R90aaowC0PrB2zILxzV1uw=="); 
		System.out.println(" encry used ::::: " +encry); 
		String decry = Cryptor.decrypt(encry, key);
		System.out.println(" decry used ::::: " + decry);
		
		
		UUID uniqueId =  UUID.randomUUID();
		System.out.println(" Uniue ID::::: " + uniqueId.toString());
		
		
		
		 
	}
}