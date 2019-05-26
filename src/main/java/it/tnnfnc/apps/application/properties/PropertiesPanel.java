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
package it.tnnfnc.apps.application.properties;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Enumeration;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PropertiesPanel extends AbstractEditingPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JComponent rightContainer = new JPanel();// right panel
	private JComponent currentItem = new JPanel();// current props. panel
	private JPanel buttons = new JPanel(new FlowLayout());// buttons panel
	private JLabel rightTitle = new JLabel();
	private JLabel leftTitle = new JLabel();
	private JList<AbstractEditingPanel> list = new JList<AbstractEditingPanel>(); // props.
	private DefaultListModel<AbstractEditingPanel> model = new DefaultListModel<AbstractEditingPanel>();

	// private ArrayList<Action> commands = new ArrayList<Action>();// buttons
	// private JSplitPane splitPane = new JSplitPane();
	// selector

	public PropertiesPanel() {
		super();
		list.setCellRenderer(new InternalListRenderer());
		list.setModel(model);
		list.addListSelectionListener(new PropertiesSelectionListener());
		createGUI();
		initGUI();
	}

	private final void createGUI() {
		// The list
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCursor(new Cursor(Cursor.HAND_CURSOR));
		list.setLayoutOrientation(JList.VERTICAL);

		// Titles
		leftTitle.setText("");
		rightTitle.setText("");
		// Layout

		// Right Container with
		JPanel _rightContainer = new JPanel();
		_rightContainer.setLayout(new BorderLayout());

		_rightContainer.add(getRightLable(), BorderLayout.NORTH);
		_rightContainer.add(rightContainer, BorderLayout.CENTER);
		rightContainer.add(currentItem);

		// Left Container
		JPanel _leftContainer = new JPanel();
		_leftContainer.setLayout(new BoxLayout(_leftContainer,
				BoxLayout.PAGE_AXIS));
		_leftContainer.add(getLeftLable());
		_leftContainer.add(new JScrollPane(list));

		// Split pane
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		_leftContainer.setMinimumSize(minimumSize);
		_rightContainer.setMinimumSize(minimumSize);
		split.setDividerLocation(minimumSize.width);
		split.setLeftComponent(_leftContainer);
		split.setRightComponent(_rightContainer);

		this.setLayout(new BorderLayout());
		this.add(split, BorderLayout.CENTER);

		_rightContainer.add(buttons, BorderLayout.SOUTH);

	}

	/**
	 * @return
	 */
	private JLabel getLeftLable() {
		return leftTitle;
	}

	/**
	 * @return
	 */
	private JLabel getRightLable() {
		return rightTitle;
	}

	/**
	 * Add a preferences panel.
	 * 
	 */
	public void addPanel(AbstractEditingPanel p) {
		model.addElement(p);
		list.setSelectedIndex(0);
	}

	/**
	 * Add an action button.
	 * 
	 */
	public void addButton(Action a) {
		// setCommand(a);
		buttons.add(new JButton(a));
	}

	/**
	 * Enable a panel.
	 * 
	 */
	public void setEnabled(String name, boolean b) {
		for (int i = 0; i < model.size(); i++) {
			if (model.get(i).getLabel().equals(name)) {
				((JComponent) model.get(i)).setEnabled(b);
				break;
			}
		}
	}

	/**
	 * Get the list of panels.
	 * 
	 * @return the list of panels.
	 * 
	 */
	public Enumeration<AbstractEditingPanel> getPanels() {
		return model.elements();
	}

	/**
	 * Set the selected item.
	 * 
	 * @param index
	 *            the index.
	 */
	public void setSelectedPanel(int index) {
		this.list.setSelectedIndex(index);

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
			try {
				((JComponent) model.get(i)).setEnabled(b);
			} catch (Exception e) {
				// is null
			}
		}
	}

	/**
	 * Call the init on every panel.
	 * 
	 * @see it.tnnfnc.apps.application.properties.AbstractEditingPanel#initGUI()
	 */
	@Override
	public void initGUI() {
		for (int i = 0; i < model.size(); i++) {
			// if (model.get(i).equals(currentItem))
			model.get(i).initGUI();
		}
	}

	/**
	 * Call the change on every panel.
	 * 
	 * @see it.tnnfnc.apps.application.properties.AbstractEditingPanel#update()
	 */
	@Override
	public void update() {
		for (int i = 0; i < model.size(); i++) {
			// if (model.get(i).equals(currentItem))
			model.get(i).update();
		}
	}

	@Override
	public String getLabel() {
		return null;
	}

	private class PropertiesSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			// int i = e.getLastIndex();
			int i = list.getSelectedIndex();
			// System.out.println(i);
			rightContainer.getLayout().removeLayoutComponent(currentItem);
			currentItem.setVisible(false);
			currentItem = list.getModel().getElementAt(i);
			rightContainer.add(currentItem);
			// update(); If update the input is canceled
			currentItem.setVisible(true);
			rightTitle.setText(list.getModel().getElementAt(i).getLabel());
			rightTitle.setIcon(null);
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
				class Dummyprop extends AbstractEditingPanel {
					String text = "";
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void initGUI() {

					}

					@Override
					public void update() {

					}

					@Override
					public String getLabel() {
						return "Test " + text;
					}
				}
				// Create and set up the content pane.
				PropertiesPanel demo = new PropertiesPanel();
				demo.addPanel(new Dummyprop());
				demo.addPanel(new Dummyprop());
				demo.addPanel(new Dummyprop());
				frame.getContentPane().add(demo);

				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * 
	 */
	private class InternalListRenderer extends JLabel implements
			ListCellRenderer<AbstractEditingPanel> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InternalListRenderer() {
			setOpaque(true);
			setHorizontalAlignment(SwingConstants.LEADING);
			setVerticalAlignment(CENTER);
		}

		@Override
		public Component getListCellRendererComponent(
				JList<? extends AbstractEditingPanel> list,
				AbstractEditingPanel value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			// Set the icon and text. If icon was null, say so.
			setText(value.getLabel());
			setFont(list.getFont());

			return this;
		}

	}
}
