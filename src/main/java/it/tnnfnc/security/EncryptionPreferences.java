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
package it.tnnfnc.security;

import it.tnnfnc.security.xml.EncryptionConstants;

/**
 * Properties for encryption processing.
 * 
 * @author Franco Toninato
 */
public class EncryptionPreferences {

	/**
	 * The security services provider.
	 */
	public static final String PREF_SECURITYPROVIDER = "SecurityProvider";
	/**
	 * Preference: Password based key derivation function.
	 */
	public static final String PREF_PBKDF = EncryptionConstants.XML_PBKDF;
	/**
	 * Preference: Message authentication code. MAC syntax: HMAC/hash.
	 */
	public static final String PREF_HMAC = EncryptionConstants.XML_HMAC;
	/**
	 * Element: <code>Salt</code>: salt for password generation.
	 */
	public static final String PREF_SALT = EncryptionConstants.XML_SALT;
	/**
	 * Preference <code>IterationsCount</code>: iterations for enhancing the
	 * security level against dictionary attacks.
	 */
	public static final String PREF_ITERATIONS = EncryptionConstants.XML_ITERATIONS;
	/**
	 * Preference: <code>HashAlgorithm</code>.
	 */
	public static final String PREF_HASH = "HashAlgorithm";
	/*----------------------------------------------------------------------
	 */
	//
	// EncryptionMethod
	//
	/*----------------------------------------------------------------------*/
	/**
	 * Preference: Block cipher algorithm, according to the following syntax:
	 * <code>EncryptionMethod Algorithm="block-cipher/operation-mode/pad-scheme"</code>
	 * attribute.
	 */
	public static final String PREF_CIPHER_ALGORITHM = EncryptionConstants.XML_ATT_ALGORITHM;
	/**
	 * Preference: secret key size in bits. /* public static final String
	 */
	public static final String PREF_KEYSIZE = EncryptionConstants.XML_KEYSIZE;
	/**
	 * The preference <code>IV</code>.
	 */
	public static final String PREF_IV = EncryptionConstants.XML_IV;

}
