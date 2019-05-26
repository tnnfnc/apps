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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import it.tnnfnc.apps.application.ui.ApplicationClipboard;
import it.tnnfnc.apps.application.ui.PopupListWindow;
import it.tnnfnc.apps.application.undo.I_TracedStatus;
import it.tnnfnc.apps.application.undo.ObjectStatus;
import it.tnnfnc.apps.application.undo.ObjectStatusModel;
import it.tnnfnc.datamodel.I_IndexModel;

/**
 * Restore the changes history.
 * 
 * @author franco toninato
 * 
 * 
 * @param <T>
 *            the object type.
 */
public class CommandCellHistory extends TableCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CLEAR = "clear history";
	private static final String CLOSE = "close";
	private static final String SET = "set";
	private static final String COPY = "copy";
	public static final String ACTION_COMMAND = "SetHistory";
	private ObjectStatusModel<?> statusModel;
	private PopupListWindow<Object> popup;
	private JList<Object> history;
	private int col;
	private int row;
	private Object value;
	private Object traceID;
	private JTextField cellValue;
	private boolean isChangeEnabled = false;

	/**
	 * Create a command.
	 * 
	 * @param t
	 *            the table.
	 * @param m
	 *            the trace model.
	 */
	public <T> CommandCellHistory(JTable t, ObjectStatusModel<T> m) {
		super(t);
		setTraceModel(m);
		history = new JList<Object>();
	}

	/**
	 * Create a command.
	 * 
	 * @param name
	 *            command name.
	 * @param t
	 *            the table.
	 * @param m
	 *            the trace model.
	 */
	public <T> CommandCellHistory(String name, JTable t, ObjectStatusModel<T> m) {
		super(name, t);
		setTraceModel(m);
		history = new JList<Object>();
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
	 * @param m
	 *            the trace model.
	 */
	public <T> CommandCellHistory(String name, Icon icon, JTable t, ObjectStatusModel<T> m) {
		super(name, icon, t);
		setTraceModel(m);
		history = new JList<Object>();
	}

	/**
	 * Set the trace model.
	 * 
	 * @param m
	 *            the trace model.
	 */
	public <T> void setTraceModel(ObjectStatusModel<T> m) {
		statusModel = m;
	}

	/**
	 * Enable the cell change from an history value.
	 * 
	 * @param b
	 *            enable or disable.
	 */
	public void setChangeEnable(boolean b) {
		isChangeEnabled = b;
		popup = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		// Display a popup over the table starting from the bottom left of the
		// cell
		row = table.getSelectedRow();
		col = table.getSelectedColumn();
		if (row > -1 && col > -1) {
			I_IndexModel<?> model = getModel();
			Object entry = model.getEntry(row);
			if (entry instanceof I_TracedStatus) {
				I_TracedStatus o = (I_TracedStatus) entry;
				traceID = o.getTraceID(table.convertColumnIndexToModel(col));
				ObjectStatus<?>[] trace = (ObjectStatus<?>[]) statusModel.getHistory(traceID);
				if (popup == null) {
					createPopup();
				}
				// Show the history panel
				cellValue.setText(table.getValueAt(row, col) + "");
				Point tl = table.getLocationOnScreen();
				Point cl = new Point(table.getCellRect(row, col, false).x, table.getCellRect(row, col, true).y);
				tl.setLocation(tl.x + cl.x, tl.y + cl.y);
				history.setListData(trace);
				popup.setMinimumSize(new Dimension(table.getCellRect(row, col, true).width, 0));
				popup.pack();
				popup.setLocation(tl);
				popup.setVisible(true);
			}
		}

	}

	/**
	 * Create the window for the content.
	 */
	private void createPopup() {
		final JPanel buttons = new JPanel();
		// p.setLayout(new GridLayout(1, 2));
		buttons.setLayout(new FlowLayout());
		JButton clear = new JButton(language.getString("Clear History"));
		JButton close = new JButton(language.getString("Close"));
		JButton set = new JButton(language.getString("Set"));
		JButton copy = new JButton(language.getString("Copy"));

		clear.setActionCommand(CLEAR);
		close.setActionCommand(CLOSE);
		set.setActionCommand(SET);
		copy.setActionCommand(COPY);
		ActionListener buttonsListener = new InternalListener();
		clear.addActionListener(buttonsListener);
		close.addActionListener(buttonsListener);
		set.addActionListener(buttonsListener);
		copy.addActionListener(buttonsListener);
		if (isChangeEnabled) {
			buttons.add(set);
		}
		buttons.add(copy);
		buttons.add(clear);
		buttons.add(close);

		cellValue = new JTextField(table.getValueAt(row, col) + "");
		cellValue.setEditable(false);

		popup = new PopupListWindow<Object>(history) {

			private static final long serialVersionUID = 1L;

			@Override
			public void windowsLostFocus() {
				popup.dispose();
			}

			@Override
			public void windowsGainFocus() {
			}

			@Override
			public void performOnClick(MouseEvent e) {
				if (e.getSource() == buttons) {
				}
				int selIndex = history.getSelectedIndex();
				if (selIndex >= 0) {
					value = ((ObjectStatus<?>) history.getSelectedValue()).getStatus();
					// System.out.println(this.getClass().getName()
					// + " selected value = " + value);
				}
			}
		};

		popup.addItem(buttons);
		popup.addItem(cellValue);

	}

	private class InternalListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getActionCommand() == CLEAR) {
				if (traceID != null) {
					statusModel.trimToSize(traceID, 0);
					history.setListData(new ObjectStatus<?>[0]);
					value = null;
				}
			} else if (event.getActionCommand() == CLOSE) {
				popup.windowsLostFocus();
				// return;
			} else if (event.getActionCommand() == SET) {
				if (value != null) {
					table.setValueAt(value, row, col);
					table.repaint();
					popup.windowsLostFocus();
				}
			} else if (event.getActionCommand() == COPY) {
				if (value != null) {
					ApplicationClipboard.copyToClipboard(value + "");
				}
			}

		}
	}
}
