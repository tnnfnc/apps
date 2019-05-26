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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import it.tnnfnc.security.crypto.BlockCipherInputStream;
import it.tnnfnc.security.crypto.I_BlockCipherStream;

/**
 * This class reads the encrypted content into plain text from an XML-Encryption
 * formatted input stream.
 * 
 * @author Franco Toninato
 * 
 */
public class XmlCipherInputStream extends FilterInputStream {

	/**
	 * Constructor.
	 * 
	 * @param in
	 *            input stream.
	 * @param cipher
	 *            the initialized cipher.
	 * @throws IOException
	 *             when something goes wrong.
	 */
	public XmlCipherInputStream(InputStream in, I_BlockCipherStream cipher)
			throws IOException {
		super(new GZIPInputStream(new BlockCipherInputStream(
				new XMLEncryptionInputStream(in), cipher)));
	}

}
