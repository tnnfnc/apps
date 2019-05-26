/*
 * Developed by www.bouncycastle.org
 * Adapted by: Franco Toninato
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
 *
 */
package it.tnnfnc.security.crypto;

import javax.crypto.IllegalBlockSizeException;

import it.tnnfnc.security.algorithm.AlgorithmParameters;

/**
 * Block cipher engines are expected to implements this interface.
 */
public interface I_BlockCipher {
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
	public void init(boolean forEncryption, AlgorithmParameters params)
			throws IllegalArgumentException;

	/**
	 * Return the name of the algorithm the cipher implements.
	 * 
	 * @return the name of the algorithm the cipher implements.
	 */
	public String getAlgorithmName();

	/**
	 * Return the block size for this cipher (in bytes).
	 * 
	 * @return the block size for this cipher in bytes.
	 */
	public int getBlockSize();

	/**
	 * Return the allowed key size (in bytes).
	 * 
	 * @return the allowed key size (in bytes).
	 */
	public String[] getKeySize();

	/**
	 * Processes a block-length of input from the array in and writes it to the
	 * out array.
	 * 
	 * @param in
	 *            the array containing the input data.
	 * @param inOff
	 *            offset into the in array the data starts at.
	 * @param out
	 *            the array the output data will be copied into.
	 * @param outOff
	 *            the offset into the out array the output will start at.
	 * @return the number of bytes processed and produced.
	 * @exception IllegalArgumentException
	 *                if there isn't enough data in in, or space in out.
	 * @exception IllegalStateException
	 *                if the cipher isn't initialised.
	 * @throws javax.crypto.IllegalBlockSizeException
	 */
	public int processBlock(byte[] in, int inOff, byte[] out, int outOff)
			throws IllegalStateException, IllegalBlockSizeException;

	/**
	 * Reset the cipher. After resetting the cipher is in the same state as it
	 * was after the last init (if there was one).
	 */
	public void reset();

}
