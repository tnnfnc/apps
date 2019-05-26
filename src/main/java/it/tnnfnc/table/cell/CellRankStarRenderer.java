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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import it.tnnfnc.apps.application.ui.FiveStarsListRenderer;

/**
 * Renders a table cell as a text string in a progress bar. The string is
 * displayed as long as the progress bar reaches its upper limit. A timer must
 * be provided.
 * 
 * @author Franco Toninato
 */
public class CellRankStarRenderer extends FiveStarsListRenderer implements
		TableCellRenderer {

	private static final long serialVersionUID = 1L;

	public CellRankStarRenderer() {
		super();
		// setBorder(null);
	}

	/**
	 * Returns a progress bar renderer.
	 * 
	 * @return (methods only)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		if (value != null && value instanceof RankStarValue)
			setFillLevel(((RankStarValue) value).getLevel());
		else {
		}

		if (table == null) {
			return this;
		}
		if (this.isOpaque() == false) {
			super.setOpaque(true);
		}
		if (isSelected) {
			super.setBackground(table.getSelectionBackground());
			super.setForeground(table.getSelectionForeground());
		} else {
			super.setBackground(table.getBackground());
			super.setForeground(table.getForeground());
		}

		if (hasFocus) {
			if (!isSelected && table.isCellEditable(row, column)) {
				Color col = UIManager.getColor("TableR.focusCellForeground");
				if (col != null) {
					super.setForeground(col);
				}
				col = UIManager.getColor("TableR.focusCellBackground");
				if (col != null) {
					super.setBackground(col);
				}
			}
		} else {
		}
		return this;
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

	/**
	 * Test.
	 * 
	 * @param args
	 *            no arguments.
	 */
	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame("Five stars panel");
				CellRankStarRenderer up = new CellRankStarRenderer();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.getContentPane().setLayout(new BorderLayout());
				f.getContentPane().add(up, BorderLayout.NORTH);
				up.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
					}
				});
				f.pack();
				f.setVisible(true);
			}
		});
	}
}
