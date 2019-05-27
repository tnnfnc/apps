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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * The class save a JTable layout, the visible columns, their order and width,
 * into a Properties object.
 * 
 * @author Franco Toninato
 * 
 */
public class TableProperties {
	public static final String COLUMN = "tableColumn";

	// private JTable table;
	// private Properties properties;

	private TableProperties(JTable t) {
		// this.table = t;
		// properties = new Properties();
	}

	/**
	 * Save the table layout into a Properties object.
	 * 
	 * @param table
	 *            the table.
	 * @param p
	 *            the properties.
	 */
	public static void saveTableColumnsProperties(JTable table, Properties p) {
		// It works!!
		for (Enumeration<?> keys = p.propertyNames(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			if (key.toString().startsWith(getColumnKey(""))) {
				p.remove(key);
			}

		}

		TableColumnModel columnModel = table.getColumnModel();
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			TableColumn column = columnModel.getColumn(i);
			p.setProperty(getColumnKey(column.getIdentifier()), i + ";"
					+ column.getWidth() + "" + ";" + "true");
		}
	}

	/**
	 * Get the property key from the column identifier.
	 * 
	 * @param columnID
	 *            the table column identifier.
	 * @return the property key from the column name.
	 */
	public static String getColumnKey(Object columnID) {
		return COLUMN + "." + columnID + "";
	}

	/**
	 * Get the saved layout from the properties and apply to the table.
	 * 
	 * @param table
	 *            the table.
	 * @param p
	 *            the properties.
	 */
	public static void applyTableColumnsProperties(JTable table, Properties p) {

		TableColumnModel columnModel = table.getColumnModel();
		Hashtable<Integer, TableColumn> tableColumns = new Hashtable<Integer, TableColumn>();
		ArrayList<TableColumn> removedColumns = new ArrayList<TableColumn>();
		for (int j = 0; j < columnModel.getColumnCount(); j++) {
			TableColumn t = columnModel.getColumn(j);
			String value = p.getProperty(getColumnKey(t.getIdentifier()));
			if (value != null) {
				t.setPreferredWidth(parseWidth(value));
				tableColumns.put(Integer.valueOf(parsePosition(value)), t);
			} else {
				removedColumns.add(t);
			}
		}

		// Prepare the table column
		int i = 0;
		TableColumn t = null;
		while ((t = tableColumns.get(Integer.valueOf(i++))) != null) {
			columnModel.moveColumn(
					columnModel.getColumnIndex(t.getIdentifier()), i);
		}

		// Remove doubled
		for (TableColumn tableColumn : removedColumns) {
			columnModel.removeColumn(tableColumn);
		}
	}

	private static int parsePosition(String property) {
		// System.out
		// .println("TableProperties.getTableColumnsSettings() Column dump: id= "
		// + COLUMN + "." + property);
		try {
			return toInteger(property.split(";")[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}

	private static int parseWidth(String property) {
		try {
			return toInteger(property.split(";")[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}

	/**
	 * @param n
	 * @return
	 */
	private static int toInteger(String n) {
		int i = 0;
		try {
			i = Integer.parseInt(n);
		} catch (NumberFormatException e) {
		}
		return i;
	}
	// private static boolean parseEdit(String property) {
	// // System.out
	// // .println("TableProperties.getTableColumnsSettings() Column dump: id= "
	// // + COLUMN + "." + property);
	// try {
	// return Utility.toBoolean(property.split(";")[2]);
	// } catch (ArrayIndexOutOfBoundsException e) {
	// return true;
	// }
	// }

	// private void getTableRowsSettings() {
	// properties.setProperty("table.rows.height", table.getRowHeight() + "");
	// System.out.println("Rows height: id= " + table.getRowHeight());
	// }
	//
	// private void getTableSettings() {
	// properties.setProperty("table.height", table.getHeight() + "");
	// properties.setProperty("table.width", table.getWidth() + "");
	// System.out.println("table.height:= " + table.getHeight());
	// System.out.println("table.width:= " + table.getWidth());
	// }
	//
	// /**
	// * Returns the max height of the cells in a table column. The methods
	// takes
	// * into account only the visible columns in the table row.
	// *
	// * @param column
	// * the table column
	// * @return the max height
	// */
	// private int cellMaxHeight(TableColumn column, int col) {
	// /*
	// * The methods takes into account only the visible columns in the table
	// * row
	// */
	// int height = 0;
	// int maxHeight = 0;
	// for (int r = 0; r < table.getRowCount(); r++) {
	// TableCellRenderer renderer = table.getCellRenderer(r, col);
	// Component comp = renderer.getTableCellRendererComponent(table,
	// table.getValueAt(r, col), false, false, r, col);
	// height = comp.getPreferredSize().height;
	// maxHeight = height > maxHeight ? height : maxHeight;
	// }
	// return maxHeight;
	// }
}
