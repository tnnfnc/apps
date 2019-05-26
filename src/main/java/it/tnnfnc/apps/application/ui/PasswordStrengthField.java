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

import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A progress bar shows the password strength.
 * 
 * @author Franco Toninato
 * 
 */
public class PasswordStrengthField extends JProgressBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PasswordStrengthField(final JPasswordField p) {
		super();
		setFocusable(false);
		setStringPainted(true);
		p.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				getPasswordStrength(p.getPassword());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				getPasswordStrength(p.getPassword());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				getPasswordStrength(p.getPassword());
			}

			private void getPasswordStrength(char[] d) {
				setValue(PasswordGauge.getStrength(d));
			}
		});
	}

}
