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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import it.tnnfnc.apps.application.ui.style.I_StyleFormatter;
import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.ui.style.StyleFormatter;
import it.tnnfnc.apps.application.ui.style.StyleObject;

public class LogsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String CLEAR = "CLEAR";
	private static final String PURGE = "PURGE";

	private JList<Object> list;
	private InternalEventListener mouseListener;
	private int logIndex = 0;
	private JTextField logField;
	private DefaultListModel<Object> model;
	private int bufferLength = Integer.MAX_VALUE;
	private I_StyleFormatter style_formatter = new StyleFormatter();
	private ListResourceBundle language = (ListResourceBundle) ResourceBundle
			.getBundle(LocaleBundle.class.getName());

	private JButton clear;
	private JButton purge;

	/**
	 * Create a pop-up window.
	 */
	public LogsPanel() {
		createGUI();

	}

	private void createGUI() {
		mouseListener = new InternalEventListener();
		// Log component
		logField = new JTextField("");
		logField.addMouseListener(mouseListener);
		logField.setEditable(false);
		// logField.setBorder(new EmptyBorder(0, 0, 0, 0));
		model = new DefaultListModel<Object>();
		list = new JList<Object>();
		list.setModel(model);
		list.setCellRenderer(new ThisCellRenderer(list.getCellRenderer()));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.addMouseListener(mouseListener);

		GuiUtility.changeFontSize(list, 0.9F);

		JScrollPane listScroller = new JScrollPane(list);
		listScroller
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScroller
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Buttons
		clear = new JButton(language.getString("Clear"));
		clear.setActionCommand(CLEAR);
		purge = new JButton(language.getString("Purge old logs"));
		purge.setActionCommand(PURGE);
		clear.addActionListener(mouseListener);
		purge.addActionListener(mouseListener);

		JPanel buttons = new JPanel(new BorderLayout());
		JPanel p = new JPanel(new FlowLayout());
		p.add(purge);
		p.add(clear);
		buttons.add(p, BorderLayout.PAGE_START);
		buttons.add(logField, BorderLayout.PAGE_END);

		this.setLayout(new BorderLayout());
		this.add(buttons, BorderLayout.PAGE_START);
		this.add(listScroller, BorderLayout.CENTER);
	}

	/**
	 * Adds a new text line to the log.
	 * 
	 * @param log
	 *            the log text (see {@link it.tnnfnc.apps.application.ui.style.StyleObject
	 *            StatusFormattedText}). for a description of types allowed.
	 */
	public void appendLog(I_StyleObject log) {
		if (model.size() >= bufferLength && bufferLength > 0) {
			model.remove(0);
			model.trimToSize();
		} else if (bufferLength == 0) {
			return;
		}
		log.set(new String(++logIndex + ".  " + Utility.now() + "  "
				+ log.getValue()), log.getStyle());
		logField.setText(log.getValue().toString());
		// model.addElement(log);
		model.insertElementAt(log, 0);
	}

	/**
	 * Adds a new text line to the log.
	 * 
	 * @param log
	 *            the log text (see {@link it.tnnfnc.apps.application.ui.style.StyleObject
	 *            StatusFormattedText}). for a description of types allowed.
	 * @param format
	 *            the format as CSS.
	 */
	public void appendLog(String log, String format) {
		I_StyleObject so = new StyleObject(log, format);
		appendLog(so);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		try {
			// list.setEnabled(b);
			purge.setEnabled(b);
			clear.setEnabled(b);
		} catch (Exception e) {
		}
	}

	/**
	 * Returns the action log in the content.
	 * 
	 * @return the logs array.
	 */
	public StyleObject[] getLog() {
		return (StyleObject[]) model.toArray();
	}

	/**
	 * Clears the action log.
	 */
	public void clearLog() {
		model.removeAllElements();
		logIndex = 0;
	}

	/**
	 * Perform the specified action when the mouse is clicked. This method does
	 * nothing.
	 * 
	 * @param e
	 *            the mouse event.
	 */
	public void performOnClick(MouseEvent e) {
	}

	/**
	 * Set the maximum visible row count. The actually content number of
	 * displayed rows is calculated from the model.
	 * 
	 * @param max
	 *            the maximum number of displayed rows.
	 */
	public void setVisibleRowCount(int max) {
		if (list.getModel() == null) {
			// System.out.println(" 0");
		} else {
			int size = list.getModel().getSize();
			if (size <= 2) {
				list.setVisibleRowCount(4);
				// System.out.println(" 4");
			} else if (2 < size && size < max) {
				// System.out.println(size + 2);
				list.setVisibleRowCount(size + 2);
			} else {
				list.setVisibleRowCount(max);
				// System.out.println(max);
			}
		}
	}

	/**
	 * Sets the log buffer length.
	 * 
	 * @param length
	 *            the buffer length.
	 */
	public void setLogBuffer(int length) {
		this.bufferLength = length;
		int size = model.size();
		if (size > bufferLength) {
			while (model.size() > bufferLength) {
				model.remove(model.size() - 1);
				// model.trimToSize();
			}
			logField.setText(""
					+ ((model.size() - 1 > -1) ? model.get(model.size() - 1)
							: ""));
		}
	}

	/**
	 * The mouse click over a selected value stops the editing.
	 */
	private class InternalEventListener extends MouseAdapter implements
			ActionListener {

		/*
		 * (non-Javadoc) Trigger a sorting. Sort ascending, descending, unsort.
		 * 
		 * @see
		 * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			performOnClick(e);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(CLEAR)) {
				trim(0);
				appendLog(new StyleObject(language.getString("Clear")));
			}
			if (e.getActionCommand().equals(PURGE)) {
				trim(10);
				appendLog(new StyleObject(language.getString("Purge old logs")));
			}
		}

		private void trim(int i) {
			if (model.size() > i) {
				while (model.size() > i) {
					model.remove(model.size() - 1);
				}
				logField.setText(""
						+ ((model.size() - 1 > -1) ? model.get(model.size() - 1)
								: ""));
			}
		}
	}

	private class ThisCellRenderer extends JLabel implements
			ListCellRenderer<Object> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1447093861488195535L;

		private ListCellRenderer<? super Object> listCellRenderer;

		public ThisCellRenderer(ListCellRenderer<? super Object> r) {
			this.listCellRenderer = r;
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JPanel p = new JPanel(new BorderLayout());
			Component c = listCellRenderer.getListCellRendererComponent(list,
					value, index, isSelected, cellHasFocus);
			p.add(c, BorderLayout.CENTER);

			if (value instanceof I_StyleObject) {
				I_StyleObject avalue = (I_StyleObject) value;
				style_formatter.applyStyle(c, avalue.getStyle(), isSelected,
						cellHasFocus);
			}

			return p;
		}
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame f = new JFrame();
				f.setSize(600, 300);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				LogsPanel sb = new LogsPanel();
				sb.appendLog("ciao", "");
				sb.appendLog("ciao", "");
				sb.appendLog("ciao", "");
				sb.appendLog("ciao", "");
				sb.appendLog("ciao", "");
				sb.appendLog(new StyleObject(
						"log 1",
						"font-family:Dialog;font-style:plain;font-size:12;color:0,0,0;background-color:238,238,238;border-style:solid;border-color:0,255,0;border-top-width:1;border-left-width:1;border-bottom-width:1;border-right-width:1;"));
				sb.appendLog(new StyleObject(
						"log 2",
						"font-family:Dialog;font-style:plain;font-size:12;color:0,0,0;background-color:238,238,238;border-style:solid;border-color:255,204,0;border-top-width:1;border-left-width:1;border-bottom-width:1;border-right-width:1;"));

				f.getContentPane().add(sb, BorderLayout.CENTER);

				f.setVisible(true);
			}
		});

	}

}
