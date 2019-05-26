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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;

import it.tnnfnc.security.algorithm.AESEngine;
import it.tnnfnc.security.algorithm.IdentityEngine;
import it.tnnfnc.security.algorithm.PBES2KeyAlgorithm;
import it.tnnfnc.security.algorithm.PKCS5Padding;
import it.tnnfnc.security.algorithm.PaddedBlockCipherStream;
import it.tnnfnc.security.algorithm.SerpentEngine;
import it.tnnfnc.security.algorithm.TwofishEngine;
import it.tnnfnc.security.crypto.CBCBlockCipher;
import it.tnnfnc.security.crypto.ECBlockCipher;
import it.tnnfnc.security.crypto.HMac;
import it.tnnfnc.security.crypto.I_BlockCipher;
import it.tnnfnc.security.crypto.I_BlockCipherStream;
import it.tnnfnc.security.crypto.I_BlockPadding;
import it.tnnfnc.security.crypto.I_Mac;
import it.tnnfnc.security.crypto.I_PasswordBasedKeyDerivator;

public class SecurityProvider {

	private static final HashMap<String, String[]> services = new HashMap<String, String[]>();
	private static final String INTERNAL = "internal";
	/*----------------------------------------------------------------------*/
	//
	// EncryptedType: block cipher parameters
	//
	/*----------------------------------------------------------------------*/
	/**
	 * Types of block cipher algorithms, is a part of
	 * <code>EncryptionMethod Algorithm="block-cipher / operation-mode / pad-scheme"</code>
	 * attribute.
	 */
	public static final String TYPE_BLOCKCIPHER = "BlockCipher";
	/**
	 * Types of multiple blocks cipher stream operation mode, is a part of
	 * <code>EncryptionMethod Algorithm="block-cipher / operation-mode / pad-scheme"</code>
	 */
	public static final String TYPE_BLOCKCIPHER_MODE = "StreamCipherMode";
	/**
	 * Types of last block padding schema, is a part of
	 * <code>EncryptionMethod Algorithm="block-cipher / operation-mode / pad-scheme"</code>
	 */
	public static final String TYPE_BLOCK_PADDING = "BlockPadding";
	static {
		// Blockciphers
		services.put(TYPE_BLOCKCIPHER, new String[] { "AES", "SERPENT",
				"TWOFISH", "IDENTITY", });
		// Cipher stream Operation Mode
		services.put(TYPE_BLOCKCIPHER_MODE, new String[] { "CBC", "ECB", });
		// Block padding scheme
		services.put(TYPE_BLOCK_PADDING, new String[] { "PKCS5Padding" }); // PAD8
		// Hash
		services.put(EncryptionPreferences.PREF_HASH, new String[] { "MD5",
				"SHA-1", "SHA-256", "SHA-512", });
		// Hmac
		services.put(EncryptionPreferences.PREF_HMAC, new String[] { "HMAC" });
		// Password based key derivation functions
		services.put(EncryptionPreferences.PREF_PBKDF,
				new String[] { "PBKDF2", });
	}

	private boolean isAvailable(String s) {
		if (getServiceType(s) != null)
			return true;
		return false;
	}

	/**
	 * Parses an algorithm.
	 * 
	 * @param algorithm
	 *            the algorithm.
	 * @return the parsed and formatted result.
	 */
	public static String[] parseRequest(String algorithm) {
		String[] parse = algorithm.trim().split("/");
		for (int i = 0; i < parse.length; i++) {
			parse[i] = parse[i].trim().toUpperCase();
		}
		return parse;
	}

	/**
	 * Build the service descriptor.
	 * 
	 * @param a
	 *            the services IDs.
	 * @return the array of reqistered services.
	 */
	protected Service[] getServicesImpl(String[] a) {
		Service s[] = new Service[a.length];
		for (int i = 0; i < a.length; i++) {
			s[i] = new Service(this, a[i]);
			// System.out.println("Service " + a[i]);
			if (getServiceType(a[i]) != null
					&& getServiceType(a[i]).equalsIgnoreCase(TYPE_BLOCKCIPHER)) {
				// s[i].setKeyLenght(new int[] { 128, 192, 256 });
				s[i].setKeyLenght(createBlockCipher(a[i]).getKeySize());
			}
		}
		return s;
	}

	/**
	 * Creates a padding object.
	 * 
	 * @param padSchema
	 *            the pad schema adopted up to the block cipher size. Possible
	 *            values are "PAD" or "noPAD".
	 * @return an instance of the padding class.
	 * @throws IllegalArgumentException
	 *             when the padding schema is not supported or implemented.
	 */
	protected I_BlockPadding createBlockPadding(String padSchema)
			throws IllegalArgumentException {
		// paddings
		if (padSchema.equalsIgnoreCase("PAD8")
				|| padSchema.equalsIgnoreCase("PKCS5Padding")) {
			return new PKCS5Padding();
		} else if (padSchema.equalsIgnoreCase("NoPadding")) {
			return null;
		}
		throw new IllegalArgumentException(padSchema
				+ " is not a valid padding schema.");
	}

