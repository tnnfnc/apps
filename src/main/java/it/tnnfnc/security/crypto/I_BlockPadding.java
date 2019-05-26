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

import javax.crypto.BadPaddingException;

/**
 * Provides padding for a block of data.
 * 
 * @author Franco Toninato
 */
public interface I_BlockPadding {

	/**
	 * Pads the input starting from the offset.
	 * 
	 * @param input
	 *            the input array to be padded.
	 * @param offs
	 *            the input end point from which the padding starts.
	 * @return the number of pad bytes appended.
	 */
	public int pad(byte[] input, int offs);

	/**
	 * Returns the number of pad bytes.
	 * 
	 * @param input
	 *            the input data to be unpadded.
	 * @return the number of pad bytes.
	 * @exception BadPaddingException
	 *                if the padding schema is not followed.
	 */
	public int unpad(byte[] input) throws BadPaddingException;

	/**
	 * Returns the name of the padding schema.
	 * 
	 * @return the name of the padding schema.
	 */
	public String getPaddingName();

}