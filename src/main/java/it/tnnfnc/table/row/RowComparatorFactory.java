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
package it.tnnfnc.table.row;

import java.text.Collator;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JButton;

import it.tnnfnc.apps.application.ui.style.I_StyleObject;

public class RowComparatorFactory {

	/**
	 * Get the defined comparator for sort.
	 * 
	 * @param col
	 *            the column index.
	 * @return the defined comparator for sort.
	 */
	public static  Comparator<I_TableRow<?>> getDefaultComparator(
			final int col, Class<?> clazz) {
		if (clazz.equals(Object.class)) {
			return getDefaultComparator(col);
		} else if (clazz.equals(JButton.class)) {
			return getButtonComparator(col);
		} else if (clazz.equals(Date.class)) {
			return getDateComparator(col);
		} else if (clazz.equals(Integer.class)) {
			return getIntegerComparator(col);
		} else {
			return getDefaultComparator(col);
		}
	}

	/**
	 * Get the defined comparator for sort.
	 * 
	 * @param col
	 *            the column index.
	 * @return the defined comparator for sort.
	 */
	public static  Comparator<I_TableRow<?>> getDefaultComparator(
			final int col) {

		return new Comparator<I_TableRow<?>>() {
			@Override
			public int compare(I_TableRow<?> o1, I_TableRow<?> o2) {
				Collator collator = Collator.getInstance();
				// Both null are equals
				Object s1;
				Object s2;
				if (o1 == null && o2 == null) {
					return 0;
				}
				s1 = (o1 == null || (o1 != null && o1.get(col) == null)) ? ""
						: o1.get(col);
				s2 = (o2 == null || (o2 != null && o2.get(col) == null)) ? ""
						: o2.get(col);
				return (collator.compare(value(s1).toString(), value(s2)
						.toString()));
			}

			@Override
			public String toString() {
				return "Locale string comparison";
			}
		};
	}

	/**
	 * Get the defined comparator for sort.
	 * 
	 * @param col
	 *            the column index.
	 * @return the defined comparator for sort.
	 */
	public static <T extends I_TableRow<?>> Comparator<T> getDateComparator(
			final int col) {

		return new Comparator<T>() {
			@Override
			public int compare(T row1, T row2) {
				// Both null are equals
				if (row1 == null && row2 == null) {
					return 0;
				} else if (row1.get(col) == null && row2.get(col) == null) {
					return 0;
				} else if (row1.get(col) != null && row2.get(col) == null) {
					return 1;
				} else if (row1.get(col) == null && row2.get(col) != null) {
					return -1;
				} else {
					// System.out.println(this.getClass().getName()
					// + " Date 1 = "
					// + (Date) value(row1.get(col))
					// + " Date 2 = "
					// + (Date) value(row2.get(col))
					// + " result "
					// + ((Date) value(row1.get(col)))
					// .compareTo((Date) value(row1.get(col))));
					return ((Date) value(row1.get(col)))
							.compareTo((Date) value(row2.get(col)));
				}
			}

			@Override
			public String toString() {
				return "Locale date comparison";
			}
		};
	}

	/**
	 * Get the defined comparator for sort.
	 * 
	 * @param col
	 *            the column index.
	 * @return the defined comparator for sort.
	 */
	public static <T extends I_TableRow<?>> Comparator<T> getIntegerComparator(
			final int col) {

		return new Comparator<T>() {
			@Override
			public int compare(T row1, T row2) {
				// Both null are equals
				if (row1 == null && row2 == null) {
					return 0;
				} else if (row1.get(col) == null && row2.get(col) == null) {
					return 0;
				} else if (row1.get(col) != null && row2.get(col) == null) {
					return 1;
				} else if (row1.get(col) == null && row2.get(col) != null) {
					return -1;
				} else {
					return ((Integer) value(row1.get(col)))
							.compareTo((Integer) value(row2.get(col)));
				}
			}

			@Override
			public String toString() {
				return "Locale integer number comparison";
			}
		};
	}

	/**
	 * Get the defined comparator for sort.
	 * 
	 * @param col
	 *            the column index.
	 * @return the defined comparator for sort.
	 */
	public static <T extends I_TableRow<?>> Comparator<T> getButtonComparator(
			final int col) {

		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				// Both null are equals
				Object s1 = (o1 == null || (o1 != null && o1.get(col) == null)) ? null
						: o1.get(col);
				Object s2 = (o2 == null || (o2 != null && o2.get(col) == null)) ? null
						: o2.get(col);
				if (s1 == null && s2 == null) {
					return 0;
				} else if (s1 == null && s2 != null) {
					return -1;
				} else if (s1 != null && s2 == null) {
					return 1;
				}
				return 0;
			}

			@Override
			public String toString() {
				return "Button: no comparison";
			}
		};
	}

	/**
	 * Extract the value stored in a {@link net.catode.table.style.I_FormatCell}
	 * object.
	 * 
	 * @param o
	 *            the object.
	 * @return the value extracted.
	 */
	public static Object value(Object o) {
		if (o instanceof I_StyleObject) {
			return ((I_StyleObject) o).getValue();
		}
		return o;
	}
}
