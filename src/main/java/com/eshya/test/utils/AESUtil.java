package com.eshya.test.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class AESUtil {

	public static byte[][] generateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password,
			MessageDigest md) {

		int digestLength = md.getDigestLength();
		int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
		byte[] generatedData = new byte[requiredLength];
		int generatedLength = 0;

		try {
			md.reset();

			// Repeat process until sufficient data has been generated
			while (generatedLength < keyLength + ivLength) {

				// Digest data (last digest if available, password data, salt if available)
				if (generatedLength > 0)
					md.update(generatedData, generatedLength - digestLength, digestLength);
				md.update(password);
				if (salt != null)
					md.update(salt, 0, 8);
				md.digest(generatedData, generatedLength, digestLength);

				// additional rounds
				for (int i = 1; i < iterations; i++) {
					md.update(generatedData, generatedLength, digestLength);
					md.digest(generatedData, generatedLength, digestLength);
				}

				generatedLength += digestLength;
			}

			// Copy key and IV into separate byte arrays
			byte[][] result = new byte[2][];
			result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
			if (ivLength > 0)
				result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);

			return result;

		} catch (DigestException e) {
			throw new RuntimeException(e);

		} finally {
			// Clean out temporary data
			Arrays.fill(generatedData, (byte) 0);
		}
	}

	/**
	 * A function to encrypt a text
	 * @param plainText
	 * @param secret
	 * @return
	 */
	public static String encrypt(String plainText, String secret) {

//		String secret = Constant.UI__SECRET_KEY;

		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[8];
		sr.nextBytes(salt);
		byte[][] keyAndIV;
		try {
			keyAndIV = generateKeyAndIV(32, 16, 1, salt, secret.getBytes(StandardCharsets.UTF_8),
					MessageDigest.getInstance("MD5"));
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyAndIV[0], "AES"), new IvParameterSpec(keyAndIV[1]));
			byte[] encryptedData = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			byte[] prefixAndSaltAndEncryptedData = new byte[16 + encryptedData.length];
			// Copy prefix (0-th to 7-th bytes)
			System.arraycopy("Salted__".getBytes(StandardCharsets.UTF_8), 0, prefixAndSaltAndEncryptedData, 0, 8);
			// Copy salt (8-th to 15-th bytes)
			System.arraycopy(salt, 0, prefixAndSaltAndEncryptedData, 8, 8);
			// Copy encrypted data (16-th byte and onwards)
			System.arraycopy(encryptedData, 0, prefixAndSaltAndEncryptedData, 16, encryptedData.length);
			return Base64.getEncoder().encodeToString(prefixAndSaltAndEncryptedData);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	/**
	 * A function to decrypt a encrypted text
	 * @param cipherText
	 * @param secret
	 * @return
	 */
	public static String decrypt(String cipherText, String secret) {

		byte[] cipherData = Base64.getDecoder().decode(cipherText);
		byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

		MessageDigest md5;

		try {
			md5 = MessageDigest.getInstance("MD5");
			final byte[][] keyAndIV = generateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8),
					md5);
			SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
			IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

			byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
			Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
			aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] decryptedData = aesCBC.doFinal(encrypted);
			String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);

			return decryptedText;

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
