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
package it.tnnfnc.table.cell; //Package

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import it.tnnfnc.apps.application.ui.style.I_StyleObject;

/**
 * This class implement a a cell renderer with a specific format.
 * 
 * 
 * @author Franco Toninato
 */
public class StyleCellRenderer extends AbstractStyleCellRenderer implements
		TableCellRenderer {
	/**
	 * The renderer defined for the cell value.
	 */
	protected TableCellRenderer cellRenderer;

	/**
	 * Class constructor.
	 * 
	 * @param defaultRenderer
	 *            the default renderer for the table column class.
	 */
	public StyleCellRenderer(TableCellRenderer defaultRenderer) {
		this.cellRenderer = defaultRenderer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
	 * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component component = cellRenderer.getTableCellRendererComponent(table,
				valueOf(value), isSelected, hasFocus, row, column);
		if (value instanceof I_StyleObject)
			formatter.applyStyle(component, ((I_StyleObject) value).getStyle(),
					isSelected, hasFocus);

		return component;
	}
}