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
package it.tnnfnc.apps.application.ui; //Package

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Implements the a general purpose file filter.
 * 
 * @author Franco Toninato
 */
public class GeneralFileFilter extends FileFilter {
	private static final int INITLEN = 20;
	private String[] filters = new String[INITLEN];
	private int count;
	private String description = new String();

	/**
	 * Add a new file extension to the filter.
	 * 
	 * @param extension
	 *            the new extension.
	 *@return true when the extension is added. It returns false when an
	 *         invalid extension is added.
	 */
	public boolean addExtension(String extension) {
		if (extension == null)
			return false;
		filters[count] = extension;
		count++;
		if (count == filters.length) {
			String[] newfilters = new String[filters.length + 10];
			System.arraycopy(filters, 0, newfilters, 0, filters.length);
			filters = newfilters;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String extension = fileExtension(f);
		if (extension != null) {
			if (match(extension))
				return true;
			else
				return false;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of this file filter.
	 * 
	 *@param s
	 *            the description of this filter.
	 */
	public void setDescription(String s) {
		this.description = s;
	}

	/**
	 * Get the extension of a file.
	 * 
	 * @param f
	 *            the file.
	 *@return the file extension (the characters following the last dot in the
	 *         file name).
	 */
	public static String fileExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/* Look for a match */
	private boolean match(String ext) {
		for (int j = 0; j < count; j++) {
			if (filters[j].equals(ext))
				return true;
		}
		return false;
	}
}