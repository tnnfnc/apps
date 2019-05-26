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
import java.util.EventObject;


/**
 * @author Franco Toninato
 * 
 */
public class SortEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SortOrder sortType;
	private Comparator<?> comparator;

	// private int row;
	// private int column;

	/**
	 * Class constructor.
	 * 
	 * @param source
	 *            the originator object.
	 * @param sortType
	 *            the sorting type.
	 * @param comparator
	 *            the comparator used for sorting.
	 * @throws IllegalArgumentException
	 *             when a wrong type is passed to the constructor.
	 */
	public SortEvent(Object source, SortOrder sortType, Comparator<?> comparator)
			throws IllegalArgumentException {
		super(source);
		this.sortType = sortType;
		this.comparator = comparator;
	}

	/**
	 * Get the sorting type.
	 * 
	 * @return the sorting type.
	 */
	public SortOrder getType() {
		return sortType;
	}

	/**
	 * Get the comparator.
	 * 
	 * @return the comparator.
	 */
	public Comparator<?> getComparator() {
		return comparator;
	}

}
