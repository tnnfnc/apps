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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 * This class implements a table cell component basic editor . After the cell
 * editor is installed, all listeners registered with this object are notified
 * with the corresponding table row and column indexes.
 * 
 * @author Franco Toninato
 */
public abstract class CellEditor extends AbstractCellEditor implements
		TableCellEditor {

	private static final long serialVersionUID = 1L;
	/**
	 * Current cell value.
	 */
	protected Object value;
	/**
	 * Mouse clicks needed to start editing.
	 */
	protected int clickCountToStart = 2;
	/**
	 * Fired event.
	 */
	private ChangeEvent changeEvent;
	/**
	 * The table cell position.
	 */
	protected Point cell = new Point(-1, -1);
	/**
	 * The table.
	 */
	protected JTable table;

	/**
	 * Adds a listener to the events of stopping or canceling edidting.
	 * 
	 * @param listener
	 *            the listener (the model is generally the listener).
	 */
	@Override
	public void addCellEditorListener(CellEditorListener listener) {
		listenerList.add(CellEditorListener.class, listener);
	}

	/**
	 * Stops the editing and refuses the value. The model is notified the editor
	 * was cancelled.
	 */
	@Override
	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	/**
	 * Fire a editing canceled event to cell listeners. The renderer is
	 * installed.
	 */
	@Override
	protected void fireEditingCanceled() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((CellEditorListener) listeners[i + 1])
						.editingCanceled(changeEvent);
			}
		}
	}

	/**
	 * Fire a editing stopped event to cell listeners. The renderer is
	 * installed.
	 */
	@Override
	protected void fireEditingStopped() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((CellEditorListener) listeners[i + 1])
						.editingStopped(changeEvent);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return value;
	}

	/**
	 * Returns the number of mouse clicks needed for installing the editor.
	 * 
	 * @return the number of mouse clicks.
	 */
	public int getClickCountToStart() {
		return clickCountToStart;
	}

	/**
	 * Returns the editor component.
	 * 
	 * @param table
	 *            The table.
	 *@param value
	 *            The current value from the model.
	 *@param isSelected
	 *            The selection status of the cell.
	 *@param row
	 *            The table row index.
	 *@param column
	 *            The table column index.
	 */
	@Override
	public abstract Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column);

	/**
	 * Check if the editor can starts editing according to the that event.
	 * 
	 * @param anEvent
	 *            the event supposed to trigger the editor.
	 */
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		// System.out.println(this.getClass().getName() + anEvent);
		if (anEvent instanceof MouseEvent) {
			MouseEvent e = (MouseEvent) anEvent;
			return e.getClickCount() >= getClickCountToStart();
		} else {
			return true;
		}
	}

	/**
	 * Removes an editor listeners (the model is generally the listener)
	 * 
	 * @param listener
	 *            the listener.
	 */
	@Override
	public void removeCellEditorListener(CellEditorListener listener) {
		listenerList.remove(CellEditorListener.class, listener);
	}

	/**
	 * Sets the number of mouse clicks for installing the editor.
	 * 
	 * @param count
	 *            The number of mouse click.
	 */
	public void setClickCountToStart(int count) {
		clickCountToStart = count;
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
		return true;
	}

	/**
	 * Stops the editing and updates the value. The model is notified the editor
	 * has finished.
	 * 
	 * @return true when the new value was accepted.
	 */
	@Override
	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}
}