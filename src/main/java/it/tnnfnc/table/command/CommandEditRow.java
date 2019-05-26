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
package it.tnnfnc.table.command;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import it.tnnfnc.apps.application.ui.GuiUtility;
import it.tnnfnc.datamodel.I_IndexModel;
import it.tnnfnc.table.row.AbstractTableRowDetail;
import it.tnnfnc.table.row.I_TableRow;

public class CommandEditRow<E> extends TableCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JDialog dialog;
	private AbstractTableRowDetail<I_TableRow<?>> editPanel;
	private I_TableRow<?> entry;
	private JPanel buttons = new JPanel();
	public static final String ACTION_COMMAND = "Edit row";

	/**
	 * Create a command.
	 * 
	 * @param t
	 *            the table.
	 * @param p
	 *            the editing row panel.
	 */
	public CommandEditRow(JTable t, AbstractTableRowDetail<I_TableRow<?>> p) {
		super(t);
		this.editPanel = p;
	}

	/**
	 * Create a command.
	 * 
	 * @param name
	 *            command name.
	 * @param t
	 *            the table.
	 * @param p
	 *            the editing row panel.
	 */
	public CommandEditRow(String name, JTable t, AbstractTableRowDetail<I_TableRow<?>> p) {
		super(name, t);
		this.editPanel = p;
	}

	/**
	 * Create a command.
	 * 
	 * @param name
	 *            command name.
	 * @param icon
	 *            command icon.
	 * @param t
	 *            the table.
	 * @param p
	 *            the editing row panel.
	 */
	public CommandEditRow(String name, Icon icon, JTable t,
			AbstractTableRowDetail<I_TableRow<?>> p) {
		super(name, icon, t);
		this.editPanel = p;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();

		if (row > -1 && col > -1 && table.getRowCount() > 0) {
			I_IndexModel<?> model = getModel();
			// Object entry = model.getEntry(row);
			entry = (I_TableRow<?>) model.getEntry(row);
			editPanel.init(entry);
			showDialog();
			setSelectedRows(row, row);
		}

	}

	private void showDialog() {
		class InternalWindowsListener extends WindowAdapter {
			@Override
			public void windowClosing(WindowEvent event) {
			}
		}
		if (dialog == null) {
			dialog = new JDialog();
			dialog.setModal(true);
			dialog.addWindowListener(new InternalWindowsListener());
			dialog.getContentPane().setLayout(new BorderLayout());
			dialog.getContentPane().add(
					GuiUtility.createInputPanel(editPanel, buttons),
					BorderLayout.CENTER);
			JButton change = new JButton(language.getString("ok"));
			JButton cancel = new JButton(language.getString("Cancel"));
			ChangeButtonListener listener = new ChangeButtonListener();
			change.addActionListener(listener);
			cancel.addActionListener(listener);
			change.setActionCommand(ChangeButtonListener.OK);
			cancel.setActionCommand(ChangeButtonListener.CANCEL);
			buttons.add(change);
			buttons.add(cancel);
			// dialog.getContentPane().add(editPanel, BorderLayout.CENTER);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		}
		// createGui();
		// dialog.setLocationRelativeTo(super.table);
		dialog.pack();
		dialog.setVisible(true);

	}

	/**
	 * Listener for the change button.
	 * 
	 */
	private class ChangeButtonListener implements ActionListener {

		private static final String OK = "OK";
		private static final String CANCEL = "CANCEL";

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getActionCommand().equals(OK)) {
				try {
					editPanel.update(entry);
				} catch (IllegalArgumentException e) {
				}
				dialog.setVisible(false);
			} else if (event.getActionCommand().equals(CANCEL)) {
				dialog.setVisible(false);
			} else {
			}
		}
	}
}
