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
package it.tnnfnc.security.crypto;

import java.security.DigestException;
import java.security.InvalidKeyException;

import it.tnnfnc.security.algorithm.AlgorithmParameters;

public interface I_Mac {

	/**
	 * Returns the algorithm name.
	 * 
	 * @return algorithm name.
	 */
	public abstract String getAlgorithmName();

	/**
	 * Returns the length of the processed bytes.
	 * 
	 * @return the MAC length in bytes.
	 */
	public abstract int getOutputLength();

	/**
	 * Initializes this MAC object with the given key.
	 * 
	 * @param key
	 *            the key for authentication.
	 * @exception InvalidKeyException
	 *                when the key is null.
	 * @exception HashException
	 *                when the HMAC internal status is in error.
	 */
	public abstract void init(byte[] key) throws InvalidKeyException,
	DigestException;

	/**
	 * Initializes this MAC object with the given key and a digest algorithm.
	 * 
	 * @param hash
	 *            the digest algorithm.
	 * 
	 * @exception InvalidKeyException
	 *                when the key is null.
	 * @exception HashException
	 *                when the HMAC internal status is in error.
	 */
	public abstract void init(AlgorithmParameters p)
			throws InvalidKeyException, DigestException;

	/**
	 * Completes the processing and performs padding. A call to this method
	 * resets the internal state.
	 * 
	 * @return the array of bytes returned by the MAC.
	 * 
	 * @throws DigestException
	 */
	public abstract byte[] doFinal() throws DigestException;

	/**
	 * Performs the update from input and completes the processing performing
	 * padding. A call to this method resets the internal state.
	 * 
	 * @param input
	 *            the input bytes for the final update of the MAC.
	 * @return the MAC result as an array of bytes.
	 * 
	 * 
	 * @throws DigestException
	 *             when the random generator fails processing bytes.
	 */
	public abstract byte[] doFinal(byte[] input) throws DigestException;

	/**
	 * Performs the update from input and completes the processing performing
	 * padding. A call to this method resets the internal state.
	 * 
	 * @param outBuffer
	 *            the output buffer for the computed MAC.
	 * @param offset
	 *            buffer offset from witch the MAC is stored.
	 * @param len
	 *            length of the output MAC written on the buffer.
	 * @throws HashException
	 *             when the random generator fails processing bytes.
	 */
	public abstract int doFinal(byte[] outBuffer, int offset, int len)
			throws DigestException;

	/**
	 * Resets to the initial state. A call to this method resets this Mac object
	 * to the state it was in when previously initialized via a call to
	 * init(KeyboardKey) or init(KeyboardKey, AlgorithmParameterSpec). That is,
	 * the object is reset and available to generate another MAC from the same
	 * key, if desired, via new calls to update and doFinal. (In order to reuse
	 * this Mac object with a different key, it must be reinitialized via a call
	 * to init(KeyboardKey) or init(KeyboardKey, AlgorithmParameterSpec).
	 **/
	public abstract void reset();

	/**
	 * Updates the MAC from the specified byte.
	 * 
	 * @param input
	 *            the byte.
	 * @exception IllegalStateException
	 *                when the internal status is in error.
	 */
	public abstract void update(byte input) throws IllegalStateException;

	/**
	 * Updates the MAC from the specified array of bytes.
	 * 
	 * @param input
	 *            the array of bytes.
	 * @exception IllegalStateException
	 *                when the internal status is in error.
	 */
	public abstract void update(byte[] input) throws IllegalStateException;

	/**
	 * Updates the MAC from the the specified array of bytes starting at the
	 * offset inclusive up to a length.
	 * 
	 * @param input
	 *            the input array of bytes.
	 * @param offset
	 *            the offset in input where the input starts.
	 * @param len
	 *            the number of bytes to process starting from offset.
	 * @exception IllegalStateException
	 *                when the internal status is in error.
	 */
	public abstract void update(byte[] input, int offset, int len)
			throws IllegalStateException;

}