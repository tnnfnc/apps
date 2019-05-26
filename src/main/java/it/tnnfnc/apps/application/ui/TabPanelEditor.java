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
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import it.tnnfnc.apps.application.ui.style.StyleObject;

@SuppressWarnings("serial")
public class TabPanelEditor extends JPanel {

	private ListResourceBundle language = (ListResourceBundle) ResourceBundle
			.getBundle(LocaleBundle.class.getName());
	private JTextField newTitle;
	private JTextArea textArea;
	private JTabbedPane tabbedpane;
	private JDialog dialog;
	private JButton closeButton;
	private JButton removeButton;
	private JButton okButton;
	private JButton formatButton;
	private StyleChooserDialog chooser;
	protected int tab;

	/**
	 * This component provides an edit panel to customize a JTabbedPane tab.
	 */
	public TabPanelEditor() {
		createGUI();
	}

	private void createGUI() {

		setLayout(new BorderLayout());

		JPanel inputPanel = new JPanel(new GridBagLayout());
		JPanel actionPanel = new JPanel();
		this.add(GuiUtility.createInputPanel(inputPanel, actionPanel),
				BorderLayout.CENTER);

		newTitle = new JTextField(15);
		textArea = new JTextArea("", 3, 1);
		textArea.setAutoscrolls(true);
		textArea.setLineWrap(true);

		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);

		GridBagLayoutUtility.add(new JLabel(language.getString("Name")),
				newTitle, inputPanel, gbc);
		GridBagLayoutUtility.add(
				new JLabel(language.getString("Description")),
				new JScrollPane(textArea),
				inputPanel,
				GridBagLayoutUtility.spanBlock(
						GridBagLayoutUtility.newLine(gbc), 2, 2));
		GridBagLayoutUtility.refreshConstraints(gbc);

		closeButton = new JButton(
				new CommandClose(language.getString("Cancel")));
		removeButton = new JButton(new CommandRemove(
				language.getString("Remove")));
		okButton = new JButton(new CommandChange(language.getString("Change")));
		formatButton = new JButton(new CommandChangeFormat(
				language.getString("Format")));
		okButton.setActionCommand(TabbedPane.CHANGE);
		closeButton.setActionCommand(TabbedPane.CANCEL);
		removeButton.setActionCommand(TabbedPane.REMOVE);
		formatButton.setActionCommand(TabbedPane.FORMAT);
		actionPanel.add(okButton);
		actionPanel.add(formatButton);
		actionPanel.add(removeButton);
		actionPanel.add(closeButton);
	}

	/**
	 * Add an action listener to the panel commands.
	 * 
	 * @param listener
	 *            the action listener.
	 */
	public void addActionListener(ActionListener listener) {
		okButton.addActionListener(listener);
		closeButton.addActionListener(listener);
		removeButton.addActionListener(listener);
	}

	/**
	 * remove an action listener from the panel commands.
	 * 
	 * @param listener
	 *            the action listener.
	 */
	public void removeActionListener(ActionListener listener) {
		okButton.removeActionListener(listener);
		closeButton.removeActionListener(listener);
		removeButton.removeActionListener(listener);
	}

	/**
	 * Get the index of current edited tab.
	 * 
	 * @return the index of current edited tab.
	 */
	public int getIndex() {
		return tab;
	}

	/**
	 * Get the tab title.
	 * 
	 * @return the tab title.
	 */
	public String getTitle() {
		return newTitle.getText();
	}

	/**
	 * Get the tab description.
	 * 
	 * @return the tab description.
	 */
	public String getTooltip() {
		return textArea.getText();
	}

	/**
	 * Show an edit tab dialog.
	 * 
	 * @param i
	 *            the currently editing tab.
	 */
	public void showEditDialog(final JTabbedPane pane, int i) {
		tabbedpane = pane;
		tab = i;
		class InternalWindowsListener extends WindowAdapter {

			@Override
			public void windowClosing(WindowEvent event) {

			}

		}
		if (dialog == null) {
			dialog = new JDialog();
			dialog.setTitle(language.getString("Edit tab"));
			dialog.setModal(true);
			dialog.addWindowListener(new InternalWindowsListener());
			dialog.getContentPane().setLayout(new BorderLayout());
			dialog.getContentPane().add(this, BorderLayout.CENTER);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		}

		if (tabbedpane.getTabCount() > 0) {
			newTitle.setText(tabbedpane.getTitleAt(i));
			textArea.setText(tabbedpane.getToolTipTextAt(i));
			dialog.setLocationRelativeTo(tabbedpane.getTabComponentAt(tab));
		}
		dialog.pack();
		dialog.setVisible(true);
	}

	/**
	 * Close command.
	 * 
	 */
	private class CommandClose extends AbstractAction {

		public CommandClose(String action) {
			super(action);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			dialog.setVisible(false);
			// dialog.dispose();
		}
	}

	/**
	 * Remove the current tab command.
	 * 
	 */
	private class CommandRemove extends AbstractAction {

		public CommandRemove(String action) {
			super(action);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			if (tab == tabbedpane.getTabCount()) {
			} else if (tab > 0 && tab < tabbedpane.getTabCount()) {
				tabbedpane.removeTabAt(tab);
			}
			dialog.setVisible(false);
			// dialog.dispose();
		}
	}

	/**
	 * Change the tab description command.
	 * 
	 */
	private class CommandChange extends AbstractAction {

		public CommandChange(String action) {
			super(action);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			tabbedpane.setTitleAt(tab, newTitle.getText());
			tabbedpane.setToolTipTextAt(tab, textArea.getText());
			dialog.setVisible(false);
			// dialog.dispose();
		}
	}

	/**
	 * Change the tab description command.
	 * 
	 */
	private class CommandChangeFormat extends AbstractAction {

		public CommandChangeFormat(String action) {
			super(action);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			if (chooser == null) {
				chooser = new StyleChooserDialog(TabPanelEditor.this);
			}

			String style = chooser.showDialog("");
			Component tabc = tabbedpane.getTabComponentAt(tab);
			if (tabc instanceof TabComponent) {
				((TabComponent) tabc).updateTabComponent(new StyleObject(
						newTitle.getText(), style));
			} else {
				tabbedpane.setTitleAt(tab, newTitle.getText());
				tabbedpane.setToolTipTextAt(tab, textArea.getText());
			}
			dialog.setVisible(false);
		}
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				// Create and set up the content pane.
				TabPanelEditor demo = new TabPanelEditor();
				demo.showEditDialog(new JTabbedPane(), 0);
				// Display the window.
			}
		});
	}
}
