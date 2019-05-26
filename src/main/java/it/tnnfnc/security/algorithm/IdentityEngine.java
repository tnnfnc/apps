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

import javax.crypto.IllegalBlockSizeException;

import it.tnnfnc.security.crypto.I_BlockCipher;

/**
 * Block cipher identity, the output block is identical to the input block.
 */
public final class IdentityEngine implements I_BlockCipher {

	private static final int BLOCK_SIZE = 16;

	/**
	 * Initialise the cipher.
	 * 
	 * @param forEncryption
	 *            if true the cipher is initialised for encryption, if false for
	 *            decryption.
	 * @param params
	 *            the key and other data required by the cipher.
	 * @exception IllegalArgumentException
	 *                if the params argument is inappropriate.
	 */
	@Override
	public void init(boolean forEncryption, AlgorithmParameters params)
			throws IllegalArgumentException {
	}

	/**
	 * Return the name of the algorithm the cipher implements.
	 * 
	 * @return the name of the algorithm the cipher implements.
	 */
	@Override
	public String getAlgorithmName() {
		return "Identity";
	}

	/**
	 * Return the block size for this cipher (in bytes).
	 * 
	 * @return the block size for this cipher in bytes.
	 */
	@Override
	public int getBlockSize() {
		return BLOCK_SIZE;
	}

	@Override
	public String[] getKeySize() {
		return new String[] { "0" };
	}

	/**
	 * Process one block of input from the array in and write it to the out
	 * array.
	 * 
	 * @param in
	 *            the array containing the input data.
	 * @param inOff
	 *            offset into the in array the data starts at.
	 * @param out
	 *            the array the output data will be copied into.
	 * @param outOff
	 *            the offset into the out array the output will start at.
	 * @exception IllegalArgumentException
	 *                if there isn't enough data in in, or space in out.
	 * @exception IllegalStateException
	 *                if the cipher isn't initialised.
	 * @return the number of bytes processed and produced.
	 */
	@Override
	public final int processBlock(byte[] in, int inOff, byte[] out, int outOff)
			throws IllegalBlockSizeException, IllegalStateException {
		if (in.length - inOff != getBlockSize()) {
			throw new IllegalBlockSizeException(
					" Input buffer not equal to the block length.");
		}
		if (getBlockSize() + outOff > out.length) {
			throw new IllegalBlockSizeException(
					" Cipher output buffer too short.");
		}

		System.arraycopy(in, inOff, out, outOff, getBlockSize());

		return getBlockSize();
	}

	/**
	 * Reset the cipher. After resetting the cipher is in the same state as it
	 * was after the last init (if there was one).
	 */
	@Override
	public void reset() {
	}
}
