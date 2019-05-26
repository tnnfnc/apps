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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JTable;


/**
 * Implements editor and renderer for a button in a table cell. The toggle
 * button is supported.
 * 
 * @author Franco Toninato
 * 
 */
public class CellButtonEditor extends CellEditor implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// protected Action action;
	protected AbstractButton button;

	/**
	 * Create the editor from a button.
	 * 
	 * @param b
	 *            the button, it can be also a toggle button.
	 */
	public CellButtonEditor() {
		// this.action = a;
		// button = new JButton(b);
		this.button = new JButton();
		this.button.setBorderPainted(false);
		this.button.addActionListener(this);
		this.button.setOpaque(true);
		this.setClickCountToStart(1);
	}

	/**
	 * Set the button for this editor.
	 * 
	 * @param b
	 *            the button, it can be also a toggle button.
	 */
	public CellButtonEditor setButton(AbstractButton b) {
		button.removeActionListener(this);
		button = b;
		button.addActionListener(this);
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
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (performAction(event)) {
			fireEditingStopped();
		} else {
			fireEditingCanceled();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.catode.table.cell.CellEditor#getTableCellEditorComponent(javax.swing
	 * .JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.value = value;
		return button;
	}

	/**
	 * Check if the editor can starts editing according to the that event.
	 * 
	 * @param anEvent
	 *            the event supposed to trigger the editor.
	 */
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	/**
	 * Perform the action event.
	 * 
	 * @param event
	 *            the action event.
	 * @return true for stop editing, false for cancel editing.
	 */
	public boolean performAction(ActionEvent event) {
		pressButton(value);

		return true;
	}

	/**
	 * Returns if the cell must be selected as a result of this event.
	 * 
	 * @param anEvent
	 *            the event delivered to this cell.
	 *@return true when the event should perform the cell selection.
	 */
	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return false;
	}

	/**
	 * Set the button status.
	 * 
	 * @param cell
	 *            the value.
	 */
	protected void pressButton(Object cell) {

		if (cell instanceof ButtonValue) {
			ButtonValue button_val = (ButtonValue) cell;
			button_val.setSelected(!button_val.isSelected());
			button.setSelected(button_val.isSelected());
			value = button_val;
		}
	}
}
