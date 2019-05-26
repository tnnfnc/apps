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
package it.tnnfnc.xml; //Package

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.OutputKeys;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;

import org.xml.sax.SAXException;

//import IllegalArgumentException;

/**
 * This class is a collection of methods useful in changing the DOM tree.
 * <p />
 * In particular pay attention when using the <code>NodeList</code> object in a
 * loop. In this case it is better to perform a conversion of the content to an
 * array of nodes
 * 
 * @see XmlUtil toArray method.
 * 
 */
public class XmlUtil {
	private XmlUtil() {
	}

	/**
	 * Create a new DOM document.
	 * 
	 *@return a new DOM document.
	 * 
	 *@exception ParserConfigurationException
	 *                when the <code>Document</code> cannot be be created.
	 */
	public static Document createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.newDocument();
	}

	/**
	 * Return a DOM document from an input stream. The input document must be
	 * well formed.
	 * 
	 *@param in
	 *            the input stream from which the DOM document is created.
	 *@return the DOM document.
	 * 
	 *@exception SAXException
	 *                report a parsing error.
	 *@exception IOException
	 *                report a parser configuration error.
	 */
	public static Document read(InputStream in) throws SAXException,
			IOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);
			in.close();
			doc.normalizeDocument();
			return doc;
		} catch (ParserConfigurationException e) {
			SAXException exception = new SAXException(
					"Parser Configuration Exception - " + e.getMessage());
			exception.initCause(e);
			throw exception;
		} finally {
			in.close();
		}
	}

	/**
	 * Write a DOM node to an output stream as a new DOM document. The XML
	 * Declaration is added at the beginning of the output with the default
	 * properties setting: <code>encoding="UTF-8"</code> and
	 * <code>standalone="no"</code>.
	 * 
	 *@param node
	 *            the DOM node.
	 *@param out
	 *            the output stream.
	 *@exception IOException
	 *                when an error occurs in the XML transformation step.
	 */
	public static void write(Document doc, OutputStream out) throws IOException {
		/* Writing the DOM to an output stream */
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			// Document document = node.getOwnerDocument();
			// if (document.getXmlStandalone())
			// t.setOutputProperty(OutputKeys.STANDALONE, "yes");
			// if (document.getXmlVersion() != null)
			// t.setOutputProperty(OutputKeys.VERSION, document
			// .getXmlVersion());
			// t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType);
			// t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StreamResult output = new StreamResult(out); // To cancel the lock
			// after saving
			t.transform(new DOMSource(doc), output);
		} catch (TransformerException e) {
			IOException ee = new IOException("Transformer Exception - "
					+ e.getMessage());
			ee.initCause(e);
			throw ee;
		} catch (TransformerFactoryConfigurationError e) {
			IOException ee = new IOException("Transformer Factory Exception - "
					+ e.getMessage());
			ee.initCause(e);
			throw ee;
		} finally {
			// out.close();
		}
	}

	/**
	 * Write a DOM node to an output stream omitting the XML declaration.
	 * 
	 * @param node
	 *            the node.
	 *@param out
	 *            the output stream.
	 *@exception IOException
	 *                when an error occurs in the XML transformation step.
	 */
	public static void write(Node node, OutputStream out) throws IOException {
		/* Writing the DOM to an output stream */
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			// t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType);
			// t.setOutputProperty(OutputKeys.STANDALONE, "yes");
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StreamResult output = new StreamResult(out); // To cancel the lock
			// after saving
			t.transform(new DOMSource(node), output);
		} catch (TransformerException e) {
			IOException ee = new IOException("Transformer Exception - "
					+ e.getMessage());
			ee.initCause(e);
			throw ee;
		} catch (TransformerFactoryConfigurationError e) {
			IOException ee = new IOException("Transformer Factory Exception - "
					+ e.getMessage());
			ee.initCause(e);
			throw ee;
		} finally {
			// out.close();
		}
	}

	/**
	 * Add an only-child to an existing node. It removes all existing children
	 * with the same name as the one is to be added.
	 * 
	 *@param parent
	 *            the "parent" node the element is added to.
	 *@param onlyChild
	 *            the child element.
	 * 
	 *@exception DOMException
	 *                when the element cannot be appended.
	 */
	public static void appendUniqueChild(Element parent, Element onlyChild)
			throws DOMException {
		int n = parent.getElementsByTagName(onlyChild.getTagName()).getLength();
		if (n == 0) {
			parent.appendChild(onlyChild);
		} else if (n == 1) {
			parent.replaceChild(onlyChild, parent.getElementsByTagName(
					onlyChild.getTagName()).item(0));
		} else {
			NodeList nodes = parent
					.getElementsByTagName(onlyChild.getTagName());
			parent.replaceChild(onlyChild, parent.getElementsByTagName(
					onlyChild.getTagName()).item(0));
			for (int i = 1; i < n; i++) {
				parent.removeChild(nodes.item(1));
			}
		}
	}

	/**
	 * Return true if the node has a child with the given tag name.
	 * 
	 * @param parent
	 *            the parent element.
	 *@param tagName
	 *            the element tag name.
	 *@return the element if the node has a child with the given tag name, else
	 *         return the <code>null</code> reference.
	 */
	public static Element hasElement(Element parent, String tagName) {
		Element e = null;
		if (parent.getElementsByTagName(tagName).getLength() > 0) {
			e = (Element) parent.getElementsByTagName(tagName).item(0);
		}
		return e;
	}

	/**
	 * Returns the text from an element with a text.
	 * 
	 *@param e
	 *            the <code>Element</code> object.
	 *@return the text from its first child text node, else it returns a null
	 *         string.
	 */
	public static String getText(Element e) {
		if (e == null)
			return null;
		NodeList childs = e.getChildNodes();
		String text = "";
		for (int i = 0; i < childs.getLength(); i++) {
			short type = childs.item(i).getNodeType();
			switch (type) {
			case Node.TEXT_NODE:
				text = text + ((Text) childs.item(i)).getData().trim();
				break;
			default:
			}
		}
		return text;
	}

	/**
	 * Return an element content as a string object. The output is not a well
	 * formed document, XML declaration is missing.
	 * 
	 *@param element
	 *            the element of the document.
	 *@return the element content as a string object.
	 *@exception IOException
	 *                when an error occurs during the node conversion to a
	 *                string.
	 * 
	 */
	public static String getContent(Element element) throws IOException {
		String elementContent = new String();
		NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++)
			elementContent += toString(nodes.item(i));
		return elementContent;
	}

	/**
	 * Return a DOM document node as a string object. The output is not a well
	 * formed document, XML declaration is missing.
	 * 
	 *@param node
	 *            the node to be serialized into a string.
	 *@return the node serialized into a string object.
	 *@exception IOException
	 *                when an error occurs during the node conversion to a
	 *                string.
	 * 
	 */
	public static String toString(final Node node) throws IOException {
		node.normalize();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		write(node, bos);

		return bos.toString();
	}

	/**
	 * Return a DOM document node as a string object.
	 * 
	 *@param document
	 *            the node to be serialized into a string.
	 *@return the node serialized into a string object.
	 *@exception IOException
	 *                when an error occurs during the node conversion to a
	 *                string.
	 * 
	 */
	public static String toString(final Document document) throws IOException {
		document.normalize();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		write(document, bos);

		return bos.toString();
	}

	/**
	 * Return a <code>NodeList</code> as an array of nodes. <b> This conversion
	 * is useful because the removal of a node child alters its
	 * <code>NodeList</code> object index. </b>
	 * 
	 *@param content
	 *            the <code>NodeList</code> object.
	 *@return the array of nodes.
	 * 
	 */
	public static Node[] toArray(NodeList list) {
		Node[] nodes = new Node[list.getLength()];
		for (int i = 0; i < list.getLength(); i++) {
			nodes[i] = list.item(i);
		}
		return nodes;
	}

	/**
	 * Tidy up an element according to a sequence of elements tag names. All
	 * nodes left are appended at the end in the same order the were found.
	 * 
	 *@param parentElement
	 *            the parent element to be ordered.
	 *@param elementSequence
	 *            the element tag name sequence.
	 * 
	 */
	public static void tidyUp(Element parentElement, String[] elementSequence) {
		// Empty the element
		Node[] nodes = empty(parentElement);
		// Tidy up the element
		for (String s : elementSequence)
			for (int i = 0; i < nodes.length; i++) {
				if (nodes[i] != null
						&& nodes[i].getNodeType() == Node.ELEMENT_NODE
						&& ((Element) nodes[i]).getTagName().equals(s)) {
					parentElement.appendChild(nodes[i]);
					nodes[i] = null;
				}
			}
		// Append left nodes
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null)
				// && nodes[i].getNodeType() == Node.ELEMENT_NODE ) nothing
				// discarded!!
				parentElement.appendChild(nodes[i]);
		}
	}

	/**
	 * Empty node of its children and return them as an array.
	 * 
	 *@param node
	 *            the node.
	 *@return the array of the removed children.
	 * 
	 */
	public static Node[] empty(Node node) {
		// Empty the element
		NodeList children = node.getChildNodes();
		Node[] nodes = new Node[children.getLength()];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = node.removeChild(children.item(0));
		}
		return nodes;
	}

}
