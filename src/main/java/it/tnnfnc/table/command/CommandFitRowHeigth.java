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

public class CommandFitRowHeigth extends TableCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ACTION_COMMAND = "FitRowHeigth";

	/**
	 * Create a command.
	 * 
	 * @param path
	 *            command name.
	 * @param t
	 *            the table.
	 */
	public CommandFitRowHeigth(JTable t) {
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
	public CommandFitRowHeigth(String name, JTable t) {
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
	public CommandFitRowHeigth(String name, Icon icon, JTable t) {
		super(name, icon, t);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int rows[] = table.getSelectedRows();
		if (rows != null && rows.length > 0) {
			for (int row : rows) {
				int h = cellMaxHeight(row);
				if (table.getRowHeight(row) != h)
					table.setRowHeight(row, h);
			}
		}
	}
}
