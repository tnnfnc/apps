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

import javax.crypto.IllegalBlockSizeException;

import it.tnnfnc.security.algorithm.AlgorithmParameters;

/**
 * Implements the Electronic Code-book mode for a block cipher algorithm. See
 * NIST Special Publication 800-38A 2001 Edition for details on CBC block cipher
 * mode of operation: <H3>Algorithm specification:</H3> <H4>EC Encryption:</H4>
 * C(j) = CIPH-k(P(j)) <H4>EC Decryption:</H4> P(j) = CIPH-1-k(C(j))
 * 
 * @author Franco Toninato
 */
public final class ECBlockCipher implements I_BlockCipher {
	/* The cipher */
	private I_BlockCipher cipher;
	/* The last ciphered block in the chain */
	private byte[] buffer;
	/* The block size */
	private int blockSize;

	/**
	 * Creates a <code>ECBlockCipher</code> with a cipher.
	 * 
	 * @param cipher
	 *            the block cipher.
	 */
	public ECBlockCipher(I_BlockCipher cipher) {
		this.cipher = cipher;
		this.blockSize = cipher.getBlockSize();
		this.buffer = new byte[blockSize];
	}

	/**
	 * Initializes the cipher.
	 * 
	 *@param forEncryption
	 *            if true the cipher is initialized for encryption, if false for
	 *            decryption.
	 *@param params
	 *            the key and other data required by the cipher.
	 * 
	 * @exception IllegalArgumentException
	 *                if the params argument is inappropriate.
	 */
	@Override
	public void init(boolean forEncryption, AlgorithmParameters params)
			throws IllegalArgumentException {
		// Init the underlying block cipher with a KeyParameter
		cipher.init(forEncryption, params);
	}

	/**
	 * Returns the name of the algorithm the cipher implements.
	 * 
	 * @return the name of the algorithm the cipher implements.
	 */
	@Override
	public String getAlgorithmName() {
		return cipher.getAlgorithmName() + "/" + "ECB";
	}

	/**
	 * Returns the block size for this cipher (in bytes).
	 * 
	 * @return the block size for this cipher in bytes.
	 */
	@Override
	public int getBlockSize() {
		return cipher.getBlockSize();
	}

	@Override
	public String[] getKeySize() {
		return cipher.getKeySize();
	}

	/**
	 * Processes one block of input from the input array and write it to the out
	 * array. The bytes further than the block length are discarded.
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
		if (blockSize + inOff > in.length)
			throw new IllegalBlockSizeException("input buffer too short");

		// Write the processed bytes into the output starting from the offs
		System.arraycopy(in, inOff, buffer, 0, blockSize);
		int processedBytes = cipher.processBlock(buffer, 0, out, outOff);

		return processedBytes;
	}

	/**
	 * Resets the cipher. After resetting the cipher is in the same state as it
	 * was after the last init (if there was one).
	 */
	@Override
	public void reset() {
		buffer = new byte[getBlockSize()];
		// Reset the cipher
		cipher.reset();
	}

}
