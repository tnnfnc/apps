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
package it.tnnfnc.apps.application.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

/**
 * Password check. This checker is neither perfect nor foolproof, and should
 * only be utilized as a loose guide in determining methods for improving the
 * password creation process.
 * 
 * @author Franco Toninato
 * 
 */
public class PasswordGauge {
	private static boolean debug = false;

	/**
	 * Get the password strength.
	 * 
	 * @param password
	 *            the input password to check.
	 * @return the password strength.
	 */
	public static int getStrength(final char[] password) {
		String p = new String(password);
		int l = password.length;
		// if (debug) System.out.println("Password = " + p);

		int good = 4 * numberOfCharacters(p)//
				+ 2 * (l - uppercaseLetters(p))//
				+ 2 * (l - lowercaseLetters(p))//
				+ 4 * numbers(p)//
				+ 6 * symbols(p)//
				+ 2 * middleNumbersOrSymbols(p)//
				+ 2 * requirements(p);

		int bad = subtractLettersOnly(p)//
				+ subtractNumbersOnly(p)//
				+ subtractRepeatCharacters(p)//
				+ 2 * subtractConsecutiveUppercaseLetters(p)//
				+ 2 * subtractConsecutiveLowercaseLetters(p) //
				+ 2 * subtractConsecutiveNumbers(p)//
				+ 3 * subtractSequentials(p)//
		;

		return good - bad;
	}

	/**
	 * # x 4
	 * 
	 * @return # x 4
	 */
	private static int numberOfCharacters(final String password) {
		int n = password.length();
		if (debug)
			System.out.println("numberOfCharacters = " + n);
		return n;
	}

	/**
	 * +((len-n)*2)
	 * 
	 * @return
	 */
	private static int lowercaseLetters(final String password) {
		int n = 0;
		String patternString = "[a-z]";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(password);
		while (matcher.find()) {
			n++;
		}
		if (debug)
			System.out.println("lowercaseLetters = " + n);
		return n;
	}

	/**
	 * +((len-n)*2)
	 * 
	 * @return
	 */
	private static int uppercaseLetters(final String password) {
		int n = 0;
		String patternString = "[A-Z]";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(password);
		while (matcher.find()) {
			n++;
		}
		if (debug)
			System.out.println("uppercaseLetters = " + n);

		return n;
	}

	/**
	 * +(n*6)
	 * 
	 * @return
	 */
	private static int symbols(final String password) {
		int n = 0;
		String patternString = "[^\\w]";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(password);
		while (matcher.find()) {
			n++;
		}
		if (debug)
			System.out.println("symbols = " + n);
		return n;
	}

	/**
	 * +(n*4)
	 * 
	 * @return
	 */
	private static int numbers(final String password) {
		int n = 0;
		String patternString = "[0-9]";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(password);
		while (matcher.find()) {
			n++;
		}
		if (debug)
			System.out.println("numbers = " + n);
		return n;
	}

	/**
	 * Symbols or numbers not in the first or last position counts for +(n*2).
	 * 
	 * @return
	 */
	private static int middleNumbersOrSymbols(final String password) {
		int n = 0;
		if (password.length() < 3)
			return 0;
		String patternString = "[^\\w]";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(password.substring(1,
				password.length() - 1));
		while (matcher.find()) {
			n++;
		}

		patternString = "[0-9]";
		pattern = Pattern.compile(patternString);
		matcher = pattern.matcher(password.substring(1, password.length() - 1));
		while (matcher.find()) {
			n++;
		}
		if (debug)
			System.out.println("middleNumbersOrSymbols = "
					+ password.substring(1, password.length() - 1) + " : " + n);

		return n;
	}

	/**
	 * (n*2) Minimum 8 characters in length Contains 3/4 of the following items:
	 * - Uppercase Letters - Lowercase Letters - Numbers - Symbols
	 * 
	 * @return
	 */
	private static int requirements(final String password) {
		int n = 0;
		// 8 chars
		if (password.length() > 8) {
			n++;
		}
		// Uppercase
		String patternString = "[A-Z]";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(password);
		if (matcher.find()) {
			n++;
		}

		// Lowercase
		patternString = "[a-z]";
		pattern = Pattern.compile(patternString);
		matcher = pattern.matcher(password);
		if (matcher.find()) {
			n++;
		}

		// Number
		patternString = "[0-9]";
		pattern = Pattern.compile(patternString);
		matcher = pattern.matcher(password);
		if (matcher.find()) {
			n++;
		}

		// Symbols
		patternString = "[^\\w]";
		pattern = Pattern.compile(patternString);
		matcher = pattern.matcher(password);
		if (matcher.find()) {
			n++;
		}

		if (debug)
			System.out.println("requirements = " + n);
		return n;
	}

	/**
	 * @return
	 */
	private static int subtractLettersOnly(final String password) {
		int n = 0;
		String patternString = "[a-zA-Z]";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(password);
		while (matcher.find()) {
			n++;
		}
		if (n < password.length()) {
			n = 0;
		}
		if (debug)
			System.out.println("subtractLettersOnly = " + n);
		return n;
	}

