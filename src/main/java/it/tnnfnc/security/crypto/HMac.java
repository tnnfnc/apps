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
package it.tnnfnc.security.crypto; //Package

import java.security.DigestException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import it.tnnfnc.security.algorithm.AlgorithmParameters;
import it.tnnfnc.security.algorithm.KeyAlgorithmParameter;

/**
 * Message authentication code. A <code>SmartHMac</code> is a message
 * authentication code based on the following formula: (K-ey, m-essage) ->
 * HF((K+opad)||HF((K+ipad)||text))) where:
 * <ul>
 * <li>HF is a hash function (f.e. SHA-2)
 * <li>ipad = 0x36, opad = 0x5C.
 * <li>+ is a XOR on bits
 * <li>|| is the concatenation operator
 * </ul>
 * The K length should be in between the hash algorithm output length and the
 * algorithm block length (for block hashing functions). Preferences may be
 * expressed on the provider of the SHA-512 algorithm, a change of provider may
 * not reproduces the same result.
 * <p />
 * The following table summarizes the characteristics of the SHA family:
 * <table>
 * <tbody>
 * <tr>
 * <th>Algorithm</th>
 * <th>Output size (bits)</th>
 * <th>Internal state size (bits)</th>
 * <th>Block size (bits)</th>
 * <th>Max message size (bits)</th>
 * <th>Word size (bits)</th>
 * <th>Rounds</th>
 * <th>Operations</th>
 * <th>Collision</th>
 * </tr>
 * 
 * <tr align="center">
 * <td><b>SHA-0</b></td>
 * <td>160</td>
 * <td>160</td>
 * <td>512</td>
 * <td>2<sup>64</sup>-1</td>
 * <td>32</td>
 * <td>80</td>
 * <td>+,and,or,xor,rotl</td>
 * <td>Yes</td>
 * </tr>
 * 
 * <tr align="center">
 * <td><b>SHA-1</b></td>
 * <td>160</td>
 * <td>160</td>
 * <td>512</td>
 * <td>2<sup>64</sup>-1</td>
 * <td>32</td>
 * <td>80</td>
 * <td>+,and,or,xor,rotl</td>
 * <td>2<sup>63</sup> attack</td>
 * </tr>
 * 
 * <tr align="center">
 * <td><b>SHA-256/224</b></td>
 * <td>256/224</td>
 * <td>256</td>
 * <td>512</td>
 * <td>2<sup>64</sup>-1</td>
 * <td>32</td>
 * <td>64</td>
 * <td>+,and,or,xor,shr,rotr</td>
 * <td>None yet</td>
 * </tr>
 * 
 * <tr align="center">
 * <td><b>SHA-512/384</b></td>
 * <td>512/384</td>
 * <td>512</td>
 * <td>1024</td>
 * <td>2<sup>128</sup>-1</td>
 * <td>64</td>
 * <td>80</td>
 * <td>+,and,or,xor,shr,rotr</td>
 * <td>None yet</td>
 * </tr>
 * 
 * <tr align="center">
 * <td><b>MD2</b></td>
 * <td>128</td>
 * <td>128</td>
 * <td>128</td>
 * <td>2<sup>64</sup>-1</td>
 * <td>32</td>
 * <td>18</td>
 * <td>not,and,or,xor</td>
 * <td>Vulnerable to a pre-image attack with time complexity equivalent to
 * 2<sup>104</sup> (2004)</td>
 * </tr>
 * 
 * <tr align="center">
 * <td><b>MD4/MD5</b></td>
 * <td>128/128</td>
 * <td>128</td>
 * <td>512/512</td>
 * <td>2<sup>64</sup>-1</td>
 * <td>32/32</td>
 * <td>3/4</td>
 * <td>not,and,or,xor</td>
 * <td>Collision found in 1996/collision found in 2004)</td>
 * </tr>
 * 
 * </tbody>
 * </table>
 * 
 * 
 */
public class HMac implements I_Mac {
	// Static constants
	/** Input padding constant: 0x36. */
	public final static byte IPAD = (byte) 0x36;
	/** Output padding constant: 0x5C. */
	public final static byte OPAD = (byte) 0x5C;

	// Instance variables
	/* Input message padded to the algorithm block length */
	private byte[] iPad;
	/* Output message padded to the algorithm block length */
	private byte[] oPad;
	/* Hashing algorithm block length */
	private int blockLength;
	/* Hashing algorithm output length */
	// private int hashLength;

	// Private state
	/* Digest algorithm */
	private MessageDigest hashAlg;

