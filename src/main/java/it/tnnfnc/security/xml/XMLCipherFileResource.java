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
package it.tnnfnc.security.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import it.tnnfnc.apps.resource.FileResource;
import it.tnnfnc.security.SecurityObject;

/**
 * Ciphered file resource.
 * 
 */
public class XMLCipherFileResource extends FileResource {
	private SecurityObject securityObject;
	private Properties properties;

	/**
	 * @param f
	 * @param so
	 * @param p
	 */
	public XMLCipherFileResource(File f, SecurityObject so, Properties p) {
		super(f);
		this.securityObject = so;
		this.properties = p;
	}

	/**
	 * Get the output stream for this resource. The input is deciphered.
	 * 
	 * @return the input stream for this resource.
	 * 
	 * @throws IOException
	 *             when the operation fails.
	 * 
	 * @see it.tnnfnc.apps.resource.FileResource#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		try {
			return new XmlCipherInputStream(new FileInputStream(getFile()),
					securityObject.getCipher(false));
		} catch (IllegalStateException e) {
			throw new IOException(e.getMessage(), e.getCause());
			// e.printStackTrace();
		} catch (IllegalArgumentException e) {
			throw new IOException(e.getMessage(), e.getCause());
			// e.printStackTrace();
		}
	}

	/**
	 * Get the output stream for this resource. The output is ciphered.
	 * 
	 * @return the output stream for this resource.
	 * 
	 * @throws IOException
	 *             when the operation fails.
	 * 
	 * @see it.tnnfnc.apps.resource.FileResource#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		try {
			return new XmlCipherOutputStream(new FileOutputStream(getFile()),
					securityObject.getCipher(true), properties);
		} catch (IllegalStateException e) {
			throw new IOException(e.getMessage(), e.getCause());
			// e.printStackTrace();
		} catch (IllegalArgumentException e) {
			throw new IOException(e.getMessage(), e.getCause());
			// e.printStackTrace();
		}
	}

}
