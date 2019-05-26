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
package it.tnnfnc.apps.application.document;

import java.io.IOException;

import it.tnnfnc.apps.resource.I_Resource;


/**
 * This interface represent the basic application responsabilities: create,
 * open, save, close a file, exit(), set a current file, saveAs, get the current
 * file, get a default file name, get a file extension.
 * 
 * @author Franco Toninato
 * 
 */
public interface I_IOBasicOperations {


	/**
	 * Creates a new document with a name and opens it.
	 * 
	 * @param r
	 *            the document resource.
	 * @throws IOException
	 *             when the document cannot be created.
	 */
	void create(I_Resource r) throws IOException;

	/**
	 * Open a new document.
	 * 
	 * @param r
	 *            the document resource.
	 * @throws IOException
	 *             when the document can't be opened.
	 */
	void open(I_Resource r) throws IOException;

	/**
	 * Saves the active document.
	 * 
	 * @param r
	 *            the document resource.
	 * @throws IOException
	 *             when the document can't be saved.
	 */
	void save(I_Resource r) throws IOException;

	/**
	 * Saves the active document as a new document.
	 * 
	 * @param r
	 *            the document resource.
	 * 
	 * @throws IOException
	 *             when the document can't be saved.
	 */
	void saveAs(I_Resource r) throws IOException;

	/**
	 * Closes the active document.
	 * 
	 * @param r
	 * 
	 * 
	 * @throws IOException
	 *             when the document can't be closed.
	 */
	void close(I_Resource r) throws IOException;

	/**
	 * Exit the application. It calls the {@link exitApplication} method.
	 * 
	 * 
	 * @throws IOException
	 *             when the application exits with errors.
	 */
	void exit() throws IOException;

}
