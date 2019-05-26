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
package it.tnnfnc.datamodel;

import java.util.Comparator;


/**
 * This class implements insertion sort.
 * 
 * @param <T>
 *            the type of the data model entry. The type is the same for the
 *            comparator used for model entry comparison.
 * 
 * @author Franco Toninato
 * 
 */
public class InsertionSort<T> extends IndexAbstractSorter<T> {

	public InsertionSort(I_IndexModel<T> index) {
		super(index);
	}

	@Override
	public void sort(Comparator<T> cmp, SortOrder sortOrder) {
		int ord = 0;
		switch (sortOrder) {
		case ASCENDING:
			ord = 1;
			break;
		case DESCENDING:
			ord = -1;
			break;
		default:
			return;
		}
		for (int j = 1; j < index.getActiveSize(); j++) {
			// get the first object
			int ind = index.indexOf(j);
			T key = index.getEntry(j);
			// Compares to the second, third,...
			int i = j - 1;
			while (i >= 0 && ord * cmp.compare(index.getEntry(i), key) > 0) {
				index.changeIndex(i + 1, index.indexOf(i));
				i--;
			}
			index.changeIndex(i + 1, ind);
		}

	}

}
