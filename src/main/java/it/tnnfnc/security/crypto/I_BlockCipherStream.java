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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import it.tnnfnc.security.algorithm.AlgorithmParameters;


/**
 * Any block cipher with an internal buffer for processing a stream of bytes in
 * a many steps processing task is expected to implement this interface. New
 * input bytes can be processed or buffered for further processing within a many
 * steps processing task.
 */
public interface I_BlockCipherStream {
	/**
	 * Initialize the cipher. This a pass-through method to the underlying
	 * <code>BlockCipherMode</code> cipher.
	 * 
	 * @param forEncryption
	 *            true if the cipher is initialized for encryption, false for
	 *            decryption.
	 * @param params
	 *            the key and other data required by the cipher.
	 * 
	 * @exception IllegalArgumentException
	 *                if the parameters argument is inappropriate.
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
	 * Returns the parameters of the cipher.
	 * 
	 * @return the cipher parameters.
	 */
	public AlgorithmParameters getParameters();

	/**
	 * Returns the block size for this cipher (in bytes).
	 * 
	 * @return the block size for this cipher in bytes.
	 */
	public int getBlockSize();

	/**
	 * Returns the processed output size including the bytes currently in the
	 * buffer.
	 * 
	 * @param inLen
	 *            the input length in bytes.
	 * @return the output length.
	 */
	public int getOutputSize(int inLen);

	/**
	 * Returns the total output size for a input length, including padding. In
	 * decrypting mode the returned length can be greater than the actual size
	 * because of padding.
	 * 
	 * @param inLen
	 *            the input length in bytes.
	 * @return the total output length.
	 */
	public int getFinalOutputSize(int inLen);

	/**
	 * Reset the cipher. After resetting the cipher is in the same state as it
	 * was after the last initialization (if there was one).
	 */
	public void reset();

	/**
	 * Processes a single byte, producing an output block when the buffered
	 * input has the required length. If the input does not match the cipher
	 * block length the extra data are buffered for the next process step.
	 * 
	 * @param input
	 *            the input byte.
	 * @param output
	 *            the space for any output that might be produced.
	 * @param outOffs
	 *            the offset from which the output will be copied.
	 * @return the number of output bytes copied to out.
	 * @exception IllegalArgumentException
	 *                if there isn't enough space in out.
	 * @exception IllegalStateException
	 *                if the cipher isn't initialized.
	 * @exception IllegalBlockSizeException
	 *                if an anomalous block of data is passed to the cipher.
	 * @throws  
	 */
	public int update(byte input, byte[] output, int outOffs)
			throws ShortBufferException, IllegalStateException,
			IllegalBlockSizeException;

	/**
	 * Processes an input block of data and copies the result into the output
	 * buffer. If the input does not match a multiple of the cipher block length
	 * the extra data are buffered for the next call or the
	 * <code>doFinal()</code> method.
	 * 
	 * @param input
	 *            the array containing the input data.
	 * @param inOffs
	 *            the input starting point.
	 * @param inLen
	 *            the input length to be processed.
	 * @param output
	 *            the bytes array containing the result.
	 * @param outOffs
	 *            the output offset from which processed bytes are copied.
	 * @return the returned processed bytes.
	 * @exception ShortBufferException
	 *                if the output buffer is smaller than result.
	 * @exception IllegalStateException
	 *                if the cipher isn't initialised.
	 * @exception IllegalBlockSizeException
	 *                if an anomalous block of data is passed to the cipher.
	 */
	public int update(byte[] input, int inOffs, int inLen, byte[] output,
			int outOffs) throws ShortBufferException, IllegalStateException,
			IllegalBlockSizeException;

	/**
	 * Processes data currently buffered providing padding and finishes a data
	 * processing. The padding follows the RFC-1423 properly extended.
	 * 
	 * @param output
	 *            the bytes array containing the result.
	 * @param outOffs
	 *            the output offset from which processed bytes are copied.
	 * @return the number of processed bytes.
	 * @exception ShortBufferException
	 *                if the output buffer is smaller than result.
	 * @exception IllegalStateException
	 *                if the cipher isn't initialized.
	 * @exception BadPaddingException
	 *                if the padding does not compliant to the RFC-1423.
	 * @exception IllegalBlockSizeException
	 *                if an anomalous block of data is passed to the cipher.
	 */
	public int doFinal(byte[] output, int outOffs) throws ShortBufferException,
			IllegalStateException, BadPaddingException,
			IllegalBlockSizeException;

	/**
	 * Processes the input and data currently buffered providing padding and
	 * finishes a data processing. The padding follows the RFC-1423 properly
	 * extended.
	 * 
	 * @param input
	 *            the array containing the input data.
	 * @param inOffs
	 *            the input starting point.
	 * @param inLen
	 *            the input length to be processed.
	 * 
	 * @return the returned processed bytes. Proper padding is added if needed.
	 * 
	 * @exception IllegalStateException
	 *                if the cipher isn't initialized.
	 * @exception BadPaddingException
	 *                if the padding does not compliant to the RFC-1423.
	 * @exception IllegalBlockSizeException
	 *                if an anomalous block of data is passed to the cipher.
	 */
	public byte[] doFinal(byte[] input, int inOffs, int inLen)
			throws IllegalStateException, BadPaddingException,
			IllegalBlockSizeException;
}
