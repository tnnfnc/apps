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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import it.tnnfnc.security.crypto.I_BlockCipher;
import it.tnnfnc.security.crypto.I_BlockCipherStream;
import it.tnnfnc.security.crypto.I_BlockPadding;

/**
 * Implements a block cipher algorithm in a sequence of multiple blocks
 * processing with a padding of the last block of bytes. A
 * <code>PaddedBlockCipherStream</code> is responsible for encrypting and
 * decrypting data, it is suitable for symmetric key block cipher algorithms
 * implementing the <code>BlockCipherMode</code> interface. <br />
 * Performs the padding in encrypting or decrypting mode according to RFC-1423
 * (default) properly extended to allow block of more than 8 octets, but any
 * padding schema can be used. The padding needs to buffer a block before return
 * tho output.
 * 
 * @author Franco Toninato
 * @version %I%, %G%
 * @since 1.5
 */
public class PaddedBlockCipherStream implements I_BlockCipherStream {
	/* The cipher in a ciphering mode */
	private I_BlockCipher blockcipher;
	/* The parameters in a ciphering mode */
	private AlgorithmParameters params;
	/* The internal input buffer */
	private byte[] buffer; // The buffer is at most as long as the cipher block
	/* The buffer offset accumulated bytes to be processed */
	private int offs;
	/* The padding object */
	private I_BlockPadding padding;
	/* The block size */
	private int blockSize;
	/* The processing mode */
	private boolean forEncryption;
	/* The processing state */
	private int internalState;
	private static int NEW = 0;
	private static int READY = 1;
	private static int RUNNING = 2;
	private static int IDLE = -1;

	/**
	 * Creates a <code>PaddedBlockCipherStream</code> with a cipher, the default
	 * padding schema RFC-1423.
	 * 
	 * @param cipher
	 *            the block cipher in a multiple block processing mode.
	 */
	public PaddedBlockCipherStream(I_BlockCipher cipher) {
		this(cipher, new PKCS5Padding());
	}

	/**
	 * Creates a <code>PaddedBlockCipherStream</code> with a cipher.
	 * 
	 * @param cipher
	 *            the block cipher in a block cipher processing mode.
	 * @param padding
	 *            the padding schema.
	 */
	public PaddedBlockCipherStream(I_BlockCipher cipher,
			I_BlockPadding padding) {
		this.blockcipher = cipher;
		this.padding = padding;
		blockSize = blockcipher.getBlockSize();
		buffer = new byte[blockSize];
		internalState = NEW;
	}

	@Override
	public void init(boolean forEncryption, AlgorithmParameters params)
			throws IllegalArgumentException {
		this.forEncryption = forEncryption;
		this.params = params;
		reset();
		blockcipher.init(forEncryption, params);
		internalState = READY;
		// IV control
	}

	@Override
	public String getAlgorithmName() {
		return this.blockcipher.getAlgorithmName() + "/"
				+ padding.getPaddingName();
	}

	@Override
	public AlgorithmParameters getParameters() {
		return this.params;
	}

	@Override
	public int getBlockSize() {
		return blockSize;
	}

	@Override
	public int getOutputSize(int inLen) {
		int total = inLen + offs; // Total to be processed
		int leftOver = total % blockSize; // Leftover
		leftOver = leftOver == 0 ? blockSize : leftOver; // Buffer a block
		if (inLen == 0) {
			return 0;
		}
		return total - leftOver;
	}

	@Override
	public int getFinalOutputSize(int inLen) {
		int leftOver = inLen % blockSize;
		// Buffer a block
		leftOver = leftOver == 0 ? blockSize : (blockSize - leftOver);
		return inLen + leftOver;
	}

	/*
	 * Resets the cipher for a new processing task.
	 */
	@Override
	public void reset() {
		if (internalState == RUNNING || internalState == IDLE) {
			blockcipher.reset();
			offs = 0;
			for (int i = 0; i < buffer.length; i++) {
				buffer[i] = 0;
			}
			buffer = new byte[blockSize];
		}
		internalState = READY;
	}

	@Override
	public int update(byte input, byte[] output, int outOffs)
			throws ShortBufferException, IllegalStateException,
			IllegalBlockSizeException {
		if (internalState == IDLE) {
			throw new IllegalStateException(
					"Internal state not ready for input");
		}

		int expectedBytes = 0;
		// Return an output only when the buffer if full
		if (offs == buffer.length) {
			expectedBytes = blockcipher
					.processBlock(buffer, 0, output, outOffs);
			offs = 0;
		}
		// Buffer the input
		buffer[offs++] = input;
		internalState = RUNNING;
		return expectedBytes;
	}

