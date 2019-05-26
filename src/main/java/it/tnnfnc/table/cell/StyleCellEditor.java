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
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.ui.style.StyleObject;

public class StyleCellEditor extends AbstractStyleCellRenderer implements
		TableCellEditor {

	/**
	 * Editor cellEditor.
	 */
	protected TableCellEditor cellEditor;

	/**
	 * Current cell value passed to the editor when installed.
	 */
	protected Object value;

	/**
	 * @param editor
	 *            the editor cellEditor.
	 */
	public StyleCellEditor(TableCellEditor editor) {
		this.cellEditor = editor;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.value = value;
		Component component = cellEditor.getTableCellEditorComponent(table,
				valueOf(value), isSelected, row, column);
		// if (value instanceof I_StyleObject<?>)
		// formatter.applyStyle(component, ((I_StyleObject<?>) value)
		// .getStyle(), isSelected, true);
		return component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		if (value instanceof I_StyleObject) {
			I_StyleObject s = (I_StyleObject) value;
			// System.out.println("StyleCellEditor " + s.getValue());
			// s.set(cellEditor.getCellEditorValue(), s.getStyle());
			// New object to prevent update of table model
			s = new StyleObject(cellEditor.getCellEditorValue(), s
					.getStyle());
			// System.out.println("StyleCellEditor " + s.getValue());
			return s;
		}
		return cellEditor.getCellEditorValue();
	}

	/**
	 * Adds a listener to the events of stopping or canceling edidting.
	 * 
	 * @param listener
	 *            the listener (the model is generally the listener).
	 */
	@Override
	public void addCellEditorListener(CellEditorListener listener) {
		cellEditor.addCellEditorListener(listener);
	}

	/**
	 * Removes an editor listeners (the model is generally the listener)
	 * 
	 * @param listener
	 *            the listener.
	 */
	@Override
	public void removeCellEditorListener(CellEditorListener listener) {
		cellEditor.removeCellEditorListener(listener);
	}

	/**
	 * Check if the editor can starts editing according to the that event.
	 * 
	 * @param anEvent
	 *            the event supposed to trigger the editor.
	 */
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return cellEditor.isCellEditable(anEvent);
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
		return cellEditor.shouldSelectCell(anEvent);
	}

	@Override
	public void cancelCellEditing() {
		cellEditor.cancelCellEditing();
	}

	@Override
	public boolean stopCellEditing() {
		return cellEditor.stopCellEditing();
	}
}
