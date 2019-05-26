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
package it.tnnfnc.table.header;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import it.tnnfnc.datamodel.FilterStatus;
import it.tnnfnc.datamodel.SortOrder;
import it.tnnfnc.table.FilterPanel;
import it.tnnfnc.table.SortPanel;

/**
 * The class provides a default header renderer component for a content header
 * or a table column header. Default behavior affecting data model are: sort
 * ascending, sort descending, revert to initial order, filter box with drop
 * down content. Behaviors affecting data column model are: hide, display, hide
 * all others, show all others, enable easy copy (when enabled the value of the
 * content can be copied through a SHIFT+Click).
 * 
 * @author Franco Toninato
 * 
 */
public class HeaderComponent extends JPanel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FilterPanel filterPanel;
	private SortPanel sortPanel;
	protected JLabel headerLabel;
	protected JPanel buttonContainer;

	public static void main(String[] args) {
		HeaderComponent test = new HeaderComponent();
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(test);

		f.setSize(400, 400);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	/*
	 * JPanel (container) - JPanel (display header) - JPanel (for drawing
	 * arrows) and mouse listening - JPanel for filtering as a dropdown content.
	 */
	public HeaderComponent() {
		super();
		this.setOpaque(false);

		sortPanel = new SortPanel();
		filterPanel = new FilterPanel();
		JPanel container = new JPanel(new BorderLayout());
		headerLabel = new JLabel("header name");
		buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

		buttonContainer.add(sortPanel);
		buttonContainer.add(filterPanel);
		container.add(buttonContainer, BorderLayout.EAST);
		this.setLayout(new BorderLayout());
		add(container, BorderLayout.NORTH);
		add(headerLabel, BorderLayout.SOUTH);

		/*
		 * Borders: The left border side is not painted otherwise it sums up.
		 */
		headerLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1,
				UIManager.getColor("Table.gridColor")));
		Color backGroundcolor = UIManager.getColor("TableHeader.background");
		Color foreGroundcolor = UIManager.getColor("TableHeader.foreground");
		sortPanel.setBackground(backGroundcolor);
		sortPanel.setForeground(foreGroundcolor);
		filterPanel.setBackground(backGroundcolor);
		filterPanel.setForeground(foreGroundcolor);
		buttonContainer.setBackground(UIManager.getColor("Table.background"));
		buttonContainer.setForeground(foreGroundcolor);
		headerLabel.setBackground(backGroundcolor);
		headerLabel.setForeground(foreGroundcolor);
		setBackground(backGroundcolor);
		setForeground(foreGroundcolor);
		setPreferredIconSize(new Dimension(12, 12));
	}

	/**
	 * Set the action panel icon size.
	 * 
	 * @param d
	 *            the dimension.
	 */
	public void setPreferredIconSize(Dimension d) {
		filterPanel.setPreferredSize(d);
		sortPanel.setPreferredSize(d);
	}

	/**
	 * Set the current filter internal status.
	 * 
	 */
	public void filter(FilterStatus filterStatus) {
		filterPanel.setFilter(filterStatus);
	}

	/**
	 * Set the current sort internal status.
	 * 
	 */
	public void sort(SortOrder sortOrder) {
		sortPanel.sort(sortOrder);
	}

	/**
	 * Returns the filter panel bounds.
	 * 
	 * @return the filter panel bounds with respect to the containing panel.
	 */
	public Rectangle getFilterButtonRect() {
		return filterPanel.getBounds();
	}

	/**
	 * Returns the sort panel bounds.
	 * 
	 * @return the sort panel bounds with respect to the containing panel.
	 */
	public Rectangle getSortButtonRect() {
		return sortPanel.getBounds();
	}

	/**
	 * Returns the button panel bounds. The button panel contains the defined
	 * controls: like a sort indicator, a filter status indicator and so on.
	 * 
	 * @return the button panel bounds with respect to the containing panel.
	 */
	public Rectangle getButtonsRect() {
		return buttonContainer.getBounds();
	}

}
