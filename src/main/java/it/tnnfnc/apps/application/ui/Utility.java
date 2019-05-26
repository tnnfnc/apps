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

import java.awt.Dimension;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JComponent;

/**
 * Provides useful methods for debugging.
 */
public class Utility {

	/**
	 * Creates the detail pane.
	 */
	private Utility() {
	}

	/**
	 * @param n
	 * @return
	 */
	public static int toInteger(String n) {
		int i = 0;
		try {
			i = Integer.parseInt(n);
		} catch (NumberFormatException e) {
		}
		return i;
	}

	/**
	 * Return a boolean from a sting (true or false).
	 * 
	 * @param boole
	 *            the string with a boolean.
	 * @return the boolean.
	 */
	public static boolean toBoolean(String n) {
		boolean i = false;
		try {
			i = Boolean.getBoolean(n);
		} catch (Exception e) {
		}
		return i;
	}

	/**
	 * Set the preferred component size in percentage of the actual preferred
	 * size. If 0 increase is passed the component preserve its default
	 * preferred size.
	 * 
	 * @param c
	 *            component.
	 * @param w
	 *            width percentage. 0 means no increase.
	 * @param h
	 *            height percentage.
	 */
	public static void setComponentSize(JComponent c, float w, float h) {
		Dimension d = c.getPreferredSize();
		d.width += d.width * w;
		d.height += d.height * h;
		c.setPreferredSize(d);
	}

	/**
	 * Represents a number as a string of bits.
	 * 
	 * @param n
	 *            number.
	 * @return String the string of bits.
	 */
	public static String bitWriter(long n) {
		StringBuffer sb = new StringBuffer();
		char bit_0 = '0';
		char bit_1 = '1';
		long lastBit = 1L;
		long buffer = 0L;

		for (int i = 0; i < 64; i++) {
			buffer = n >> i;
			if ((lastBit & buffer) == lastBit) {
				sb.append(bit_1);
			} else {
				sb.append(bit_0);
			}
		}

		sb.reverse();
		return sb.toString();
	}

	/**
	 * Represents a number as a string of bits.
	 * 
	 * @param n
	 *            number.
	 * @return String the string of bits.
	 */
	public static String bitWriter(int n) {
		StringBuffer sb = new StringBuffer();
		char bit_0 = '0';
		char bit_1 = '1';
		int lastBit = 1;
		int buffer = 0;

		for (int i = 0; i < 32; i++) {
			buffer = n >> i;
			if ((lastBit & buffer) == lastBit) {
				sb.append(bit_1);
			} else {
				sb.append(bit_0);
			}
		}

		sb.reverse();
		return sb.toString();
	}

	/**
	 * Represents a number as a string of bits.
	 * 
	 * @param n
	 *            number.
	 * @return String the string of bits.
	 */
	public static String bitWriter(short n) {
		StringBuffer sb = new StringBuffer();
		char bit_0 = '0';
		char bit_1 = '1';
		short lastBit = 1;
		short buffer = 0;

		for (int i = 0; i < 16; i++) {
			buffer = (short) (n >> i);
			if ((lastBit & buffer) == lastBit) {
				sb.append(bit_1);
			} else {
				sb.append(bit_0);
			}
		}

		sb.reverse();
		return sb.toString();
	}

	/**
	 * Represents a number as a string of bits.
	 * 
	 * @param n
	 *            number.
	 * @return String the string of bits.
	 */
	public static String bitWriter(byte n) {
		StringBuffer sb = new StringBuffer();
		char bit_0 = '0';
		char bit_1 = '1';
		byte lastBit = 1;
		byte buffer = 0;

		for (int i = 0; i < 8; i++) {
			buffer = (byte) (n >> i);
			if ((lastBit & buffer) == lastBit) {
				sb.append(bit_1);
			} else {
				sb.append(bit_0);
			}
		}

		sb.reverse();
		return sb.toString();
	}

