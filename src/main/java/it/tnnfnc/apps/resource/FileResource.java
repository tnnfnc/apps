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
package it.tnnfnc.apps.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FileResource extends AbstractResource {
	protected File file;

	public FileResource(File f) {
		file = f;
	}

	/**
	 * Get the file.
	 * 
	 * @return the file.
	 */
	public File getFile() {
		return this.file;
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public boolean isLocked() {
		return super.isLocked();
	}

	@Override
	public String[] list() {
		return file.list();
	}

	@Override
	public URL getURL() {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * Get the extension of a file.
	 * 
	 * @param f
	 *            the file name.
	 * @return the extension of a file.
	 */
	public static String getExtension(String f) {
		String ext = null;
		int i = f.lastIndexOf('.');

		if (i > 0 && i < f.length() - 1) {
			ext = f.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	@Override
	public I_Resource getParent() {
		file.getParent();
		return null;
	}

	@Override
	public void setLock(boolean locked) {
		super.setLock(locked);
	}

	@Override
	public boolean isLeaf() {
		return file.isFile();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// if (!file.exists()) {
		// file.createNewFile();
		// }
		return new FileInputStream(file);
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		// if (!file.exists()) {
		// file.createNewFile();
		// }
		return new FileOutputStream(file);
	}

}
