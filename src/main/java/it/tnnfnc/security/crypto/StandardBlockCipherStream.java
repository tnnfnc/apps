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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import it.tnnfnc.security.algorithm.AlgorithmParameters;

public class StandardBlockCipherStream implements I_BlockCipherStream {
	private Cipher cipher;

	public StandardBlockCipherStream(Cipher c) {
		cipher = c;
	}

	@Override
	public void init(boolean forEncryption, AlgorithmParameters params)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAlgorithmName() {
		return cipher.getAlgorithm();
	}

	@Override
	public AlgorithmParameters getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBlockSize() {
		return cipher.getBlockSize();
	}

	@Override
	public int getOutputSize(int inLen) {
		return cipher.getOutputSize(inLen);
	}

	@Override
	public int getFinalOutputSize(int inLen) {
		// ??
		return cipher.getOutputSize(inLen);
	}

	@Override
	public void reset() {
		// Nothing
	}

	@Override
	public int update(byte input, byte[] output, int outOffs)
			throws ShortBufferException, IllegalStateException,
			IllegalBlockSizeException {
		return cipher.update(new byte[] { input }, 0, 1, output, outOffs);
	}

	@Override
	public int update(byte[] input, int inOffs, int inLen, byte[] output,
			int outOffs) throws ShortBufferException, IllegalStateException,
			IllegalBlockSizeException {
		return cipher.update(input, inOffs, inLen, output, outOffs);
	}

	@Override
	public int doFinal(byte[] output, int outOffs) throws ShortBufferException,
			IllegalStateException, BadPaddingException,
			IllegalBlockSizeException {
		return cipher.doFinal(output, outOffs);
	}

	@Override
	public byte[] doFinal(byte[] input, int inOffs, int inLen)
			throws IllegalStateException, BadPaddingException,
			IllegalBlockSizeException {
		return cipher.doFinal(input, inOffs, inLen);
	}

}
