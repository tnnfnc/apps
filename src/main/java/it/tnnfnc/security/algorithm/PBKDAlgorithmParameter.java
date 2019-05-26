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
package it.tnnfnc.security.algorithm;

import it.tnnfnc.security.crypto.I_Mac;


/**
 * Password based key generation parameters.
 * 
 * @author Franco Toninato
 * 
 */
public class PBKDAlgorithmParameter implements I_AlgorithmParameter {
	// implements I_PBEAlgorithmParameter {

	private I_Mac prf;
	private int iterationCount;
	private byte[] salt;
	private byte[] password;
	private int keyLen;

	/**
	 * Create a new key. An "HMAC" is the pseudo random number generator.
	 * 
	 * @param password
	 *            the user input password.
	 * @param salt
	 *            the random bytes.
	 * @param iterationCount
	 *            the number of times the algorithm is applied to the key
	 *            material.
	 * @param randomAlgorithm
	 *            the pseudo random algorithm.
	 * @param keyLen
	 *            the result key length.
	 */
	public PBKDAlgorithmParameter(byte[] password, byte[] salt,
			int iterationCount, I_Mac randomAlgorithm, int keyLen) {
		this.password = new byte[password.length];
		System.arraycopy(password, 0, this.password, 0, password.length);
		this.salt = new byte[salt.length];
		System.arraycopy(salt, 0, this.salt, 0, salt.length);
		this.iterationCount = iterationCount;
		this.prf = randomAlgorithm;
		this.keyLen = keyLen;
	}

	/**
	 * Returns the algorithm name.
	 * 
	 * @return the algorithm name.
	 */
	public String getAlgorithmName() {
		return prf.getAlgorithmName();
	}

	/**
	 * Returns the iteration count.
	 * 
	 * @return the iteration count.
	 */
	public int getIterationCount() {
		return iterationCount;
	}

	/**
	 * Return the salt.
	 * 
	 * @return the salt.
	 */
	public byte[] getSalt() {
		byte p[] = new byte[salt.length];
		System.arraycopy(salt, 0, p, 0, salt.length);
		return p;
	}

	/**
	 * Return the password.
	 * 
	 * @return the password.
	 */
	public byte[] getPassword() {
		byte p[] = new byte[password.length];
		System.arraycopy(password, 0, p, 0, password.length);
		return p;
	}

	/**
	 * Return the key length.
	 * 
	 * @return the key length.
	 */
	public int getKeyLen() {
		return keyLen;
	}

	/**
	 * Returns the algorithm that generates the key.
	 * 
	 * @return the algorithm that generates the key.
	 */
	public I_Mac getPseudoRandomFunction() {
		return prf;
	}

}
