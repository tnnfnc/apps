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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import it.tnnfnc.xml.XmlUtil;

/**
 * Writes a content of object record to an XML content representation. The specific
 * object must has its adapter.
 * 
 * @author Franco Toninato
 * 
 */
public class XMLListWriter {
	// It may be interesting to write XML sequentially providing
	// instructions to map field into an XML structure.
	// public static final String DOCUMENT_TYPE = "content";
	private XMLListItemAdapter xmlAdapter;

	/**
	 * Creates an XML content writer.
	 * 
	 * @param adapter
	 *            the adapter of objects to be written to the content.
	 */
	public XMLListWriter(XMLListItemAdapter adapter) {
		this.xmlAdapter = adapter;
	}

	/**
	 * Writes an array of objects to an XML content.
	 * 
	 * @param items
	 *            the object array.
	 * @param out
	 *            the output content.
	 * @throws IOException
	 *             when an error occurs.
	 */
	public void write(ArrayList<Object> items, OutputStream out)
			throws IOException {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			Element list = document.createElement(XMLListReader.LIST);
			Element header = document.createElement(XMLListReader.HEADER);
			document.appendChild(list);
			list.appendChild(header);
			// Attributes
			list.setAttribute(XMLListReader.VERSION, "1.0");

			/*
			 * For each object record write a content child. Every child is
			 * splitted into field-value pairs
			 */
			int recnum = 0;
			for (Object record : items) {
				Element item = document.createElement(XMLListReader.ITEM);
				item.setAttribute("id", recnum + "");
				list.appendChild(item);
				xmlAdapter.newInstance(record);
				/*
				 * For each getter appends a field element with the attribute
				 * name equal to the processing attribute.
				 */
				for (String f : xmlAdapter.getFields()) {
					Element field = document.createElement(XMLListReader.FIELD);
					field.setAttribute(XMLListReader.NAME, f);
					item.appendChild(field);
					try {
						field.appendChild(document.createTextNode(xmlAdapter
								.getField(f).toString()));
					} catch (DOMException e) {
						// Nothing, simply skip
					} catch (IllegalArgumentException e) {
						// Nothing, simply skip
					} catch (IllegalAccessException e) {
						// Nothing, simply skip
					}
				}
				/* Then appends the item to the content */
				list.appendChild(item);
			}
			XmlUtil.write(document, out);
		} catch (ParserConfigurationException e) {
			IOException ee = new IOException(e.getMessage());
			ee.initCause(e);
			throw ee;
		}
	}

}
