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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import it.tnnfnc.apps.application.ui.CalendarDialog;

/**
 * A <code>CellDateEditor</code> displays a table cell component editor as
 * spinner date.
 * 
 * @author Franco Toninato
 * @version %I%, %G%
 * @since 1.2
 */
public class CellDateEditor extends CellEditor implements TableCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton button;
	private CalendarDialog dateChooser;
	private JTable table;

	public CellDateEditor() {
		button = new JButton();
		button.addActionListener(new ThisButtonListener());
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		dateChooser = new CalendarDialog();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.table = table;
		this.value = value;

		if (value != null)
			button.setText(DateFormat.getDateInstance(DateFormat.MEDIUM)
					.format((Date) value));
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.table.cell.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return value;
	}

	/**
	 * Button listener, when pressed calls the dialog.
	 * 
	 */
	private class ThisButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			// Press the button display the dialog
			Date adate = (Date) value;
			if (value == null) {
				adate = new Date();
			}
			value = dateChooser.showCalendarDialog(table, adate);
			// System.out.println("CellDateEditor: new date to set = " + (Date)
			// value);
			stopCellEditing();
		}
	}

	/**
	 * Check if the editor can starts editing according to the that event.
	 * 
	 * @param anEvent
	 *            the event supposed to trigger the editor.
	 */
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof MouseEvent) {
			MouseEvent e = (MouseEvent) anEvent;
			return e.getClickCount() >= getClickCountToStart();
		} else {
			// button.doClick();
			return true;
		}
	}
}