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

import java.io.IOException;
import java.util.Properties;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import it.tnnfnc.security.EncryptionPreferences;

/**
 * Parse an XML-Encryption formatted input.
 * 
 * 
 * @author Franco Toninato
 * 
 */
public class SAXEncryptionHandler extends DefaultHandler {

	protected StringBuffer buffer;
	protected Attributes attributes;
	protected XMLEncryptionInputStream xmlEncriptedIntputStream;
	protected BufferUpdater updater;
	protected Properties parameters = new Properties();

	/**
	 * Constructor.
	 * 
	 * @param xmlEncriptedIntputStream
	 *            the input to be parsed.
	 */
	public SAXEncryptionHandler(
			XMLEncryptionInputStream xmlEncriptedIntputStream) {
		this.xmlEncriptedIntputStream = xmlEncriptedIntputStream;
		updater = new BufferUpdater();
	}

	/**
	 * Calls the subclasses method XMLEncryptHandler.BufferUpdater.write(char[],
	 * int, int).
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		try {
			updater.write(ch, start, length);
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		// Does specific actions at the beginning of a document
		super.startDocument();
		buffer = new StringBuffer();
		updater = new BufferUpdater();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// System.out.println("startElement "+qName);
		this.attributes = attributes;
		openElement(qName);

		if (localName.equals(EncryptionConstants.XML_ENCRYPTEDDATA)) {
			updater = new BufferUpdater(); // added
		} else if (localName.equals(EncryptionConstants.XML_ENCRYPTIONMETHOD)) {
			updater = new BufferUpdater(); // added
		} else if (localName.equals(EncryptionConstants.XML_KEYINFO)) {
			// Parse the KEY INFO elements
			updater = new KeyInfoUpdater();
		} else if (localName.equals(EncryptionConstants.XML_CIPHERVALUE)) {
			// Parse the encrypted element
			updater = new StreamUpdater();
		} else if (localName
				.equals(EncryptionConstants.XML_ENCRYPTIONPROPERTIES)) {
			// Parse the encryprion properties
			updater = new PropertiesUpdater();
		} else {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (localName.equals(EncryptionConstants.XML_ENCRYPTEDDATA)) {

		} else if (localName.equals(EncryptionConstants.XML_ENCRYPTIONMETHOD)) {
			parameters.setProperty(EncryptionPreferences.PREF_CIPHER_ALGORITHM,
					attributes.getValue(EncryptionConstants.XML_ATT_ALGORITHM));
			// parameters.setProperty(EncryptionPreferences.TYPE_BLOCKCIPHER,
			// attributes.getValue(EncryptionConstants.XML_ATT_ALGORITHM));
			// String pars[] = SecurityProvider.parseRequest(attributes
			// .getValue(EncryptionConstants.XML_ATT_ALGORITHM));
			// parameters.setProperty(EncryptionPreferences.TYPE_BLOCKCIPHER,
			// pars[0]);
			// parameters
			// .setProperty(EncryptionPreferences.TYPE_BLOCKCIPHER_MODE,
			// pars[1]);
			// parameters.setProperty(EncryptionPreferences.TYPE_BLOCK_PADDING,
			// pars[2]);
			//
		} else if (localName.equals(EncryptionConstants.XML_KEYINFO)) {

		} else if (localName.equals(EncryptionConstants.XML_CIPHERVALUE)) {

		} else if (localName
				.equals(EncryptionConstants.XML_ENCRYPTIONPROPERTIES)) {

		} else if (localName.equals(EncryptionConstants.XML_ENCRYPTIONPROPERTY)) {

		}
		// PBE
		else if (localName.equals(EncryptionConstants.XML_PBKDF)) {
			((KeyInfoUpdater) updater)
					.setProperty(EncryptionPreferences.PREF_PBKDF);
		} else if (localName.equals(EncryptionConstants.XML_HMAC)) {
			((KeyInfoUpdater) updater)
					.setProperty(EncryptionPreferences.PREF_HMAC);
		} else if (localName.equals(EncryptionConstants.XML_ITERATIONS)) {
			((KeyInfoUpdater) updater)
					.setProperty(EncryptionPreferences.PREF_ITERATIONS);
		} else if (localName.equals(EncryptionConstants.XML_SALT)) {
			((KeyInfoUpdater) updater)
					.setProperty(EncryptionPreferences.PREF_SALT);
		} else if (localName.equals(EncryptionConstants.XML_KEYSIZE)) {
			((KeyInfoUpdater) updater)
					.setProperty(EncryptionPreferences.PREF_KEYSIZE);
		}
		// IV
		else if (localName.equals(EncryptionConstants.XML_IV)) {
			((PropertiesUpdater) updater)
					.setProperty(EncryptionPreferences.PREF_IV);
		}
		// Security Provider
		// else if (localName.equals(EncryptionConstants.XML_SECURITYPROVIDER))
		// {
		// ((PropertiesUpdater) updater)
		// .setProperty(EncryptionPreferences.PREF_SECURITYPROVIDER);
		// }
		//
		else {
			try {
				((PropertiesUpdater) updater)
						.setProperty(EncryptionConstants.XML_ENCRYPTIONPROPERTIES
								+ "." + qName);
			} catch (Exception e) {
				// nothing
			}
		}
		closeElement(qName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		try {
			// Set the properties to the cipher stream
			// xmlEncriptedIntputStream.setCipherProperties(securProperties);
			xmlEncriptedIntputStream.endStream();
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}

	/**
	 * Get the properties. At the end of processing it return the cipher
	 * properties.
	 * 
	 * @return the properties.
	 */
	public Properties getProperties() {
		return parameters;
	}

	/**
	 * Receive notification of the start of an element.
	 * 
	 * @param string
	 *            the element Qname.
	 */
	protected void openElement(String string) {
	}

	/**
	 * Receive notification of the end of an element.
	 * 
	 * @param string
	 *            the element Qname.
	 */
	protected void closeElement(String string) {
		buffer.delete(0, buffer.length());
	}

	/**
	 * Update the internal buffer with the the characters content.
	 * 
	 */
	protected class BufferUpdater {
		public void write(char[] ch, int start, int length) throws IOException {
			buffer.append(new String(ch, start, length));
		}
	}

	/**
	 * Update the stream with the the CipherValue content.
	 * 
	 */
	protected class StreamUpdater extends BufferUpdater {
		@Override
		public void write(char[] ch, int start, int length) throws IOException {
			xmlEncriptedIntputStream.updateStream(ch, start, length);
		}
	}

	/**
	 * Parse the properties from the input.
	 * 
	 */
	protected class PropertiesUpdater extends BufferUpdater {
		protected StringBuffer b = new StringBuffer();

		@Override
		public void write(char[] ch, int start, int length) throws IOException {
			b.append(new String(ch, start, length));
		}

		public void setProperty(String key) {
			parameters.setProperty(key, b.toString().trim());
			b.delete(0, b.length());
		}
	}

	/**
	 * Update the stream with the the key material content.
	 * 
	 */
	protected class KeyInfoUpdater extends BufferUpdater {
		protected StringBuffer b = new StringBuffer();

		@Override
		public void write(char[] ch, int start, int length) throws IOException {
			b.append(new String(ch, start, length));
		}

		public void setProperty(String key) {
			parameters.setProperty(key, b.toString().trim());
			b.delete(0, b.length());
		}
	}

}
