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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Utility class for layout component aligning field into separated and
 * resizable columns.
 * 
 * @author Franco Toninato
 */
public class FieldLayout {

	protected JPanel container;
	private GridBagConstraints gbc;
	private ArrayList<Integer> columns;

	/*
	 * Creates the detail pane.
	 */
	public FieldLayout(JPanel container) {
		this.container = container;
		container.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		initConstraints();
		columns = new ArrayList<Integer>();
	}

	/**
	 * Append a label-field-unit to a column.
	 * 
	 * @param column
	 *            layout column.
	 * @param span
	 *            span element to more columns.
	 * 
	 * @param label
	 *            the label for the component.
	 * @param field
	 *            the component.
	 * @param unit
	 *            the unit of measure. It can be null.
	 * @param container
	 *            the container component where placing the labelled component.
	 * @param gbc
	 *            the layout constraints.
	 */
	public void append(int column, int span, JComponent label, JComponent field) {
		this.append(column, span, label, field, null);
	}

	/**
	 * Append a label-field-unit to a column.
	 * 
	 * @param column
	 *            layout column.
	 * @param span
	 *            span element to more columns.
	 * 
	 * @param label
	 *            the label for the component.
	 * @param field
	 *            the component.
	 * @param unit
	 *            the unit of measure. It can be null.
	 * @param container
	 *            the container component where placing the labelled component.
	 * @param gbc
	 *            the layout constraints.
	 */
	public void append(int column, int span, JComponent label,
			JComponent field, JComponent unit) {
		gbc.gridx = column;
		gbc.gridwidth = span;
		gbc.insets = new Insets(2, 2, 0, 2); // top, left, bottom, right,
		// Append to the last row of the specified column
		try {
			gbc.gridy = columns.get(column);
			gbc.gridy++;
		} catch (IndexOutOfBoundsException e) {
			columns.add(Integer.valueOf(0));
			gbc.gridy = 0;
		}
		JPanel panel = createPanel();
		if (label != null) {
			panel.add(label, BorderLayout.LINE_START);
		}
		if (field != null) {
			panel.add(field, BorderLayout.CENTER);
		}
		if (unit != null) {
			panel.add(unit, BorderLayout.LINE_END);
		}
		container.add(panel, gbc);
		// Update the last y
		columns.set(column, Integer.valueOf(gbc.gridy));
		gbc.gridwidth = 1;
	}

	/**
	 * Append a label-field-unit to a column.
	 * 
	 * @param column
	 *            layout column.
	 * @param span
	 *            span element to more columns.
	 * 
	 * @param label
	 *            the label for the component.
	 * @param field
	 *            the component.
	 * @param unit
	 *            the unit of measure. It can be null.
	 * @param container
	 *            the container component where placing the labelled component.
	 * @param gbc
	 *            the layout constraints.
	 */
	public void pair(int column, int span, JComponent label, JComponent field) {
		pair(column, span, label, field, null);
	}

	/**
	 * Append a label-field-unit to a column.
	 * 
	 * @param column
	 *            layout column.
	 * @param span
	 *            span element to more columns.
	 * 
	 * @param label
	 *            the label for the component.
	 * @param field
	 *            the component.
	 * @param unit
	 *            the unit of measure. It can be null.
	 * @param container
	 *            the container component where placing the labelled component.
	 * @param gbc
	 *            the layout constraints.
	 */
	public void pair(int column, int span, JComponent label, JComponent field,
			JComponent unit) {
		gbc.gridx = column + 1;
		gbc.gridy = columns.get(column);
		// Update the columns
		columns.add(Integer.valueOf(gbc.gridy + 1));
		gbc.gridwidth = span;
		gbc.insets = new Insets(2, 10, 0, 2); // top, left, bottom, right,
		JPanel panel = createPanel();
		if (label != null) {
			panel.add(label, BorderLayout.LINE_START);
		}
		if (field != null) {
			panel.add(field, BorderLayout.CENTER);
		}
		if (unit != null) {
			panel.add(unit, BorderLayout.LINE_END);
		}
		container.add(panel, gbc);
		// Update the last y
		// columns.set(column, Integer.valueOf(gbc.gridy + 1));
		gbc.gridwidth = 1;
	}

	/**
	 * @return
	 */
	private JPanel createPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, gbc.insets.top + 2));
		return panel;
	}

	/**
	 * Initialize the grid-bag layout constraints.
	 * 
	 * @param gbc
	 *            the object constraints. No resize, line start, one spanned
	 *            column and row.
	 */
	private void initConstraints() {
		/* Constraints default */
		gbc.gridx = 0; // col
		gbc.gridy = 0; // row
		gbc.gridwidth = 1; // Spanned cols
		gbc.gridheight = 1; // Spanned rows
		gbc.ipadx = 4; // Enlarges component of x-pixels
		gbc.ipady = 4; // Enlarges component of y-pixels
		gbc.fill = GridBagConstraints.HORIZONTAL; // Resizes component
		// External padding
		gbc.weightx = 1.0; // Gives extra x-space to the component
		gbc.weighty = 0.0; // Gives extra y-space to the component
		// gbc.insets = new Insets(1, 2, 1, 2); // top, left, bottom, right,
		gbc.insets = new Insets(2, 4, 0, 0); // top, left, bottom, right,
		// External padding
		gbc.anchor = GridBagConstraints.PAGE_START; // Alignment
		// gbc.anchor = GridBagConstraints.LINE_START; // Alignment
		gbc.weightx = 1.0; // Gives extra x-space to the component
		gbc.weighty = 0.0; // Gives extra y-space to the component
	}

}