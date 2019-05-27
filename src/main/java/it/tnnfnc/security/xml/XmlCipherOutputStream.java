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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

import it.tnnfnc.encoders.Base64EncodeOutputStream;
import it.tnnfnc.security.crypto.BlockCipherOutputStream;
import it.tnnfnc.security.crypto.I_BlockCipherStream;

/**
 * This class transforms an encrypted XML encryption formatted output stream
 * into cipher output stream.
 * 
 * @author Franco Toninato
 * 
 */
public class XmlCipherOutputStream extends FilterOutputStream {
	/**
	 * Constructor.
	 * 
	 * @param out
	 *            output stream.
	 * @param cipher
	 *            the initialized cipher.
	 * @param properties
	 *
	 * @throws IOException
	 *             when something goes wrong.
	 */
	public XmlCipherOutputStream(OutputStream out, I_BlockCipherStream cipher,
			Properties properties) throws IOException {
		super(new GZIPOutputStream(new BlockCipherOutputStream(
				new Base64EncodeOutputStream(new XMLEncryptionOutputStream(out,
						new SAXEncriptionPBKDF2Writer(properties))), cipher)));

	}

}
