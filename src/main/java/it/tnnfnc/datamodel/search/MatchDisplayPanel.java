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
package it.tnnfnc.datamodel.search;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import it.tnnfnc.datamodel.I_HyperLinkListener;
import it.tnnfnc.datamodel.LinkEvent;


/**
 * A component that displays a content of match results from a regular
 * expression search and allows the user navigate to the selected item.
 * 
 * @author franco toninato
 * 
 */
public class MatchDisplayPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MatchResultModel model;
	private JList<Object> list;

	/**
	 * Class constructor. A default model is created.
	 */
	public MatchDisplayPanel() {
		this.model = new MatchResultModel();
		createGUI();
	}

	/**
	 * Create a content with a model.
	 * 
	 * @param model
	 *            the data model.
	 */
	public MatchDisplayPanel(MatchResultModel model) {
		this.model = model;
		createGUI();
	}

	/**
	 * Create the GUI.
	 */
	private void createGUI() {
		setLayout(new BorderLayout());
		list = new JList<Object>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.add(list, BorderLayout.CENTER);
		list.addMouseListener(new ListListener());
	}

	/**
	 * Notify listeners that data or their order is changed.
	 * 
	 * @param event
	 *            the change event.
	 */
	protected void fireLinkEvent(LinkEvent event) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		if (event == null)
			return;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == I_HyperLinkListener.class) {
				((I_HyperLinkListener) listeners[i + 1]).link(event);
			}
		}
	}

	/**
	 * Add a link listener.
	 * 
	 * @param listener
	 *            the link listener.
	 */
	public void addLinkListener(I_HyperLinkListener listener) {
		listenerList.add(I_HyperLinkListener.class, listener);
	}

	/**
	 * Remove a link listener.
	 * 
	 * @param listener
	 *            the listener.
	 */
	public void removeLinkListener(I_HyperLinkListener listener) {
		listenerList.remove(I_HyperLinkListener.class, listener);
	}

	/**
	 * Clear the matches list.
	 */
	public void clear() {
		this.model.clear();
	}

	/**
	 * Get the model.
	 * 
	 * @return the model.
	 */
	public MatchResultModel getModel() {
		return this.model;
	}

	/**
	 * Set the model.
	 * 
	 * @param model
	 *            the content model.
	 */
	public void setModel(MatchResultModel model) {
		this.model = model;
		list.setModel(model);
	}

	/**
	 * Refresh the list and update the found matches from the input.
	 * 
	 * @param m
	 *            the model.
	 */
	public void updateModel(MatchResultModel m) {
		model.clear();
		for (int i = 0; i < m.getSize(); i++) {
			model.addElement(new MatchResultModel.ItemType(m.getResultAt(i), m
					.getSourcetAt(i)));
		}
	}

	/**
	 * The mouse click over a selected value stops the editing.
	 */
	private class ListListener extends MouseAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() > 0) {
				if (list.getValueIsAdjusting() == false) {
					int selIndex = list.getSelectedIndex();
					if (selIndex > -1 && model.getSourcetAt(selIndex) != null) {
						fireLinkEvent(new LinkEvent(MatchDisplayPanel.this,
								model.getSourcetAt(selIndex)));
					}
				}
			}
		}

	}

	

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Create and set up the window.
				JFrame frame = new JFrame("Match list panel demo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// Create and set up the content pane.
				MatchDisplayPanel demo = new MatchDisplayPanel();
				frame.getContentPane().add(demo);

				// Display the window.
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}
