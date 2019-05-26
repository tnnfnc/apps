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
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import it.tnnfnc.apps.application.ui.style.I_StyleFormatter;
import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.ui.style.PredefinedStyles;
import it.tnnfnc.apps.application.ui.style.StyleFormatter;
import it.tnnfnc.apps.application.ui.style.StyleObject;

public class CategorySelector extends JList<I_StyleObject> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComponent rightContainer = new JPanel();// right panel
	private JList<I_StyleObject> list; // props.
	// selector
	private DefaultListModel<I_StyleObject> model;
	private I_StyleFormatter formatter;

	public CategorySelector() {
		super();

		createGUI();
	}

	private final void createGUI() {
		// The model
		model = new DefaultListModel<I_StyleObject>();
		// The list
		list = new JList<I_StyleObject>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setCellRenderer(new InternalListRenderer(list.getCellRenderer()));
		list.addListSelectionListener(new PropertiesSelectionListener());
		// list.setCursor(new Cursor(Cursor.HAND_CURSOR));

		formatter = new StyleFormatter();

		// Right Container with
		JPanel _rightContainer = new JPanel();
		_rightContainer.setLayout(new BorderLayout());

		_rightContainer.add(rightContainer, BorderLayout.CENTER);

		// Left Container
		JPanel _leftContainer = new JPanel();
		_leftContainer.setLayout(new BoxLayout(_leftContainer,
				BoxLayout.PAGE_AXIS));
		_leftContainer.add(new JScrollPane(list));
		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		_leftContainer.setMinimumSize(minimumSize);
		_rightContainer.setMinimumSize(minimumSize);

		// Split pane
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		split.setDividerLocation(minimumSize.width);
		split.setLeftComponent(_leftContainer);
		split.setRightComponent(_rightContainer);
		split.setOneTouchExpandable(false);
		split.setDividerSize(2 < split.getDividerSize() / 2 ? 2 : split
				.getDividerSize());

		this.setLayout(new BorderLayout());
		this.add(split, BorderLayout.CENTER);
	}

	/**
	 * Enable every panel.
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		for (int i = 0; i < model.size(); i++) {
			((JComponent) model.get(i)).setEnabled(b);
		}
	}

	/**
	 * Call the update on every panel.
	 * 
	 * @see it.tnnfnc.apps.application.properties.AbstractEditingPanel#initGUI()
	 */
	public void init() {

	}

	/**
	 * Call the change on every panel.
	 * 
	 * @see it.tnnfnc.apps.application.properties.AbstractEditingPanel#update()
	 */
	public void update() {
		for (int i = 0; i < model.size(); i++) {
			// if (model.get(i).equals(currentItem))
			model.get(i);
		}
	}

	public String getLabel() {
		return null;
	}

	public void addItem(int index, I_StyleObject item) {
		model.add(index, item);
	}

	public void removeItemAt(int index) {
		model.remove(index);
	}

	public void move(int from, int to) {
		if (from < to) {
			I_StyleObject o = model.get(from);
			model.add(to, o);
			model.remove(from);
		} else if (from > to) {
			I_StyleObject o = model.get(to);
			model.add(from, o);
			model.remove(to);
		} else if (from == to) {
		}
	}

	/**
	 * 
	 */
	private class InternalListRenderer implements
			ListCellRenderer<I_StyleObject> {

		private ListCellRenderer<? super I_StyleObject> r;

		public InternalListRenderer(
				ListCellRenderer<? super I_StyleObject> cellRenderer) {
			this.r = cellRenderer;
		}

		@Override
		public Component getListCellRendererComponent(
				JList<? extends I_StyleObject> list, I_StyleObject value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Component c = r.getListCellRendererComponent(list, value, index,
					isSelected, cellHasFocus);
			// this.setText(value.getValue() + "");
			formatter.applyStyle(c, value.getStyle(), isSelected, cellHasFocus);
			System.out.println(value.getValue());
			return c;
		}

	}

	private class PropertiesSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int i = list.getSelectedIndex();
			System.out.println("Value changed at " + i);
		}
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Create and set up the window.
				JFrame frame = new JFrame("Properties panel demo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// Create and set up the content pane.
				CategorySelector demo = new CategorySelector();
				demo.addItem(0,
						new StyleObject("pippo",
								PredefinedStyles.getStyles()[1]));
				demo.addItem(0,
						new StyleObject("pippo",
								PredefinedStyles.getStyles()[7]));
				demo.addItem(0,
						new StyleObject("pippo",
								PredefinedStyles.getStyles()[3]));
				demo.addItem(0,
						new StyleObject("pippo",
								PredefinedStyles.getStyles()[15]));
				demo.addItem(0,
						new StyleObject("pippo",
								PredefinedStyles.getStyles()[7]));
				demo.addItem(0,
						new StyleObject("pippo",
								PredefinedStyles.getStyles()[18]));
				demo.addItem(0,
						new StyleObject("pippo",
								PredefinedStyles.getStyles()[14]));

				frame.getContentPane().add(demo);

				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}
