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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public interface I_Resource {

	/**
	 * Return true when something has changed.
	 * 
	 * @return true when something has changed.
	 */
	public boolean isChanged();

	/**
	 * Set true when a change occurred.
	 * 
	 * @param changed
	 *            true when a change occurred.
	 */
	public void setChanged(boolean changed);

	/**
	 * Return the name of this resource.
	 * 
	 * @return the name of this resource.
	 */
	public URL getURL();

	/**
	 * Returns the resource lock status.
	 * 
	 * @return the resource lock status.
	 */
	public boolean isLocked();

	/**
	 * Sets the resource lock status. The application sets the lock when using
	 * this resource.
	 */
	public void setLock(boolean locked);

	/**
	 * Return true when the resource exists.
	 */
	public boolean exists();

	/**
	 * Get the input stream for this resource.
	 * 
	 * @return the input stream for this resource.
	 * @throws IOException
	 *             when the operation fails.
	 */
	public InputStream getInputStream() throws IOException;

	/**
	 * Get the output stream for this resource.
	 * 
	 * @return the output stream for this resource.
	 * @throws IOException
	 *             when the operation fails.
	 */
	public OutputStream getOutputStream() throws IOException;

	/**
	 * Set the input stream for this resource.
	 * 
	 * @param inputStream
	 *            the input stream for this resource.
	 */
	// void setInputStream(InputStream inputStream);

	/**
	 * Set the output stream for this resource.
	 * 
	 * @param outputStream
	 *            the output stream for this resource.
	 */
	// void setOutputStream(OutputStream outputStream);

}