	@Override
	public int update(byte[] input, int inOffs, int inLen, byte[] output,
			int outOffs) throws ShortBufferException, IllegalStateException,
			IllegalBlockSizeException {
		if (internalState == READY || internalState == RUNNING) {
			/*
			 * Process a segment from the input starting from inOffs into an
			 * output starting from outOffs
			 */
			if (inLen == 0)
				return 0;
			// 1 - Check the segment is a subarray of the input.
			if (input.length < inOffs || output.length < outOffs || inOffs < 0
					|| outOffs < 0 || inLen < 0
					|| inLen > input.length - inOffs) {
				throw new IllegalBlockSizeException("Input buffer length is "
						+ inLen);
			}
			/*
			 * 2 - Check the output lenght can accomodate for the processed
			 * input (ShortBufferException).
			 */
			if (getOutputSize(inLen) > (output.length - outOffs)) {
				throw new ShortBufferException(" Output buffer too short.");
			}
			/*
			 * 3 - Cicle over the to be processed bytes taken as multiple of the
			 * block length.
			 */
			int leftover = buffer.length - offs;
			int processedBytes = 0;
			if (inLen > leftover) // Buffer available shorter than input buffer
			{
				System.arraycopy(input, inOffs, buffer, offs, leftover);
				processedBytes += blockcipher.processBlock(buffer, 0, output,
						outOffs);
				inLen -= leftover;
				inOffs += leftover;
				offs = 0;

				while (inLen > buffer.length) {
					processedBytes += blockcipher.processBlock(input, inOffs,
							output, outOffs + processedBytes);
					inLen -= blockSize;
					inOffs += blockSize;
				}
			}
			/* 4 - Buffer the left over bytes if any. */
			System.arraycopy(input, inOffs, buffer, offs, inLen);
			offs += inLen;
			internalState = RUNNING;
			return processedBytes;
		} else {
			throw new IllegalStateException(
					"Internal state not ready for input");
		}
	}

	@Override
	public int doFinal(byte[] output, int outOffs) throws ShortBufferException,
			IllegalStateException, BadPaddingException,
			IllegalBlockSizeException {
		if (internalState == RUNNING) {
			int processedBytes = 0;
			// The last buffer is full or partially filled
			if (forEncryption) {
				int outSize = getOutputSize(blockSize) == 0 ? 2 * blockSize
						: blockSize;
				if (outSize + outOffs > output.length) {
					throw new IllegalBlockSizeException(
							" Output buffer too short.");
				}
				if (offs == blockSize) { // Full: add a block of padding
					processedBytes = blockcipher.processBlock(buffer, 0,
							output, outOffs);
					padding.pad(buffer, 0);
					processedBytes += blockcipher.processBlock(buffer, 0,
							output, outOffs + processedBytes);
				} else if (offs < blockSize) { // Partially filled: pad the
					// leftover
					padding.pad(buffer, offs);
					processedBytes += blockcipher.processBlock(buffer, 0,
							output, outOffs);
				} else {
					throw new IllegalBlockSizeException(
							" Block size too short.");
				}
				// Decipher ciphered text
			} else if (!forEncryption) {
				if (offs == blockSize) {
					processedBytes = blockcipher.processBlock(buffer, 0,
							buffer, 0);
					// Remove padding from the output
					processedBytes -= padding.unpad(buffer);
					if (processedBytes + outOffs > output.length) {
						throw new IllegalBlockSizeException(
								" Output buffer too short");
					}
					System.arraycopy(buffer, 0, output, outOffs, processedBytes);
				} else {
					throw new IllegalBlockSizeException(
							" Last block size incomplete: expected ="
									+ blockSize + " actual=" + offs);
				}
			}
			internalState = IDLE;
			reset();
			return processedBytes;
		} else if (internalState == READY) {
			throw new IllegalStateException("No input bytes to process");
		} else {
			throw new IllegalStateException(
					"Internal state not ready for input");
		}
	}

	@Override
	public byte[] doFinal(byte[] input, int inOffs, int inLen)
			throws IllegalStateException, BadPaddingException,
			IllegalBlockSizeException {
		if (internalState == RUNNING || internalState == READY) {

			byte[] output = new byte[getFinalOutputSize(inLen)];
			try {
				int n = this.update(input, inOffs, inLen, output, 0);
				n = n + this.doFinal(output, n);
				byte[] result = new byte[n];
				System.arraycopy(output, 0, result, 0, n);
				internalState = IDLE;
				reset();
				return result;
			} catch (ShortBufferException e) {
				throw new IllegalBlockSizeException(e.getMessage());
			}

		} else {
			throw new IllegalStateException(
					"Internal state not ready for input");
		}
	}
}
