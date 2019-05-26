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
package it.tnnfnc.table.cell; //Package

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;


/**
 * Renders a table cell as a text string in a progress bar. The string is
 * displayed as long as the progress bar reaches its upper limit. A timer must
 * be provided.
 * 
 * @author Franco Toninato
 * @version %I%, %G%
 * @since 1.2
 */
public class CellProgressBarRenderer extends JProgressBar implements
		TableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	private static final Color background = UIManager
			.getColor("ProgressBar.foreground");

	/**
	 * Returns a progress bar renderer.
	 * 
	 * @return (methods only)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		setValue(value);

		if (table == null) {
			return this;
		}
		// super.setBorder(new EmptyBorder(1, 1, 1, 1));
		if (this.isOpaque() == false) {
			setOpaque(true);
		}
		if (isSelected) {
			super.setBackground(table.getSelectionBackground());
			// super.setForeground(table.getSelectionForeground());
		} else {
//			super.setBackground(table.getBackground());
			super.setBackground(table.getBackground());
			super.setForeground(background);
		}

		setFont(table.getFont());
		setBorderPainted(false);

		if (hasFocus) {
			Border b = null;
			if (isSelected)
				b = UIManager
						.getBorder("Table.focusSelectedCellHighlightBorder");
			if (b == null)
				b = UIManager.getBorder("Table.focusCellHighlightBorder");

			// if (!isSelected && table.isCellEditable(row, column)) {
			// Color col = UIManager.getColor("TableR.focusCellForeground");
			// if (col != null) {
			// // super.setForeground(col);
			// }
			// col = UIManager.getColor("TableR.focusCellBackground");
			// if (col != null) {
			// super.setBackground(col);
			// }
			// }
		} else {
			setBorder(noFocusBorder);
		}

		Color back = getBackground();
		setOpaque(back != null && back.equals(table.getBackground()));

		return this;
	}

	/**
	 * Set the value from the table into the rendering component.
	 * 
	 * @param cell
	 *            the value.
	 */
	protected void setValue(Object cell) {
		if (cell != null && cell instanceof RangedValue) {
			RangedValue rv = (RangedValue) cell;
			setValue(rv.getGauge());
			setStringPainted(true);
			String content = "";
			if (rv.getMax() != rv.getMin()
					&& (rv.getMax() != getMaximum() || rv.getMin() != getMinimum())) {
				setMaximum(rv.getMax());
				setMinimum(rv.getMin());
			}
			int percentage = (rv.getMax() - rv.getMin()) == 0 ? 1 : rv.getMax()
					- rv.getMin();
			content = (rv.getValue() == null ? "" : (rv.getValue() + " "))
					+ (percentage == 1 ? rv.getGauge() + "" : (int) ((rv
							.getGauge() * 100.00) / percentage)
							+ "%");
			setString(content);
			// System.out.println(rv.getGauge() + "<- gauge | percentage ->"
			// +percentage);
		} else {
			// Nothing to change
			if (cell != null)
				setString(cell.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Container#invalidate()
	 */
	@Override
	public void invalidate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#isOpaque()
	 */
	@Override
	public boolean isOpaque() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#repaint()
	 */
	@Override
	public void repaint() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#repaint(long, int, int, int, int)
	 */
	@Override
	public void repaint(long tm, int x, int y, int width, int height) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#repaint(java.awt.Rectangle)
	 */
	@Override
	public void repaint(Rectangle r) {
	}

	@Override
	public void revalidate() {
	}

	@Override
	public void validate() {
	}
}