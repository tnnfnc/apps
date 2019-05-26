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
import it.tnnfnc.datamodel.SortOrder;


/**
 * A panel with a double arrow control indicating a current sort. Up arrow, down
 * arrow, both arrows. Sorting events are notified to the listeners registered
 * with the panel.
 * 
 * @author Franco Toninato
 * 
 */
public class SortPanel extends JPanel {

	/* */
	private static final long serialVersionUID = 1L;
	/**
	 * The internal current status.
	 */
	private SortOrder sortingOrder = SortOrder.UNSORTED;
	private int padding = 1;

	/**
	 * Draw an ascending arrow.
	 */
	private void paintAscendingArrow(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Shape shape = ArrowGraphics.createDoubleArrowUp(this.getWidth(), this
				.getHeight(), padding);
		g2.setPaint(UIManager.getColor("Button.focus"));
		g2.draw(shape);
		g2.fill(shape);

		shape = ArrowGraphics.createDoubleArrowDown(this.getWidth(), this
				.getHeight(), padding);
		g2.setPaint(UIManager.getColor("Button.focus"));
		g2.draw(shape);
	}

	/**
	 * Draw an descending arrow.
	 */
	private void paintDescendingArrow(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Shape shape = ArrowGraphics.createDoubleArrowUp(this.getWidth(), this
				.getHeight(), padding);
		g2.setPaint(UIManager.getColor("Button.focus"));
		g2.draw(shape);

		shape = ArrowGraphics.createDoubleArrowDown(this.getWidth(), this
				.getHeight(), padding);
		g2.setPaint(UIManager.getColor("Button.focus"));
		g2.draw(shape);
		g2.fill(shape);
	}

	/**
	 * Draw an ascending and descending arrow.
	 */
	private void paintUnsortArrow(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Shape shape = ArrowGraphics.createDoubleArrowUp(this.getWidth(), this
				.getHeight(), padding);
		g2.setPaint(UIManager.getColor("Button.focus"));
		g2.draw(shape);

		shape = ArrowGraphics.createDoubleArrowDown(this.getWidth(), this
				.getHeight(), padding);
		g2.setPaint(UIManager.getColor("Button.focus"));
		g2.draw(shape);
	}
	
	/**
	 * Draw an ascending and descending arrow.
	 */
	private void paintUnsortable(Graphics g) {
		//
	}

	/**
	 * Set the current sort status.
	 * 
	 * @param sortStatus
	 */
	public void sort(SortOrder sortStatus) {
		sortingOrder = sortStatus;
		repaint();
	}

	/**
	 * Get the current arrow status.
	 * 
	 * @return the current status.
	 */
	public SortOrder getSortOrder() {
		return sortingOrder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		switch (sortingOrder) {
		case ASCENDING:
			paintAscendingArrow(g);
			break;
		case DESCENDING:
			paintDescendingArrow(g);
			break;
		case UNSORTED:
			paintUnsortArrow(g);
			break;
		case UNSORTABLE:
			paintUnsortable(g);
			break;
		default:
			paintUnsortArrow(g);
			break;
		}
	}

}