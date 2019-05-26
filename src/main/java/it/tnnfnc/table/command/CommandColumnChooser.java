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

import it.tnnfnc.table.TableColumnChooser;


/**
 * Command for changing the columns in a table.
 * 
 * @author franco toninato
 * 
 */
public class CommandColumnChooser extends TableCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ACTION_COMMAND = "ColumnChooser";
	private TableColumnChooser chooser;

	/**
	 * Create a command.
	 * 
	 * @param path
	 *            command name.
	 * @param t
	 *            the table.
	 */
	public CommandColumnChooser(JTable t) {
		super(t);
		initialize();
	}

	/**
	 * Create a command.
	 * 
	 * @param name
	 *            command name.
	 * @param t
	 *            the table.
	 */
	public CommandColumnChooser(String name, JTable t) {
		super(name, t);
		initialize();
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
	public CommandColumnChooser(String name, Icon icon, JTable t) {
		super(name, icon, t);
		initialize();
	}

	/**
	 * Initialize the column model selector.
	 */
	public void initialize() {
		if (chooser == null)
			chooser = new TableColumnChooser(table);
		chooser.refresh();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// Lazy initialization for performances
		// chooser.initialize();
		chooser.showDialog();
	}

}
