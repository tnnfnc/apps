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

import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.datamodel.search.AbstractRegexSearch;
import it.tnnfnc.datamodel.search.I_RegexMatcher;

/**
 * This class implements a regular text pattern search through a table. Every
 * cell from the row is converted into text separated by one blank space.
 * Inserting any other characters would change the search result.
 * 
 * @author Franco Toninato
 * 
 */
public class RowRegexSearch extends AbstractRegexSearch {

	public RowRegexSearch(I_RegexMatcher s, I_TableRow<?> source) {
		super(s, source);
	}

	/**
	 * Class constructor.
	 * 
	 * @param s
	 *            the object implementing a regular expression search.
	 */

	public StringBuffer visitText(Object object) {
		StringBuffer text = new StringBuffer();
		boolean hasMoreElements = true;
		try {
			I_TableRow<?> row = (I_TableRow<?>) object;
			for (int j = 0; hasMoreElements; j++) {

				Object o = row.get(j);
				if (o instanceof I_StyleObject) {
					o = ((I_StyleObject) o).getValue();
				}
				text.append(o + " ");
			}
		} catch (Exception e) {
			hasMoreElements = false;
		}
		return text;
	}

}
