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
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import it.tnnfnc.apps.application.ui.style.IconsFactory;

/**
 * @author Franco Toninato
 * 
 */
public class RolledPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel cardsPanel;
	private Component component;
	private CardLayout cardLayout;
	private JButton button;
	private boolean status;
	private static final boolean OPENED = true;
	private static final boolean CLOSED = false;
	private JLabel buttonLabel;
	private Component follower;
	private Dimension follower_pref_Size;
	private Dimension component_pref_Size;
	private Dimension closed_size;
	private Dimension follower_expanded_size;
	protected ListResourceBundle language = (ListResourceBundle) ResourceBundle
			.getBundle(LocaleBundle.class.getName());

	/**
	 * @param c
	 * @param f
	 */
	public RolledPanel(Component c, Component f) {
		this.component = c;
		this.follower = f;
		initialise();
		createGui();
	}

	private void initialise() {
		buttonLabel = new JLabel();
		cardLayout = new CardLayout();
		cardsPanel = new JPanel(cardLayout);
		status = OPENED;
		button = new JButton();
		button.add(buttonLabel);
		button.addActionListener(this);
		component_pref_Size = new Dimension(component.getPreferredSize());
		follower_pref_Size = new Dimension(follower.getPreferredSize());
		closed_size = new Dimension(component_pref_Size.width, 0);
		follower_expanded_size = new Dimension(follower_pref_Size.width,
				follower_pref_Size.height + component_pref_Size.height);
	}

	private void createGui() {
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder());
		// cardsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		add(button, BorderLayout.NORTH);
		add(cardsPanel, BorderLayout.CENTER);

		cardsPanel.add(new JPanel(), CLOSED + "");
		cardsPanel.add(component, OPENED + "");
		change();
	}

	private void change() {
		status = !status;
		String text = (status == OPENED ? language.getString("Close")
				: language.getString("Open"))
				+ (component.getName() == null ? "" : " " + component.getName());
		buttonLabel.setIcon(status == OPENED ? new IconsFactory.DownArrow(
				Color.BLACK) : new IconsFactory.UpArrow(Color.BLACK));
		button.setText(text + " ");
		cardLayout.show(cardsPanel, status + "");
		if (status) {
			cardsPanel.setPreferredSize(component_pref_Size);
			follower.setPreferredSize(follower_pref_Size);
		} else {
			cardsPanel.setPreferredSize(closed_size);
			follower.setPreferredSize(follower_expanded_size);
		}
		follower.invalidate();
		cardsPanel.invalidate();
		cardLayout.layoutContainer(cardsPanel);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		change();
		repaint();
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame("AbstractListWindow");

				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f
						.getContentPane()
						.add( //
								new RolledPanel(
										new JLabel(
												"<html> <h1>This is a Title</h1> <p> hello world"),
										f.getContentPane()));

				f.pack();

				f.setVisible(true);
			}
		});
	}
}