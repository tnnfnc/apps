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

import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Provides graphic resources as button icons. A <code>GraphicResources</code>
 * locates the graphic resources like button icons and provides methods for
 * retrival. Graphic resources must be located in the sub directory "icons" with
 * respect to the GraphicResources class location.
 * 
 * 
 * @author Franco Toninato
 */
public class GraphicResources {

	/* Resources map */
	public static final String DEFAULT_PATH = "icons";

	/**
	 * Class constructor.
	 */
	private GraphicResources() {
	}

	/**
	 * Retrieves an icon from its file name. Retrieves an icon from its file
	 * name, if no resource is found an empty <code>Icon</code> object is
	 * returned.
	 * 
	 * @param path
	 *            the icon name
	 * @return the icon
	 */
	public static ImageIcon createImageIcon(String path) {
		return createImageIcon(path, null);
	}

	/**
	 * Returns an ImageIcon, or null if the path was invalid.
	 * 
	 * @param path
	 * @param description
	 * @return
	 */
	public static ImageIcon createImageIcon(String path, String description) {
		URL imgURL = GraphicResources.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Returns an ImageIcon, or null if the path was invalid.
	 * 
	 * @param path
	 * @param description
	 * @return
	 */
	public static ImageIcon createImageIcon(Class<?> claz, String path,
			String description) {
		URL imgURL = claz.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Return an image URL.
	 * 
	 * @param name
	 *            the image name
	 * @return the URL
	 */
	// public static java.net.URL getImageURL(String name) {
	// /* Check the file name */
	// System.out.println(GraphicResources.class.getResource(DEFAULT_PATH
	// + "/" + name));
	// return GraphicResources.class.getResource(DEFAULT_PATH + "/" + name);
	// }
}