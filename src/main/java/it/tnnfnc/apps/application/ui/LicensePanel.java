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
import java.awt.Dimension;
import java.io.IOException;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class LicensePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String GNU_GENERAL_HEADER = "gnu_general_header";
	public static final String DEMO_LICENSE = "demo_license";
	private ListResourceBundle loc = (ListResourceBundle) ResourceBundle.getBundle(LicenseBundle.class.getName());
	private String licenseType;

	public LicensePanel(final String licenseType) {
		super();
		this.licenseType = licenseType;
		createGui();

	}

	private void createGui() {
		// Help Panel
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		java.net.URL helpURL = LicensePanel.class.getResource(loc.getString(licenseType)); // "ReaderMapHelp.htm"
		if (helpURL != null) {
			try {
				textPane.setPage(helpURL);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + helpURL);
			}
		} else {
			System.err.println("Couldn't find file " + helpURL);
		}
		JScrollPane helpScrollPane = new JScrollPane(textPane);
		helpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		helpScrollPane.setPreferredSize(new Dimension(250, 145));
		helpScrollPane.setMinimumSize(new Dimension(100, 100));
		setLayout(new BorderLayout());
		add(helpScrollPane, BorderLayout.CENTER);

	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Create and set up the window.
				JFrame frame = new JFrame("Properties panel demo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// Create and set up the content pane.
				LicensePanel demo = new LicensePanel(DEMO_LICENSE);
				frame.getContentPane().add(demo);

				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}
