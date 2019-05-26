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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Utility class for managing the layout of user panels.
 * 
 * @author Franco Toninato
 * @version %I%, %G%
 * @since 1.2
 */
public class GridBagLayoutUtility {

	/*
	 * Creates the detail pane.
	 */
	private GridBagLayoutUtility() {
	}

	/**
	 * Places an in-line input component with a label and a unit of measure into
	 * a container. The unit of measure is laid after the input component.
	 * 
	 * @param label
	 *            the label for the component.
	 * @param field
	 *            the component.
	 * @param unitOfMeasure
	 *            the unit of measure.
	 * @param container
	 *            the container component where placing the labelled component.
	 * @param gbc
	 *            the layout constraints.
	 */
	public static void add(JComponent label, JComponent field,
			JLabel unitOfMeasure, JPanel container, GridBagConstraints gbc) {
		// if (container.getLayout() instanceof GridBagLayout) {
		// JPanel panel = new JPanel(new BorderLayout(5, gbc.insets.top + 2));
		// panel.add(field, BorderLayout.CENTER);
		// panel.add(unitOfMeasure, BorderLayout.EAST);
		// panel.add(label, BorderLayout.WEST);
		// container.add(panel, gbc);
		// }
		if (container.getLayout() instanceof GridBagLayout) {

			JPanel panel = new JPanel(new BorderLayout(5, gbc.insets.top + 2));
			panel.add(field, BorderLayout.CENTER);
			panel.add(unitOfMeasure, BorderLayout.EAST);
			panel.add(label, BorderLayout.WEST);
			add(label, panel, container, gbc);
		}
	}

	/**
	 * Places an in-line component with a label into a container using a
	 * <code>GridBagConstraints</code> layout manager.
	 * 
	 * @param label
	 *            the label for the component.
	 * @param field
	 *            the component.
	 * @param container
	 *            the container component where placing the labelled component.
	 * @param gbc
	 *            the layout constraints.
	 */
	public static void add(JComponent label, JComponent field,
			JPanel container, GridBagConstraints gbc) {

		if (container.getLayout() instanceof GridBagLayout) {
			// add label
			if (label instanceof JLabel) {
				((JLabel) label).setLabelFor(field);
			}
			GridBagConstraints igbc = new GridBagConstraints();
			initConstraints(igbc);
			igbc.fill = GridBagConstraints.BOTH;
			igbc.gridx = gbc.gridx;
			igbc.gridy = gbc.gridy;
			igbc.weightx = 0.1;
			igbc.insets.set( //
					gbc.insets.top + 2, // top
					5, // left
					gbc.insets.bottom + 2, // bottom
					5); // right
			container.add(label, igbc);

			// add component
			// igbc.anchor = GridBagConstraints.LINE_END;
			igbc.anchor = GridBagConstraints.LINE_START;
			igbc.weightx = 0.9;
			igbc.weighty = 0.0;
			igbc.gridx++;
			// Span instructions
			igbc.gridheight = gbc.gridheight;
			igbc.gridwidth = gbc.gridwidth;
			igbc.fill = GridBagConstraints.BOTH;
			// igbc.fill = gbc.fill;
			//
			container.add(field, igbc);
			igbc.insets = gbc.insets;
			gbc.gridx = igbc.gridx;
			gbc.gridy = Math.max(igbc.gridy + igbc.gridheight - 1, 0);
		}
	}

	/**
	 * Add a vertical space.
	 * 
	 * 
	 * @param i
	 *            the number of vertical space.
	 * @param container
	 *            the container.
	 * @param gbc
	 *            grid bag constraint.
	 */
	public static void addVerticalSpace(int i, JPanel container,
			GridBagConstraints gbc) {
		JLabel vspace = new JLabel(" ");
		vspace.setPreferredSize(new Dimension(5, i));
		if (container.getLayout() instanceof GridBagLayout) {
			GridBagConstraints igbc = new GridBagConstraints();
			initConstraints(igbc);
			igbc.gridx = 0;
			igbc.gridy = gbc.gridy + 1;
			igbc.weightx = 0.0;
			igbc.insets = gbc.insets;
			container.add(vspace, igbc);
			gbc.gridy += i + 1;
		}
	}

	/**
	 * Increases the vertical position by one place.
	 * 
	 * @param gbc
	 *            the layout constraints.
	 * @return the layout constraints
	 */
	public static GridBagConstraints down(GridBagConstraints gbc) {
		gbc.gridx = gbc.gridx;
		gbc.gridy += 1;
		return gbc;
	}

	/**
	 * Set the new grid.
	 * 
	 * @param x
	 *            horizontal position.
	 * @param y
	 *            vertical position.
	 * @param gbc
	 *            the costraint object.
	 */
	public static void grid(int x, int y, GridBagConstraints gbc) {
		gbc.gridx = x;
		gbc.gridy = y;
	}

