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
package it.tnnfnc.apps.application.ui.style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import javax.swing.Icon;


public class IconsFactory {

	public static class VoidIcon implements Icon {
		@Override
		public int getIconHeight() {
			return 10;
		}

		@Override
		public int getIconWidth() {
			return 10;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color save = g.getColor();
			g.setColor(c.getForeground());
			g.drawRect(x, y, 10, 10);
			g.setColor(save);
		}
	}

	/**
	 */
	public static class BookMarkIcon implements Icon {

		private final Color color;

		public BookMarkIcon(Color c) {
			color = c;
		}

		@Override
		public int getIconHeight() {
			return 10;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#getIconWidth()
		 */
		@Override
		public int getIconWidth() {
			return 10;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#paintIcon(java.awt.Component,
		 * java.awt.Graphics, int, int)
		 */
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			// Color save = g.getColor();
			int center = (c.getBounds().height - getIconHeight()) / 2;
			Shape bkm = new Ellipse2D.Double(0, center, //
					getIconWidth(),//
					getIconHeight());
			g2.setPaint(color);
			g2.fill(bkm);
		}
	}

	/**
	 */
	public static class StarIcon implements Icon {

		private final Color color;

		public StarIcon(Color c) {
			color = c;
		}

		@Override
		public int getIconHeight() {
			return 10;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#getIconWidth()
		 */
		@Override
		public int getIconWidth() {
			return 10;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#paintIcon(java.awt.Component,
		 * java.awt.Graphics, int, int)
		 */
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			Shape star = new Ellipse2D.Double(x, y, getIconWidth(),
					getIconHeight());
			g2.setPaint(color);
			g2.fill(star);

		}
	}

	/**
	 */
	public static class UpArrow implements Icon {

		private final Color color;

		public UpArrow(Color c) {
			color = c;
		}

		@Override
		public int getIconHeight() {
			return 10;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#getIconWidth()
		 */
		@Override
		public int getIconWidth() {
			return 10;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#paintIcon(java.awt.Component,
		 * java.awt.Graphics, int, int)
		 */
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			// Color save = g.getColor();
			x = c.getPreferredSize().width;
			y = c.getPreferredSize().height;

			Path2D.Double downArrow = ArrowGraphics.createUpArrow(x, y, 3);
			g2.setPaint(color);
			g2.draw(downArrow);
			g2.fill(downArrow);
		}
	}

	/**
	 */
	public static class DownArrow implements Icon {

		private final Color color;

		public DownArrow(Color c) {
			color = c;
		}

		@Override
		public int getIconHeight() {
			return 10;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#getIconWidth()
		 */
		@Override
		public int getIconWidth() {
			return 10;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#paintIcon(java.awt.Component,
		 * java.awt.Graphics, int, int)
		 */
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			// Color save = g.getColor();
			x = c.getPreferredSize().width;
			y = c.getPreferredSize().height;

			Path2D.Double downArrow = ArrowGraphics.createDownArrow(x, y, 3);
			g2.setPaint(color);
			g2.draw(downArrow);
			g2.fill(downArrow);
		}
	}

	/**
	 */
	public static class LeftArrow implements Icon {

		private final Color color;

		public LeftArrow(Color c) {
			color = c;
		}

		@Override
		public int getIconHeight() {
			return 10;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#getIconWidth()
		 */
		@Override
		public int getIconWidth() {
			return 10;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#paintIcon(java.awt.Component,
		 * java.awt.Graphics, int, int)
		 */
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			// Color save = g.getColor();
			x = c.getPreferredSize().width;
			y = c.getPreferredSize().height;

			Path2D.Double downArrow = ArrowGraphics.createDownArrow(x, y, 4);
			g2.setPaint(color);
			g2.draw(downArrow);
			g2.fill(downArrow);
		}
	}

}
