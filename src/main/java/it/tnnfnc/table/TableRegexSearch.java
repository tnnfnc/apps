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
package it.tnnfnc.table;

import java.util.regex.Pattern;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

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
public class TableRegexSearch extends AbstractRegexSearch {

	public TableRegexSearch(I_RegexMatcher s, TableModel source) {
		super(s, source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.AbstractRegexSearch#search(java.util.regex.Pattern)
	 */
	@Override
	public void search(Pattern pattern) {
		regularSearch.setPattern(pattern);
		TableModel table_model = (TableModel) source;
		StringBuffer text = new StringBuffer();
		boolean hasMoreElements = true;
		for (int i = 0; i < table_model.getRowCount(); i++) {
			// -> A row
			hasMoreElements = true;
			try {
				for (int j = 0; hasMoreElements; j++) {
					Object o_ij = table_model.getValueAt(i, j);

					if (o_ij instanceof I_StyleObject) {
						o_ij = ((I_StyleObject) o_ij).getValue();
					}
					text.append(o_ij + " ");
				}
			} catch (Exception e) {
				hasMoreElements = false;
			}
			// System.out.println(this.getClass().getName() + " text was " +
			// text);
			matchFound(regularSearch.lookAt(text), new TableModelEvent(
					table_model, i, i, table_model.getColumnCount() - 1));
			text.delete(0, text.length());
			// <-
		}
	}
}
