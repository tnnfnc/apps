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

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonUI;

import it.tnnfnc.apps.application.ui.style.I_StyleObject;

/**
 * This class implement a tabbed pane where a single component can be added.
 * Tabs represent actions performed on the displayed component. The class
 * supports adding a new tab, editing and deleting an existing tab but not the
 * first one.
 * 
 * @author franco toninato
 */
public class TabbedPane extends JTabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ListResourceBundle language = (ListResourceBundle) ResourceBundle
			.getBundle(LocaleBundle.class.getName());
	private Component component;
	// private JLabel bingoLabel = new
	// JLabel(language.getString("Add new tab"));
	private TabPanelEditor editTabPanel = new TabPanelEditor();
	private AddTabButton addButton;
	/**
	 * Cancel action command.
	 */
	public static final String CANCEL = "CANCEL";
	/**
	 * Change tab title action command.
	 */
	public static final String CHANGE = "CHANGE";
	/**
	 * Remove tab action command.
	 */
	public static final String REMOVE = "REMOVE";
	/**
	 * Add tab action command.
	 */
	public static final String ADD = "ADD";
	/**
	 * Format tab action command.
	 */
	public static final String FORMAT = "FORMAT";

	/**
	 * Object constructor.
	 */
	public TabbedPane() {
		super();
		addButton = new AddTabButton();
		super.addTab(language.getString("New"), null, null,
				language.getString("Add new tab"));
		setTabComponentAt(0, addButton);
	}

	/**
	 * Object constructor.
	 * 
	 * @param edit
	 *            the edit component.
	 */
	public TabbedPane(TabPanelEditor edit) {
		this();
		this.editTabPanel = edit;
	}

	/**
	 * Add an action listener to the panel commands.
	 * 
	 * @param listener
	 *            the action listener.
	 */
	public void addActionListener(ActionListener listener) {
		editTabPanel.addActionListener(listener);
		addButton.addActionListener(listener);
	}

	/**
	 * remove an action listener from the panel commands.
	 * 
	 * @param listener
	 *            the action listener.
	 */
	public void removeActionListener(ActionListener listener) {
		editTabPanel.removeActionListener(listener);
		addButton.removeActionListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, java.awt.Component)
	 */
	public void addTab(I_StyleObject title, Component component) {
		addTabButton();
		int index = getTabCount() - 1;
		String t = title.getValue() + "";
		super.insertTab(t, null, addXOR(component), t, index);
		setTabComponent(title, null, t, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, javax.swing.Icon,
	 * java.awt.Component)
	 */
	public void addTab(I_StyleObject title, Icon icon, Component component) {
		addTabButton();
		int index = getTabCount() - 1;
		String t = title.getValue() + "";
		super.insertTab(t, icon, addXOR(component), t, index);
		setTabComponent(title, icon, t, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, javax.swing.Icon,
	 * java.awt.Component, java.lang.String)
	 */
	public void addTab(I_StyleObject title, Icon icon, Component component,
			String tip) {
		addTabButton();
		int index = getTabCount() - 1;
		String t = title.getValue() + "";
		super.insertTab(t, icon, addXOR(component), tip, index);
		setTabComponent(title, icon, tip, index);
	}

	/**
	 * @param title
	 * @param icon
	 * @param component2
	 * @param tip
	 * @param i
	 */
	public void insertTab(I_StyleObject title, Icon icon, Component component2,
			String tip, int i) {
		String t = title.getValue() + "";
		super.insertTab(t, icon, addXOR(component), tip, i);
		setTabComponent(title, icon, tip, i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, java.awt.Component)
	 */
	@Override
	public void addTab(String title, Component component) {
		// System.out.println("Insert tab at " + count);
		addTabButton();
		int index = getTabCount() - 1;
		super.insertTab(title, null, addXOR(component), title, index);
		setTabComponent(title, null, title, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, javax.swing.Icon,
	 * java.awt.Component)
	 */
	@Override
	public void addTab(String title, Icon icon, Component component) {
		addTabButton();
		int index = getTabCount() - 1;
		super.insertTab(title, icon, addXOR(component), title, index);
		setTabComponent(title, icon, title, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, javax.swing.Icon,
	 * java.awt.Component, java.lang.String)
	 */
	@Override
	public void addTab(String title, Icon icon, Component component, String tip) {
		addTabButton();
		int index = getTabCount() - 1;
		super.insertTab(title, icon, addXOR(component), tip, index);
		setTabComponent(title, icon, tip, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTabbedPane#removeAll()
	 */
	@Override
	public void removeAll() {
		super.removeAll();
		this.component = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTabbedPane#removeTabAt(int)
	 */
	@Override
	public void removeTabAt(int index) {
		super.removeTabAt(index);
	}

	/**
	 * Get the edit tab component for the selected tab.
	 * 
	 * @return the edit tab component.
	 */
	public TabPanelEditor getEditComponent() {
		return editTabPanel;

	}

	/**
	 * Set the custom tab component. The first tab component is the standard
	 * one. Subclasses can implement their own tab component.
	 * 
	 * @param title
	 *            the tab title.
	 * @param icon
	 *            the tab icon.
	 * @param tip
	 *            the tooltip text.
	 * @param index
	 *            the tab index.
	 */
	protected void setTabComponent(String title, Icon icon, String tip,
			int index) {
		TabComponent actionTab = new TabComponent(this, title, icon, tip,
				index, editTabPanel);
		if (index > 0) {
			setTabComponentAt(index, actionTab);
		}
		setSelectedIndex(getSelectedIndex());
	}

	/**
	 * Set the custom tab component. The first tab component is the standard
	 * one. Subclasses can implement their own tab component.
	 * 
	 * @param title
	 *            the tab title.
	 * @param icon
	 *            the tab icon.
	 * @param tip
	 *            the tooltip text.
	 * @param index
	 *            the tab index.
	 */
	protected void setTabComponent(I_StyleObject title, Icon icon, String tip,
			int index) {
		TabComponent actionTab = new TabComponent(this, title, icon, tip,
				index, editTabPanel);
		if (index > 0) {
			setTabComponentAt(index, actionTab);
		}
		setSelectedIndex(getSelectedIndex());
	}

	/**
	 */
	private void addTabButton() {
		int count = getTabCount() - 1;
		if (count == -1) {
			super.addTab(language.getString("New"), null, null,
					language.getString("Add new tab"));
			setTabComponentAt(0, addButton);
		}
	}

	/**
	 * Add a component. It does nothing if the component is already present,
	 * otherwise it is replaced.
	 * 
	 * @param component
	 *            the component.
	 * @return the component or null if it is present.
	 */
	private Component addXOR(Component component) {
		if (this.component == component) {
			// return bingoLabel;
			// return new JPanel();
			return null;
		} else {
			return this.component = component;
		}
		// return component;
	}

	@SuppressWarnings("serial")
	private class AddTabButton extends JButton {
		public AddTabButton() {
			int size = 15;
			setPreferredSize(new Dimension(size, size));
			setToolTipText(language.getString("Add new tab"));
			// Make the button looks the same for all Laf's
			setUI(new BasicButtonUI());
			setContentAreaFilled(false);
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			setActionCommand(TabbedPane.ADD);
		}

		// No update UI for this button
		@Override
		public void updateUI() {
		}

		// Draws on/off icons
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			double margin = 3;
			double hspace = getPreferredSize().getHeight();
			double wspace = getPreferredSize().getWidth();
			// double linespace = hspace / 4;
			// if (isSelected()) {
			g2.setStroke(new BasicStroke(2));
			g2.setColor(UIManager.getColor("Button.disabledText"));
			if (getModel().isRollover()) {
				g2.setColor(UIManager.getColor("Button.focus"));
			}
			// Vline
			Line2D shape = new Line2D.Double(wspace / 2, 0 + margin,
					wspace / 2, wspace - margin - 1);
			g2.draw(shape);
			// Hline
			shape = new Line2D.Double(0 + margin, hspace / 2, wspace - margin
					- 1, hspace / 2);
			g2.draw(shape);

			g2.dispose();
		}
	}

	private final static MouseListener buttonMouseListener = new MouseAdapter() {
		@Override
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};

}