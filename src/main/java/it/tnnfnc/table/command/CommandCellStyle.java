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

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import it.tnnfnc.apps.application.ui.StyleChooserDialog;
import it.tnnfnc.apps.application.ui.style.ComponentStyle;
import it.tnnfnc.apps.application.ui.style.I_StyleObject;

/**
 * Command for changing the style of selected cells in a table supporting the
 * format characteristic.
 * 
 * @author franco toninato
 * 
 */
public class CommandCellStyle extends TableCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ACTION_COMMAND = "CellStyle";
	private StyleChooserDialog chooser;

	/**
	 * Create a command.
	 * 
	 * @param path
	 *            command name.
	 * @param t
	 *            the table.
	 */
	public CommandCellStyle(JTable t) {
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
	public CommandCellStyle(String name, JTable t) {
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
	public CommandCellStyle(String name, Icon icon, JTable t) {
		super(name, icon, t);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int rs[] = table.getSelectedRows();
		int cs[] = table.getSelectedColumns();
		if ((rs == null || rs.length == 0) || (cs == null || cs.length == 0)) {
			// Do nothing
			return;
		}
		// Lazy initialization for performances
		if (chooser == null)
			chooser = new StyleChooserDialog(table);

		int row = rs[rs.length - 1] < table.getRowCount() ? rs[rs.length - 1]
				: table.getRowCount() - 1;
		int col = cs[cs.length - 1] < table.getColumnCount() ? cs[cs.length - 1]
				: table.getColumnCount() - 1;
		if (table.getValueAt(row, col) instanceof I_StyleObject) {
			I_StyleObject o = ((I_StyleObject) table.getValueAt(row, col));
			String style = o.getStyle();
			if (style == null)
				style = ComponentStyle.getComponentStyle(table);
			if ((style = chooser.showDialog(style)) != null) {
				for (int r : rs) {
					if (r < table.getRowCount()) {
						for (int c : cs) {
							if (c < table.getColumnCount()) {
								try {
									o = ((I_StyleObject) table.getValueAt(r,
											c));
									o.setStyle(style);
								} catch (Exception e) {
									messageException();
								}
							}
						}
					}
				}
				table.repaint();
			}
		} else {
			messageException();
		}

	}

	/**
	 * @throws HeadlessException
	 */
	private void messageException() throws HeadlessException {
		JOptionPane.showMessageDialog(table,//
				language.getString("Style is not supported"),//
				language.getString("Format exception"),//
				JOptionPane.INFORMATION_MESSAGE);//
	}
}
