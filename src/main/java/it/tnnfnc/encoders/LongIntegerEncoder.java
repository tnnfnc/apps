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
 * Transforms a bytes array into an array of UNICODE symbols. The transformation
 * is irreversible. <br />
 * The set of Unicode symbols can be set as an array of Unicode character
 * subsets (only characters, only numbers, including punctuation and other
 * symbols).
 * <p />
 * The algorithm is as follows:
 * <ol>
 * <li>Cutting the array into slices of 64 bits.
 * <li>Assigning any unicode symbol to a number starting from 0 to the number of
 * allowed symbols.
 * <li>Writing any words in the base of the number of allowed symbols.
 * <li>Collecting the encoded words into an array of symbols.
 * <li>Taking a symbol from any encoded word until the output length is reached.
 * <li>Leftover encoded material is discarded.
 * </ol>
 * 
 * @author Franco Toninato
 */
public class LongIntegerEncoder implements I_BytesEncoder {
	// public static String algorithm = "Long integer decomposition";
	public static String algorithm = "64 bit decomposition";

	/*
	 * Converts the input word in a symbols array.
	 * 
	 * @param input the input word
	 * 
	 * @param d the dimension
	 * 
	 * @return the symbols array
	 */
	private static int[] wordToSymbol(long word, int d) throws java.lang.ArithmeticException {
		int[] symbol = new int[10];
		// 1 - Representing the number word in the base d
		
		long buffer = word > 0 ? word : -word;
		int i = 0;
		do {
			symbol[i] = (int) (buffer % d);
			buffer = buffer / d;
			i++;
			if (i > symbol.length - 1) {
				int[] temp = new int[symbol.length * 2 + 1];
				System.arraycopy(symbol, 0, temp, 0, symbol.length);
				symbol = temp;
			}
		} while (!(buffer == 0));

		// Trim the array
		int[] temp = new int[i];
		System.arraycopy(symbol, 0, temp, 0, i);
		symbol = temp;
		return symbol;
	}

	/**
	 * Splits the input bytes in a 56-bit words array.
	 * 
	 * @param input
	 *            the input bytes
	 * 
	 * @return the words array
	 */
	private static long[] byteToWords(byte[] input) {
		// 1 - Determines how long a word is: 8 bytes
		int wordLen = (Long.SIZE / 8) - 0; // This prevents from having
		// System.out.println(PasswordEncoder.class.getName() + "wordLen = " +
		// wordLen);
		// negatives

		// 2 - Padding (0x0000) if last bytes are less than a word
		int words = input.length / wordLen;
		int padLen = wordLen - input.length % wordLen; // bytes
		if (padLen < wordLen) {
			words++;
		}
		// System.out.println(PasswordEncoder.class.getName() + "words = " +
		// words);
		byte[] iPad = new byte[words * wordLen];
		for (int i = 0; i < iPad.length; i++) {
			iPad[i] = 0x00;
		}
		System.arraycopy(input, 0, iPad, 0, input.length);

		// 3 - Filling the words array
		long lastByte = 0xFFL;
		long[] speech = new long[words];

		long buffer;
		int j = 0;
		// Write the speech
		for (int i = 0; i < iPad.length; i += wordLen) {
			buffer = 0x00L;
			// Write a word
			for (int c = 0; c < wordLen - 1; c++) {
				buffer = (buffer | (lastByte & iPad[i + c])) << 8;
			}
			buffer = buffer | (lastByte & iPad[i + wordLen - 1]);
			speech[j] = buffer; // The first bit is the sign!
			j++;
		}
		return speech;
	}

	@Override
	public char[] encode(byte[] input, String[] alphabeth, int len) throws IndexOutOfBoundsException {
		// 2 - Splitting the input in a 32-bit word array
		long words[] = byteToWords(input);
		/*
		 * 3 - Representing every word in the base s(R): appending the symbols
		 * array and 4 - Converts number to symbols
		 */
		int[][] out = new int[words.length][0];
		for (int i = 0; i < words.length; i++) {
			out[i] = wordToSymbol(words[i], alphabeth.length);
		}

		int l = 0;
		for (int i = 0; i < words.length; i++) {
			l += out[i].length;
		}
		// System.out.println(PasswordEncoder.class.getName() + "number of
		// symbols = " + l);
		// It fills the password taking a unicode element for every words until
		// there is not elements left
		String[] text = new String[l];
		int j = 0;
		int k = 0;
		while (k < l) {
			for (int i = 0; i < words.length; i++) {
				try {
					text[j] = alphabeth[out[i][k]];
					j++;
				} catch (ArrayIndexOutOfBoundsException e) {
				} // Skip the null components
			}
			k++;
		}

		// 5 - Checking the required len is not bigger than the out array of
		// symbols
		StringBuffer buffer = new StringBuffer();
		for (String s : text) {
			buffer.append(s);
		}
		return buffer.toString().substring(0, len).toCharArray();
	}

	@Override
	public String getAlgorithm() {
		return algorithm;
	}
}
