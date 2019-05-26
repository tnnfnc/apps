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

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Index of a data model.
 * 
 * @author Franco Toninato
 * 
 */
public class Index implements Cloneable {

	/**
	 * The index. A slot contains the model index as a one element integer
	 * array. The array position is the view index and the array value is the
	 * model index.
	 */
	private final ArrayList<int[]> index = new ArrayList<int[]>();
	/**
	 * The index size.
	 */
	private int activeSize = 0;
	/**
	 * The model size.
	 */
	private int modelSize = 0;

	/**
	 * Internal position
	 */
	public final static int position = 0;

	/**
	 * Get the entry index.
	 * 
	 * @param i
	 *            the index.
	 * @return the value of the index.
	 */
	public int get(int i) {
		if (0 <= i && i < index.size()) {
			return index.get(i)[Index.position];
		}
		return -1;
	}

	/**
	 * Get the full index size.
	 * 
	 * @return the full index size.
	 */
	public int getFullSize() {
		return index.size();
	}

	/**
	 * Get the model size. This includes the deleted entries.
	 * 
	 * @return the model size.
	 */
	public int getModelSize() {
		return modelSize;
	}

	/**
	 * Get the active size. This does not include the deleted entries.
	 * 
	 * @return the model size.
	 */
	public int getActiveSize() {
		return activeSize;
	}

	/**
	 * Set the active size. This does not include the deleted entries.
	 * 
	 * @param S
	 *            the new model active size.
	 */
	public void setActiveSize(int s) {
		activeSize = s;
	}

