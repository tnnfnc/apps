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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class FiveStarsListRenderer extends JLabel implements
		ListCellRenderer<Object> {

	private static final long serialVersionUID = 1L;
	private final Color colorON = new Color(255, 140, 0);
	private final Color colorOFF = new Color(255, 220, 180);
	public final Color COLOR_SHADOW = new Color(220, 210, 0);
	private int fillLevel;
	private static final double STAR_HEIGHT = 10;
	private static final int STAR_WIDTH = 10;
	private static final int STAR_GAP = 3;

	public FiveStarsListRenderer() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
		// super.setBorder(null);
	}

	/*
	 * This method finds the image and text corresponding to the selected value
	 * and returns the label, set up to display the text and image.
	 */
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		setFillLevel(Integer.parseInt(value + ""));

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		this.setText(" ");
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		for (int i = 0; i < FiveStarsCombo.stars.length; i++) {
			if (i <= getFillLevel()) {
				drawStar(i * (STAR_WIDTH + STAR_GAP) + STAR_GAP, STAR_GAP, g2,
						colorON);
			} else {
				drawStar(i * (STAR_WIDTH + STAR_GAP) + STAR_GAP, STAR_GAP, g2,
						colorOFF);
			}
		}
	}

	private void drawStar(double x, double y, Graphics2D g2, Color c) {
		RoundRectangle2D.Double star = new RoundRectangle2D.Double(x, y,
				STAR_WIDTH, STAR_HEIGHT, STAR_WIDTH * 0.45, STAR_HEIGHT * 0.45);
		g2.setPaint(COLOR_SHADOW);
		g2.draw(star);
		g2.setPaint(c);
		g2.fill(star);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("FiveStarsCombo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				JComponent newContentPane = new FiveStarsCombo();
				newContentPane.setOpaque(true); // content panes must be opaque
				frame.setContentPane(newContentPane);

				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * @return the fillLevel
	 */
	public int getFillLevel() {
		return fillLevel;
	}

	/**
	 * @param fillLevel the fillLevel to set
	 */
	public void setFillLevel(int fillLevel) {
		this.fillLevel = fillLevel;
	}
}
