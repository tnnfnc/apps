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
package it.tnnfnc.xml.list;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import it.tnnfnc.xml.XmlUtil;

/**
 * Reader class for a content of items from an XML document.
 * 
 * @author Franco Toninato
 * 
 */
public class XMLListReader {

	/**
	 * List DTD.
	 */
	public static final String LIST_DTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!-- DTD for simple content -->"
			+ "<!ELEMENT content ( header?, item* ) >"
			+ "<!ATTLIST content version CDATA #FIXED \"1.0\">"
			+ "<!ELEMENT header (#PCDATA) >"
			+ "<!ELEMENT item ( field* ) >"
			+ "<!ATTLIST item id CDATA #REQUIRED>"
			+ "<!ELEMENT field (#PCDATA) >"
			+ "<!ATTLIST field name CDATA #REQUIRED>";

	/**
	 * Document element.
	 */
	static final String LIST = "content";
	/**
	 * Header element.
	 */
	static final String HEADER = "header";
	/**
	 * Item element.
	 */
	static final String ITEM = "item";
	/**
	 * Field element.
	 */
	static final String FIELD = "field";
	/**
	 * Attribute of field element.
	 */
	static final String NAME = "name";
	/**
	 * DTD Version attribute.
	 */
	static final String VERSION = "version";

	private String buffer; // record buffer
	// private OutputByteStream outbuffer; // record buffer
	private XMLListItemAdapter xmlAdapter; // 
	private String fieldName;
	private String header;
	private String version;
	private ArrayList<Object> out = new ArrayList<Object>();
	private ArrayList<String> records;
	/**
	 * The internal SAX document handler for parsing the content.
	 */
	protected DefaultHandler _XMLListHandler;

	/**
	 * Creates an XML content reader.
	 * 
	 * @param wrapper
	 *            the adapter of objects to be written to the content.
	 */
	public XMLListReader(XMLListItemAdapter wrapper) {
		this.xmlAdapter = wrapper;
	}

	/**
	 * Reads the input into an array of object records.
	 * 
	 * @param in
	 *            the document source.
	 * @return the content of objects each representing a record of the content.
	 * @throws IOException
	 *             when the input process fails.
	 */
	public ArrayList<Object> read(InputStream in) throws IOException {
		// Parser setup.
		XMLReader reader;
		try {
			_XMLListHandler = new XMLListHandler();
			reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(_XMLListHandler);
			reader.setErrorHandler(_XMLListHandler);
		} catch (SAXException e) {
			IOException ee = new IOException(e.getMessage());
			ee.initCause(e);
			throw ee;
		}
		// Reads the input and converts the XML content to an array of objects.
		// Creates an input source from the input stream.
		try {
			reader.parse(new InputSource(new BufferedReader(
					new InputStreamReader(in))));
		} catch (SAXException e) {
			throw new IOException(e.getMessage());
		}
		return out;
	}

	/**
	 * Reads an input XML content document into an array of object records.
	 * 
	 * @param in
	 *            the document source.
	 * @return the content of objects each representing a record of the content.
	 * @throws IOException
	 *             when the input process fails.
	 */
	public ArrayList<Object> read(Document document) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XmlUtil.write(document, bos);
		return read(new ByteArrayInputStream(bos.toByteArray()));
	}

	/**
	 * Returns the valid DTD for this content.
	 * 
	 * @return the content DTD.
	 */
	public static String getDTD() {
		return LIST_DTD;
	}

	/**
	 * Returns the header string.
	 * 
	 * @return the header.
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Returns the version of the document.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Returns the records processed as strings.
	 * 
	 * @return the records processed.
	 */
	public String[] getRecords() {
		return (String[]) records.toArray();
	}

	private class XMLListHandler extends DefaultHandler {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
		 */
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// Accumulates characters in a buffer
			// String chunk = new String(ch, start, length);
			byte[] chb = new byte[ch.length];
			for (int i = 0; i < ch.length; i++) {
				chb[i] = (byte) ch[i];
			}
			// When buffer is null it is initialized to the collected chars.
			try {
				buffer += new String(chb, start, length, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new SAXException(e.getMessage());
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
			// super.startDocument();
			// out = new ArrayList<Object>();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
		 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if (localName.equals(LIST)) {
				version = attributes.getValue(VERSION);
			} else if (localName.equals(ITEM)) {
				newItem(attributes.getValue(NAME));
			} else if (localName.equals(FIELD)) {
				newField(attributes.getValue(NAME));
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
			if (localName.equals(HEADER)) {
				header = buffer.trim();
			} else if (localName.equals(LIST)) {
				// Nothing
			} else if (localName.equals(ITEM)) {
				append();
				xmlAdapter.newInstance();
			} else if (localName.equals(FIELD)) {
				try {
					xmlAdapter.setField(fieldName, buffer.trim());
				} catch (Exception e) {
					throw new SAXException("Invalid field value");
				}
			} else {
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
		 */
		@Override
		public void endDocument() throws SAXException {
			// Auto-generated method stub
			super.endDocument();
		}

		/**
		 * Sets the current field name.
		 * 
		 * @param name
		 */
		private void newField(String name) {
			fieldName = name;
		}

		/**
		 * Initializes the buffer for a new record.
		 * 
		 * @param name
		 */
		private void newItem(String name) {
			buffer = null;
			records.add(name);
		}

		/**
		 * Appends an object to the output content.
		 */
		private void append() {
			out.add(xmlAdapter.getInstance());
		}

	}
}