	/* Hashing algorithm block lengths */
	private static Map<String, Integer> blockLengths;
	// The state of this MAC
	private static final int INITIAL = 0;
	private static final int IN_PROGRESS = 1;
	private int state = INITIAL;

	/**
	 * Class constructor.
	 * 
	 * @param hash
	 *            the hash function.
	 */
	public HMac(MessageDigest hash) {
		this.hashAlg = hash;
	}

	@Override
	public void init(byte[] key) throws InvalidKeyException, DigestException {
		if (key == null) {
			throw new InvalidKeyException("KeyboardKey null reference!");
		}
		blockLength = getBlockLen();
		key_ipad_opad(key);
		this.state = INITIAL;
		hashAlg.reset();
		hashAlg.update(iPad, 0, blockLength);
	}

	@Override
	public void init(AlgorithmParameters p) throws InvalidKeyException,
			DigestException {
		try {
			init(((KeyAlgorithmParameter) p.get(KeyAlgorithmParameter.class))
					.getKey());
		} catch (IllegalArgumentException e) {
			throw new InvalidKeyException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.crypto.mac.Hash#getMacLength()
	 */
	@Override
	public int getOutputLength() {
		return this.hashAlg.getDigestLength();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.crypto.mac.Hash#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return "HMAC/" + hashAlg.getAlgorithm();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.crypto.mac.Hash#update(byte)
	 */
	@Override
	public void update(byte input) throws IllegalStateException {
		this.state = IN_PROGRESS;
		hashAlg.update(input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.crypto.mac.Hash#update(byte[])
	 */
	@Override
	public void update(byte[] input) throws IllegalStateException {
		this.state = IN_PROGRESS;
		hashAlg.update(input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.crypto.mac.Hash#update(byte[], int, int)
	 */
	@Override
	public void update(byte[] input, int offset, int len)
			throws IllegalStateException {
		this.state = IN_PROGRESS;
		hashAlg.update(input, offset, len);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.crypto.mac.Hash#doFinal()
	 */
	@Override
	public byte[] doFinal() throws DigestException {
		if (state == INITIAL)
			throw new IllegalStateException("The HMAC is in initial state ");
		byte[] tmp = new byte[blockLength];
		// hashAlg.digest(tmp, 0, blockLength);

		hashAlg.digest(tmp, 0, blockLength);
		hashAlg.update(oPad);
		hashAlg.update(tmp);
		byte[] mac = hashAlg.digest();
		reset();
		return mac;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.crypto.mac.Hash#doFinal(byte[])
	 */
	@Override
	public byte[] doFinal(byte[] input) throws DigestException {
		update(input);
		return doFinal();
	}

	@Override
	public int doFinal(byte[] tmp, int i, int blockLength) {
		reset();
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.crypto.mac.Hash#reset()
	 */
	@Override
	public void reset() {
		hashAlg.reset();
		hashAlg.update(iPad, 0, blockLength);
		this.state = INITIAL;
	}

	/* Returns the key i-pad exclusive-oring and the key o-pad exclusive-oring */
	private void key_ipad_opad(byte[] k) {
		/* MAC key */
		iPad = new byte[blockLength];
		oPad = new byte[blockLength];
		for (int i = 0; i < blockLength; i++) {
			iPad[i] = (i + 0 < k.length) ? (byte) (k[i] ^ IPAD)
					: (byte) (0x00 ^ IPAD);
			oPad[i] = (i + 0 < k.length) ? (byte) (k[i] ^ OPAD)
					: (byte) (0x00 ^ OPAD);
		}
	}

	/* Get the block length */
	private int getBlockLen() throws DigestException {
		// Length is expressed in bytes
		blockLengths = new HashMap<String, Integer>();
		// MD
		blockLengths.put("MD2", Integer.valueOf(16));
		blockLengths.put("MD4", Integer.valueOf(64));
		blockLengths.put("MD5", Integer.valueOf(64));
		// SHA-d
		blockLengths.put("SHA-1", Integer.valueOf(64));
		blockLengths.put("SHA-224", Integer.valueOf(64));
		blockLengths.put("SHA-256", Integer.valueOf(64));
		blockLengths.put("SHA-384", Integer.valueOf(128));
		blockLengths.put("SHA-512", Integer.valueOf(128));
		int i = 0;
		Integer b = blockLengths.get(hashAlg.getAlgorithm().trim());
		b = (b == null) ? Integer.valueOf(0) : b;
		i = b.intValue();
		if (i == 0)
			throw new DigestException("Undetermined digest block length!");
		return i;
	}

}