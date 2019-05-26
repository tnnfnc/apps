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
import java.awt.Font;

import javax.swing.border.Border;

public interface I_StyleFormatter {

	/**
	 * Apply the format to a component.
	 * 
	 * @param component
	 *            the component to be formatted.
	 * @param style
	 *            the format.
	 * @param isSelected
	 *            true when the component is selected.
	 * @param hasFocus
	 *            true when the component has focus on it.
	 */
	public void applyStyle(Component c, String style, boolean isSelected,
			boolean hasFocus);

	/**
	 * Get the font defined by the style.
	 * 
	 * @return the font.
	 * 
	 */
	public Font font();

	/**
	 * Get the foreground color defined by the style.
	 * 
	 * @return the foreground color.
	 * 
	 */
	public Color foregroundColor();

	/**
	 * Get the background color defined by the style.
	 * 
	 * @return the foreground color.
	 * 
	 */
	public Color backgroundColor();

	/**
	 * Get the border defined by the style.
	 * 
	 * @return the border.
	 * 
	 */
	public Border border();

}