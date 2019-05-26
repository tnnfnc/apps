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
package it.tnnfnc.table;

import java.awt.Rectangle;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;

import it.tnnfnc.datamodel.I_HyperLinkListener;
import it.tnnfnc.datamodel.LinkEvent;

/**
 * Scroll a table to have a cell displayed.
 * 
 * @author franco toninato
 * 
 */
public class HyperLinkTableListener implements I_HyperLinkListener {

	private JTable table;
	private ListResourceBundle appsLanguage = (ListResourceBundle) ResourceBundle
			.getBundle(TableLocalBundle.class.getName());

	public HyperLinkTableListener(JTable t) {
		this.table = t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.I_HyperLinkListener#toRef(net.catode.model
	 * .HyperLinkEvent)
	 */
	@Override
	public void link(LinkEvent event) {
		// System.out.println(this.getClass().getName() + " "+ table_row + ", "
		// + table_col);

		TableModelEvent e = (TableModelEvent) event.getTarget();
		int table_row = e.getFirstRow();
		int table_col = e.getColumn();
		table_col = table.getColumnModel().getColumnCount() - 1;
		scrollTo(table_row, table_col);

		if (table_row > table.getRowCount()
				|| table_col > table.getColumnCount()) {
			JOptionPane.showMessageDialog(table, appsLanguage
					.getString("Broken link"), appsLanguage
					.getString("Browsing exception"),
					JOptionPane.WARNING_MESSAGE);
			return;
		} else {
			table.clearSelection();
			// table.setCellSelectionEnabled(true);
			table.getSelectionModel()
					.setSelectionInterval(table_row, table_row);
			table.getColumnModel().getSelectionModel().setSelectionInterval(0,
					table_col);
			table.addRowSelectionInterval(table_row, table_row);
		}
	}

	/**
	 * Scroll a table to have a cell displayed.
	 * 
	 * @param row
	 *            the row index.
	 * @param column
	 *            the column index.
	 */
	private void scrollTo(int row, int column) {
		table.requestFocusInWindow();
		if (row < table.getModel().getRowCount()) {
			Rectangle rect = table.getCellRect(row, column, true);
			table.scrollRectToVisible(rect);
		} else {
			// do nothing
		}
	}

}
