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


/**
 * Resource can be http://, ftp://user:@www.dominio/, local file system. The
 * subclasses of this resource return proper objects (input and output stream)
 * for input and output.
 * 
 * @author Franco Toninato
 * 
 */
public abstract class AbstractResource implements I_Resource {

	protected boolean isLocked = false;
	private boolean isChanged = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.resource.I_Resource#list()
	 */
	public String[] list() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.resource.I_Resource#isLeaf()
	 */
	public boolean isLeaf() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.resource.I_Resource#getParent()
	 */
	public I_Resource getParent() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.resource.I_Resource#isLocked()
	 */
	@Override
	public boolean isLocked() {
		return this.isLocked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.resource.I_Resource#setLock(boolean)
	 */
	@Override
	public void setLock(boolean locked) {
		this.isLocked = locked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.resource.I_Resource#getInputStream()
	 */
	@Override
	public abstract InputStream getInputStream() throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.resource.I_Resource#getOutputStream()
	 */
	@Override
	public abstract OutputStream getOutputStream() throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.resource.I_Resource#isChanged()
	 */
	@Override
	public boolean isChanged() {
		return this.isChanged;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.resource.I_Resource#setChanged(boolean)
	 */
	@Override
	public void setChanged(boolean changed) {
		this.isChanged = changed;
	}

}
