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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//import net.catode.properties.AbstractEditingPanel;

/**
 * A <code>ChangePasswordPanel</code> is a panel with enter password and an
 * optional re-enter password field. If the password does not match the
 * re-entered one an error message is displayed.
 * 
 * @author Franco Toninato
 */
public class LoginPanel extends JPanel {
	// AbstractEditingPanel {

	private static final long serialVersionUID = 42L;
	private ListResourceBundle resource;
	private JTextField user;
	private JPasswordField password;
	private KeyboardComponentEditor passwordKeyboard;
	private boolean toConfirm;
	private ChangePasswordPanel changePasswordPanel;
	private int loginType = BASIC_LOGIN;

	/**
	 * Display option: password.
	 */
	public static int PASSWORD = 0;
	/**
	 * Display option: basic authentication, user and password check.
	 */
	public static int BASIC_LOGIN = 2;
	/**
	 * Display option: current password, new password and confirm.
	 */
	public static int PASSWORD_CHANGE = 4;
	/**
	 * Display option: password and confirm.
	 */
	public static int PASSWORD_CONFIRM = 8;
	/**
	 * Display option: password and password strength.
	 */
	public static int PASSWORD_STRENGTH = 16;

	/**
	 * Create a login panel.
	 * 
	 * 
	 * @param loginType
	 *            when true a user input field is displayed.
	 */
	public LoginPanel(int loginType) {
		resource = (ListResourceBundle) ResourceBundle.getBundle(SecurityResources.class.getName());
		this.loginType = loginType;
		changePasswordPanel = new ChangePasswordPanel();
		user = new JTextField(10);
		password = new JPasswordField(10);
		passwordKeyboard = new KeyboardComponentEditor(password);
		setConfirmRequired(loginType == PASSWORD_CHANGE || loginType == PASSWORD_CONFIRM);
		createGUI();
	}

	/**
	 * @param displayUser
	 */
	private void createGUI() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);
		gbc.weightx = 1.0F;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		if (loginType == BASIC_LOGIN) {
			GridBagLayoutUtility.add(new JLabel(resource.getString("user")), user, this, gbc);
			GridBagLayoutUtility.addVerticalSpace(10, this, gbc);
			// Password
			GridBagLayoutUtility.newLine(gbc);
			GridBagLayoutUtility.add(new JLabel(resource.getString("Password")), passwordKeyboard, this, gbc);
			GridBagLayoutUtility.newLine(gbc);
			GridBagLayoutUtility.add(new JLabel(resource.getString("Strength")), new PasswordStrengthField(password),
					this, gbc);
		} else if (loginType == PASSWORD) {
			GridBagLayoutUtility.add(new JLabel(resource.getString("Password")), passwordKeyboard, this, gbc);
		} else if (loginType == PASSWORD_STRENGTH) {
			GridBagLayoutUtility.add(new JLabel(resource.getString("Password")), passwordKeyboard, this, gbc);
			GridBagLayoutUtility.newLine(gbc);
			GridBagLayoutUtility.add(new JLabel(resource.getString("Strength")), new PasswordStrengthField(password),
					this, gbc);
		} else if (loginType == PASSWORD_CHANGE) {
			GridBagLayoutUtility.add(new JLabel(resource.getString("Password")), passwordKeyboard, this, gbc);
			GridBagLayoutUtility.newLine(gbc);
			GridBagLayoutUtility.add(new JLabel(resource.getString("Strength")), new PasswordStrengthField(password),
					this, gbc);
			GridBagLayoutUtility.addVerticalSpace(5, this, gbc);
			GridBagLayoutUtility.newLine(gbc);
			gbc.gridwidth = 3;
			changePasswordPanel.setBorder(BorderFactory.createTitledBorder(resource.getString("Change password")));
			this.add(changePasswordPanel, gbc);
		} else if (loginType == PASSWORD_CONFIRM) {
			changePasswordPanel.setBorder(BorderFactory.createTitledBorder(resource.getString("Password")));
			this.add(changePasswordPanel, gbc);
		}
	}

	/**
	 * Get the user.
	 * 
	 * @return the user.
	 */
	public String getUser() {
		return user.getText();
	}

	/**
	 * Get the login mode.
	 * 
	 * @return the login mode.
	 */
	public int getType() {
		return this.loginType;
	}

	/**
	 * Return the password.
	 * 
	 * @return the password. Or null if something goes wrong.
	 */
	public char[] getPassword() {

		char c[] = Arrays.copyOf(password.getPassword(), password.getPassword().length);
		if (ChangePasswordPanel.isPasswordValid(c)) {
			Arrays.fill(password.getPassword(), '0');

			return c;
		}

		return null;
	}

	/**
	 * Return the new password.
	 * 
	 * @return the password. Or null if something goes wrong.
	 */
	public char[] getNewPassword() {
		char c[] = changePasswordPanel.getPassword();

		return c;

	}

	/**
	 * Converts characters to bytes.
	 * 
	 * @param cs
	 *            the bytes array.
	 * @return the byte arrays.
	 */
	public static byte[] toBytes(char[] cs) {
		return new String(cs).getBytes();
	}

	/**
	 * Check if entered password and re-entered password are equal. If different
	 * an error message is displayed in the info area of the panel.
	 * 
	 * @return true if the check is successful.
	 */
	public boolean passwordCheck() {

		if (toConfirm) {
			return changePasswordPanel.passwordCheck();
		} else {
			return true;
		}
	}

	/**
	 * Clear the panel.
	 */
	public void reset() {
		password.setText(new String());
		changePasswordPanel.reset();
	}

	/**
	 * Turn on or off the password confirmation.
	 * 
	 * @param toConfirm
	 *            enable or disable the password confirmation check.
	 */
	public void setConfirmRequired(boolean toConfirm) {
		this.toConfirm = toConfirm && (loginType == PASSWORD_CHANGE || loginType == PASSWORD_CONFIRM);
		reset();
	}

	public void init() {
		reset();
	}

	public void update() {
		if (toConfirm)
			passwordCheck();

	}

	public String getLabel() {
		return resource.getString("Password");
	}

	public boolean changed() {
		return true;
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
		passwordKeyboard.setEnabled(b);
		changePasswordPanel.setEnabled(b);
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame("Login Panel test");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// f.getContentPane().add(new LoginPanel(PASSWORD_STRENGTH));
				f.getContentPane().add(new LoginPanel(BASIC_LOGIN));
				f.pack();
				f.setVisible(true);
			}
		});
	}
}