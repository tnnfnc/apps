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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.transform.sax.TransformerHandler;


import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * The class is responsible of writing a stream of data into an XML encrypted
 * standard output stream using SAX events. Subclasses can implements public
 * methods starting with the <code>writeProperty</code> prefix for adding their
 * properties into the <code>EncryptionProperties</code> element section.
 * 
 * 
 * @author Franco Toninato
 * 
 */
public abstract class SAXEncrypionWriter {
	/**
	 * 
	 */
	protected ContentHandler handler;

	/**
	 * Set the content handler.
	 * 
	 * @param h
	 *            the content handler.
	 */
	public void setContentHandler(TransformerHandler h) {
		this.handler = h;
	}

	/**
	 * Shot events for writing the encrypted stream;
	 * 
	 * @param b
	 *            the char array.
	 * @throws SAXException
	 */
	public void write(char[] b) throws SAXException {
		// System.out.println(new String(b));
		// System.out.println(this.getClass().getName() + ".write");
		handler.characters(b, 0, b.length);
	}

	/**
	 * The method starts the document. It adds <code>EncryptedData</code>
	 * element, <code>EncryptionMethod</code>, <code>KeyInfo</code>, opens
	 * <code>CipherData</code> and opens <code>CipherValue</code>.
	 * 
	 * @throws SAXException
	 *             when something goes wrong.
	 */
	public void startDocument() throws SAXException {
		// System.out.println(this.getClass().getName() + ".startDocument");
		handler.startDocument();
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("", "", "xmlns", "CDATA", EncryptionConstants.XMLENC);
		handler.startElement("", "", EncryptionConstants.XML_ENCRYPTEDDATA, atts);
		writeEncryptionMethod();
		writeKeyInfo();
		openCipherData();
		openCipherValue();
	}

	/**
	 * The method ends the document. It closes the <code>CipherValue</code>
	 * element and the <code>CipherData</code>. It opens the
	 * <code>EncryptionProperties</code>, writes the specific
	 * <code>EncryptionProperty</code> implemented by subclasses and then
	 * closes.
	 * 
	 * @throws SAXException
	 *             when something goes wrong.
	 */
	public void endDocument() throws SAXException {
		// System.out.println(this.getClass().getName() + ".endDocument");
		closeCipherValue();
		closeCipherData();
		openEncryptionProperties();
		writeEncryptionProperties();
		closeEncryptionProperties();

		handler.endElement("", "", EncryptionConstants.XML_ENCRYPTEDDATA);
		handler.endDocument();
	}

	/**
	 * Shot events for writing an <code>EncryptionMethod</code> element. The
	 * method does nothing, a subclass must implement.
	 * 
	 * @param cipher
	 *            the block cipher.
	 * @throws SAXException
	 */
	protected abstract void writeEncryptionMethod() throws SAXException;

	/**
	 * Shot events for writing an <code>KeyInfo</code> element. The method does
	 * nothing, a subclass must implement.
	 * 
	 * @throws SAXException
	 *             when something goes wrong.
	 */
	protected abstract void writeKeyInfo() throws SAXException;

	/**
	 * Shot events for opening the <code>CipherData</code> element;
	 * 
	 * @throws SAXException
	 */
	protected void openCipherData() throws SAXException {
		AttributesImpl atts;
		atts = new AttributesImpl();
		handler.startElement("", "", EncryptionConstants.XML_CIPHERDATA, atts);
		atts.clear();
	}

	/**
	 * Shot events for closing the <code>CipherData</code> element;
	 * 
	 * @throws SAXException
	 */
	protected void closeCipherData() throws SAXException {
		handler.endElement("", "", EncryptionConstants.XML_CIPHERDATA);
	}

	/**
	 * Shot events for opening the <code>CipherValue</code> element;
	 * 
	 * @throws SAXException
	 */
	protected void openCipherValue() throws SAXException {
		AttributesImpl atts;
		atts = new AttributesImpl();
		handler.startElement("", "", EncryptionConstants.XML_CIPHERVALUE, atts);
		atts.clear();
	}

	/**
	 * Shot events for closing the <code>CipherValue</code> element;
	 * 
	 * @throws SAXException
	 */
	protected void closeCipherValue() throws SAXException {
		handler.endElement("", "", EncryptionConstants.XML_CIPHERVALUE);
	}

	/**
	 * Shot events for opening the <code>EncryptionProperties</code> element;
	 * 
	 * @throws SAXException
	 */
	protected void openEncryptionProperties() throws SAXException {
		AttributesImpl atts;
		atts = new AttributesImpl();
		handler.startElement("", "", EncryptionConstants.XML_ENCRYPTIONPROPERTIES,
				atts);
		atts.clear();
	}

	/**
	 * Shot events for closing the <code>EncryptionProperties</code> element;
	 * 
	 * @throws SAXException
	 */
	protected void closeEncryptionProperties() throws SAXException {
		handler.endElement("", "", EncryptionConstants.XML_ENCRYPTIONPROPERTIES);
	}

	/**
	 * Reflective method: it calls all public methods prefixed by
	 * <code>writeProperty</code>.
	 * 
	 * @throws SAXException
	 *             when a method invocation exception is thrown.
	 */
	protected void writeEncryptionProperties() throws SAXException {
		// Reflection
		Class<?> c = this.getClass();
		Method ms[] = c.getMethods();
		for (int i = 0; i < ms.length; i++) {
			if (ms[i].getName().startsWith("writeProperty")) {
				try {
					ms[i].invoke(this, new Object[] {});
				} catch (IllegalArgumentException e) {
					throw new SAXException(e);
				} catch (IllegalAccessException e) {
					throw new SAXException(e);
				} catch (InvocationTargetException e) {
					throw new SAXException(e);
				}
			}
		}
	}

}