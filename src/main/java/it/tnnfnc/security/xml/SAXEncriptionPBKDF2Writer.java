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

import java.util.Properties;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import it.tnnfnc.security.EncryptionPreferences;

/**
 * The class is responsible of writing a stream of data into an XML encrypted
 * standard output stream using SAX events. It manages XML encryption based on
 * PBE standard.
 * 
 * 
 * @author Franco Toninato
 * 
 */
public class SAXEncriptionPBKDF2Writer extends SAXEncrypionWriter {
	// private I_BlockCipherStream cipher;
	// private AlgorithmParameters algorithmParameters;
	// private String securityProvider;
	private Properties cipherProperties;

	// private SecurityProvider securityProvider;

	/**
	 * Constructor.
	 * 
	 * @param cipherProperties
	 *            the cipher parameters.
	 */
	public SAXEncriptionPBKDF2Writer(Properties cipherProperties) {
		this.cipherProperties = cipherProperties;
		// this.algorithmParameters = cipher.getParameters();
		// this.securityProvider = provider;
	}

	/**
	 * Shot events for writing an <code>EncryptionMethod</code> element;
	 * 
	 * @param cipher
	 *            the block cipher.
	 * @throws SAXException
	 */
	@Override
	protected void writeEncryptionMethod() throws SAXException {
		AttributesImpl atts;
		atts = new AttributesImpl();
		StringBuffer algo = new StringBuffer();
		// algo.append(cipherProperties
		// .getProperty(EncryptionConstants.BLOCKCIPHER));
		// algo.append("/");
		// algo.append(cipherProperties
		// .getProperty(EncryptionConstants.CIPHERMODE));
		// algo.append("/");
		// algo.append(cipherProperties.getProperty(EncryptionConstants.PADDING));

		algo.append(cipherProperties
				.getProperty(EncryptionConstants.XML_ATT_ALGORITHM));
		atts.addAttribute("", "", EncryptionConstants.XML_ATT_ALGORITHM,
				"CDATA", algo.toString());
		handler.startElement("", "", EncryptionConstants.XML_ENCRYPTIONMETHOD,
				atts);
		handler.endElement("", "", EncryptionConstants.XML_ENCRYPTIONMETHOD);
		atts.clear();
	}

	@Override
	protected void writeKeyInfo() throws SAXException {
		char buffer[] = new char[0];

		AttributesImpl atts;
		atts = new AttributesImpl();
		// Open KeyInfo
		atts.addAttribute("", "", "xmlns", "CDATA", EncryptionConstants.XMLDSIG);
		handler.startElement("", "", EncryptionConstants.XML_KEYINFO, atts);
		atts.clear();

		// Algorithm
		handler.startElement("", "", EncryptionConstants.XML_PBKDF, atts);
		buffer = "PBKDF2".toCharArray();
		handler.characters(buffer, 0, buffer.length);
		handler.endElement("", "", EncryptionConstants.XML_PBKDF);
		atts.clear();
		// Random Function
		handler.startElement("", "", EncryptionConstants.XML_HMAC, atts);
		buffer = cipherProperties.getProperty(EncryptionPreferences.PREF_HMAC)
				.toCharArray();
		handler.characters(buffer, 0, buffer.length);
		handler.endElement("", "", EncryptionConstants.XML_HMAC);
		atts.clear();
		// Iteration count
		handler.startElement("", "", EncryptionConstants.XML_ITERATIONS, atts);
		buffer = (cipherProperties
				.getProperty(EncryptionPreferences.PREF_ITERATIONS) + "")
				.toCharArray();
		handler.characters(buffer, 0, buffer.length);
		handler.endElement("", "", EncryptionConstants.XML_ITERATIONS);
		atts.clear();
		handler.startElement("", "", EncryptionConstants.XML_SALT, atts);
		// buffer = new
		// String(Base64.encode(params.getSalt())).toCharArray();
		buffer = new String(
				cipherProperties.getProperty(EncryptionPreferences.PREF_SALT))
				.toCharArray();
		handler.characters(buffer, 0, buffer.length);
		handler.endElement("", "", EncryptionConstants.XML_SALT);
		atts.clear();
		// Key length
		handler.startElement("", "", EncryptionConstants.XML_KEYSIZE, atts);
		buffer = (cipherProperties.getProperty(EncryptionPreferences.PREF_KEYSIZE) + "")
				.toCharArray();
		// buffer = (params.getKeyLen() + "").toCharArray();
		handler.characters(buffer, 0, buffer.length);
		handler.endElement("", "", EncryptionConstants.XML_KEYSIZE);
		atts.clear();

		// Close KeyInfo
		handler.endElement("", "", EncryptionConstants.XML_KEYINFO);
		atts.clear();
	}

	protected void writeKeyName() throws SAXException {
		// char buffer[] = new char[0];
		// AttributesImpl atts;
		// atts = new AttributesImpl();
		// atts.clear();
	}

	/**
	 * Shot events for writing <code>EncryptionProperty</code> security service
	 * provider.
	 * 
	 * @throws SAXException
	 */
	public void writePropertyServiceProvider() throws SAXException {
		char buffer[] = new char[0];
		String provider = cipherProperties
				.getProperty(EncryptionPreferences.PREF_SECURITYPROVIDER);
		if (provider != null) {
			AttributesImpl atts;
			atts = new AttributesImpl();

			atts.addAttribute("", "", EncryptionConstants.XML_ATT_TYPE,
					"CDATA", EncryptionPreferences.PREF_SECURITYPROVIDER);
			handler.startElement("", "",
					EncryptionConstants.XML_ENCRYPTIONPROPERTY, atts);
			atts.clear();
			// Service provider
			handler.startElement("", "",
					EncryptionPreferences.PREF_SECURITYPROVIDER, atts);
			buffer = (cipherProperties
					.getProperty(EncryptionPreferences.PREF_SECURITYPROVIDER))
					.toCharArray();
			handler.characters(buffer, 0, buffer.length);
			handler.endElement("", "",
					EncryptionPreferences.PREF_SECURITYPROVIDER);
			atts.clear();
			handler.endElement("", "",
					EncryptionConstants.XML_ENCRYPTIONPROPERTY);
		}
	}

	/**
	 * Shot events for writing <code>EncryptionProperty</code> security service
	 * provider.
	 * 
	 * @throws SAXException
	 */
	public void writePropertyEncryptionMethod() throws SAXException {
		char buffer[] = new char[0];
		AttributesImpl atts;
		atts = new AttributesImpl();
		atts.addAttribute("", "", EncryptionConstants.XML_ATT_TYPE, "CDATA",
				EncryptionConstants.XML_IV);
		handler.startElement("", "",
				EncryptionConstants.XML_ENCRYPTIONPROPERTY, atts);
		atts.clear();

		// IV
		handler.startElement("", "", EncryptionConstants.XML_IV, atts);
		buffer = (cipherProperties.getProperty(EncryptionPreferences.PREF_IV))
				.toCharArray();
		handler.characters(buffer, 0, buffer.length);
		handler.endElement("", "", EncryptionConstants.XML_IV);
		atts.clear();

		handler.endElement("", "", EncryptionConstants.XML_ENCRYPTIONPROPERTY);
	}

}
