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
package it.tnnfnc.table.cell;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


/**
 * Implements editor and renderer for a button in a table cell. The toggle
 * button is supported.
 * 
 * @author Franco Toninato
 * 
 */
public class CellButtonRenderer implements TableCellRenderer {

	// protected Action action;
	protected AbstractButton button;
	protected Object value;

	/**
	 * Create the editor from a button.
	 * 
	 * @param b
	 *            the button, it can be also a toggle button.
	 */
	public CellButtonRenderer() {
		// this.action = b;
		this.button = new JButton();
		this.button.setBorderPainted(false);
		this.button.setOpaque(true);
	}

	/**
	 * Set the button for this editor.
	 * 
	 * @param b
	 *            the button, it can be also a toggle button.
	 */
	public CellButtonRenderer setButton(AbstractButton b) {
		button = b;
		button.setBorderPainted(false);
		button.setOpaque(true);
		return this;
	}

	/**
	 * Get the button of this editor.
	 * 
	 * @return the button, it can be also a toggle button.
	 */
	public AbstractButton getButton() {
		return button;
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
		this.value = value;
		setValue(value);
		if (hasFocus) {
			button.requestFocus();
		}
		return button;
	}

	/**
	 * Set the current button status from the table.
	 * 
	 * @param cell
	 *            the value.
	 */
	protected void setValue(Object cell) {
		if (cell instanceof ButtonValue) {
			ButtonValue button_val = (ButtonValue) cell;
			button.setSelected(button_val.isSelected());
			button.setText(button_val.isSelected()+"");
		}
	}
}