	/**
	 * Set the full index size.
	 * 
	 */
	public void setFullSize() {
		activeSize = getFullSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Index clone() {
		Index cloned = new Index();
		// cloned.command = this.command;
		for (int i = 0; i < this.index.size(); i++) {
			cloned.index.add(Index.index(this.index.get(i)[Index.position]));
		}
		cloned.activeSize = this.activeSize;
		cloned.modelSize = this.modelSize; // Fix the remove bug
		return cloned;
	}

	/**
	 * Create a new entry in the index.
	 * 
	 * @param modelIndex
	 *            the index of the data in the model.
	 * @return the index of the data in the model.
	 */
	private static int[] index(int modelIndex) {
		int i[] = new int[position + 1];
		i[position] = modelIndex;
		return i;
	}

	/**
	 * Build the index from the data model. All previous index history is lost
	 * and the current index is initialized from the current model entries.
	 * 
	 * @param modelSize
	 *            the size of the data model.
	 */
	public void initialize(int modelSize) {
		// Add to dataIndex
		for (int j = 0; j < (activeSize = modelSize); j++) {
			index.add(Index.index(j));
		}

	}

	/**
	 * Shift one block. If the interval is negative it is reversed.
	 * 
	 * @param index
	 *            the index.
	 * @param begin
	 *            index of the first array element.
	 * @param end
	 *            index of the last array element.
	 * @param to
	 *            index of the destination in the array.
	 */
	public static void shiftBlock(Index index, int begin, int end, int to) {
		int first = 0; // Start
		int last = 0; // End
		int target = 0; // Target
		// Reverse the interval
		if (end < begin) {
			first = begin;
			begin = end;
			end = first;
		}
		// Move up = move down the upper block
		if (to < begin) {
			first = to;
			last = begin - 1;
			target = end;
			// Move down
		} else if (to > end) {
			first = begin;
			last = end;
			target = to;
		} else {
			// Equals or between nothing to change
			return;
		}

		// Duplicate the index
		int distance = 0;
		int len = 0;
		int left = 1; // Only for first
		/*
		 * After shifting the block its internal order must be recovered. This
		 * can accomplished with new shifts of the sub blocks. Starting and end
		 * points are changed in a recursive way.
		 */
		while (left > 0) {
			Index.swapBlock(index, first, last, target);
			// Quotient & Rest
			distance = target - last;
			len = last - first + 1;
			left = distance % len;
			// Recalculate next block
			first = first + distance;
			last = last + distance - left;
		}
	}

	/**
	 * Shift a block along a distance. The shift breaks the block internal order
	 * because of permutations. It swaps elements starting at the element at
	 * index "starting block" with the element following the index
	 * "ending block", up to a distance (target index - ending block index).
	 * 
	 * @param index
	 *            the index.
	 * @param s
	 *            starting block index.
	 * @param e
	 *            ending block index.
	 * @param t
	 *            target index.
	 */
	public static void swapBlock(Index index, int s, int e, int t) {
		for (int i = 0; i < t - e; i++) {
			swap(index, s + i, e + i + 1);
		}
	}

	/**
	 * Append the last model entry index to the data index. If the active size
	 * is less than the full index size the it inserts the model index after the
	 * last active size.
	 * 
	 * @param index
	 *            the index.
	 * @return the index active size.
	 */
	public static int appendIndex(Index index) {
		int size = index.index.size();// fix remove bug
		// int size = index.modelSize;
		// System.out.println("Index: append " + " model size= " +
		// index.modelSize
		// + " activeSize= " + index.activeSize);
		if (index.activeSize < size) {
			index.index.add(index.activeSize, Index.index(index.modelSize));
		} else {
			index.index.add(Index.index(index.modelSize));// fix remove bug
		}
		++index.modelSize; // fix remove bug
		++index.activeSize;
		return index.activeSize;
	}

	/**
	 * Append the last model entry index to the data index. If the active size
	 * is less than the full index size the it inserts the model index after the
	 * last active size.
	 * 
	 * @param index
	 *            the index.
	 * @param i
	 *            the insertion point.
	 * @return the index active size.
	 */
	public static int insertIndex(Index index, int i) {
		int size = index.index.size();
		if (0 <= i && i < size) {
			index.index.add(i, Index.index(index.modelSize)); // fix remove bug
		} else if (i >= size) {
			index.index.add(Index.index(index.modelSize)); // fix remove bug
		}
		++index.modelSize;// fix remove bug
		++index.activeSize;
		return index.activeSize;
	}

	/**
	 * Swap the model's entry index between indexes i and j.
	 * 
	 * @param index
	 *            the index.
	 * @param i
	 *            the index at i.
	 * @param j
	 *            the index at j.
	 */
	public static void swap(Index index, int i, int j) {
		int key = index.index.get(i)[Index.position];
		index.index.get(i)[Index.position] = index.index.get(j)[Index.position];
		index.index.get(j)[Index.position] = key;
	}

	/**
	 * Remove entries from the index.
	 * 
	 * @param index
	 *            the index.
	 * @param from
	 *            first removed item.
	 * @param to
	 *            last removed item.
	 * @return the array of model indexes removed from the index.
	 */
	public static int[] remove(Index index, int from, int to) {
		// TODO the array should not be shorter than the model length
		if (to < from) {
			int temp = to;
			to = from;
			from = temp;
		}
		int items[] = new int[to - from + 1];
		for (int r = 0; r <= to - from; r++) {
			// Remove what is in the current view
			if (0 <= from && from < index.activeSize) {
				items[r] = index.index.remove(from)[Index.position];
				index.activeSize--;
			}
		}
		return items;
	}

	/**
	 * Get the index from the value.
	 * 
	 * @param index
	 *            the index.
	 * @param value
	 *            the value.
	 * @return the index corresponding to the value. When it is not found -1 is
	 *         returned.
	 */
	public static int indexFor(Index index, int value) {
		for (int i = 0; i < index.index.size(); i++) {
			if (index.index.get(i)[Index.position] == value)
				return i;
		}
		return -1;
	}

	/**
	 * 
	 * Perform sorting. Subclasses must implement different sorting strategies.
	 * 
	 * @param <T>
	 * @param cmp
	 *            comparator.
	 * @param sortOrder
	 *            order type.
	 * @param index
	 *            the data index.
	 */
	public static <T> void sort(Comparator<T> cmp, SortOrder sortOrder, I_IndexModel<T> index) {
		if (index.getFullSize() > 100)
			IndexSorterFactory.getShellSorter(index).sort(cmp, sortOrder);
		IndexSorterFactory.getInsertionSorter(index).sort(cmp, sortOrder);
	}

	/**
	 * Perform filtering against a match.
	 * 
	 * @param <T>
	 * @param cmp
	 *            comparator.
	 * @param index
	 *            the data index.
	 */
	public static <T> void filter(Comparator<T> cmp, I_IndexModel<T> index, T match) {
		/*
		 * This algorithm works like the insertion sort one. A simpler way could
		 * be swap the first matching element with the last not matching one.
		 */
		int last = -1;
		if (match == null) {
			for (int i = 0; i < index.getActiveSize(); i++) {
				// When both null they do match
				if (index.getEntry(i) == null) {
					int key = index.indexOf(i);
					int j = i - 1;
					while (j >= 0 && index.getEntry(j) != null) {
						index.changeIndex(j + 1, index.indexOf(j));
						j--;
					}
					index.changeIndex(j + 1, key);
					last = j + 1;
				}
			}
		} else {
			// When match is not null it is of type T
			for (int i = 0; i < index.getActiveSize(); i++) {
				if (match.getClass() == index.getEntry(i).getClass() && cmp.compare(index.getEntry(i), match) == 0) {
					// Found a match for the model element key at i
					// it must be shifted back before not matching elements
					int key = index.indexOf(i);
					int j = i - 1;
					// Move all non matching elements
					while (j >= 0 && match.getClass() == index.getEntry(j).getClass()
							&& cmp.compare(index.getEntry(j), match) != 0) {
						index.changeIndex(j + 1, index.indexOf(j));
						j--;
					}
					index.changeIndex(j + 1, key);
					last = j + 1;
				}
			}
		}

		index.setActiveSize(last == -1 ? 0 : last + 1);
	}

	/**
	 * Delete adjacent duplicates. Sorting is a precondition.
	 * 
	 * @param <T>
	 * @param cmp
	 *            comparator.
	 * @param index
	 *            the data index.
	 */
	public static <T> void getDistincts(Comparator<? super T> cmp, I_IndexModel<T> index) {
		int len = index.getActiveSize();
		int size = len;
		int i = 0;
		while (i < len) {
			T value = index.getEntry(i);
			for (int j = i + 1; j < len && cmp.compare(value, index.getEntry(j)) == 0;) {
				// --> move i to the end
				int b = index.indexOf(j);
				for (int k = j; k < size - 1; k++) {
					index.changeIndex(k, index.indexOf(k + 1));
				}
				index.changeIndex(size - 1, b);
				// --< move i to the end
				len--;
			}
			i++;
		}
		index.setActiveSize(i);
	}

	/**
	 * Set the new value at the position.
	 * 
	 * @param i
	 *            the index position.
	 * @param value
	 *            the new value.
	 */
	public void set(int i, int value) {
		index.get(i)[Index.position] = value;
	}
}
