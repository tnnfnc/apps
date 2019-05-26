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
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import it.tnnfnc.apps.application.ui.FiveStarsCombo;

/**
 * Edits a table cell as a text string in a progress bar. The string is
 * displayed as long as the progress bar reaches its upper limit. A timer must
 * be provided.
 * 
 * @author Franco Toninato
 */
public class CellRankStarEditor extends CellEditor implements TableCellEditor {

	private static final long serialVersionUID = 1L;

	private FiveStarsCombo fiveStarCombo;

	public CellRankStarEditor() {
		this.setClickCountToStart(1);
		fiveStarCombo = new FiveStarsCombo();
		fiveStarCombo.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				getCellEditorValue();
				stopCellEditing();
			}

		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.value = value;

		if (value != null) {
			if (table == null) {
				return fiveStarCombo;
			}
			// if (isSelected) {
			// if (fiveStarCombo.isOpaque() == false) {
			// fiveStarCombo.setOpaque(true);
			// }
			// fiveStarCombo.setBackground(table.getSelectionBackground());
			// }
		}
		return  fiveStarCombo;
	}

	@Override
	public Object getCellEditorValue() {
		if (value != null && value instanceof RankStarValue) {
			RankStarValue w = new RankStarValue();
			w.setLevel(fiveStarCombo.getSelectedIndex());
			return w;
		} else if (value == null) {
			RankStarValue w = new RankStarValue();
			w.setLevel(fiveStarCombo.getSelectedIndex());
			return w;
		} else {
			return value; // do nothing
		}
	}

	@Override
	public boolean shouldSelectCell(EventObject event) {
		return true;
	}
}
