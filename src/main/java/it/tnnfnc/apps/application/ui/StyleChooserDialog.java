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
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.JTable;

import it.tnnfnc.apps.application.ui.style.ComponentStyle;

public class StyleChooserDialog extends AbstractDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StyleChooser chooser;
	private Component parent;
	private String defaultStyle;
	private ListResourceBundle language = (ListResourceBundle) ResourceBundle
			.getBundle(LocaleBundle.class.getName());

	public StyleChooserDialog(Component c) {
		chooser = new StyleChooser();
		parent = c;
		this.setModal(true);
		// this.setModal(false);
		this.inputPanel.add(chooser);
		this.enableOption(AbstractDialog.Options.OK_OPTION,
				language.getString("OK"));
		this.enableOption(AbstractDialog.Options.NO_OPTION,
				language.getString("Default"));
		this.enableOption(AbstractDialog.Options.CLOSE_OPTION,
				language.getString("Close"));
	}

	/**
	 * Set the default style returned when default button is pressed.
	 * 
	 * @param style
	 *            the default style returned when default button is pressed.
	 */
	public void setDefaultStyle(String style) {
		this.defaultStyle = style;
	}

	/**
	 * Show a modal style chooser dialog.
	 * 
	 * @param style
	 *            the component style.
	 * @return the chosen style, when default value is selected a null reference
	 *         is returned.
	 */
	public String showDialog(String style) {
		pack();
		chooser.setStyle(style);
		String returnStyle = null;
		Options o = super.showDialog(parent);

		if (o.equals(AbstractDialog.Options.OK_OPTION)) {
			returnStyle = chooser.getStyle();
		} else if (o.equals(AbstractDialog.Options.NO_OPTION)) {
			returnStyle = (defaultStyle = ComponentStyle
					.getCleanStyle(defaultStyle));
		} else if (o.equals(AbstractDialog.Options.CLOSE_OPTION)) {
			returnStyle = null;
		}
		return returnStyle;
	}

	/**
	 * Show a modal style chooser dialog.
	 * 
	 * @param style
	 *            the component style.
	 * @param style
	 *            the default style returned when default button is pressed.
	 * @return the chosen style, when default value is selected a null reference
	 *         is returned.
	 */
	public String showDialog(String style, String defaultStyle) {
		this.defaultStyle = defaultStyle;
		return showDialog(style);
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// JFrame f = new JFrame("Calendar");
				StyleChooserDialog c = new StyleChooserDialog(new JTable());
				c.pack();
				System.out.println(c.showDialog(""));
			}
		});
	}
}
