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

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JTable;

import it.tnnfnc.datamodel.I_IndexModel;
import it.tnnfnc.table.row.I_RowFactory;
import it.tnnfnc.table.row.I_TableRow;

/**
 * Command to insert rows into a table.
 * 
 * @author franco toninato
 * 
 * @param <T>
 */
public class CommandInsertRows<T extends I_TableRow<?>> extends
		TableCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ACTION_COMMAND = "InsertRows";
	private I_RowFactory<T> rowfactory;

	/**
	 * Create a command.
	 * 
	 * @param <T>
	 *            the row type.
	 * @param path
	 *            command name.
	 * @param t
	 *            the table.
	 * @param f
	 *            the row factory.
	 */
	public CommandInsertRows(JTable t, I_RowFactory<T> f) {
		super(t);
		setRowFactory(f);
	}

	/**
	 * Create a command.
	 * 
	 * @param name
	 *            command name.
	 * @param t
	 *            the table.
	 * @param f
	 *            the row factory.
	 */
	public CommandInsertRows(String name, JTable t, I_RowFactory<T> f) {
		super(name, t);
		setRowFactory(f);
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
	 * @param f
	 *            the row factory.
	 */
	public CommandInsertRows(String name, Icon icon, JTable t, I_RowFactory<T> f) {
		super(name, icon, t);
		setRowFactory(f);
	}

	/**
	 * Set the row factory object.
	 * 
	 * @param f
	 *            the row factory object.
	 */
	public void setRowFactory(I_RowFactory<T> f) {
		rowfactory = f;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// I_IndexModel<I_TableRow<?>> model =
		// I_IndexModel.class.cast(getModel());
		int rows[] = table.getSelectedRows();
		I_IndexModel<I_TableRow<?>> model = getModel();

		command.append(this.getValue(NAME));
		try {
			I_TableRow<?> row = rowfactory.newRow();
			int at = model.getActiveSize();

			super.command.append(" (" + model.getActiveSize() + ")");
			if (rows != null && rows.length > 0) {
				// at = rows[rows.length - 1] + 1;
				at = rows[rows.length - 1];
				model.insertEntry(at, row);
			} else {
				model.addEntry(row);
			}
			// System.out.println(at + " table rows = " +table.getRowCount());
			at = at < table.getRowCount() ? at : table.getRowCount() - 1;
			table.getSelectionModel().setSelectionInterval(at, at);
			table.addRowSelectionInterval(at, at);
			table.getColumnModel().getSelectionModel()
					.setSelectionInterval(0, table.getColumnCount());
		} catch (Exception e) {
			command.append(e.getMessage());
			e.printStackTrace();
		}

	}
}
