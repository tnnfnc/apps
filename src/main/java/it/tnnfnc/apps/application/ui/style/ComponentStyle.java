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
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 * This class converts between a CSS like style format to a java component style
 * format. Style is assumed to be in line like the CSS in line style definition.
 * The syntax is (name : value) pairs separated by semi-columns;
 * 
 * @author franco
 * 
 */
public class ComponentStyle {

	public static String fontFamily = "font-family";
	public static String fontSize = "font-size";
	public static String fontStyle = "font-style"; // An integer...
	public static String color = "color";
	public static String backgroundColor = "background-color";
	public static String borderStyle = "border-style"; // solid
	public static String borderColor = "border-color";
	public static String borderBottomWidth = "border-bottom-width";
	public static String borderLeftWidth = "border-left-width";
	public static String borderTopWidth = "border-top-width";
	public static String borderRightWidth = "border-right-width";
	public static String cleanStyle = "";

	/**
	 * Get the style attribute style by name. A null is returned if nothing is
	 * found.
	 * 
	 * @param attr
	 *            the style attribute name.
	 * @param style
	 *            the style.
	 * @return the attribute value.
	 */
	public static String parseStyle(String attr, String style) {
		String s = null;
		if (style != null) {
			int i = style.indexOf(attr);
			if (i >= 0
					&& (i == 0 || style.charAt(i - 1) == ' ' || style
							.charAt(i - 1) == ';')) {
				int j = style.indexOf(":", i);
				int k = style.indexOf(";", j);
				k = k > 0 ? k : style.length();
				s = style.substring(j + 1, k).trim();
				s = s.length() == 0 ? null : s;
			}
		}
		return s;
	}

	/**
	 * Get the style well formatted attribute - value pair.
	 * 
	 * @param attribute
	 *            the style attribute name.
	 * @param value
	 *            the attribute value.
	 * @return the well formatted attribute - value pair.
	 */
	public static String getStyleElement(String attribute, Object value) {
		StringBuffer b = new StringBuffer();
		b.append(attribute);
		b.append(":");
		if (attribute.equalsIgnoreCase(fontStyle)) {
			int s = i(value.toString());
			switch (s) {
			case Font.BOLD:
				b.append("bold");
				break;
			case Font.ITALIC:
				b.append("italic");
				break;
			case Font.PLAIN:
				b.append("plain");
				break;
			default:
				b.append("plain");
				break;
			}
		} else if (value instanceof Color) {
			Color c = (Color) value;
			b.append(c.getRed() + ",");
			b.append(c.getGreen() + ",");
			b.append(c.getBlue());
		} else {
			b.append(value);
		}
		b.append(";");
		return b.toString();
	}

