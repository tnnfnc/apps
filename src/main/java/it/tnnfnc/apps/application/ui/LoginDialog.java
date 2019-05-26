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

import java.awt.GridBagConstraints;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;


/**
 * The class for creating a dialog for entering a user and a pass-prase. The
 * class provides methods for accessing user and password.
 * 
 * @author Franco Toninato
 * @version 1.0
 */
public class LoginDialog extends AbstractDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* The localisation resources */
	protected ListResourceBundle resource;
	/* The password field */
	private LoginPanel loginPanel;

	/**
	 * Creates a password dialog.
	 */
	public LoginDialog(int loginType) {
		this.setModal(true);
		this.enableOption(Options.OK_OPTION, "OK");
		this.enableOption(Options.CLOSE_OPTION, "Close");
		this.setResizable(false);
		loginPanel = new LoginPanel(loginType);
		init();
	}

	/* */
	private void init() {
		resource = (ListResourceBundle) ResourceBundle
				.getBundle(SecurityResources.class.getName());
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);
		loginPanel.setBorder(BorderFactory.createEmptyBorder());
		inputPanel.add(loginPanel, gbc);
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public char[] getPassword() {
		char[] p = loginPanel.getPassword();
		return p;
	}

	/**
	 * Gets the new password.
	 * 
	 * @return the old password
	 */
	public char[] getNewPassword() {
		char[] p = loginPanel.getNewPassword();
		return p;
	}

	/**
	 * Reinitializes the password.
	 */
	public void reset() {
		loginPanel.init();
	}

	/**
	 * Enable or disable the re-enter password field for confirmation.
	 * 
	 * @param b
	 *            enable or disable the password confirm check.
	 */
	public void setConfirmRequired(boolean b) {
		loginPanel.setConfirmRequired(b);
		// this.pack();
	}

	@Override
	protected boolean performOK() {
		return loginPanel.passwordCheck();
	}

	@Override
	protected boolean performClose() {
		reset();
		return true;
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		final LoginDialog t = new LoginDialog(LoginPanel.PASSWORD_CHANGE) {
			private static final long serialVersionUID = 1L;

		};
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				t.setTitle("title");
				t.setName("name");
				t.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				t.pack();
				t.setVisible(true);
			}
		});
	}
}