	/**
	 * Creates a random bytes generator.
	 * 
	 * @return an instance of the generator class.
	 * @throws IllegalArgumentException
	 *             when the padding schema is not supported or implemented.
	 * @throws NoSuchAlgorithmException
	 */
	public SecureRandom createSecureRandom() throws IllegalArgumentException,
			NoSuchAlgorithmException {
		return SecureRandom.getInstance("SHA1PRNG");
	}

	/**
	 * Returns a hash function as requested by the provided parameters.
	 * 
	 * @param algorithm
	 *            the hash algorithm.
	 * @return a cipher.
	 * @exception IllegalArgumentException
	 *                when the algorithm is not supported by the current
	 *                factory.
	 */
	public MessageDigest createMessageDigest(String algorithm)
			throws IllegalArgumentException {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage() + algorithm);
		}
	}

	/**
	 * Creates a Message Authentication Code. Syntax: HMAC/HASH (
	 * {@link #createMessageDigest(String) createHash} is called to provide the
	 * hash function implementation).
	 * 
	 * @param algorithm
	 *            the hash function algorithm name. For example "HMAC/SHA-1"
	 *            provides a HMAC compliant to RFC2104 using a SHA-1 hash
	 *            function.
	 * @return a Message authentication code.
	 * @throws IllegalArgumentException
	 *             when the algorithm is not supported or invalid.
	 */
	public I_Mac createMac(String algorithm) throws IllegalArgumentException {
		/* HMAC with SHA-1: mac type "/" hash function */
		String[] parse = algorithm.trim().split("/");
		if (parse[0].toUpperCase().trim().equals("HMAC")) {
			MessageDigest h = createMessageDigest(parse[1].toUpperCase().trim());
			return new HMac(h);
		}
		throw new IllegalArgumentException("algorithm not implemented: "
				+ algorithm);
	}

	/**
	 * Creates a cipher. Syntax: ALGORITHM.
	 * 
	 * @param algorithm
	 *            the name of the block cipher algorithm.
	 * @return the block cipher.
	 * @throws IllegalArgumentException
	 *             when the algorithm is not supported or invalid.
	 * 
	 */
	public I_BlockCipher createBlockCipher(String algorithm)
			throws IllegalArgumentException {
		String[] a = parseRequest(algorithm);
		// blockCiphers
		if (a[0].equals("AES"))
			return new AESEngine();
		if (a[0].equals("TWOFISH"))
			return new TwofishEngine();
		if (a[0].equals("SERPENT"))
			return new SerpentEngine();
		if (a[0].equals("IDENTITY"))
			return new IdentityEngine();
		throw new IllegalArgumentException("Algorithm not supported ");
	}

	/**
	 * Creates a block cipher stream for processing input stream. Syntax:
	 * ALGORITHM(*)/CIPHER_MODE(**)/PADDING_SCHEMA(***). </br>(*) It calls
	 * {@link #createBlockCipher(String) createBlockCipher}, (***) it calls
	 * {@link #createBlockPadding(String) createBlockPadding}.
	 * 
	 * @param algorithm
	 *            string case insensitive with the block cipher "algorithm"
	 *            followed by the "cipher mode" followed by the "PAD" or "noPAD"
	 *            option for padding all separated by the slash character. The
	 *            default padding schema is detailed in RFC 1423.
	 * @return a block cipher ready to process data stream.
	 * @throws IllegalArgumentException
	 *             when the algorithm is not supported or invalid.
	 */
	public I_BlockCipherStream createBlockCipherStream(String algorithm)
			throws IllegalArgumentException {
		// Parse the params: 0 = alg, 1 = mode, 2 = padding
		String[] parts = parseRequest(algorithm);
		if (parts.length < 3)
			throw new IllegalArgumentException("Missing algorithm parameters: "
					+ parts[0] + ", " + parts[1] + ", " + parts[2]);

		I_BlockCipher blockcipher = createBlockCipher(parts[0]);
		I_BlockPadding padding = createBlockPadding(parts[2]);
		I_BlockCipherStream i_BlockCipherStream;

		if (padding != null) {
			padding = createBlockPadding("PKCS5Padding");
		}
		if (parts[1].equals("ECB")) {
			i_BlockCipherStream = new PaddedBlockCipherStream(
					new ECBlockCipher(blockcipher), padding);
		} else if (parts[1].equals("CBC")) {
			i_BlockCipherStream = new PaddedBlockCipherStream(
					new CBCBlockCipher(blockcipher), padding);
		} else {
			throw new IllegalArgumentException(parts[1]
					+ ": not supported encoding mode");
		}

		return i_BlockCipherStream;
	}

	/**
	 * Create a password based secret key generator. Syntax: METHOD/HASH.
	 * 
	 * @param algorithm
	 *            string case insensitive with the type of "BPKDF2".
	 * @return a password based secret key generator.
	 * @throws IllegalArgumentException
	 *             when the algorithm is not supported or invalid.
	 */
	public I_PasswordBasedKeyDerivator createPasswordBasedKeyDerivator(
			String algorithm) throws IllegalArgumentException {
		/*
		 * string case insensitive with the type of "PBES1" followed by the
		 * "hash algorithm" all separated by the slash character.
		 */
		String[] parse = algorithm.trim().split("/");
		if (parse[0].toUpperCase().trim().equals("PBKDF2")) {
			I_PasswordBasedKeyDerivator pbe = new PBES2KeyAlgorithm();
			return pbe;
		}
		throw new IllegalArgumentException(algorithm + " not supported.");
	}

	/**
	 * Get e service.
	 * 
	 * @param type
	 *            the type of services required.
	 * @param id
	 *            the service identification key.
	 * @return the service.
	 * @throws IllegalArgumentException
	 *             when the service required is not provided.
	 */
	public Service getService(String type, String id)
			throws IllegalArgumentException {
		for (Service s : getAvailableServices(type)) {
			if (s.getId().equalsIgnoreCase(id))
				return s;
		}
		return null;
	}

	/**
	 * Get the available services types.
	 * 
	 * @return the available services types.
	 */
	public String[] getServiceTypes() {
		return services.keySet().toArray(new String[0]);
	}

	/**
	 * Get the available services under a type.
	 * 
	 * @param type
	 *            the type of services required.
	 * @return the available services per type.
	 */
	public Service[] getAvailableServices(String type) {
		Service[] s = getServicesImpl(services.get(type));
		return s;
	}

	/**
	 * Get a specific service.
	 * 
	 * @param parameters
	 *            string parameters describing a service.
	 * @return a specific service.
	 * @throws IllegalArgumentException
	 *             when the service required is not provided.
	 */
	public Service[] parseService(String parameters)
			throws IllegalArgumentException {
		// 1) Parse the request
		String[] servicesLiteral = parseRequest(parameters);

		// 2) Check service availability
		for (String s : servicesLiteral) {
			if (!isAvailable(s)) {
				// throw new IllegalArgumentException(s +
				// " Algorithm not supported");
			}
		}
		// 3)
		return getServicesImpl(servicesLiteral);
	}

	/**
	 * Get a service type.
	 * 
	 * @param id
	 *            the service identification key.
	 * @return the service type or null.
	 */
	public String getServiceType(String id) {
		String type = new String();
		for (Iterator<String> iterator = services.keySet().iterator(); iterator
				.hasNext();) {
			type = iterator.next();
			for (String service : services.get(type)) {
				if (service.equalsIgnoreCase(id))
					return type;
			}
		}
		return new String();
	}

	/**
	 * Get unsupported service.
	 * 
	 * @param id
	 *            the service identification key.
	 * @return the unsupported service from the id.
	 */
	public Service getUnsupportedService(String id) {
		return new Service(this, id);
	}

	/**
	 * Get the service provider name.
	 * 
	 * @return the service provider name.
	 */
	public String getName() {
		return INTERNAL;
	}

	/**
	 * Service definition.
	 * 
	 * @author Franco Toninato
	 * 
	 */
	public static class Service {
		protected SecurityProvider provider;
		protected String id;
		protected String classname;
		private String[] keyLength = new String[0];

		/**
		 * Service constructor from a security provider.
		 * 
		 * @param securityProviderImpl
		 *            the security provider.
		 * @param id
		 *            the service id.
		 */
		public Service(SecurityProvider securityProviderImpl, String id) {
			// this.parent = null;
			this.provider = securityProviderImpl;
			this.id = id;
			// this.children = new ArrayList<Service>();
			// this.children = new HashMap<Object, Service>();
		}

		/**
		 * Service constructor from a service parent. The security provider is
		 * inherited from its parent.
		 * 
		 * @param parent
		 *            the service parent.
		 * @param id
		 *            the service id.
		 */
		public Service(Service parent, String id) {
			// this.parent = parent;
			this.provider = parent.provider;
			this.id = id;
			// this.children = new ArrayList<Service>();
			// this.children = new HashMap<Object, Service>();
		}

		/**
		 * Get the class implementing this service.
		 * 
		 * @return the implementing class name.
		 */
		public String getImpmentingClassName() {
			return classname;
		}

		/**
		 * Get the service identifier.
		 * 
		 * @return the service identifier.
		 */
		public String getId() {
			return id;
		}

		/**
		 * Get the service type.
		 * 
		 * @return the service type.
		 */
		public String getType() {
			return provider.getServiceType(this.id);
		}

		public void setKeyLenght(String[] keys) {
			this.keyLength = keys;
		}

		public String[] getKeyLenght() {
			return this.keyLength;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return id;
			// return id + " - " + provider.getServiceName(this.id);
		}

	}
}
