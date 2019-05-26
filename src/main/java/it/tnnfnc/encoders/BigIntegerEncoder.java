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

import java.math.BigInteger;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class BigIntegerEncoder implements I_BytesEncoder {
	public static String algorithm = "Big number decomposition";

	/**
	 * Returns an array of strings derived from the input bytes up to the set
	 * length.
	 * 
	 * @param input
	 *            the input bytes.
	 * @param range
	 *            the allowed symbols.
	 * @param len
	 *            the output lenght.
	 * @return the encoded array of string elements.
	 */
	public char[] encode(byte[] input, String[] alphabeth, int len) throws IndexOutOfBoundsException {
		// System.out.println(PasswordEncoder.class.getName()
		// + " start calculation");

		// 1) how big is the alphabeth?
		ArrayList<BigInteger> rr = new ArrayList<BigInteger>();

		// 2) Now is to represent the number "input" in the base "base"
		BigInteger q = new BigInteger(input).abs();
		BigInteger d = new BigInteger(alphabeth.length + "");
		BigInteger[] step;
		// System.out.println(this.getClass().getName() + " number=" + q + "
		// divider=" + d);

		while (q.compareTo(BigInteger.ZERO) != 0) {
			step = q.divideAndRemainder(d);
			q = step[0];
			// "quotient step-" + step[0] " reminder = " + step[1]);
			rr.add(step[1]);
			// System.out.println(this.getClass().getName() + " quotient=" + q +
			// " rest=" + step[1]);
		}

		StringBuffer output = new StringBuffer();
		for (int i = rr.size() - 1; i > -1; i--) {
			// System.out.println(this.getClass().getName() + " symbol id=" +
			// rr.get(i) + " value=" + alphabeth[rr.get(i).intValue()]);
			output.append(alphabeth[rr.get(i).intValue()]);
		}
		// System.out
		// .println(PasswordEncoder.class.getName() + "number of symbols = " +
		// rr.size());
		return output.toString().substring(0, len).toCharArray();
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String[] alphabeth = { //
						"0", "1", //
						"2", "3", //
						"4", "5", //
						"6", "7" //

				};

				byte[] input = { (byte) 10, //

				};
				new BigIntegerEncoder();
				// String[] alphabeth = {//
				// "ba", "bu",//
				// "ba1", "bu1",//
				// "ba2", "bu2",//
				// "ba3", "bu3",//
				// "ba4", "bu4",//
				// "ba5", "bu5",//
				// "ba6", "bu6",//
				// "ba7", "bu7",//
				// "ba8", "bu8",//
				// "ba9", "bu9",//
				// "ba10", "b10u",//
				// "ba11", "b11u",//
				// "ba12", "bu12",//
				// "ba13", "bu13",//
				// "ba14", "bu14",//
				// "ba15", "bu15",//
				// "ba16", "bu16",//
				//
				// };
				// byte[] input = { (byte) 123, //
				// (byte) 123, //
				// (byte) 122, //
				// (byte) 133, //
				// (byte) 13, //
				// (byte) 12, //
				// (byte) 123, //
				// (byte) 200, //
				// (byte) 23, //
				// (byte) 43, //
				// (byte) 250, //
				// (byte) 255, //
				// (byte) 1, //
				// (byte) 2, //
				// (byte) 123, //
				// (byte) 77, //
				// (byte) 128, //
				// (byte) 73, //
				// (byte) 13, //
				// (byte) 43, //
				// (byte) 73, //
				// (byte) 19, //
				// (byte) 17, //
				// (byte) 18, //
				// (byte) 19, //
				// (byte) 199, //
				// (byte) 123, //
				// (byte) 123, //
				// (byte) 122, //
				// (byte) 133, //
				// (byte) 13, //
				// (byte) 12, //
				// (byte) 123, //
				// (byte) 200, //
				// (byte) 23, //
				// (byte) 43, //
				// (byte) 250, //
				// (byte) 255, //
				// (byte) 1, //
				// (byte) 2, //
				// (byte) 123, //
				// (byte) 77, //
				// (byte) 128, //
				// (byte) 73, //
				// (byte) 13, //
				// (byte) 43, //
				// (byte) 73, //
				// (byte) 19, //
				// (byte) 17, //
				// (byte) 18, //
				// (byte) 19, //
				// (byte) 199, //
				// (byte) 123, //
				// (byte) 123, //
				// (byte) 122, //
				// (byte) 133, //
				// (byte) 13, //
				// (byte) 12, //
				// (byte) 123, //
				// (byte) 200, //
				// (byte) 23, //
				// (byte) 43, //
				// (byte) 250, //
				// (byte) 255, //
				// (byte) 1, //
				// (byte) 2, //
				// (byte) 123, //
				// (byte) 77, //
				// (byte) 128, //
				// (byte) 73, //
				// (byte) 13, //
				// (byte) 43, //
				// (byte) 73, //
				// (byte) 19, //
				// (byte) 17, //
				// (byte) 18, //
				// (byte) 19, //
				// (byte) 199, //
				// (byte) 123, //
				//
				// };
				System.out.println(new BigIntegerEncoder().encode(input, alphabeth, 10));
			}
		});

	}

	@Override
	public String getAlgorithm() {
		return algorithm;
	}
}
