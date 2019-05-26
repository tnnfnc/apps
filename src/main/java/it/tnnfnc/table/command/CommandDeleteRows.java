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

/**
 * Command to remove rows from a table.
 * 
 * @author franco toninato
 * 
 */
public class CommandDeleteRows extends TableCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ACTION_COMMAND = "DeleteRows";

	/**
	 * Create a command.
	 * 
	 * @param path
	 *            command name.
	 * @param t
	 *            the table.
	 */
	public CommandDeleteRows(JTable t) {
		super(t);
	}

	/**
	 * Create a command.
	 * 
	 * @param name
	 *            command name.
	 * @param t
	 *            the table.
	 */
	public CommandDeleteRows(String name, JTable t) {
		super(name, t);
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
	public CommandDeleteRows(String name, Icon icon, JTable t) {
		super(name, icon, t);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int rows[] = table.getSelectedRows();
		// int cs[] = table.getSelectedColumns();
		I_IndexModel<?> model = getModel();
		command.append(this.getValue(NAME));
		if (rows != null && rows.length > 0 && model != null) {
			int from = 0, to = 0, gap = 0;
			int at = rows[0];
			
			for (int i = 0; i < rows.length; i++) {
				if (i + 1 < rows.length && rows[i + 1] - rows[i] == 1) {
					to = i + 1;
					commandWrite(rows[from] - gap, rows[to] - gap, -1);
				} else {
					commandWrite(rows[from] - gap, rows[to] - gap, -1);
					model.removeAt(rows[from] - gap, rows[to] - gap);
					gap += to - from + 1;
					from = i + 1;
					to = from;
				}
			}
			
			at = at < table.getRowCount() ? at : table.getRowCount() - 1;
			if (at > -1) {
				table.getSelectionModel().setSelectionInterval(at, at);
				table.addRowSelectionInterval(at, at);
				table.getColumnModel().getSelectionModel()
						.setSelectionInterval(0, table.getColumnCount());
			}
		}
	}

}
