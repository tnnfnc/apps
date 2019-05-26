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
package it.tnnfnc.table;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JPanel;
import javax.swing.UIManager;

import it.tnnfnc.apps.application.ui.style.ArrowGraphics;
import it.tnnfnc.datamodel.FilterStatus;

/**
 * A panel with a filter data control. Filtering events are notified to the
 * listeners registered with the panel.
 * 
 * @author Franco Toninato
 * 
 */
public class FilterPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Current filter status - editing
	 */
	private FilterStatus filterStatus = FilterStatus.OFF;
	/**
	 * Padding for arrows drawings.
	 */
	private int padding = 1;

	/**
	 * Draw arrow active.
	 */
	private void paintArrowActive(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Shape shape = ArrowGraphics.createDownArrow(this.getWidth(), this
				.getHeight(), padding);
		g2.setPaint(UIManager.getColor("Button.focus"));
		g2.fill(shape);
	}

	/**
	 * Draw arrow unactive.
	 */
	private void paintArrowUnactive(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Shape shape = ArrowGraphics.createDownArrow(this.getWidth(), this
				.getHeight(), padding);
		g2.setPaint(UIManager.getColor("Button.focus"));
		g2.draw(shape);
	}

	/**
	 * Draw arrow unactive.
	 */
	private void paintIsFilteringArrow(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Shape shape = ArrowGraphics.createDownArrow(this.getWidth(), this
				.getHeight(), padding);
		g2.setPaint(UIManager.getColor("Button.focus"));
		// g2.draw(shape);
		g2.setPaint(UIManager.getColor("Button.light"));
		g2.fill(shape);
	}

	/**
	 * Draw empty.
	 */
	private void paintFilterDisabled(Graphics g) {
		// no arrows;
	}

	/**
	 * Set the current filter.
	 * 
	 * @param filterStatus
	 */
	public void setFilter(FilterStatus filterStatus) {
		if (filterStatus == FilterStatus.ON
				|| filterStatus == FilterStatus.OFF
				|| filterStatus == FilterStatus.FILTERING
				|| filterStatus == FilterStatus.DISABLED) {
			this.filterStatus = filterStatus;
			repaint();
		}
	}

	/**
	 * Get the current filter.
	 * 
	 * @return the component filter status.
	 */
	public FilterStatus getFilter() {
		return filterStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		switch (filterStatus) {
		case ON:
			paintArrowActive(g);
			break;
		case OFF:
			paintArrowUnactive(g);
			break;
		case FILTERING:
			paintIsFilteringArrow(g);
			break;
		case DISABLED:
			paintFilterDisabled(g);
			break;
		default:
			paintFilterDisabled(g);
			break;
		}
	}

}