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

/**
 * Table row interface suitable for being used with <code>I_IndexModel</code> .
 * A row is an array of object of type T.
 * 
 * @author Franco Toninato
 * 
 * 
 * @param <T>
 *            the object type.
 */
public interface I_TableRow<T> {
	/**
	 * Current row unique identifier or index. This index may be temporary and
	 * must be updated every time it is needed.
	 * 
	 * @return the current view position.
	 */
	Object getIdentifier();

	/**
	 * Set row unique identifier or index. This index may be temporary and must
	 * be updated every time it is needed.
	 * 
	 * @param id
	 *            current view position.
	 */
	void setIdentifier(Object id);

	/**
	 * Get the value at the column position.
	 * 
	 * @param col
	 *            the column position.
	 * @return the value at the visible column position.
	 */
	T get(int col);

	/**
	 * Set the value at the column position.
	 * 
	 * @param col
	 *            the column position.
	 * @return the old value.
	 */
	T set(int col, T value);

	/**
	 * Get the number of column of this row.
	 * 
	 * @return the number of column of this row.
	 */
	int columnCount();

	/**
	 * Convert the row into an array.
	 * 
	 * @return the array.
	 */
	T[] toArray();

	/**
	 * Get the value at the field identifier.
	 * 
	 * @param key
	 *            the field identifier.
	 * @return the value at the field identifier.
	 */
	T get(Object key);

	/**
	 * Set the value at the column identifier.
	 * 
	 * @param key
	 *            the field identifier.
	 * @param value
	 *            the field value.
	 * @return the old value.
	 */
	T set(Object key, T value);

	/**
	 * Get the row size.
	 * 
	 * @return the row size.
	 */
	int size();
}
