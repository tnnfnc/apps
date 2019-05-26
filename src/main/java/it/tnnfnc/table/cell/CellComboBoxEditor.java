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
package it.tnnfnc.table.cell;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * @author franco toninato
 * 
 */
public class CellComboBoxEditor<T> extends CellEditor implements
		TableCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<?> combo;

	// private DefaultComboBoxModel<T> listData;

	// private ArrayList<Object> listData;

	/**
	 * Share the listData.
	 * 
	 * @param categoryModel
	 */
	public CellComboBoxEditor(DefaultComboBoxModel<T> categoryModel) {
		combo = new JComboBox<T>(categoryModel);
		JTextField dim = new JTextField(10);
		combo.setPreferredSize(dim.getPreferredSize());
		combo.addItemListener(new ComboListener());
		// this.listData = categoryModel;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.value = value;
		if (value != null) {
			if (table == null) {
				return null;
			}

			combo.setSelectedItem(value);
		}
		return combo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.table.cell.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return combo.getSelectedItem();
	}

	private class ComboListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			getCellEditorValue();
			stopCellEditing();
		}

	}

}
