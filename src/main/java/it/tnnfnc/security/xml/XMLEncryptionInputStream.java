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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import it.tnnfnc.encoders.Base64DecodeInputStream;

/**
 * The class read from an input XML Encryption standard stream and read the
 * base-64 content of the XML CipherValue element, converts into bytes making
 * them available for reading and deciphering. The cipher parameters are parsed
 * into cipher properties.
 * 
 * @author franco toninato
 * 
 */
public class XMLEncryptionInputStream extends FilterInputStream {

	// private ByteArrayInputStream inbuffer;
	private ByteArrayOutputStream outbuffer;
	private Base64DecodeInputStream b64decoder;
	private Properties properties;

	/**
	 * Constructor.
	 * 
	 * @param out
	 *            the output stream.
	 * @param cipher
	 *            the cipher.
	 * @param params
	 *            the password based key generation parameters.
	 * @throws IOException
	 *             when initialization fails.
	 */
	public XMLEncryptionInputStream(InputStream in) throws IOException {
		super(in);
		init();
	}

	/**
	 * Processing
	 */
	private void init() throws IOException {
		outbuffer = new ByteArrayOutputStream();
		//
		try {
			XMLReader xr;
			xr = XMLReaderFactory.createXMLReader();
			SAXEncryptionHandler handler = new SAXEncryptionHandler(this);
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);

			try {
				InputSource inputSource = new InputSource(in);
				xr.parse(inputSource);

				// Build
			} catch (SAXException e) {
				throw new IOException(e.getMessage());
			} finally {
				this.properties = handler.getProperties();
			}
		} catch (SAXException e) {
			throw new IOException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#read()
	 */
	@Override
	public int read() throws IOException {
		return b64decoder.read();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return b64decoder.read(b, off, len);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#close()
	 */
	@Override
	public void close() throws IOException {
		b64decoder.close();
		super.close();
	}

	/**
	 * Update the encrypted XML content from the handler. Receive notification
	 * when reading the encrypted content from XML input stream.
	 * 
	 * @param ch
	 *            the accumulated characters from the parse.
	 * @param start
	 *            start from the array position.
	 * @param length
	 *            keep up to the length array position.
	 * @throws IOException
	 *             when something goes wrong.
	 */
	public void updateStream(char[] ch, int start, int length)
			throws IOException {
		String s = new String(ch, start, length);
		outbuffer.write(s.getBytes());
	}

	/**
	 * Close the the encrypted XML content from the handler.Receive notification
	 * at the end of the XML input stream.
	 * 
	 * @throws IOException
	 *             hen something goes wrong.
	 */
	public void endStream() throws IOException {
		outbuffer.flush();
		ByteArrayInputStream inbuffer = new ByteArrayInputStream(
				outbuffer.toByteArray());
		b64decoder = new Base64DecodeInputStream(inbuffer);
		outbuffer.close();
	}

	/**
	 * Get the properties. At the end of processing it return the cipher
	 * properties.
	 * 
	 * @return the properties.
	 */
	public Properties getProperties() {
		return properties;
	}

}
