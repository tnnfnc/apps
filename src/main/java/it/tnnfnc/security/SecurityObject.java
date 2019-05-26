/*
 * Copyright (c) 2015, Franco Toninato. All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW. 
 * EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER 
 * PARTIES PROVIDE THE PROGRAM �AS IS� WITHOUT WARRANTY OF ANY KIND, EITHER 
 * EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE 
 * QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE 
 * DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION.
 */
package it.tnnfnc.security;

import java.security.DigestException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Properties;

import it.tnnfnc.apps.application.ui.Utility;
import it.tnnfnc.encoders.Base64;
import it.tnnfnc.security.algorithm.AlgorithmParameters;
import it.tnnfnc.security.algorithm.IVAlgorithmParameter;
import it.tnnfnc.security.algorithm.KeyAlgorithmParameter;
import it.tnnfnc.security.algorithm.PBKDAlgorithmParameter;
import it.tnnfnc.security.crypto.I_BlockCipherStream;
import it.tnnfnc.security.crypto.I_Mac;
import it.tnnfnc.security.crypto.I_PasswordBasedKeyDerivator;

/**
 * This class provides the cipher from the user password.
 * 
 * @author Franco Toninato
 * 
 */
public class SecurityObject {

	private SecurityProvider securityProvider;
	private I_BlockCipherStream cipher;
	private I_PasswordBasedKeyDerivator keyDerivator;
	private SecureRandom random;
	private MessageDigest hashFunction;
	private KeyAlgorithmParameter master_key;
	private int key_size = 0; // bits
	private Properties properties;
	private byte[] salt;
	protected byte[] securityID;

