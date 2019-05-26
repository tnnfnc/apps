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
package it.tnnfnc.apps.application.ui.style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Changes the rendering formats applying a style to the component.
 * 
 * @author Franco Toninato
 * 
 */
public class StyleFormatter implements I_StyleFormatter {

	/**
	 * The style.
	 */
	protected String style;
	/**
	 * The component.
	 */
	protected Component component;
	/**
	 * The component is selected status.
	 */
	protected boolean isSelected;
	/**
	 * The component has focus status.
	 */
	protected boolean hasFocus;

	/**
	 * Apply the format to a component.
	 * 
	 * @param component
	 *            the component to be formatted.
	 * @param style
	 *            the format.
	 * @param isSelected
	 *            true when the component is selected.
	 * @param hasFocus
	 *            true when the component has focus on it.
	 */
	@Override
	public void applyStyle(Component c, String style, boolean isSelected,
			boolean hasFocus) {
		if ((this.style = style) == null)
			return;
		this.component = c;
		this.isSelected = isSelected;
		this.hasFocus = hasFocus;
		Font f = font();
		if (f != null && component.getFont() != f)
			component.setFont(f);

		Color foreground = foregroundColor();
		if (foreground != null && foreground != component.getForeground()) {
			if (isSelected || hasFocus) {
				if (isSelected) {
					component.setBackground(getSelectedBackground(component
							.getBackground()));
					component.setForeground(getSelectedForeground(component
							.getForeground()));
				}
				if (hasFocus) {
					component.setBackground(getFocusBackground(component
							.getBackground()));
					component.setForeground(getFocusForeground(component
							.getForeground()));
				}
			} else {
				component.setForeground(foreground);
				Color background = backgroundColor();
				if (background != null
						&& background != component.getBackground()) {
					component.setBackground(background);
				}
				if (component instanceof JComponent) {
					JComponent jcomponent = (JComponent) component;
					Border b = border();
					if (b != jcomponent.getBorder())
						jcomponent.setBorder(b);
				}
			}
		}

	}

	private Color getFocusForeground(Color color) {

		return color;

	}

	private Color getFocusBackground(Color color) {
		return color;

	}

	private Color getSelectedForeground(Color color) {
		return color;

	}

	private Color getSelectedBackground(Color color) {
		return color;

	}

	@Override
	public Font font() {
		return ComponentStyle.getFont(component.getFont(), style);
	}

	@Override
	public Color foregroundColor() {
		return ComponentStyle.getForegroundColor(style);
	}

	@Override
	public Color backgroundColor() {
		return ComponentStyle.getBackgroundColor(style);
	}

	@Override
	public Border border() {
		return ComponentStyle.getBorder(style);
	}

	/**
	 * Test mode.
	 * 
	 * @param args
	 *            no args.
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame f = new JFrame();
				f.setSize(300, 100);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JLabel label = new JLabel("Formatted text label");
				String astyle = "font-family:Freestyle Script;font-style:plain;font-size:28;color:51,51,51;background-color:204,204,204;border-style:solid;border-color:0,153,153;border-top-width:2;border-left-width:2;border-bottom-width:2;border-right-width:2;";
				new StyleFormatter().applyStyle(label, astyle, true, false);
				f.add(label);
				f.setVisible(true);
			}
		});
	}
}