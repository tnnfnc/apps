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

import java.awt.geom.Path2D;

/**
 * Implements a graphic 2D path with a up arrow and a down arrow.
 * 
 * @author Franco Toninato
 * 
 */
public class ArrowGraphics {

	/**
	 * Return a path with an up arrow.
	 * 
	 * @param width
	 *            the enclosing rectangular box width.
	 * @param height
	 *            the enclosing rectangular box height.
	 * @param padding
	 *            the internal padding from the component border.
	 * @return a path representing an up arrow.
	 */
	public static Path2D.Double createDoubleArrowUp(double width,
			double height, double padding) {

		double base_x_min = padding;
		double base_x_max = width - padding;
		double base_y_min = padding;
		double base_y_max = height * 0.5 - padding;

		Path2D.Double path = new Path2D.Double();

		path.moveTo(base_x_min, base_y_max);
		path.lineTo(base_x_max, base_y_max);
		path.lineTo((base_x_max + padding) * 0.5, base_y_min);
		path.lineTo(base_x_min, base_y_max);

		return path;
	}

	/**
	 * Return a path with an down arrow.
	 * 
	 * @param width
	 *            the enclosing rectangular box width.
	 * @param height
	 *            the enclosing rectangular box height.
	 * @param padding
	 *            the internal padding from the component border.
	 * @return path representing a down arrow.
	 */
	public static Path2D.Double createDoubleArrowDown(double width,
			double height, double padding) {

		double base_x_min = padding;
		double base_x_max = width - padding;
		double base_y_min = height * 0.5 + padding;
		double base_y_max = height - padding;

		Path2D.Double path = new Path2D.Double();

		path.moveTo(base_x_min, base_y_min);
		path.lineTo(base_x_max, base_y_min);
		path.lineTo((base_x_max + padding) * 0.5, base_y_max);
		path.lineTo(base_x_min, base_y_min);

		return path;
	}

	/**
	 * Return a path with an down arrow.
	 * 
	 * @param width
	 *            the enclosing rectangular box width.
	 * @param height
	 *            the enclosing rectangular box height.
	 * @param padding
	 *            the internal padding from the component border.
	 * @return path representing a down arrow.
	 */
	public static Path2D.Double createDownArrow(double width,
			double height, double padding) {

		double base_x_min = padding;
		double base_x_max = width - padding;
		double base_y_min = padding;
		double base_y_max = height - padding;

		Path2D.Double path = new Path2D.Double();

		path.moveTo(base_x_min, base_y_min);
		path.lineTo(base_x_max, base_y_min);
		path.lineTo((base_x_max + padding) * 0.50F, base_y_max);
		path.lineTo(base_x_min, base_y_min);

		return path;
	}
	
	/**
	 * Return a path with an up arrow.
	 * 
	 * @param width
	 *            the enclosing rectangular box width.
	 * @param height
	 *            the enclosing rectangular box height.
	 * @param padding
	 *            the internal padding from the component border.
	 * @return path representing a down arrow.
	 */
	public static Path2D.Double createUpArrow(double width,
			double height, double padding) {
		
		double base_x_min = padding;
		double base_y_min = padding;
		double base_x_max = width - padding;
		double base_y_max = height - padding;
		
		Path2D.Double path = new Path2D.Double();
		
		path.moveTo(base_x_min, base_y_max);
		path.lineTo(base_x_max, base_y_max);
		path.lineTo((base_x_max + padding) * 0.50, base_y_min);
		path.lineTo(base_x_min, base_y_max);
		
		return path;
	}
}