	/**
	 * @return
	 */
	private static int subtractNumbersOnly(final String password) {
		int n = 0;
		String patternString = "[0-9]";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(password);
		while (matcher.find()) {
			n++;
		}
		if (n < password.length()) {
			n = 0;
		}
		if (debug)
			System.out.println("subtractNumbersOnly = " + n);
		return n;
	}

	/**
	 * @return
	 */
	private static int subtractRepeatCharacters(final String password) {
		int n = 0;
		char[] arrayPassword = password.toCharArray();
		HashMap<Character, Integer> repeated = new HashMap<Character, Integer>();
		for (int i = 0; i < arrayPassword.length; i++) {
			Integer old = repeated.get(arrayPassword[i]);
			repeated.put(arrayPassword[i], old == null ? 1 : old.intValue() + 1);
			if (debug)
				System.out.println("key = " + arrayPassword[i] + " value = "
						+ old);
		}

		for (Iterator<Character> iterator = repeated.keySet().iterator(); iterator
				.hasNext();) {
			int sum = repeated.get(iterator.next()).intValue();
			if (sum > 1) {
				n += sum;
			}

		}
		if (debug)
			System.out.println("subtractRepeatCharacters = " + n);
		return n;
	}

	/**
	 * @return
	 */
	private static int subtractConsecutiveUppercaseLetters(final String password) {
		int n = 0;
		char[] p = password.toCharArray();
		boolean on = false;
		for (int i = 0; i < p.length - 1; i++) {
			if (Character.isUpperCase(p[i])) {
				on = true;
			} else {
				on = false;
			}
			if (on && Character.isUpperCase(p[i + 1])) {
				n++;
			}
		}
		if (debug)
			System.out.println("subtractConsecutiveUppercaseLetters = " + n);
		return n;
	}

	/**
	 * @return
	 */
	private static int subtractConsecutiveLowercaseLetters(final String password) {
		int n = 0;
		char[] p = password.toCharArray();
		boolean on = false;
		for (int i = 0; i < p.length - 1; i++) {
			if (Character.isLowerCase(p[i])) {
				on = true;
			} else {
				on = false;
			}
			if (on && Character.isLowerCase(p[i + 1])) {
				n++;
			}
		}
		if (debug)
			System.out.println("subtractConsecutiveLowercaseLetters = " + n);
		return n;
	}

	/**
	 * @return
	 */
	private static int subtractConsecutiveNumbers(final String password) {
		int n = 0;
		char[] p = password.toCharArray();
		boolean on = false;
		for (int i = 0; i < p.length - 1; i++) {
			if (Character.isDigit(p[i])) {
				on = true;
			} else {
				on = false;
			}
			if (on && Character.isDigit(p[i + 1])) {
				n++;
			}
		}
		if (debug)
			System.out.println("subtractConsecutiveNumbers = " + n);
		return n;
	}

	/**
	 * (3+)
	 * 
	 * @return
	 */
	private static int subtractSequentials(final String password) {
		int n = 0;
		char[] p = password.toLowerCase().toCharArray();
		boolean onDown = false;
		boolean onUp = false;

		for (int i = 0; i < p.length - 2; i++) {
			// Up
			if (Character.getNumericValue(p[i + 1]) == Character
					.getNumericValue(p[i]) + 1) {
				onUp = true;
			} else {
				onUp = false;
			}
			// Down
			if (Character.getNumericValue(p[i + 1]) == Character
					.getNumericValue(p[i]) - 1) {
				onDown = true;
			} else {
				onDown = false;
			}

			// Up
			if (onUp
					&& Character.getNumericValue(p[i + 2]) == Character
							.getNumericValue(p[i]) + 2) {
				n++;
			}
			// Down
			if (onDown
					&& Character.getNumericValue(p[i + 2]) == Character
							.getNumericValue(p[i]) - 2) {
				n++;
			}
		}

		if (debug)
			System.out.println("subtractSequentials = " + n);
		return n;
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				String p = "cba+f@FFrancopqr17;Fra#NCO2abcd";
				System.out.println(p + " Strenght = "
						+ PasswordGauge.getStrength(p.toCharArray()));
				p = "cba-opqr-abcd-defg-123-654";
				System.out.println(p + " Strenght = "
						+ PasswordGauge.getStrength(p.toCharArray()));
				p = "cba+f@FFrancopqr17;Fra#NCO2abcd";
				System.out.println(p + " Strenght = "
						+ PasswordGauge.getStrength(p.toCharArray()));
				p = "cba+f@FFrancopqr17;Fra#NCO2abcd";
				System.out.println(p + " Strenght = "
						+ PasswordGauge.getStrength(p.toCharArray()));
				p = "Buongiorno50";
				System.out.println(p + " Strenght = "
						+ PasswordGauge.getStrength(p.toCharArray()));
			}
		});

	}
}
