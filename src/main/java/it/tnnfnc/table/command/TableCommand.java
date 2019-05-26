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
package it.tnnfnc.table.command;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import it.tnnfnc.datamodel.I_IndexModel;
import it.tnnfnc.table.TableLocalBundle;
import it.tnnfnc.table.row.I_TableRow;

public abstract class TableCommand extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JTable table;
	protected StringBuffer command = new StringBuffer();
	protected ListResourceBundle language = (ListResourceBundle) ResourceBundle
			.getBundle(TableLocalBundle.class.getName());

	/**
	 * Create a command.
	 * 
	 * @param t
	 *            the table.
	 */
	public TableCommand(JTable t) {
		table = t;
	}

	/**
	 * Create a command.
	 * 
	 * @param name
	 *            command name.
	 * @param t
	 *            the table.
	 */
	public TableCommand(String name, JTable t) {
		super(name);
		table = t;
	}

	/**
	 * Create a command.
	 * 
	 * @param name
	 *            command name.
	 * @param icon
	 *            command icon.
	 * @param t
	 *            the table.
	 */
	public TableCommand(String name, Icon icon, JTable t) {
		super(name, icon);
		table = t;
	}

	/**
	 * Get and clear the command log.
	 * 
	 * @return the command log.
	 */
	public String getLog() {
		String l = new String(command);
		command.delete(0, command.length());
		return l;
	}

	/**
	 * @param rows
	 * @param from
	 * @param to
	 */
	protected void commandWrite(int from, int to, int target) {
		command.append(" (" + (from + 1) + ", " + (to + 1) + ") > "
				+ (target + 1));
	}

	/**
	 * @param rows
	 * @param from
	 * @param to
	 */
	protected void setSelectedRows(int[] rows, int from, int to) {
		int d = -1;
		table.getSelectionModel().setSelectionInterval(rows[from] + d,
				rows[to] + d);
		table.getColumnModel().getSelectionModel()
				.setSelectionInterval(0, table.getColumnCount());
		table.addRowSelectionInterval(rows[from] + d, rows[to] + d);
	}

	/**
	 * @param rows
	 * @param from
	 * @param to
	 */
	protected void setSelectedRows(int from, int to) {
		if (to >= from) {
			table.getSelectionModel().setSelectionInterval(from, to);
			table.getColumnModel().getSelectionModel()
					.setSelectionInterval(0, table.getColumnCount());
			table.addRowSelectionInterval(from, to);
		}

	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected I_IndexModel<I_TableRow<?>> getModel() {
		if (table.getModel() instanceof I_IndexModel) {
			return (I_IndexModel<I_TableRow<?>>) table.getModel();
		}
		return null;
	}

	/**
	 * Scroll table up to the cell.
	 * 
	 * @param row
	 *            table row.
	 * @param column
	 *            table column.
	 */
	protected void scrollTable(int row, int column) {
		try {
			if (row < table.getModel().getRowCount()) {
				Rectangle rect = table.getCellRect(row, column, true);
				table.scrollRectToVisible(rect);
			} else {
				// Nothing to do here
			}
		} catch (IllegalArgumentException e) {
			// Nothing to do here
		}
	}

	/**
	 * Returns the max height of the cells in a table row. The methods takes
	 * into account only the visible columns in the table row.
	 * 
	 * @param row
	 *            the table row.
	 * @return the max height.
	 */
	protected int cellMaxHeight(int row) {
		/*
		 * The methods takes into account only the visible columns in the table
		 * row.
		 */
		int height = 0;
		int maxHeight = 0;
		for (int r = 0; r < table.getColumnCount(); r++) {
			TableCellRenderer renderer = table.getCellRenderer(row, r);
			Component comp = renderer.getTableCellRendererComponent(table,
					table.getValueAt(row, r), false, false, row, r);
			height = comp.getPreferredSize().height;
			maxHeight = height > maxHeight ? height : maxHeight;
		}
		if (maxHeight == 0) {
			maxHeight = table.getRowHeight();
		}
		return maxHeight;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}
}
