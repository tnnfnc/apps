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

import java.awt.Component;
import java.io.Serializable;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import it.tnnfnc.table.cell.ButtonValue;
import it.tnnfnc.table.cell.CellButtonEditor;
import it.tnnfnc.table.cell.CellButtonRenderer;
import it.tnnfnc.table.cell.CellDateEditor;
import it.tnnfnc.table.cell.CellProgressBarEditor;
import it.tnnfnc.table.cell.CellProgressBarRenderer;
import it.tnnfnc.table.cell.CellRankStarEditor;
import it.tnnfnc.table.cell.CellRankStarRenderer;
import it.tnnfnc.table.cell.RangedValue;
import it.tnnfnc.table.cell.RankStarValue;
import it.tnnfnc.table.cell.StyleCellEditor;
import it.tnnfnc.table.cell.StyleCellRenderer;

/**
 * 
 * @author Franco Toninato
 */
public class StyleTable extends JTable implements Serializable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent event) {
		// Good performances, but insensible to format or value change
		// System.out.println("Dressedtable-tablechanged" + event.getType());
		super.tableChanged(event);
		for (int i = 0; i < this.getRowCount(); i++) {
			int rh = rowMaxHeight(i);
			if (getRowHeight(i) != rh) {
				setRowHeight(i, rh);
			}
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the table and define the defaults editors and renderers.
	 */
	public StyleTable() {
		// Add editors
		this.setDefaultEditor(Date.class, new CellDateEditor());
		this.setDefaultEditor(ButtonValue.class, new CellButtonEditor());
		this.setDefaultEditor(RankStarValue.class, new CellRankStarEditor());
		this.setDefaultEditor(RangedValue.class, new CellProgressBarEditor());
		// Renderers
		this.setDefaultRenderer(ButtonValue.class, new CellButtonRenderer());
		this.setDefaultRenderer(RankStarValue.class, new CellRankStarRenderer());

		this.setDefaultRenderer(RangedValue.class,
				new CellProgressBarRenderer());
	}

	/**
	 * Returns the max width of the cells in a table column. If the width is
	 * equal or less than 0 it returns the default width.
	 * 
	 * @param col
	 *            the table column.
	 * 
	 * @return the width of the column header.
	 */
	protected int columnMaxWidth(TableColumn col) {
		// TableColumn get the component, then get its preferred size
		int c = col.getModelIndex();
		int width = 0;
		int maxWidth = 0;
		Component comp;
		TableCellRenderer renderer = col.getCellRenderer();
		for (int r = 0; r < this.getRowCount(); r++) {
			comp = renderer.getTableCellRendererComponent(this,
					this.getValueAt(r, c), false, false, r, c);
			width = comp.getPreferredSize().width;
			maxWidth = width > maxWidth ? width : maxWidth;
		}
		return maxWidth;
	}

	/**
	 * Returns the max height of the cells in a table column. The methods takes
	 * into account only the visible columns in the table row.
	 * 
	 * @return the max height.
	 */
	protected int rowMaxHeight(int row) {
		/*
		 * The methods takes into account only the visible columns in the table
		 * row.
		 */
		int height = 0;
		int maxHeight = 0;
		for (int r = 0; r < getColumnCount(); r++) {
			TableCellRenderer renderer = super.getCellRenderer(row, r);
			Component comp = renderer.getTableCellRendererComponent(this,
					getValueAt(row, r), false, false, row, r);
			height = comp.getPreferredSize().height;
			maxHeight = height > maxHeight ? height : maxHeight;
		}
		if (maxHeight == 0) {
			maxHeight = getRowHeight();
		}
		return maxHeight;
	}

	/**
	 * Get the default editor for the type wrapped with a style.
	 * 
	 * @param columnClass
	 *            the type of data that must be edited.
	 * @see javax.swing.JTable#getDefaultEditor(java.lang.Class)
	 */
	@Override
	public TableCellEditor getDefaultEditor(Class<?> columnClass) {
		return new StyleCellEditor(super.getDefaultEditor(columnClass));
	}

	/**
	 * Get the default renderer for the type wrapped with a style.
	 * 
	 * @param columnClass
	 *            the type of data that must be rendered.
	 * @see javax.swing.JTable#getDefaultEditor(java.lang.Class)
	 */
	@Override
	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
		return new StyleCellRenderer(super.getDefaultRenderer(columnClass));
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return super.getCellRenderer(row, column);
	}


}