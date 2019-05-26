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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

public class PopupListWindow<E> extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The content.
	 */
	protected JList<E> list;
	/**
	 * The upper container.
	 */
	protected JPanel upperContent;
	/**
	 * 
	 */
	private ListMouseListener mouseListener;
	/**
	 * Sort content display origin.
	 */
	private static final Point listOrigin = new Point();

	// private static final int magic_number = 0;

	/**
	 * Create a pop-up window.
	 */
	public PopupListWindow() {
		createListWindow();
		createListPanel();
	}

	/**
	 * Create a pop-up window.
	 * 
	 * @param l
	 *            the displayed list.
	 */
	public PopupListWindow(JList<E> l) {
		list = l;
		createListWindow();
		createListPanel();
	}

	/**
	 * Implement for managing the behavior when the window looses the focus.
	 */
	public void windowsLostFocus() {
	}

	/**
	 * Implement for managing the behavior when the window gains the focus.
	 */
	public void windowsGainFocus() {
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
	 * Create the content.
	 */
	private void createListPanel() {
		if (list == null) {
			list = new JList<E>();
		}
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);

		GuiUtility.changeFontSize(list, 0.9F);

		JScrollPane listScroller = new JScrollPane(list);
		listScroller
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScroller
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(listScroller, BorderLayout.CENTER);
		mouseListener = new ListMouseListener();

		list.addMouseListener(mouseListener);
	}

	/**
	 * Create the window for the content.
	 */
	private void createListWindow() {
		setModal(false);
		setAlwaysOnTop(true);
		setUndecorated(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		upperContent = new JPanel();
		upperContent
				.setLayout(new BoxLayout(upperContent, BoxLayout.PAGE_AXIS));
		p.add(upperContent, BorderLayout.NORTH);
		setContentPane(p);
		// Exiting: focus lost to exit and cancel editing
		addWindowFocusListener(new ListWindowFocusListener());
	}

	/**
	 * Add a component to the pop-up content. Mouse listener is added.
	 * 
	 * @param item
	 *            the item.
	 */
	public void addItem(JComponent item) {
		// Font
		item.setFont(list.getFont().deriveFont(list.getFont().getSize2D()));
		item.setFocusable(true);
		item.addMouseListener(mouseListener);

		upperContent.add(Box.createHorizontalGlue());
		upperContent.add(item);
		upperContent.add(Box.createHorizontalGlue());
	}

	/**
	 * Remove a component from the pop-up content. Mouse listener is removed.
	 * 
	 * @param item
	 *            the item.
	 */
	public void removeItem(JComponent item) {
		item.removeMouseListener(mouseListener);
		upperContent.remove(item);
	}

	/**
	 * Display the content under the component with the same width.
	 * 
	 * @param c
	 *            the component.
	 */
	public void display(Component c) {
		// pop up the content
		Point hL = c.getLocationOnScreen();
		Rectangle hR = c.getBounds();
		listOrigin.setLocation(hL.x, hL.y + hR.height);
		setMinimumSize(new Dimension(hR.width, 0));
		setLocation(listOrigin);
		pack();
		setVisible(true);
	}

	/**
	 * Display the content under the component with the same width.
	 * 
	 * @param c
	 *            the component.
	 * @param offset
	 *            number of pixel between the owner component and the popup.
	 */
	public void display(Component c, int offset) {
		// pop up the content
		Point hL = c.getLocationOnScreen();
		Rectangle hR = c.getBounds();
		listOrigin.setLocation(hL.x, hL.y + hR.height + offset);
		setMinimumSize(new Dimension(hR.width, 0));
		setLocation(listOrigin);
		pack();
		setVisible(true);
	}

	/**
	 * Get the popup content.
	 * 
	 * @return the popup content.
	 */
	public JList<E> getList() {
		return this.list;
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
	 * When the popup filter looses focus the editing is canceled and the popup
	 * window is disposed and .
	 */
	private class ListWindowFocusListener implements WindowFocusListener {

		@Override
		public void windowGainedFocus(WindowEvent e) {
			windowsGainFocus();
		}

		@Override
		public void windowLostFocus(WindowEvent e) {
			windowsLostFocus();
		}

	}

	/**
	 * The mouse click over a selected value stops the editing.
	 */
	private class ListMouseListener extends MouseAdapter {

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

	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		final PopupListWindow<Object> t = new PopupListWindow<Object>();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame("AbstractListWindow");
				final JTextField c = new JTextField("component");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.getContentPane().add(c);
				t.addItem(new JTextField("item 1"));
				t.addItem(new JTextField("item 2"));
				t.addItem(new JTextField("item 3"));
				t.addItem(new JTextField("item 4"));
				t.addItem(new JButton("item 4"));
				Object[] oo = new Object[] { "content 1", "content 2",
						"content 3" };
				t.list.setListData(oo);
				t.setVisibleRowCount(4);
				t.pack();
				c.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						t.display(c);
					}
				});
				f.setVisible(true);
			}
		});
	}
}
