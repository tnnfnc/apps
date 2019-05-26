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
 * A model for indexed data. Every item has a unique inner numeric index and an
 * outer external index.
 * 
 * @author Franco Toninato
 * 
 * @param <T>
 *            a model item data type.
 */
public interface I_IndexModel<T> {

	/**
	 * Add a data listener.
	 * 
	 * @param listener
	 *            the data change listener.
	 */
	void addDataChangeListener(I_DataChangeListener listener);

	/**
	 * Remove a data listener.
	 * 
	 * @param listener
	 *            the data change listener.
	 */
	void removeDataChangeListener(I_DataChangeListener listener);

	/**
	 * Set the active size.
	 * 
	 * @param s
	 *            the number of the active items in the index.
	 */
	void setActiveSize(int s);

	/**
	 * Convert the view index into the model internal index. The same as
	 * modelIndex.
	 * 
	 * @param index
	 *            the view index.
	 * @return the model internal index.
	 */
	int indexOf(int index);

	/**
	 * Remove entries from the index in the view coordinates reference.
	 * 
	 * @param startFrom
	 *            first item.
	 * @param endAt
	 *            last removed item.
	 * @return the array of model indexes removed from the index.
	 */
	T[] removeAt(int startFrom, int endAt);

	/**
	 * Clear all entries from the model.
	 */
	void clear();

	/**
	 * Moves one or more rows from the inclusive range start to end to the to
	 * position in the model. At changing of rows position the sorted order
	 * becomes the new current order. If the interval is negative it is
	 * reversed.
	 * 
	 * @param startSelection
	 *            index of the first row selection in the coordinate system of
	 *            the view.
	 * @param endSelection
	 *            index of the last row selection in the coordinate system of
	 *            the view.
	 * @param target
	 *            index of the destination row in the coordinate system of the
	 *            view.
	 */
	void move(int startSelection, int endSelection, int target);

	/**
	 * Get the view index from the model index. Avoid using this method for
	 * performance reasons.
	 * 
	 * @param modelIndex
	 *            the model index.
	 * @return the view index from the internal model's entry position.
	 */
	int getIndex(int modelIndex);

	/**
	 * Get the size of the active entries of the view.
	 * 
	 * @return the number of active entries in the index.
	 */
	int getActiveSize();

	/**
	 * Get the size of the index. This can differ from the active size when a
	 * filter is active.
	 * 
	 * @return the index size.
	 */
	int getFullSize();

	/**
	 * Build the index from the data model. All previous index history is lost
	 * and the current index is initialized from the current model entries.
	 * 
	 * @param modelSize
	 *            the size of the data model.
	 */
	void buildIndex(int modelSize);

	/**
	 * Change the index for a model entry. This method does not shot any event.
	 * 
	 * @param index
	 *            the index where the model's entry reference is stored.
	 * @param modelIndex
	 *            the internal model's entry index.
	 */
	void changeIndex(int index, int modelIndex);

	/**
	 * Get an item out of the data model.
	 * 
	 * @param index
	 *            the view index.
	 * @return the data item at the index position.
	 */
	T getEntry(int index);

	/**
	 * Set an item into the data model. The index is updated accordingly.
	 */
	void addEntry(T dataItem);

	/**
	 * Insert an item into the model. The index is updated accordingly.
	 * 
	 * @param index
	 *            the index.
	 * @param dataItem
	 *            the new element.
	 * 
	 * @return the current index.
	 */
	int insertEntry(int index, T dataItem);

	/**
	 * Replace an existing data item with a new one.
	 * 
	 * @param index
	 *            the view index.
	 * @return the previous data item.
	 */
	T setEntry(int index, T item);

	/**
	 * Delete all data items.
	 * 
	 */
	void clearAll();

	/**
	 * Get a copy of this index.
	 * 
	 * @return a copy of this index.
	 */
	I_IndexModel<T> copy();

	/**
	 * Get an array of the active elements of the model in the right order.
	 * 
	 * @param a
	 *            the array type.
	 * 
	 * @return the array of active model entries.
	 */
	T[] toArray(T[] a);

	/**
	 * Filter the model. Does not shot any table event.
	 * 
	 * @param cmp
	 * @param match
	 */
	void filter(Comparator<T> cmp, T match);

	/**
	 * Sort the model. Does not shot any table event.
	 * 
	 * @param cmp
	 *            the comparator.
	 * @param order
	 *            the sort order.
	 */
	void sort(Comparator<T> cmp, SortOrder sortOrder);

	/**
	 * Sort the model and filter out the multiple occurrences.
	 * 
	 * @param cmp
	 *            the comparator.
	 * @param order
	 *            the sort order.
	 */
	void getDistincts(Comparator<T> cmp, SortOrder order);
}