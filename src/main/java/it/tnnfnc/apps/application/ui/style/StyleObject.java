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
package it.tnnfnc.apps.application.ui.style; //Package

/**
 * This class implements an internal object with a format. The format style has
 * effect on the cell renderer component only.
 * 
 * @author Franco Toninato
 * 
 *         the internal type.
 */
public class StyleObject implements I_StyleObject
// , Comparable<StyleObject>
{

	/**
	 * The value.
	 */
	private Object value = null;
	/**
	 * The current status.
	 */
	private String style = null;

	/**
	 * Class constructor.
	 * 
	 * @param value
	 *            the value.
	 * @param style
	 *            the status.
	 */
	public StyleObject(Object value, String style) {
		this.value = value;
		this.style = style;
		// if (style == null) {
		// // this.style = null;
		// } else {
		// this.style = style;
		// }
	}

	/**
	 * Class constructor.
	 * 
	 * @param value
	 *            the value.
	 */
	public StyleObject(Object value) {
		this(value, null);
		// this(value, new String());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public String getStyle() {
		return style;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public I_StyleObject set(Object value, String style) {
		StyleObject oldObject = new StyleObject(this.value, this.style);
		this.value = value;
		this.style = style;
		return oldObject;
	}

	@Override
	public String setStyle(String style) {
		String s = this.style;
		this.style = style == null ? EMPTY_STYLE : style;
		return s;
	}

	@Override
	public Object setObject(Object value) {
		Object o = this.value;
		this.value = value;
		return o;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof I_StyleObject))
			return false;
		I_StyleObject sobj = (I_StyleObject) obj;
		if (sobj.getValue() == null && this.getValue() == null)
			return true;
		return (sobj.getValue() != null && sobj.getValue().equals(
				this.getValue()));
	}

	// @SuppressWarnings("unchecked")
	// public int compareTo(StyleObject o) {
	// if (value instanceof Comparable<?>
	// && o.getValue() instanceof Comparable<?>) {
	// return ((Comparable<Object>) this.getValue()).compareTo(o
	// .getValue());
	// }
	// return 0;
	// }

}