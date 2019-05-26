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

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

/**
 * The class write an encrypted content into an XML Encryption standard output
 * stream. The input is written into the CipherValue element and is expected to
 * be in base64 format.
 * 
 * @author franco toninato
 * 
 */
public class XMLEncryptionOutputStream extends FilterOutputStream {
	private SAXEncrypionWriter xmlEncriptionWriter;

	/**
	 * Constructor.
	 * 
	 * @param out
	 *            the output stream.
	 * @param writer
	 *            the XMLencrypt writer.
	 * @throws IOException
	 *             when initialization fails.
	 */
	public XMLEncryptionOutputStream(OutputStream out, SAXEncrypionWriter writer)
			throws IOException {
		super(out);
		xmlEncriptionWriter = writer;
		init();
	}

	private void init() throws IOException {
		try {
			StreamResult streamResult = new StreamResult(out);
			// !! Cast to obtain a SAXTransformerFactory
			SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory
					.newInstance();
			TransformerHandler hd = tf.newTransformerHandler();
			hd.setResult(streamResult);

			// !! Set the parameters!
			Transformer transformer = hd.getTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			// transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
			// "xmlenc.dtd");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			xmlEncriptionWriter.setContentHandler(hd);
			// -> Serialize here
			xmlEncriptionWriter.startDocument();
		} catch (TransformerConfigurationException e) {
			IOException ex = new IOException(e.getCause());
			throw ex;
		} catch (SAXException e) {
			IOException ex = new IOException(e.getCause());
			throw ex;
		} finally {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterOutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		flush();
		try {
			// End document
			xmlEncriptionWriter.endDocument();
			super.close();
		} catch (SAXException e) {
			throw new IOException(e.getCause());
		}
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see java.io.FilterOutputStream#flush()
	// */
	// @Override
	// public void flush() throws IOException {
	// super.flush();
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterOutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		try {
			for (int j = 0; j < len; j++) {
				xmlEncriptionWriter.write(Character.toChars(b[j + off]));
			}
			// byte tb[] = new byte[len];
			// System.arraycopy(b, off, tb, 0, len);
			// xmlEncriptionWriter.write(new String(tb).toCharArray());
		} catch (SAXException e) {
			throw new IOException(e.getCause());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterOutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		try {
			xmlEncriptionWriter.write(Character.toChars(b));
			// xmlEncriptionWriter.write(new char[] { (char) b });
		} catch (SAXException e) {
			throw new IOException(e.getCause());
		}
	}
}