	/**
	 * Returns a string reprentation of a <code>java.util.Date</code> instance.
	 * 
	 * @param date
	 *            the date.
	 * @param sep
	 *            the date separator. Default is the system defined.
	 * @return the string representation formatted like YYYY/MM/DD.
	 */
	/**
	 * @param date
	 * @return
	 */
	public static String dateToString(java.util.Date date, String sep) {
		if (date == null)
			return new String();
		StringBuffer b = new StringBuffer();
		/* Date YYYY/MM/DD */
		sep = sep == null ? "" : sep;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		b.append(c.get(Calendar.YEAR));
		b.append(sep);
		b.append(c.get(Calendar.MONTH) + 1);
		b.append(sep);
		b.append(c.get(Calendar.DAY_OF_MONTH));
		// String d = new String("" + + sep
		// + (c.get(Calendar.MONTH) + 1) + sep
		// + c.get(Calendar.DAY_OF_MONTH));
		return b.toString();
	}

	/**
	 * Returns a <code>java.util.Date</code> from a string formatted as
	 * YYYY/MM/DD.
	 * 
	 * @param date
	 *            the data as a string.
	 * @return the date.
	 */
	public static java.util.Date stringToDate(String date) {
		String sep = "/";
		/* Date YYYY/MM/DD */
		Calendar c = Calendar.getInstance();
		String[] d = date.split(sep);
		c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]));
		return c.getTime();
	}

	/**
	 * Adds two arrays of objects.
	 * 
	 * @param a
	 *            array a.
	 * @param b
	 *            array b.
	 * @return Object[] array a + b
	 */
	public static Object[] addArray(final Object[] a, final Object[] b) {
		int len_a = (a == null ? 0 : a.length);
		int len_b = (b == null ? 0 : b.length);
		Object[] result = new Object[len_a + len_b];

		if (a != null)
			System.arraycopy(a, 0, result, 0, len_a);
		if (b != null)
			System.arraycopy(b, 0, result, len_a, len_b);
		return result;
	}

	/**
	 * Get the present time as HH:mm:ss.
	 * 
	 * @return the current time.
	 */
	public static String now() {
		Calendar now = Calendar.getInstance();
		return new String((now.get(Calendar.HOUR_OF_DAY) < 10 ? new String("0" + now.get(Calendar.HOUR_OF_DAY))
				: now.get(Calendar.HOUR_OF_DAY))
				+ ":"
				+ (now.get(Calendar.MINUTE) < 10 ? new String("0" + now.get(Calendar.MINUTE))
						: now.get(Calendar.MINUTE))
				+ ":" + (now.get(Calendar.SECOND) < 10 ? new String("0" + now.get(Calendar.SECOND))
						: now.get(Calendar.SECOND)));
	}

	/**
	 * Get the present time as HH:mm:ss.
	 * 
	 * @return the current time.
	 */
	public static String timeStamp() {
		Calendar now = Calendar.getInstance();
		return new String((now.get(Calendar.HOUR_OF_DAY) < 10 ? new String("0" + now.get(Calendar.HOUR_OF_DAY))
				: now.get(Calendar.HOUR_OF_DAY))
				+ ":"
				+ (now.get(Calendar.MINUTE) < 10 ? new String("0" + now.get(Calendar.MINUTE))
						: now.get(Calendar.MINUTE))
				+ ":" + (now.get(Calendar.SECOND) < 10 ? new String("0" + now.get(Calendar.SECOND))
						: now.get(Calendar.SECOND))
				+ ":" + now.get(Calendar.MILLISECOND));
	}

	/**
	 * Get the time as HH:mm:ss:mmm
	 * 
	 * @return the time.
	 */
	public static String timeStamp(long milliseconds) {
		StringBuffer buffer = new StringBuffer();

		// int msecs = (int) (milliseconds % 1000);
		milliseconds = milliseconds / 1000;
		int hours = (int) (milliseconds / 3600);
		milliseconds = milliseconds % 3600; // sec
		int mins = (int) (milliseconds / 60);
		milliseconds = milliseconds % 60;
		int secs = (int) milliseconds;

		buffer.append(hours);
		buffer.append(":");
		buffer.append(mins < 10 ? "0" + mins : mins);
		buffer.append(":");
		buffer.append(secs < 10 ? "0" + secs : secs);
		// buffer.append(":");
		// buffer.append(msecs);
		return buffer.toString();
	}

	/**
	 * Clear properties.
	 * 
	 * @param properties
	 *            the properties map.
	 */
	public static void clear(Properties properties) {
		properties.clear();
	}

}