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
package it.tnnfnc.datamodel.search;

import javax.swing.DefaultListModel;

/**
 * This class implements a search result content for a list.
 * 
 * @author Franco Toninato
 * 
 */
public class MatchResultModel extends DefaultListModel<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @author franco toninato
	 * 
	 */
	public static class ItemType {
		/**
		 * The matched string.
		 */
		public StringBuffer match;
		/**
		 * The object where the match was found.
		 */
		public Object source;

		/**
		 * Create a new item.
		 * 
		 * @param result
		 *            the matched string.
		 * @param source
		 *            the object where the match was found.
		 */
		public ItemType(StringBuffer result, Object source) {
			this.match = result;
			this.source = source;
		}

		@Override
		public String toString() {
			return match.toString();
		}
	};

	/**
	 * Get the total valid matches. Matches with null sources are discarded.
	 * 
	 * @return the total match numbers.
	 */
	public int getTotal() {
		int i = 0;
		for (int j = 0; j < this.getSize(); j++) {
			if (getSourcetAt(j) != null) {
				i++;
			}
		}
		return i;
	}

	/**
	 * Get the source.
	 * 
	 * @param index
	 *            the model index.
	 * @return the source.
	 */
	public Object getSourcetAt(int index) {
		return ((ItemType) get(index)).source;
	}

	/**
	 * Get the text.
	 * 
	 * @param index
	 *            the model index.
	 * @return the result text found.
	 */
	public StringBuffer getResultAt(int index) {
		return ((ItemType) get(index)).match;
	}

	/**
	 * Adds the match to the end of this list.
	 * 
	 * @param result
	 *            the matched string.
	 * @param source
	 *            the object where the match was found.
	 */
	public void addElement(StringBuffer match, Object source) {
		addElement(new MatchResultModel.ItemType(match, source));
	}

}
