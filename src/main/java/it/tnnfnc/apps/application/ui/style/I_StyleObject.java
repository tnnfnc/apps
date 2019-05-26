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

/**
 * Interface representing an internal object with a format. The format style has
 * effect on the cell renderer component only.
 * 
 * @author Franco Toninato
 */
public interface I_StyleObject {

	/**
	 * Empty style is a blank string.
	 */
	static String EMPTY_STYLE = "";

	/**
	 * Get the cell value.
	 * 
	 * @return the cell value.
	 */
	Object getValue();

	/**
	 * Get the cell format.
	 * 
	 * @return the value format.
	 */
	String getStyle();

	/**
	 * Set new values for the internal object and the style.
	 * 
	 * @param value
	 *            the cell value.
	 * @param style
	 *            the cell format.
	 * @return the old object.
	 */
	I_StyleObject set(Object value, String style);

	/**
	 * Set new object.
	 * 
	 * @param value
	 *            the cell value.
	 * @return the old object.
	 */
	Object setObject(Object value);

	/**
	 * Set new style.
	 * 
	 * @param style
	 *            the cell format.
	 * @return the old object.
	 */
	String setStyle(String style);

}
