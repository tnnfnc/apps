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
package it.tnnfnc.apps.application.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Utility class for managing the layout of user panels.
 * 
 * @author Franco Toninato
 * @version %I%, %G%
 * @since 1.2
 */
public class GuiUtility {
	// private static final Dimension HELP_SIZE = new Dimension(500, 300);
	// private static final Border ERROR_BORDER =
	// BorderFactory.createLineBorder(
	// Color.red, 1);

	/**
	 * Sets the location in the center of the screen.
	 * 
	 * @param c
	 *            the component.
	 */
	public static void centerScreen(Component c) {
		Dimension dim = c.getToolkit().getScreenSize();
		Rectangle abounds = c.getBounds();
		c.setLocation((dim.width - abounds.width) / 2,
				(dim.height - abounds.height) / 2);
	}

	/**
	 * Change the font size by a percentage.
	 * 
	 * @param c
	 *            the component.
	 * @param percentage
	 *            the percentage.
	 */
	public static void changeFontSize(Component c, float percentage) {
		Font smaller = c.getFont().deriveFont(
				percentage * c.getFont().getSize2D());
		c.setFont(smaller);
	}

	/**
	 * Change the font size by a percentage.
	 * 
	 * @param c
	 *            the component.
	 * @param percentage
	 *            the percentage.
	 */
	public static void changeFontStyle(Component c, int style) {
		Font f = c.getFont().deriveFont(style);
		c.setFont(f);
	}

	/**
	 * Creates a container with an input pane and a buttons pane. The buttons
	 * pane is placed at the right under the user component.
	 * 
	 * @param inputPanel
	 *            the input pane.
	 * @param actionPanel
	 *            the pane with the action buttons.
	 * @return the overall containing pane.
	 */
	public static JComponent createInputPanel(JComponent inputPanel,
			JComponent actionPanel) {

		Box contentPane = Box.createVerticalBox();
		Box inputBox = Box.createVerticalBox();
		Box actionBox = Box.createHorizontalBox();
		contentPane.add(inputBox);
		contentPane.add(Box.createVerticalGlue());
		contentPane.add(actionBox);

		inputBox.add(inputPanel);
		actionPanel.setLayout(new GridLayout(1, 0, 2, 2));
		JPanel actionBuffer = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		actionBuffer.add(actionPanel);
		actionBuffer.setAlignmentX(Component.RIGHT_ALIGNMENT);
		actionBox.add(actionBuffer);

		return contentPane;
	}

	/**
	 * Sets the preferred size for a text field. Does not work!!!
	 * 
	 * @param length
	 *            the preferred size expressed in characters unit.
	 * @return the max dimension.
	 */
	public static Dimension getFieldSize(int length) {
		JTextField template = new JTextField();
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < length; i++)
			s.append("W");
		template.setText(s.toString());
		return template.getPreferredSize();
	}

}