	/**
	 * Initialize the grid-bag layout constraints.
	 * 
	 * @param gbc
	 *            the object constraints. No resize, line start, one spanned
	 *            column and row.
	 */
	public static void initConstraints(GridBagConstraints gbc) {
		/* Constraints default */
		gbc.gridx = 0; // col
		gbc.gridy = 0; // row
		gbc.gridwidth = 1; // Spanned cols
		gbc.gridheight = 1; // Spanned rows
		gbc.ipadx = 4; // Enlarges component of x-pixels
		gbc.ipady = 4; // Enlarges component of y-pixels
		gbc.fill = GridBagConstraints.NONE; // Resizes component
		// gbc.insets = new Insets(1, 2, 1, 2); // top, left, bottom, right,
		gbc.insets = new Insets(0, 0, 1, 1); // top, left, bottom, right,
		// External padding
		gbc.anchor = GridBagConstraints.LINE_START; // Alignment
		gbc.weightx = 0.0; // Gives extra x-space to the component
		gbc.weighty = 0.0; // Gives extra y-space to the component
	}

	/**
	 * Decreases the horizontal position by one place.
	 * 
	 * @param gbc
	 *            the layout constraints.
	 * @return the layout constraints
	 */
	public static GridBagConstraints left(GridBagConstraints gbc) {
		gbc.gridx -= 1;
		gbc.gridy = gbc.gridy;
		return gbc;
	}

	/**
	 * New line. Set the horizontal position to 0 and increment the vertical
	 * position of 1.
	 * 
	 * @param gbc
	 *            the layout constraints.
	 * @return the layout constraints
	 */
	public static GridBagConstraints newLine(GridBagConstraints gbc) {
		gbc.gridx = 0;
		gbc.gridy += 1;
		return gbc;
	}

	/**
	 * Refresh the grid-bag layout constraints except the X, Y current values.
	 * 
	 * @param gbc
	 *            the object constraints.
	 * @return TODO
	 */
	public static GridBagConstraints refreshConstraints(GridBagConstraints gbc) {
		/* Constraints default */
		gbc.gridx = gbc.gridx; // col
		gbc.gridy = gbc.gridy; // row
		gbc.gridwidth = 1; // Spanned cols
		gbc.gridheight = 1; // Spanned rows
		gbc.ipadx = 0; // Enlarges component of x-pixels
		gbc.ipady = 0; // Enlarges component of y-pixels
		gbc.fill = GridBagConstraints.NONE; // Resizes component
		gbc.insets = new Insets(0, 0, 2, 2); // top, left, bottom, right,
		// External padding
		gbc.anchor = GridBagConstraints.LINE_START; // Alignment
		gbc.weightx = 0.0; // Gives extra x-space to the component
		gbc.weighty = 0.0; // Gives extra y-space to the component
		return gbc;
	}

	/**
	 * Increases the horizontal position by one place.
	 * 
	 * @param gbc
	 *            the layout constraints.
	 * @return the layout constraints
	 */
	public static GridBagConstraints right(GridBagConstraints gbc) {
		gbc.gridx += 1;
		gbc.gridy = gbc.gridy;
		return gbc;
	}

	/**
	 * Allow component to span over a number of rows and columns. Component
	 * cannot fill all the available space. Reset with
	 * {@link refreshConstraints}.
	 * 
	 * @param gbc
	 *            the layout constraints.
	 * @return the layout constraints
	 */
	public static GridBagConstraints spanBlock(GridBagConstraints gbc,
			int spannedCols, int spannedRows) {
		gbc.gridx = gbc.gridx;
		gbc.gridy = gbc.gridy;
		// span the line
		gbc.gridwidth = spannedCols; // Spanned cols
		gbc.gridheight = spannedRows; // Spanned rows
		// gbc.ipadx = 0; // Enlarges component of x-pixels
		// gbc.ipady = 0; // Enlarges component of y-pixels
		gbc.fill = GridBagConstraints.BOTH; // Resizes component
		// External padding
		// gbc.anchor = GridBagConstraints.LINE_START; // Alignment
		gbc.weightx = 0.0; // Gives extra x-space to the component
		gbc.weighty = 0.0; // Gives extra y-space to the component
		return gbc;
	}

	/**
	 * Decreases the vertical position by one place.
	 * 
	 * @param gbc
	 *            the layout constraints.
	 * @return the layout constraints
	 */
	public static GridBagConstraints up(GridBagConstraints gbc) {
		gbc.gridx = gbc.gridx;
		gbc.gridy -= 1;
		return gbc;
	}

}