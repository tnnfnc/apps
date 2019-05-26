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

/**
 * Constants in XML Encryption Syntax and Processing. This class is part of the
 * implementation of XML Encryption Standard: <a
 * href="http://www.w3.org/TR/2002/REC-xmlenc-core-20021210/"
 * >http://www.w3.org/TR/2002/REC-xmlenc-core-20021210/</a>.<br />
 * 
 * @author Franco Toninato
 */
public interface EncryptionConstants {
	/**
	 * XML Encryption Syntax and Processing: namespace
	 * URI='http://www.w3.org/2001/04/xmlenc#'
	 */
	public static final String XMLENC = "http://www.w3.org/2001/04/xmlenc#";
	/**
	 * XML-Signature Syntax and Processing: namespace
	 * URI='http://www.w3.org/2000/09/xmldsig#'
	 */
	public static final String XMLDSIG = "http://www.w3.org/2000/09/xmldsig#";
	/*----------------------------------------------------------------------*/
	//
	// EncryptionMethod
	//
	/*----------------------------------------------------------------------*/
	/**
	 * The element name <code>EncryptionMethod</code>.
	 */
	public static final String XML_ENCRYPTIONMETHOD = "EncryptionMethod"; // OK

	/**
	 * <code>Algorithm</code> is an attribute of the element
	 * <code>EncryptionMethod</code>.
	 */
	public static final String XML_ATT_ALGORITHM = "Algorithm";
	// /**
	// * The element name <code>RetrievalMethod</code> with namespace prefix
	// * <em>ds</em>.
	// */
	// public static final String XML_RETRIEVALMETHOD = "RetrievalMethod";
	/** Attribute: name='Type' type='anyURI' use='optional' */
	public static final String XML_ATT_TYPE = "Type";
	/**
	 * The element name <code>"CipherData"</code>.
	 */
	public static final String XML_CIPHERDATA = "CipherData";
	/**
	 * The element name <code>"EncryptedData"</code>.
	 */
	public static final String XML_ENCRYPTEDDATA = "EncryptedData";
	/**
	 * The element name <code>"CipherValue"</code>.
	 */
	public static final String XML_CIPHERVALUE = "CipherValue";
	/**
	 * The element name <code>"EncryptionProperties"</code>.
	 */
	public static final String XML_ENCRYPTIONPROPERTIES = "EncryptionProperties";
	/**
	 * The element name <code>"EncryptionProperty"</code>.
	 */
	public static final String XML_ENCRYPTIONPROPERTY = "EncryptionProperty";
	/**
	 * The element name <code>IV</code>.
	 */
	public static final String XML_IV = "IV";
	/**
	 * The element name <code>KeyInfo</code> with namespace prefix <em>ds</em>.
	 */
	public static final String XML_KEYINFO = "KeyInfo";
	/**
	 * The element name <code>KeyName</code> with namespace prefix <em>ds</em>.
	 */
	public static final String XML_KEYNAME = "KeyName";
	/*----------------------------------------------------------------------*/
	//
	// EncryptionMethod
	//
	/*----------------------------------------------------------------------*/

	/**
	 * The element name <code>KeySize</code>.
	 */
	public static final String XML_KEYSIZE = "KeySize";
	/**
	 * The element name <code>KeyInfo</code> with namespace prefix <em>ds</em>.
	 */
	// public static final String XML_KEYVALUE = "KeyValue";
	/**
	 * Element <code>pbe:IterationsCount</code>: iterations for enhancing the
	 * security level against dictionary attacks.
	 */
	public static final String XML_ITERATIONS = "IterationsCount";
	/**
	 * Element: Password based key derivation function. The supported standard
	 * is, at present:
	 * <ul>
	 * <li>PBKDF 2 - The pseudo random function is a HMAC with an underlying
	 * SHA2 family hash algorithm.</li>
	 * </ul>
	 * For details see the <a
	 * href="http://www.rsa.com/rsalabs/node.asp?id=2127"> PKCS #5 v2.1:
	 * Password-Based Cryptography Standard</a>.
	 */
	public static final String XML_PBKDF = "PBKDFunction";

	/**
	 * Preference <code>Salt</code>: salt for password generation.
	 */
	public static final String XML_SALT = "Salt";
	// /**
	// * The element name <code>pbe:PBEParameters</code>.
	// */
	// public static final String PREF_PBE_PARAMETERS = "PBEParameters";
	/**
	 * Element Message authentication code. MAC syntax: HMAC/hash.
	 */
	public static final String XML_HMAC = "HMACAlgorithm";

}