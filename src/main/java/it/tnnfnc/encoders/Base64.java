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
package it.tnnfnc.encoders;


/**
 * Base64 encoder and decoder class (see RFC2045). Symbols found during the
 * decoding process that are not in the 64-printable characters alphabet are
 * skipped and logged into a string. <br />
 * Errors are available calling the <code>getErrors</code> method.
 * 
 * @author Franco Toninato
 * 
 */
public class Base64 {
	/* Encoding table */
	private static final byte[] codeTable = { (byte) 'A', (byte) 'B',
			(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
			(byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
			(byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q',
			(byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V',
			(byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a',
			(byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
			(byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k',
			(byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p',
			(byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u',
			(byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
			(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
			(byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',
			(byte) '+', (byte) '/' };
	/* Padding character */
	private static final byte pad = (byte) '=';

	/**
	 * 
	 * @param b
	 *            the input.
	 * @return the inverted input.
	 */
	private static byte f_decode(byte b) {
		if (64 < b && b < 91)
			return (byte) (b - 65);

		if (96 < b && b < 123)
			return (byte) (b - 71);

		if (47 < b && b < 58)
			return (byte) (b + 4);

		if (b == 43)
			return (byte) 62;

		if (b == 47)
			return (byte) 63;

		return -1;
	}

	/**
	 * Empty constructor initialising the decoding table.
	 */
	// private static byte[] getDecodeTable() {
	// // Decoding table
	// byte[] decodeTable = new byte[128];
	// for (int i = 0; i < codeTable.length; i++) {
	// decodeTable[codeTable[i]] = (byte) i;
	// // System.out.println("decodeTable[" + codeTable[i] + "]= (byte)" +
	// // i
	// // + ";");
	// // System.out.println("t(" + i + ")= " + (char) codeTable[i] + " "
	// // + codeTable[i]);
	// }
	//
	// return decodeTable;
	// }

	/**
	 * Process input from the array in and write it to the out array.
	 * 
	 * @param in
	 *            the array containing the input data.
	 * @param inOff
	 *            offset into the in array the data starts at.
	 * @param inLen
	 *            the number of input bytes to process.
	 * @param out
	 *            the array the output data will be copied into.
	 * @param outOff
	 *            the offset into the out array the output will start at.
	 * 
	 * @return the number of bytes processed and produced.
	 * @exception IllegalArgumentException
	 *                when the process stops before the end of the array is
	 *                reached, or a wrong characters sequence is found.
	 */
	public static int encode(byte[] in, int inOff, int inLen, byte[] out,
			int outOff) throws IllegalArgumentException {
		// Check the space is enough
		if (in.length - inOff < inLen)
			throw new IllegalArgumentException(" Input buffer too short!");

		int leftOver = inLen % 3;
		inLen -= leftOver;
		int j = 0;

		// Split 24 bits of input into 4 groups of 6 bits of output.
		while (j < inLen) {
			out[outOff++] = codeTable[(in[j] >>> 2) & 0x3f];
			out[outOff++] = codeTable[(in[j] & 0x03) << 4
					| ((in[j + 1] & 0xff) >>> 4)];
			out[outOff++] = codeTable[(in[j + 1] & 0x0f) << 2
					| ((in[j + 2] & 0xff) >>> 6)];
			out[outOff++] = codeTable[(in[j + 2] & 0x3f)];
			j += 3;
		}

		// Padding the leftover.
		switch (leftOver) {
		case 0:
			break;
		case 1: // Pad with 4 zeros and add 2 '='
			out[outOff++] = codeTable[(in[j] >>> 2) & 0x3f];
			out[outOff++] = codeTable[((in[j] & 0x03) << 4) & 0x30];
			out[outOff++] = pad;
			out[outOff++] = pad;
			j += 3;
			break;
		case 2: // Pad with 2 zeros and add 1 '='
			out[outOff++] = codeTable[(in[j] >>> 2) & 0x3f];
			out[outOff++] = codeTable[(in[j] & 0x03) << 4
					| ((in[j + 1] & 0xff) >>> 4)];
			out[outOff++] = codeTable[((in[j + 1] & 0x0f) << 2) & 0x3c];
			out[outOff++] = pad;
			j += 3;
			break;
		}
		return j / 3 * 4;
	}

	/**
	 * Process input from the array in and write it to the out array.
	 * 
	 * @param in
	 *            the array containing the input data.
	 * @param inOff
	 *            offset into the in array the data starts at.
	 * @param inLen
	 *            the number of input bytes to process.
	 * 
	 * @return the encoded bytes array.
	 * @exception IllegalArgumentException
	 *                when the process stops before the end of the array is
	 *                reached, or a wrong characters sequence is found.
	 */
	public static byte[] encode(byte[] in, int inOff, int inLen)
			throws IllegalArgumentException {
		byte[] out = new byte[encodeSize(inLen)];
		inLen = encode(in, inOff, inLen, out, 0);
		byte[] result = new byte[inLen];
		System.arraycopy(out, 0, result, 0, inLen);
		return result;
	}

	/**
	 * Process input from the array in and write it to the out array.
	 * 
	 * @param in
	 *            the array containing the input data.
	 * 
	 * @return the encoded bytes array.
	 * @exception IllegalArgumentException
	 *                when the process stops before the end of the array is
	 *                reached, or a wrong characters sequence is found.
	 */
	public static byte[] encode(byte[] in) throws IllegalArgumentException {
		if (in == null)
			return encode(in, 0, 0);
		return encode(in, 0, in.length);
	}

	/**
	 * Decode input from the array in and write it to the out array. Input bytes
	 * are processed up to the last integral multiple of four, any left bytes
	 * are discarded.
	 * 
	 * @param in
	 *            the array containing the input encoded data.
	 * @param inOff
	 *            offset into the in array the data starts at.
	 * @param inLen
	 *            the number of input bytes to process.
	 * @param out
	 *            the array the output data will be copied into.
	 * @param outOff
	 *            the offset into the out array the output will start at.
	 * 
	 * @return the number of bytes processed.
	 * @exception IllegalArgumentException
	 *                when the process stops before the end of the array is
	 *                reached, or a wrong characters sequence is found.
	 */
	public static int decode(byte[] in, int inOff, int inLen, byte[] out,
			int outOff) throws IllegalArgumentException {
		// byte[] decodeTable = getDecodeTable();
		// Initilize logger
		String errors = new String();

		// Check the space is enough
		if (in.length - inOff < inLen)
			throw new IllegalArgumentException(" Input buffer too short!");
		// Discard the leftover
		inLen -= (inLen % 4);
		int count = 0;
		int j = 0;
		boolean condition = (inLen > 0);
		while (condition) {
			int i = 0;
			byte[] a = new byte[4];
			// Filter out characters not in the alphabet
			inner: while (i < 4 && j < inLen) // Could be something to decode in
			// the bytes left!
			{
				if (((byte) 'A' <= in[j] & in[j] <= (byte) 'Z')
						|| ((byte) 'a' <= in[j] & in[j] <= (byte) 'z')
						|| ((byte) '0' <= in[j] & in[j] <= (byte) '9')
						|| ((byte) '/' == in[j]) || ((byte) '+' == in[j])) {
					// a[i++] = decodeTable[in[j]];
					a[i++] = f_decode(in[j]);
				} else if (in[j] == pad) // Padding encountered
				{
					condition = false;
					break inner; // The end
				} else // Skip symbol not in the alphabeth
				{
					errors = new String(errors + "\n" + j + "-" + in[j]);
				}
				j++;
				condition = (j < inLen);
			}
			i -= 1;

			// Decode as much as possible
			if (i > 0) {
				count += 1;
				out[outOff++] = (byte) ((a[0] << 2) | (a[1] >> 4));
				// System.out.println("1-"+count+" = "+(char)out[outOff-1]);
			}
			if (i > 1) {
				count += 1;
				out[outOff++] = (byte) ((a[1] << 4) | (a[2] >> 2));
				// System.out.println("2-"+count+" = "+(char)out[outOff-1]);
			}
			if (i > 2) {
				count += 1;
				out[outOff++] = (byte) ((a[2] << 6) | a[3]);
				// System.out.println("3-"+count+" = "+(char)out[outOff-1]);
			}
		}
		return count;
	}

	/**
	 * Decode input from the array in and write it to the out array. Input bytes
	 * are processed up to the last integral multiple of four, any left bytes
	 * are discarded.
	 * 
	 * @param in
	 *            the array containing the input encoded data.
	 * @param inOff
	 *            offset into the in array the data starts at.
	 * @param inLen
	 *            the number of input bytes to process.
	 * @param out
	 *            the array the output data will be copied into.
	 * @param outOff
	 *            the offset into the out array the output will start at.
	 * 
	 * @return the number of bytes processed.
	 * @exception IllegalArgumentException
	 *                when the process stops before the end of the array is
	 *                reached, or a wrong characters sequence is found.
	 */
	public static int decodeStream(byte[] in, int inOff, int inLen, byte[] out,
			int outOff) throws IllegalArgumentException {
		// byte[] decodeTable = getDecodeTable();
		// Initilize logger
		String errors = new String();

		// Check the space is enough
		if (in.length - inOff < inLen)
			throw new IllegalArgumentException(" Input buffer too short!");
		// Discard the leftover
		inLen -= (inLen % 4);
		int count = 0;
		int j = 0;
		boolean condition = (inLen > 0);
		while (condition) {
			int i = 0;
			byte[] a = new byte[4];
			// Filter out characters not in the alphabet
			inner: while (i < 4 && j < inLen)
			// Could be something to decode in the bytes left!
			{
				if (((byte) 'A' <= in[j] & in[j] <= (byte) 'Z')
						|| ((byte) 'a' <= in[j] & in[j] <= (byte) 'z')
						|| ((byte) '0' <= in[j] & in[j] <= (byte) '9')
						|| ((byte) '/' == in[j]) || ((byte) '+' == in[j])) {

					a[i++] = f_decode(in[j]);
					// a[i++] = decodeTable[in[j]];

				}
				// Padding encountered
				else if (in[j] == pad) {
					condition = false;
					break inner; // The end
				}
				// Skip symbol not in the table
				else {
					errors = new String(errors + "\n" + j + "-" + in[j]);
				}
				j++;
				condition = (j < inLen);
			}
			i -= 1;

			// Decode as much as possible
			if (i > 0) {
				count += 1;
				out[outOff++] = (byte) ((a[0] << 2) | (a[1] >> 4));
				// System.out.println("1-"+count+" = "+(char)out[outOff-1]);
			}
			if (i > 1) {
				count += 1;
				out[outOff++] = (byte) ((a[1] << 4) | (a[2] >> 2));
				// System.out.println("2-"+count+" = "+(char)out[outOff-1]);
			}
			if (i > 2) {
				count += 1;
				out[outOff++] = (byte) ((a[2] << 6) | a[3]);
				// System.out.println("3-"+count+" = "+(char)out[outOff-1]);
			}
		}
		return count;
	}

	/**
	 * Process input from the array in and write it to the out array. Leftover
	 * bytes are discarded.
	 * 
	 * @param in
	 *            the array containing the input data.
	 * @return the number of bytes processed from the input.
	 * @exception IllegalArgumentException
	 *                when the process stops before the end of the array is
	 *                reached, or a wrong characters sequence is found.
	 */
	public static byte[] encodeStream(byte[] in) throws IllegalArgumentException {
		// Check the space is enough
		if (in.length < 1)
			throw new IllegalArgumentException(" Input buffer too short!");

		int inLen = in.length - in.length % 3;

		byte out[] = new byte[encodeMinSize(inLen)];
		int outOff = 0;
		int j = 0;

		// Split 24 bits of input into 4 groups of 6 bits of output.
		while (j < inLen) {
			out[outOff++] = codeTable[(in[j] >>> 2) & 0x3f];
			out[outOff++] = codeTable[(in[j] & 0x03) << 4
					| ((in[j + 1] & 0xff) >>> 4)];
			out[outOff++] = codeTable[(in[j + 1] & 0x0f) << 2
					| ((in[j + 2] & 0xff) >>> 6)];
			out[outOff++] = codeTable[(in[j + 2] & 0x3f)];
			j += 3;
		}

		return out;
	}

	/**
	 * Decode input from the array in and write it to the out array. Input bytes
	 * are processed up to the last integral multiple of four, any left bytes
	 * are discarded.
	 * 
	 * @param in
	 *            the array containing the input encoded data.
	 * 
	 * @return out the array the output data will be copied into.
	 * @exception IllegalArgumentException
	 *                when the process stops before the end of the array is
	 *                reached, or a wrong characters sequence is found.
	 */
	public static byte[] decodeStream(byte[] in) throws IllegalArgumentException {
		// byte[] decodeTable = getDecodeTable();
		// Initilize logger
		StringBuffer errors = new StringBuffer();

		// Check the space is enough
		if (in.length < 1)
			throw new IllegalArgumentException(" Input buffer too short!");

		// Discard the leftover
		int inLen = in.length - (in.length % 4);

		// Output
		byte out[] = new byte[inLen / 4 * 3];

		int count = 0;
		int j = 0;
		int outOff = 0;
		boolean condition = (inLen > 0);
		while (condition) {
			int i = 0;
			byte[] a = new byte[4];
			// Filter out characters not in the alphabet
			inner: while (i < 4 && j < inLen)
			// Could be something to decode in the bytes left!
			{
				if (((byte) 'A' <= in[j] & in[j] <= (byte) 'Z')
						|| ((byte) 'a' <= in[j] & in[j] <= (byte) 'z')
						|| ((byte) '0' <= in[j] & in[j] <= (byte) '9')
						|| ((byte) '/' == in[j]) || ((byte) '+' == in[j])) {

					a[i++] = f_decode(in[j]);
					// a[i++] = decodeTable[in[j]];

				}
				// Padding encountered
				else if (in[j] == pad) {
					condition = false;
					break inner; // The end
				}
				// Skip symbol not in the table
				else {
					errors.append("\n" + j + "-" + in[j]);
				}
				j++;
				condition = (j < inLen);
			}
			i -= 1;

			// Decode as much as possible
			if (i > 0) {
				count += 1;
				out[outOff++] = (byte) ((a[0] << 2) | (a[1] >> 4));
			}
			if (i > 1) {
				count += 1;
				out[outOff++] = (byte) ((a[1] << 4) | (a[2] >> 2));
			}
			if (i > 2) {
				count += 1;
				out[outOff++] = (byte) ((a[2] << 6) | a[3]);
			}
		}

		if (count < out.length) {
			byte t[] = new byte[count];
			System.arraycopy(out, 0, t, 0, count);
			out = t;
		}

		return out;
	}

	/**
	 * Decode input from the array in and write it to the out array. Input bytes
	 * are processed up to the last integral multiple of four, any left bytes
	 * are discarded.
	 * 
	 * @param in
	 *            the array containing the input encoded data.
	 * @param inOff
	 *            offset into the in array the data starts at.
	 * @param inLen
	 *            the number of input bytes to process.
	 * 
	 * @return the array of bytes processed.
	 * @exception IllegalArgumentException
	 *                when the process stops before the end of the array is
	 *                reached, or a wrong characters sequence is found.
	 */
	public static byte[] decode(byte[] in, int inOff, int inLen)
			throws IllegalArgumentException {
		byte[] out = new byte[decodeSize(inLen)];
		inLen = decode(in, inOff, inLen, out, 0);
		byte[] result = new byte[inLen];
		System.arraycopy(out, 0, result, 0, inLen);
		return result;
	}

	/**
	 * Decode input from the array in and write it to the out array. Input bytes
	 * are processed up to the last integral multiple of four, any left bytes
	 * are discarded.
	 * 
	 * @param in
	 *            the array containing the input encoded data.
	 * @return the array of bytes processed.
	 * @exception IllegalArgumentException
	 *                when the process stops before the end of the array is
	 *                reached, or a wrong characters sequence is found.
	 */
	public static byte[] decode(byte[] in) throws IllegalArgumentException {
		return decode(in, 0, in.length);
	}

	/**
	 * Return the maximum output length for decoding an input length. The
	 * padding is not subtract.
	 * 
	 * @param inLen
	 *            input length in bytes.
	 * @return the processed output length including padding.
	 */
	public static int decodeSize(int inLen) {
		inLen -= (inLen % 4);
		return inLen * 3 / 4;
	}

	/**
	 * Return the expected output length for encoding an input length.
	 * 
	 * @param inLen
	 *            input length in bytes.
	 * @return the processed output length including padding.
	 */
	public static int encodeSize(int inLen) {
		int leftOver = inLen % 3;
		inLen = inLen * 4 / 3;
		switch (leftOver) {
		case 1: // two padding
			inLen += 3;
			break;
		case 2: // one padding
			inLen += 2;
			break;
		}
		// System.out.println("base64" + inLen);
		return inLen;
	}

	/**
	 * Return the expected output length for encoding an input length.
	 * 
	 * @param inLen
	 *            input length in bytes.
	 * @return the processed output length including padding.
	 */
	public static int encodeMinSize(int inLen) {
		return (inLen - inLen % 3) / 3 * 4;
	}

}