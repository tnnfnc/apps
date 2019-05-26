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
import it.tnnfnc.security.algorithm.IVAlgorithmParameter;


/**
 * Implements a Cipher Block Chaining algorithm with a block cipher. A
 * <code>CBCBlockCipher</code> is responsible for encrypting and decrypting
 * data, it is suitable for symmetric key block cipher algorithms. See NIST
 * Special Publication 800-38A 2001 Edition for details on CBC block cipher mode
 * of operation: <H3>Algorithm specification:</H3> <H4>CBC decrypting:</H4> CBC
 * Encryption: C(1) = CIPH-k(P(1) + IV) <BR>
 * C(j) = CIPH-k(P(j) + C(j-1)) <H4>CBC decrypting:</H4> P(1) = CIPH-1-k(C(1)) +
 * IV <BR>
 * P(j) = CIPH-1-k(C(j)) + C(j-1)
 * 
 * @author Franco Toninato
 */
public final class CBCBlockCipher implements I_BlockCipher {
	/* The cipher */
	private I_BlockCipher cipher;
	/* The current cipher mode */
	private I_BlockCipher cipherState;
	/* The initialization vector */
	private byte[] IV;
	/* The last ciphered block in the chain */
	private byte[] buffer;

	/**
	 * Creates a <code>CBCBlockCipher</code> with a cipher.
	 * 
	 * @param cipher
	 *            the block cipher.
	 * 
	 */
	public CBCBlockCipher(I_BlockCipher cipher) {
		this.cipher = cipher;
		this.buffer = new byte[cipher.getBlockSize()];
		this.IV = new byte[cipher.getBlockSize()];
	}

	/**
	 * Initializes the cipher.
	 * 
	 * @param forEncryption
	 *            if true the cipher is initialized for encryption, if false for
	 *            decrypting.
	 * @param params
	 *            the key and other data required by the cipher.
	 * @exception IllegalArgumentException
	 *                if the cipher parameters are inappropriate, or the
	 *                initialization vector less than cipher block length.
	 */
	@Override
	public void init(boolean forEncryption, AlgorithmParameters params)
			throws IllegalArgumentException {
		if (forEncryption) {
			cipherState = new EncryptState();
		} else {
			cipherState = new DecryptState();
		}
		cipher.init(forEncryption, params);
		byte[] iv = ((IVAlgorithmParameter) params
				.get(IVAlgorithmParameter.class)).getIV();

		if (iv.length < cipher.getBlockSize()) {
			throw new IllegalArgumentException(
					"the initialization vector does not match the cipher block lenght");
		}
		// Initialize the buffer for the first step
		System.arraycopy(iv, 0, buffer, 0, getBlockSize());
		System.arraycopy(iv, 0, IV, 0, getBlockSize());
	}

	/**
	 * Returns the name of the algorithm the cipher implements.
	 * 
	 * @return the name of the algorithm the cipher implements.
	 */
	@Override
	public String getAlgorithmName() {
		return cipher.getAlgorithmName() + "/" + "CBC";
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
	 * 
	 * @exception IllegalArgumentException
	 *                if there isn't enough data in in, or space in out.
	 * @exception IllegalStateException
	 *                if the cipher isn't initialized.
	 * @return the number of bytes processed and produced.
	 */
	@Override
	public final int processBlock(byte[] in, int inOff, byte[] out, int outOff)
			throws IllegalBlockSizeException, IllegalStateException {
		if (out.length < outOff + cipher.getBlockSize())
			throw new IllegalBlockSizeException("Output buffer too short.");

		byte[] input = new byte[cipher.getBlockSize()];
		System.arraycopy(in, inOff, input, 0, cipher.getBlockSize());
		return cipherState.processBlock(input, 0, out, outOff);
	}

	/**
	 * Resets the cipher. After resetting the cipher is in the same state as it
	 * was after the last initialization (if there was one).
	 */
	@Override
	public void reset() {
		cipher.reset();
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = 0x00;
		}
		buffer = new byte[cipher.getBlockSize()];
		System.arraycopy(IV, 0, buffer, 0, cipher.getBlockSize());
	}

	private class DecryptState implements I_BlockCipher {

		@Override
		public String getAlgorithmName() {
			return CBCBlockCipher.this.getAlgorithmName();
		}

		@Override
		public int getBlockSize() {
			return CBCBlockCipher.this.getBlockSize();
		}

		@Override
		public String[] getKeySize() {
			return CBCBlockCipher.this.getKeySize();
		}

		@Override
		public void init(boolean forEncryption, AlgorithmParameters params)
				throws IllegalArgumentException {
		}

		@Override
		public final int processBlock(byte[] in, int inOff, byte[] out, int outOff)
				throws IllegalBlockSizeException, IllegalStateException {
			// Step 1 - Decipher the result into a temporary buffer
			int processedBytes = cipher.processBlock(in, 0, out, outOff);
			/*
			 * Step 2 - Perform bytes summation with the previous buffer: P(j) +
			 * C(j-1) Write the processed bytes into the output starting from
			 * the offs
			 */
			for (int i = 0; i < cipher.getBlockSize(); i++) {
				out[outOff + i] = (byte) (out[outOff + i] ^ buffer[i]);
			}
			// Store the result into the buffer for next step
			System.arraycopy(in, 0, buffer, 0, buffer.length);

			return processedBytes;
		}

		@Override
		public void reset() {
			CBCBlockCipher.this.reset();
		}
	}

	private class EncryptState implements I_BlockCipher {

		@Override
		public String getAlgorithmName() {
			return CBCBlockCipher.this.getAlgorithmName();
		}

		@Override
		public int getBlockSize() {
			return CBCBlockCipher.this.getBlockSize();
		}

		@Override
		public String[] getKeySize() {
			return CBCBlockCipher.this.getKeySize();
		}

		@Override
		public void init(boolean forEncryption, AlgorithmParameters params)
				throws IllegalArgumentException {
		}

		/**
		 * Cipher a block. Step 1 - Perform bytes summation: P(j) + C(j-1) Step
		 * 2 - Cipher the result into the buffer for next step
		 */
		@Override
		public final int processBlock(byte[] in, int inOff, byte[] out, int outOff)
				throws IllegalBlockSizeException, IllegalStateException {
			// Perform bytes summation: P(j) + C(j-1)
			for (int i = 0; i < cipher.getBlockSize(); i++) {
				buffer[i] = (byte) (in[i] ^ buffer[i]);
			}
			// Cipher the result into the buffer for next step
			int processedBytes = cipher.processBlock(buffer, 0, out, outOff);
			// Write the processed bytes into the output starting from the offs
			System.arraycopy(out, outOff, buffer, 0, buffer.length);
			return processedBytes;
		}

		@Override
		public void reset() {
			CBCBlockCipher.this.reset();
		}
	}

}
