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
package it.tnnfnc.security.algorithm; //Package

import java.security.DigestException;
import java.security.InvalidKeyException;

import it.tnnfnc.security.crypto.I_Mac;
import it.tnnfnc.security.crypto.I_PasswordBasedKeyDerivator;

/**
 * Implements the password-based encryption scheme PBKDF2 secret key generator
 * specification based on PKCS #5 v2.1: Password-Based Cryptography Standard.
 * 
 * @author Franco Toninato
 * @since 1.2
 */
public class PBES2KeyAlgorithm implements I_PasswordBasedKeyDerivator {

	private I_Mac prf; // pseudo-random function
	private int iterationCount; // iteration count
	private int dkLen; // derived key length in octets
	private int hLen; // pseudo-random function output length in octets
	private byte[] salt; // salt
	private byte[] password; // salt

	/**
	 * Class constructor.
	 */
	public PBES2KeyAlgorithm() {
	}

	@Override
	public void init(AlgorithmParameters algoParams) throws InvalidKeyException {
		PBKDAlgorithmParameter pbe_params = ((PBKDAlgorithmParameter) algoParams
				.get(PBKDAlgorithmParameter.class));
		this.salt = pbe_params.getSalt();
		this.iterationCount = pbe_params.getIterationCount();
		this.password = pbe_params.getPassword();
		this.dkLen = pbe_params.getKeyLen();
		this.prf = pbe_params.getPseudoRandomFunction();
		this.hLen = prf.getOutputLength();
		keyCheck();
	}

	@Override
	public KeyAlgorithmParameter generateKey() throws IllegalStateException {
		return generateKey(password, salt, iterationCount, dkLen);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.crypto.pbe.PBEKeyGenerator#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return "PBKDF2/" + prf.getAlgorithmName();
	}

	@Override
	public KeyAlgorithmParameter generateKey(AlgorithmParameters algoParams)
			throws IllegalStateException, InvalidKeyException {
		init(algoParams);

		return generateKey();
	}

	@Override
	public KeyAlgorithmParameter generateKey(byte[] password)
			throws IllegalStateException, InvalidKeyException {
		return generateKey(password, salt, iterationCount, dkLen);
	}

	@Override
	public KeyAlgorithmParameter generateKey(byte[] password, byte[] salt)
			throws IllegalStateException, InvalidKeyException {

		return generateKey(password, salt, this.iterationCount, this.dkLen);
	}

	@Override
	public KeyAlgorithmParameter generateKey(byte[] password, byte[] salt,
			int keyLen) throws IllegalStateException, InvalidKeyException {

		return generateKey(password, salt, this.iterationCount, keyLen);
		// return generateKey();
	}

	/**
	 * Generate a secret key from the password.
	 * 
	 * @param password
	 *            the password.
	 * @param salt
	 *            the salt.
	 * @param c
	 *            the iteration count.
	 * @param keyLen
	 *            the derived key length (bytes).
	 * @return the derived key.
	 * @exception DigestException
	 *                when a pseudo-random function generic error occurs.
	 * @exception IllegalStateException
	 *                when the pseudo-random function internal status is in
	 *                error.
	 */
	private KeyAlgorithmParameter generateKey(byte[] password, byte[] salt,
			int iterationCount, int keyLen) throws IllegalStateException {
		this.salt = salt;
		this.iterationCount = iterationCount;
		this.password = password;
		this.dkLen = keyLen;
		try {
			keyCheck();
			this.prf.init(password);
		} catch (InvalidKeyException e) {
			IllegalStateException ex = new IllegalStateException(e.getMessage());
			ex.initCause(e.getCause());
			throw ex;
		} catch (DigestException e) {
			IllegalStateException ex = new IllegalStateException(e.getMessage());
			ex.initCause(e.getCause());
			throw ex;
		}
		// Compute the number of blocks rounded up
		// System.out.println(this.getClass().getName() + " hlen= " + hLen);
		int blocks = dkLen / hLen + 1;
		final byte[] key = new byte[keyLen];
		// if (DEBUG) System.out.println("Blocks = "+blocks );
		// ... for each block do ...
		try {
			for (int i = 0; i < blocks - 1; i++) {
				// if (DEBUG) System.out.println("Block computed = "+i );
				System.arraycopy(_F(i), 0, key, i * hLen, hLen);
			}
			// The last
			// if (DEBUG) System.out.println("Block computed = "+(blocks - 1));
			// if (DEBUG) System.out.println("Left over = "+dkLen%hLen);
			System.arraycopy(_F(blocks - 1), 0, key, (blocks - 1) * hLen, dkLen
					% hLen);
		} catch (DigestException e) {
			IllegalStateException ex = new IllegalStateException(e.getMessage());
			ex.initCause(e.getCause());
			throw ex;
		}

		return new KeyAlgorithmParameter(key);
	}

