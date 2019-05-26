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

public class CommandMoveRowsUp extends TableCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ACTION_COMMAND = "MoveRowsUp";

	/**
	 * Create a command.
	 * 
	 * @param path
	 *            command name.
	 * @param t
	 *            the table.
	 */
	public CommandMoveRowsUp(JTable t) {
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
	public CommandMoveRowsUp(String name, JTable t) {
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
	public CommandMoveRowsUp(String name, Icon icon, JTable t) {
		super(name, icon, t);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int rows[] = table.getSelectedRows();
		if ((rows == null || rows.length == 0)) {
			return;// Do nothing
		}
		super.command.append(this.getValue(NAME));
		I_IndexModel<?> model = getModel();
		if (rows != null && rows.length > 0 && rows[0] > 0 && model != null) {
			table.getSelectionModel().clearSelection();

			int from = 0, to = 0;
			for (int i = 0; i < rows.length; i++) {
				if (i + 1 < rows.length && rows[i + 1] - rows[i] == 1) {
					to = i + 1;
					commandWrite(rows[from], rows[to], rows[from] - 1);
				} else {
					commandWrite(rows[from], rows[to], rows[from] - 1);
					model.move(rows[from], rows[to], rows[from] - 1);
					setSelectedRows(rows, from, to);
					from = i + 1;
					to = from;
				}
			}
			scrollTable(rows[0] - 1, 0);
		}
	}

	@Override
	protected void setSelectedRows(int[] rows, int from, int to) {
		int d = -1;
		table.getSelectionModel().setSelectionInterval(rows[from] + d,
				rows[to] + d);
		table.getColumnModel().getSelectionModel()
				.setSelectionInterval(0, table.getColumnCount());
		table.addRowSelectionInterval(rows[from] + d, rows[to] + d);
	}

}
