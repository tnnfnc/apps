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
package it.tnnfnc.security.crypto; //Package

import java.security.InvalidKeyException;

import it.tnnfnc.security.algorithm.AlgorithmParameters;
import it.tnnfnc.security.algorithm.KeyAlgorithmParameter;


/**
 * Password Base Encryption interface for generating a secret key. The input
 * parameters for PBE are: a user password, a salt and a number of iteration and
 * a pseudo-random function (RSA-Laboratories' PKCS#5 2.1 draft material). The
 * generator can be initialized only one time.
 * 
 * @author Franco Toninato
 */
public interface I_PasswordBasedKeyDerivator {

	/**
	 * Initialize the generator with all algorithm parameters.
	 * 
	 * @param algoParams
	 *            the parameters for generating a key.
	 * @throws InvalidKeyException
	 *             when the initialization step fails because of wrong
	 *             parameters.
	 */
	void init(AlgorithmParameters algoParams) throws InvalidKeyException;

	/**
	 * Generates a key form this generator provided the full initialization was
	 * made.
	 * 
	 * @return a new secret key.
	 * @exception IllegalStateException
	 *                when the generator internal status is in error.
	 */
	KeyAlgorithmParameter generateKey() throws IllegalStateException;

	/**
	 * Generates a key form this generator provided the full initialization was
	 * completed.
	 * 
	 * @param algoParams
	 * @return a new secret key.
	 * @throws IllegalStateException
	 *             when the generator internal status is in error.
	 * @throws InvalidKeyException
	 *             when the initialization step fails because of wrong
	 *             parameters.
	 */
	KeyAlgorithmParameter generateKey(AlgorithmParameters algoParams)
			throws IllegalStateException, InvalidKeyException;

	/**
	 * Generates a key form this generator provided the full initialization was
	 * completed.
	 * 
	 * @param password
	 *            the user password.
	 * @return a new secret key.
	 * @throws IllegalStateException
	 *             when the generator internal status is in error.
	 * @throws InvalidKeyException
	 *             when the initialization step fails because of wrong
	 *             parameters.
	 */
	KeyAlgorithmParameter generateKey(byte[] password)
			throws IllegalStateException, InvalidKeyException;

	/**
	 * Generates a key form this generator provided the full initialization was
	 * completed.
	 * 
	 * @param password
	 *            the user password.
	 * @param salt
	 *            the random source.
	 * @return a new secret key.
	 * @throws IllegalStateException
	 *             when the generator internal status is in error.
	 * @throws InvalidKeyException
	 *             when the initialization step fails because of wrong
	 *             parameters.
	 */
	KeyAlgorithmParameter generateKey(byte[] password, byte[] salt)
			throws IllegalStateException, InvalidKeyException;

	/**
	 * Generates a key form this generator provided the full initialization was
	 * completed.
	 * 
	 * @param password
	 *            the user password.
	 * @param salt
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
	KeyAlgorithmParameter generateKey(byte[] password, byte[] salt, int keyLen)
			throws IllegalStateException, InvalidKeyException;

	/**
	 * Returns the algorithm name.
	 * 
	 * @return the algorithm name.
	 */
	String getAlgorithmName();
}