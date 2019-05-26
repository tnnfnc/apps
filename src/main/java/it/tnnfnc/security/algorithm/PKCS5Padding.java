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

import it.tnnfnc.security.crypto.I_BlockPadding;

//import net.catode.crypto.*;

/**
 * A CBC padding consisting in multiple of octets. A variable number of octets
 * are appended up to the block length. See RFC 1423 for details.
 * 
 * @author Franco Toninato
 * @version %I%, %G%
 * @since 1.5
 */
public class PKCS5Padding implements I_BlockPadding {

	/**
	 * Pads the input starting from the offset.
	 * 
	 * @param input
	 *            the input array to be padded.
	 * @param offs
	 *            the input end point from which the padding starts.
	 * @return the number of pad bytes appended.
	 */
	@Override
	public int pad(byte[] input, int offs) {
		byte padByte = (byte) (input.length - offs);
		for (int i = offs; i < input.length; i++) {
			input[i] = padByte;
		}
		return padByte;
	}

	/**
	 * Returns the number of pad bytes.
	 * 
	 * @param input
	 *            the input data to be unpadded.
	 * @return the number of pad bytes.
	 * @exception BadPaddingException
	 *                if the padding schema is not PKCS5 complaint.
	 */
	@Override
	public int unpad(byte[] input) throws BadPaddingException {
		int padByte = input[input.length - 1] & 0xff;
		if (padByte > input.length)
			throw new BadPaddingException("Invalid pad schema!");
		// throw new BadPaddingException(
		// " Not a valid pad schema - pad byte (" + padByte
		// + ") greater than block length (" + input.length
		// + ")");
		// check the pad schema
		// for (int i = input.length-1; i > padByte; i--)
		for (int i = 1; i <= padByte; i++) {
			if (input[input.length - i] != padByte)
				throw new BadPaddingException("Invalid pad schema!");
		}
		return padByte;
	}

	/**
	 * Returns the name of the padding schema.
	 * 
	 * @return the name of the padding schema.
	 */
	@Override
	public String getPaddingName() {
		// return "PAD8";
		return "PKCS5Padding";
	}

}