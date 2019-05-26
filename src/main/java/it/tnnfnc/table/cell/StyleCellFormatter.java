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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import it.tnnfnc.apps.application.ui.style.StyleFormatter;

/**
 * This class manage the table cell formatting during the usual editing. Default
 * cell renderer format (colors, font, borders) and default cell editor format.
 * It manages also selection and focus.
 * 
 * @author Franco Toninato
 * 
 */
public class StyleCellFormatter extends StyleFormatter {

	// private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	// private static final Color selForeground = UIManager
	// .getColor("Table.selectionForeground");
	private static final Color selBackground = UIManager
			.getColor("Table.selectionBackground");
	private static final Color background = UIManager
			.getColor("Table.background");
	private static final Color foreground = UIManager
			.getColor("Table.foreground");

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.table.style.StyleFormatter#applyStyle(java.awt.Component,
	 * java.lang.String, boolean, boolean)
	 */
	@Override
	public void applyStyle(Component c, String style, boolean isSelected,
			boolean hasFocus) {
		this.style = style;
		this.component = c;
		this.isSelected = isSelected;
		this.hasFocus = hasFocus;
		// if (isSelected) {
		// // Change foreground and background
		// c.setFont(font());
		// c.setBackground(selBackground);
		// c.setForeground(foregroundColor());
		// } else {
		// // Change foreground and background
		// c.setFont(font());
		// // Problems with progress bar
		// c.setBackground(backgroundColor());
		// c.setForeground(foregroundColor());
		// if (component instanceof JComponent) {
		// ((JComponent) component).setBorder(border());
		// }
		// }
		//
		// if (component instanceof JComponent) {
		// Border b = null;
		// if (hasFocus) {
		// // Change border to focus border
		// if (isSelected) {
		// b = UIManager
		// .getBorder("Table.focusSelectedCellHighlightBorder");
		// if (b == null) {
		// b = UIManager
		// .getBorder("Table.focusCellHighlightBorder");
		// }
		// }
		// ((JComponent) component).setBorder(b);
		// } else {
		// // Change border to no focus border
		// ((JComponent) component).setBorder(border());
		// }
		// }

		// Font
		Font f = font();
		if (f != null && component.getFont() != f)
			component.setFont(f);
		// Foreground
		Color foreground = foregroundColor();
		if (foreground != component.getForeground())
			component.setForeground(foreground);
		if (isSelected) {
			// Selection background
			c.setBackground(selBackground);
		} else {
			// Change background
			Color background = backgroundColor();
			if (background != component.getBackground())
				component.setBackground(background);
			if (component instanceof JComponent) {
				JComponent jcomponent = (JComponent) component;
				Border b = border();
				if (b != null && b != jcomponent.getBorder())
					jcomponent.setBorder(b);
			}
		}

		if (component instanceof JComponent) {
			JComponent jcomponent = (JComponent) component;
			Border b = null;
			if (hasFocus) {
				// Change border to focus border
				if (isSelected) {
					b = UIManager
							.getBorder("Table.focusSelectedCellHighlightBorder");
					if (b == null) {
						b = UIManager
								.getBorder("Table.focusCellHighlightBorder");
					}
				}
				jcomponent.setBorder(b);
			} else {
				// Change border to no focus border
				b = border();
				if (b != null && b != jcomponent.getBorder())
					jcomponent.setBorder(b);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.table.style.StyleFormatter#backgroundColor()
	 */
	@Override
	public Color backgroundColor() {
		Color c = super.backgroundColor();
		if (c == null) {
			c = background;
		}
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.table.style.StyleFormatter#color()
	 */
	@Override
	public Color foregroundColor() {
		Color c = super.foregroundColor();
		if (c == null) {
			c = foreground;
		}
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.table.style.StyleFormatter#font()
	 */
	@Override
	public Font font() {
		return super.font();
	}

	/**
	 * 
	 */
	@Override
	public Border border() {
		Border c = super.border();
		if (c == null) {
			c = new EmptyBorder(0, 0, 0, 0);
		}
		return c;
	}

}
