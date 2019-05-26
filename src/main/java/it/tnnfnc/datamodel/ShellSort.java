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
/**
 * 
 */
package it.tnnfnc.datamodel;

import java.util.Comparator;


/**
 * This class implements Shell sorting with the following grouping; 1391376,
 * 463792, 198768, 86961, 33936, 13776, 4592, 1968, 861, 336, 112, 48, 21, 7, 3,
 * 1.
 * 
 * @param <T>
 *            the type of the data model entry. The type is the same for the
 *            comparator used for model entry comparison.
 * 
 * @author Franco Toninato
 * 
 */
public class ShellSort<T> extends IndexAbstractSorter<T> {

	private static int groups[] = { 1391376, 463792, 198768, 86961, 33936,
			13776, 4592, 1968, 861, 336, 112, 48, 21, 7, 3, 1 };

	public ShellSort(I_IndexModel<T> index) {
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

		for (int g = 0; g < groups.length; g++) {
			for (int j = groups[g]; j < index.getActiveSize(); j++) {
				int ind = index.indexOf(j);
				T key = index.getEntry(j);
				int i = j;
				/*
				 * The period is groups[g] so the number of elements per group
				 * is n / group[g]
				 */
				while (i >= groups[g]
						&& ord
								* cmp.compare(index.getEntry(i - groups[g]),
										key) > 0) {
					// System.out.println("swap = " + i);
					index.changeIndex(i, index.indexOf(i - groups[g]));
					i = i - groups[g];
				}
				index.changeIndex(i, ind);
			}
		}
	}

}
