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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

/**
 * This class is a decorator for a text component that enables input from a
 * virtual keyboard on the screen. The virtual keyboard is displayed optionally
 * clicking on a button.
 * 
 * @author Franco Toninato
 * 
 */
public class KeyboardComponentEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2778866184104070904L;
	private JTextComponent component;
	private JButton button;
	private KeyboardDialog keybordW;
	protected ListResourceBundle language = (ListResourceBundle) ResourceBundle.getBundle(LocaleBundle.class.getName());

	/**
	 * Create the decorator for the text component.
	 * 
	 * @param c
	 *            the text component.
	 */
	public KeyboardComponentEditor(JTextComponent c) {
		component = c;
		button = new JButton(language.getString("Keyboard"));
		button.setToolTipText(language.getString("tt-Keyboard"));

		JPanel jp_container = new JPanel(new BorderLayout());
		jp_container.add(component, BorderLayout.LINE_START);
		jp_container.add(button, BorderLayout.LINE_END);
		this.add(jp_container);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (keybordW == null) {
					keybordW = new KeyboardDialog();
					keybordW.setResizable(false);
				}
				// Clear before calling the component
				component.setText("");
				keybordW.setEditedComponent(component);
				keybordW.pack();
				keybordW.setLocationRelativeTo(component);
				keybordW.setVisible(true);
			}
		});
	}

	/**
	 * Get the text component.
	 * 
	 * @return the text component.
	 */
	public JTextComponent getTextComponent() {
		return component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		component.setEnabled(b);
		button.setEnabled(b);
	}

}