	/*
	 * return a 4-octets representation of the integer i with the most
	 * significant octet first
	 */
	private byte[] _INT(int i) {
		byte[] index = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

		byte a = (byte) ((i >>> 24) & 0xff);
		byte b = (byte) ((i >>> 16) & 0xff);
		byte c = (byte) ((i >>> 8) & 0xff);
		byte d = (byte) (i & 0xff);
		// First block
		if (a != 0x00) {
			index[0] = a;
			index[1] = b;
			index[2] = c;
			index[3] = d;
			// if (DEBUG) System.out.println("INT("+ i +") = [" + index[0]
			// +" "+index[1] +" "+index[2] +" "+index[3] +"]");
			return index;
		}
		// Second block
		if (b != 0x00) {
			index[0] = b;
			index[1] = c;
			index[2] = d;
			// if (DEBUG) System.out.println("INT("+ i +") = [" + index[0]
			// +" "+index[1] +" "+index[2] +" "+index[3] +"]");
			return index;
		}// Third block
		if (c != 0x00) {
			index[0] = c;
			index[1] = d;
			// if (DEBUG) System.out.println("INT("+ i +") = [" + index[0]
			// +" "+index[1] +" "+index[2] +" "+index[3] +"]");
			return index;
		}// Fourth block
		if (d != 0x00) {
			index[0] = d;
			// if (DEBUG) System.out.println("INT("+ i +") = [" + index[0]
			// +" "+index[1] +" "+index[2] +" "+index[3] +"]");
			return index;
		}
		// if (DEBUG) System.out.println("INT("+ i +") = [" + index[0]
		// +" "+index[1] +" "+index[2] +" "+index[3] +"]");
		return index;
	}

	/* Return the U(n) = PRF(p, U(n-1)) with U(1) = PRF(p, s||INT(i)) */
	private byte[] _F(int i) throws DigestException {
		// salt||INT(i)
		byte[] u0 = new byte[salt.length + 4];
		System.arraycopy(salt, 0, u0, 0, salt.length);
		System.arraycopy(_INT(i), 0, u0, salt.length, 4);
		// PRF(p, salt||INT(i))
		// byte[] u = prf.process(password, u0);
		byte[] u = prf.doFinal(u0);
		// T(i) = U(0) xor U(1) xor ... U(c-1)
		for (int j = 1; j < iterationCount; j++) {
			// U(n) = PRF(p, U(n-1))
			// byte[] v = prf.process(password, u);
			byte[] v = prf.doFinal(u);
			// U(n) xor U(n-1)
			for (int k = 0; k < hLen; k++)
				v[k] = (byte) (u[k] ^ v[k]);
			u = v;
		}
		return u;
	}

	private void keyCheck() throws InvalidKeyException {
		if (salt == null || salt.length == 0)
			throw new InvalidKeyException("invalid generation salt");
		if (iterationCount <= 0)
			throw new InvalidKeyException("iteration count out of range");
		if (password == null || password.length == 0)
			throw new InvalidKeyException("password unsettled");
		if (dkLen <= 0)
			throw new InvalidKeyException("password length out of range");
	}
}