	/**
	 * r 0-255, g 0-255, b 0-255
	 * 
	 * @param rgbColor
	 *            comma separated r 0-255, g 0-255, b 0-255.
	 * @return a color.
	 */
	public static Color getColor(String rgbColor) {
		// r 0-255, g 0-255, b 0-255
		if (rgbColor == null)
			return null;
		String rgb[] = rgbColor.split(",");
		try {
			int r = i(rgb[0]);
			int g = i(rgb[1]);
			int b = i(rgb[2]);
			return new Color(r, g, b);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Return the border from the style.
	 * 
	 * @param style
	 *            the style.
	 * @return the border or a null reference.
	 */
	public static Border getBorder(String style) {
		Border b;
		int borders[] = getBorderWidth(style);

		String s = ComponentStyle.parseStyle(ComponentStyle.borderStyle, style);
		Color c = getBorderColor(style);
		if (s == "none"
				|| c == null
				|| (borders[0] == 0 && borders[1] == 0 && borders[2] == 0 && borders[3] == 0)) {
			b = null;
		} else {
			b = new MatteBorder(borders[0], borders[1], borders[2], borders[3],
					c);
		}
		return b;
	}

	/**
	 * Get the font from the style.
	 * 
	 * @param font
	 *            the original font.
	 * @param style
	 *            the style or a null reference.
	 */
	public static Font getFont(Font font, String style) {
		String family = ComponentStyle.getFontFamily(font, style);
		String fstyle = ComponentStyle.getFontStyle(style);
		String size = ComponentStyle.getFontSize(font, style);

		Font f = null;
		if (family == null && fstyle == null && size == null) {
			f = font;
		} else {
			if (family == null && font != null) {
				f = font.deriveFont(i(fstyle), i(size));
			} else if (family != null) {
				f = new Font(family, i(fstyle), i(size));
			}
		}
		return f;
	}

	/**
	 * Get a style hash.
	 * 
	 * @param style
	 *            the style.
	 * @return the style hash code.
	 */
	public static int gethash(String style) {
		return style.hashCode();
	}

	/**
	 * Get an empty style.
	 * 
	 * @param style
	 *            the style.
	 * @return the empty style.
	 */
	public static String getCleanStyle(String style) {
		return cleanStyle;
	}

	/**
	 * Return the border width from the style.
	 * 
	 * @param style
	 *            the style.
	 * @return the border width (top, left, bottom, right).
	 */
	public static int[] getBorderWidth(String style) {
		return new int[] {
				i(ComponentStyle.parseStyle(ComponentStyle.borderTopWidth,
						style)),
				i(ComponentStyle.parseStyle(ComponentStyle.borderLeftWidth,
						style)),
				i(ComponentStyle.parseStyle(ComponentStyle.borderBottomWidth,
						style)),
				i(ComponentStyle.parseStyle(ComponentStyle.borderRightWidth,
						style)) };
	}

	/**
	 * Return the border color from the style.
	 * 
	 * @param style
	 *            the style.
	 * @return the border color.
	 */
	public static Color getBorderColor(String style) {
		return ComponentStyle.getColor(ComponentStyle.parseStyle(
				ComponentStyle.borderColor, style));
	}

	/**
	 * Return the foreground color from the style.
	 * 
	 * @param style
	 *            the style.
	 * @return the color.
	 */
	public static Color getForegroundColor(String style) {
		return ComponentStyle.getColor(ComponentStyle.parseStyle(
				ComponentStyle.color, style));
	}

	/**
	 * Return the background color from the style.
	 * 
	 * @param style
	 *            the style.
	 * @return the color.
	 */
	public static Color getBackgroundColor(String style) {
		// r 0-255, g 0-255, b 0-255
		return ComponentStyle.getColor(ComponentStyle.parseStyle(
				ComponentStyle.backgroundColor, style));
	}

	/**
	 * Get the font family from the style.
	 * 
	 * @param font
	 *            the original font.
	 * @param style
	 *            the style or a null reference.
	 * 
	 * @return the font family.
	 */
	public static String getFontFamily(Font font, String style) {
		String family = ComponentStyle.parseStyle(ComponentStyle.fontFamily,
				style);
		if (family == null && font != null) {
			family = font.getFamily();
		}
		return family;
	}

	/**
	 * Get the font style from the style.
	 * 
	 * @param style
	 *            the style or a null reference.
	 * 
	 * @return the font style.
	 */
	public static String getFontStyle(String style) {
		String fstyle = ComponentStyle.parseStyle(ComponentStyle.fontStyle,
				style);
		fstyle = fstyle == null ? "-1" : fstyle;
		if (fstyle.equals("bold")) {
			fstyle = Font.BOLD + "";
		} else if (fstyle.equals("italic")) {
			fstyle = Font.ITALIC + "";
		} else if (fstyle.equals("plain")) {
			fstyle = Font.PLAIN + "";
		} else if (fstyle.contains("bold") && style.contains("italic")) {
			fstyle = (Font.ITALIC | Font.BOLD) + "";
		} else {
			fstyle = Font.PLAIN + "";
		}
		return fstyle;
	}

	/**
	 * Get the font size from the style.
	 * 
	 * @param font
	 *            the original font.
	 * @param style
	 *            the style or a null reference.
	 * 
	 * @return the font size.
	 */
	public static String getFontSize(Font font, String style) {
		String size = ComponentStyle.parseStyle(ComponentStyle.fontSize, style);
		size = size == null ? font.getSize() + "" : size;
		return size;
	}

	/**
	 * Get the style from a component.
	 * 
	 * @param c
	 *            the component.
	 * 
	 * @return the component style.
	 */
	public static String getComponentStyle(Component c) {
		StringBuffer style = new StringBuffer();
		//
		style.append(getStyleElement(fontFamily, c.getFont().getFamily()));
		style.append(getStyleElement(fontStyle, c.getFont().getStyle()));
		style.append(getStyleElement(fontSize, c.getFont().getSize()));
		style.append(getStyleElement(color, c.getForeground()));
		style.append(getStyleElement(backgroundColor, c.getBackground()));
		try {
			MatteBorder b = (MatteBorder) ((JComponent) c).getBorder();
			b.getMatteColor();
			Insets insets = b.getBorderInsets();

			if (insets.top > 0)
				style.append(getStyleElement(borderTopWidth, insets.top));
			if (insets.left > 0)
				style.append(getStyleElement(borderLeftWidth, insets.left));
			if (insets.bottom > 0)
				style.append(getStyleElement(borderBottomWidth, insets.bottom));
			if (insets.right > 0)
				style.append(getStyleElement(borderRightWidth, insets.right));
			if (insets.top != 0 || insets.left != 0 || insets.bottom != 0
					|| insets.right != 0) {
				style.append(getStyleElement(borderStyle, "solid"));
				style.append(getStyleElement(borderColor, b.getMatteColor()));
			}
		} catch (ClassCastException e) {
			// No border defined
		} catch (java.lang.NullPointerException e) {
			// No border defined
		}
		return style.toString().trim();
	}

	/**
	 * Convert a string into a number.
	 * 
	 * @param s
	 *            string number.
	 * @return the string value converted into a number.
	 */
	private static int i(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}
}
