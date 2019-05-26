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
package it.tnnfnc.apps.application.ui; //Package

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * A <code>ChangePasswordPanel</code> is a panel with enter password and an
 * optional re-enter password field. If the password does not match the
 * re-entered one an error message is displayed.
 * 
 * @author Franco Toninato
 */
public class ChangePasswordPanel extends JPanel {

	private static final long serialVersionUID = 42L;
	private ListResourceBundle resource;
	private JPasswordField password;
	private JPasswordField passwordConfirm;
	private KeyboardComponentEditor confirmKeyboard;
	private KeyboardComponentEditor newPasswordKeyboard;
	private JLabel confirm_label;

	private static Border EMPTY_BORDER = UIManager
			.getBorder("TextField.border");
	private static final Border ERROR_BORDER = BorderFactory.createLineBorder(
			Color.red, 1);
	private static final Border CONFIRMED_BORDER = BorderFactory
			.createLineBorder(Color.GREEN, 1);

	/**
	 * Create a login panel.
	 * 
	 * 
	 * @param displayUser
	 *            when true a user input field is displayed.
	 */
	public ChangePasswordPanel() {
		resource = (ListResourceBundle) ResourceBundle
				.getBundle(SecurityResources.class.getName());

		confirm_label = new JLabel(resource.getString("confirm"));
		password = new JPasswordField(10);
		passwordConfirm = new JPasswordField(10);
		// strength = new JProgressBar(0, 100);
		// setConfirmRequired(true);
		// Document listener for check
		passwordConfirm.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent e) {
						passwordCheck();
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						passwordCheck();
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						passwordCheck();
					}
				});

		// When focused the password is selected
		ThisFocusListener focusListener = new ThisFocusListener();
		password.addFocusListener(focusListener);
		passwordConfirm.addFocusListener(focusListener);
		confirmKeyboard = new KeyboardComponentEditor(passwordConfirm);
		newPasswordKeyboard = new KeyboardComponentEditor(password);

		createGUI();
	}

	/**
	 * 
	 */
	private void createGUI() {
		// -------------- Password input panel ----------------
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);
		gbc.weightx = 1.0F;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		// Password
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(
				new JLabel(resource.getString("New Password")),
				newPasswordKeyboard, this, gbc);
		// Strength
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(new JLabel(resource.getString("Strength")),
				new PasswordStrengthField(password), this, gbc);
		// Confirm
		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.add(confirm_label, confirmKeyboard, this, gbc);
	}

	/**
	 * Return the password.
	 * 
	 * @return the password.
	 */
	public char[] getPassword() {
		if (passwordCheck()) {
			char c[] = Arrays.copyOf(password.getPassword(),
					password.getPassword().length);
			Arrays.fill(password.getPassword(), '0');
			return c;
		}
		// else if (isPasswordValid(password.getPassword())) {
		// char c[] = Arrays.copyOf(password.getPassword(),
		// password.getPassword().length);
		// Arrays.fill(password.getPassword(), '0');
		// return c;
		// }
		return null;
	}

	/**
	 * Check if entered password and re-entered password are equal. If different
	 * an error message is displayed in the info area of the panel.
	 * 
	 * @return true if the check is successful.
	 */
	public boolean passwordCheck() {
		char a[] = password.getPassword();
		char b[] = passwordConfirm.getPassword();
		if (this.isEnabled()) {
			boolean result = false;

			if (isPasswordValid(a) && Arrays.equals(a, b)) {
				password.setBorder(CONFIRMED_BORDER);
				passwordConfirm.setBorder(CONFIRMED_BORDER);
				result = true;
			} else if (isPasswordValid(a) && !Arrays.equals(a, b)) {
				password.setBorder(ERROR_BORDER);
				passwordConfirm.setBorder(ERROR_BORDER);
				result = false;
			} else {
				password.setBorder(EMPTY_BORDER);
				passwordConfirm.setBorder(EMPTY_BORDER);
				result = false;
			}
			return result;
		} else {
			return isPasswordValid(a);
		}
	}

	/**
	 * Return true when the char array is a valid password.
	 * 
	 * @param pwd
	 *            the input char array;
	 * @return true when the char array is a valid password.
	 */
	public static boolean isPasswordValid(char[] pwd) {
		// return pwd != null && pwd.length > 0;
		String regex = new String("\\s+");
		if (pwd == null || pwd.length == 0 || new String(pwd).matches(regex)) {
			return false;
		}
		return true;
	}

	// /**
	// * Return true when the char array is a valid password.
	// *
	// * @param pwd
	// * the input char array;
	// * @param rule
	// * a password rule as a regular expression.
	// * @return true when the char array is a valid password.
	// */
	// public static boolean isPasswordValid(String rule, char[] pwd) {
	// return pwd != null && pwd.length > 0 && (new String(pwd)).matches(rule);
	// }

	/**
	 * Clear the panel.
	 */
	public void reset() {
		// Init the password fields
		password.setText(new String());
		passwordConfirm.setText(new String());
		password.setBorder(EMPTY_BORDER);
		passwordConfirm.setBorder(EMPTY_BORDER);
		// confirm.setVisible(toConfirm);
		// confirm_label.setVisible(toConfirm);
		password.requestFocusInWindow();
	}

	/**
	 * Sets if the panel and its input fields are enabled or disabled.
	 * 
	 * @param b
	 *            true if the panel should be enabled, otherwise false.
	 */
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		confirmKeyboard.setEnabled(b);
		newPasswordKeyboard.setEnabled(b);
	}

	// /**
	// * Turn on or off the password confirmation.
	// *
	// * @param toConfirm
	// * enable or disable the password confirmation check.
	// */
	// public void setConfirmRequired(boolean toConfirm) {
	// this.toConfirm = toConfirm;
	// reset();
	// }

	/**
	 * Focus listener.
	 * 
	 */
	private class ThisFocusListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {

			if (e.getSource().equals(password)) {
				password.selectAll();
				// if (toConfirm) {
				password.setBorder(EMPTY_BORDER);
				passwordConfirm.setBorder(EMPTY_BORDER);
				// confirm.setText(null);
				// }
			} else if (e.getSource().equals(passwordConfirm)) {
				// if (toConfirm)
				passwordConfirm.selectAll();
			} else {
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (e.getSource().equals(password)) {
				// if (toConfirm)
				passwordConfirm.requestFocusInWindow();
			} else if (e.getSource().equals(passwordConfirm)) {
				// if (toConfirm) {
				passwordCheck();
				// }
			} else {
			}
		}
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame("Change Password Panel");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				ChangePasswordPanel cpp = new ChangePasswordPanel();
				f.getContentPane().add(cpp);
				f.pack();
				f.setVisible(true);
				// System.out.println(new String(cpp.getPassword()));
			}
		});
	}
}