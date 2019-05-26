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
package it.tnnfnc.table.header;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import it.tnnfnc.datamodel.FilterStatus;
import it.tnnfnc.datamodel.SortOrder;
import it.tnnfnc.table.TableOperator;
import it.tnnfnc.table.TableOperatorModel;

/**
 * This class provides a default renderer with sort and filter capabilities for
 * a table header.
 * 
 * @author Franco Toninato
 * 
 */
public class HeaderRenderer extends HeaderComponent implements
		TableCellRenderer, Serializable {

	private final Color backgColor = UIManager
			.getColor("TableHeader.background");
	private final Color foregColor = UIManager
			.getColor("TableHeader.foreground");
	private final Border border = BorderFactory.createEmptyBorder();
	private final Font headerFont = UIManager.getFont("TableHeader.font");
	private TableOperatorModel tableOperatorModel;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param control
	 */
	public HeaderRenderer(TableOperatorModel control) {
		super();
		this.tableOperatorModel = control;
		// System.out.println(this.getClass().getName()
		// + " constructor - tableOperatorModel is" + tableOperatorModel);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// System.out.println(this.getClass().getName()
		// + " getTableCellRendererComponent table is" + table);
		if (table == null) {
			return this;
		}
		if (this.isOpaque() == false) {
			setOpaque(true);
		}
		// -1 = header
		if (row == -1) {
			if (tableOperatorModel != null) {
				// System.out.println(this.getClass().getName()
				//	+ " tableOperatorModel is" + tableOperatorModel);
				TableOperator tcc = tableOperatorModel.getColumnControl(table
						.getColumnModel().getColumn(column));
				filter(tcc.getFilterStatus());
				sort(tcc.getSortingOrder());
			}
			// Not supported - disable all
			else {
				filter(FilterStatus.DISABLED);
				sort(SortOrder.UNSORTABLE);
			}

			setValue(value);

			// Selection - uncommon for header
			if (isSelected) {
				super.setBackground(table.getSelectionBackground());
				super.setForeground(table.getSelectionForeground());
			} else {
				if (getBackground() != backgColor)
					super.setBackground(backgColor);
				if (getForeground() != foregColor)
					super.setForeground(foregColor);
				if (getBorder() != border)
					super.setBorder(border);
			}
			// hasFocus - uncommon for header
			if (hasFocus) {
				super.setBackground(UIManager
						.getColor("TableHeader_old.focusCellBackground"));
			} else {
				if (super.getBackground() != backgColor)
					super.setBackground(backgColor);
			}
			if (headerLabel.getFont() != headerFont) {
				headerLabel.setFont(headerFont);
			}

			return this;

		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
	 */
	protected void setValue(Object value) {
		// System.out.println(this.getClass().getName() + " setValue " + value);
		if (value == null) {
			headerLabel.setText(" ");
		} else {
			if (value instanceof Icon) {
				headerLabel.setIcon((Icon) value);
			} else {
				headerLabel.setText(value.toString());
			}
		}
	}
}
