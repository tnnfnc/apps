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
package it.tnnfnc.apps.application.properties;

import java.util.Enumeration;
import java.util.Properties;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * The class is responsible of writing properties.
 * 
 * @author Franco Toninato
 * 
 */
public class PropertiesXMLWriter {

	private ContentHandler handler;
	private String key;
	private String value;

	/**
	 * Creates a new xml writer.
	 * 
	 * @param h
	 *            the content handler.
	 */
	public PropertiesXMLWriter(ContentHandler h) {
		this.handler = h;
	}

	/**
	 * Shot events for writing an access;
	 * 
	 * @param a
	 *            the access.
	 * @throws SAXException
	 */
	public void write(Properties p) throws SAXException {
		AttributesImpl atts;
		atts = new AttributesImpl();
		handler.startElement("", "", "properties", atts);

		handler.startElement("", "", "comment", atts);
		// no comment!
		handler.endElement("", "", "comment");

		if (p != null) {
			Enumeration<?> keys = p.propertyNames();
			while (keys.hasMoreElements()) {
				atts.clear();
				key = keys.nextElement().toString();
				value = p.getProperty(key);
				atts.addAttribute("", "", "key", "CDATA", key);
				handler.startElement("", "", "entry", atts);
				handler.characters(value.toCharArray(), 0, value.length());
				handler.endElement("", "", "entry");
			}
		}
		handler.endElement("", "", "properties");
	}
}
