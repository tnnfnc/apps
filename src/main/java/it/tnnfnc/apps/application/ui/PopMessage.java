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

import javax.swing.JOptionPane;

public class PopMessage {
	private static StringBuffer message = new StringBuffer();
	private static ListResourceBundle language = (ListResourceBundle) ResourceBundle
			.getBundle(LocaleBundle.class.getName());

	/**
	 * Display an error message.
	 * 
	 * @param message
	 *            the exception.
	 */
	public static void displayError(Component frame, String err) {
		message.delete(0, message.length());
		message.append(language.getString(""));
		message.append(err);
		message.append("!");
		JOptionPane.showMessageDialog(frame, message, language
				.getString("Application exception"), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Display an error message.
	 * 
	 * @param message
	 *            the exception.
	 */
	public static void displayInfo(Component frame, String info) {
		message.delete(0, message.length());
		StringBuffer message = new StringBuffer();
		message.append(language.getString(""));
		message.append(info);
		message.append("!");
		JOptionPane.showMessageDialog(frame, message, language
				.getString("Application information"),
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Display an error message.
	 * 
	 * @param message
	 *            the exception.
	 */
	public static void displayWarning(Component frame, String warn) {
		message.delete(0, message.length());
		StringBuffer message = new StringBuffer();
		message.append(language.getString(""));
		message.append(warn);
		message.append("!");
		JOptionPane.showMessageDialog(frame, message, language
				.getString("Application warning"), JOptionPane.WARNING_MESSAGE);
	}

	public static void displayError(Component frame, String err_1, String err_2) {
		message.delete(0, message.length());
		message.append(language.getString(""));
		message.append(err_1);
		message.append(": ");
		message.append(err_2);
		message.append("!");
		JOptionPane.showMessageDialog(frame, message, language
				.getString("Application exception"), JOptionPane.ERROR_MESSAGE);
	}

}