	/**
	 * Create a security instance. A new random salt is generated if an initial
	 * value is passed.
	 * 
	 * @param properties
	 *            the properties.
	 * @param password
	 *            the password for key derivation.
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws HashException
	 * @throws IllegalArgumentException
	 */
	public SecurityObject(Properties properties, byte[] key) throws InvalidKeyException, IllegalStateException,
			IllegalArgumentException, DigestException, IllegalArgumentException {
		try {
			byte dummykey[] = new byte[] { (byte) 0x00 };
			this.properties = properties;
			byte seed[] = new byte[key.length];
			System.arraycopy(key, 0, seed, 0, key.length);

			int iterations = Utility.toInteger(properties.getProperty(EncryptionPreferences.PREF_ITERATIONS));
			// String cipherAlgorithm[] = new String[3];
			String hmacAlgorithm[] = new String[2];
			if (properties.getProperty(EncryptionPreferences.PREF_CIPHER_ALGORITHM) == null) {
				throw new IllegalArgumentException("No Cipher algorithm defined");
			}
			if (properties.getProperty(EncryptionPreferences.PREF_HMAC) == null) {
				throw new IllegalArgumentException("No HMAC defined");
			}
			if (properties.getProperty(EncryptionPreferences.PREF_KEYSIZE) == null
					|| properties.getProperty(EncryptionPreferences.PREF_KEYSIZE).equals("0")) {
				throw new IllegalArgumentException("Invalid key size defined");
			}

			hmacAlgorithm = SecurityProvider.parseRequest(properties.getProperty(EncryptionPreferences.PREF_HMAC));

			key_size = Utility.toInteger(properties.getProperty(EncryptionPreferences.PREF_KEYSIZE));

			// Security provider;
			securityProvider = new SecurityProvider();

			// Random generator
			try {
				random = securityProvider.createSecureRandom();
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalArgumentException("Invalid algorithm name: " + e.getMessage());
			}
			// Hash algorithm
			hashFunction = securityProvider.createMessageDigest(hmacAlgorithm[1]);

			// For new documents generate a salt
			if (properties.getProperty(EncryptionPreferences.PREF_SALT) == null
					|| properties.getProperty(EncryptionPreferences.PREF_SALT).length() == 0) {
				// New salt
				properties.setProperty(EncryptionPreferences.PREF_SALT,
						new String(Base64.encode(getRandomBytes(key_size / 8))));
			}
			// else {
			salt = Base64.decode(properties.getProperty(EncryptionPreferences.PREF_SALT).getBytes());
			// }
			// BlockCipherStream
			cipher = securityProvider
					.createBlockCipherStream(properties.getProperty(EncryptionPreferences.PREF_CIPHER_ALGORITHM));

			// Get the secret key derivator for passphrase
			keyDerivator = securityProvider
					.createPasswordBasedKeyDerivator(properties.getProperty(EncryptionPreferences.PREF_PBKDF));
			AlgorithmParameters pbeParams = new AlgorithmParameters();
			pbeParams.setParameter(new PBKDAlgorithmParameter(dummykey,
					Base64.decode(properties.getProperty(EncryptionPreferences.PREF_SALT).getBytes()), iterations,
					securityProvider.createMac(properties.getProperty(EncryptionPreferences.PREF_HMAC)),
					// key_size)); AES requires bit
					key_size / 8));
			keyDerivator.init(pbeParams);

			// Master key
			this.master_key = keyDerivator.generateKey(seed);

			// HMAC
			I_Mac hMACfunction = getHMAC();
			hMACfunction.init(salt);

			// Enable internal check
			securityID = hMACfunction.doFinal(hashFunction.digest(master_key.getKey()));
			Arrays.fill(seed, (byte) 0x0);

		} catch (DigestException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Security check.
	 * 
	 * @param key
	 *            the byte array to be checked.
	 * @return true when match.
	 */
	public boolean check(byte[] key) {
		byte b[] = new byte[key.length];
		System.arraycopy(key, 0, b, 0, key.length);
		try {
			I_Mac hMACfunction = getHMAC();
			hMACfunction.init(salt);

			byte[] bs = keyDerivator.generateKey(b).getKey();

			return (b != null && Arrays.equals(securityID, hMACfunction.doFinal(hashFunction.digest(bs))));
		} catch (InvalidKeyException e) {
			Arrays.fill(b, (byte) 0x0);
			return false;
		} catch (DigestException e) {
			Arrays.fill(b, (byte) 0x0);
			return false;
		}
	}

	/**
	 * Return a random seed of the cipher block size.
	 * 
	 * @return a random seed of the cipher block size.
	 */
	public byte[] getRandomSeed() {
		return getRandomBytes(cipher.getBlockSize());
	}

	/**
	 * Get a random bites array.
	 * 
	 * @param len
	 *            number of bytes.
	 * @return a random bites array.
	 */
	public byte[] getRandomBytes(int len) {
		// Random key

		byte[] rnbytes = new byte[len];
		random.nextBytes(rnbytes);

		byte randomBytes[] = new byte[len];
		System.arraycopy(rnbytes, 0, randomBytes, 0, len);

		return randomBytes;
	}

	/**
	 * Get the cipher.
	 * 
	 * @param forEncrypt
	 *            true means encrypt.
	 * @return an initialized cipher ready to work.
	 * @throws IllegalArgumentException
	 * @throws HashException
	 * @throws IllegalStateException
	 */
	public I_BlockCipherStream getCipher(boolean forEncrypt) throws IllegalArgumentException, IllegalStateException {
		// Initialize the cipher
		if (forEncrypt && properties.getProperty(EncryptionPreferences.PREF_IV) == null) {
			// New IV
			byte iv[] = getRandomBytes(cipher.getBlockSize());
			properties.setProperty(EncryptionPreferences.PREF_IV, new String(Base64.encode(iv)));
		}
		AlgorithmParameters params = new AlgorithmParameters();
		params.setParameter(master_key);
		params.setParameter(new IVAlgorithmParameter(
				Base64.decode(properties.getProperty(EncryptionPreferences.PREF_IV).getBytes())));
		cipher.init(forEncrypt, params);
		return cipher;
	}

	/**
	 * Return the cipher initialized with an iv.
	 * 
	 * @param forEncrypt
	 *            true means encrypt.
	 * @param iv
	 *            initialization vector.
	 * @return an initialized cipher ready to work.
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws HashException
	 */
	public I_BlockCipherStream getCipher(byte[] iv, boolean forEncrypt)
			throws IllegalArgumentException, IllegalStateException, DigestException {
		// Initialize the cipher
		AlgorithmParameters params = new AlgorithmParameters();
		params.setParameter(master_key);
		params.setParameter(new IVAlgorithmParameter(iv));
		cipher.init(forEncrypt, params);
		return cipher;
	}

	/**
	 * Generates a key form this generator provided the full initialization was
	 * completed.
	 * 
	 * @param seed
	 *            the random source.
	 * @param keyLen
	 *            the key expected length.
	 * @return a new secret key.
	 * @throws IllegalStateException
	 *             when the generator internal status is in error.
	 * @throws InvalidKeyException
	 *             when the initialization step fails because of wrong
	 *             parameters.
	 */
	public byte[] generateKey(byte[] seed, int keyLen) throws InvalidKeyException, IllegalStateException {
		return keyDerivator.generateKey(master_key.getKey(), seed, keyLen).getKey();
	}

	/**
	 * Get the hash function.
	 * 
	 * @return the hash function.
	 */
	public MessageDigest getMessageDigest() {
		return securityProvider.createMessageDigest(properties.getProperty(EncryptionPreferences.PREF_HASH));
	}

	/**
	 * Get the HMAC function.
	 * 
	 * @return the HMAC function.
	 */
	public I_Mac getHMAC() {
		return securityProvider.createMac(properties.getProperty(EncryptionPreferences.PREF_HMAC));
	}

	/**
	 * Reset the security parameters.
	 */
	public void reset() {
		cipher.reset();
		keyDerivator = null;
		master_key = null;
		random = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString();
	}

}
