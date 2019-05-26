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
 * Abstract class provides sorting capability. Implementations of this class
 * provide different sorting strategies for the data model entries.
 * 
 * @param <T>
 *            the type of the data model entry. The type is the same for the
 *            comparator used for model entry comparison.
 * 
 * @author Franco Toninato
 */
public abstract class IndexAbstractSorter<T> {

	/**
	 * The index of the data model.
	 */
	protected I_IndexModel<T> index;

	/**
	 * Class constructor.
	 * 
	 * @param index
	 *            the index of the data model.
	 */
	public IndexAbstractSorter(final I_IndexModel<T> index) {
		this.index = index;
	}

	/**
	 * Perform sorting. Subclasses must implement different sorting strategies.
	 * 
	 * @param cmp
	 *            comparator.
	 * @param sortOrder
	 *            order type.
	 */
//	public abstract void sort(Comparator<? super T> cmp, SortOrder sortOrder);
	public abstract void sort(Comparator<T> cmp, SortOrder sortOrder);
}
