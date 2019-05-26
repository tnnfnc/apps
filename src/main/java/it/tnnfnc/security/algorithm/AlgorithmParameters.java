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
package it.tnnfnc.security.algorithm;


/**
 * This class is responsible of passing the parameters needed by ciphers. Any
 * block cipher used in a ciclic processing mode needs specific parameters to
 * perform the encryption and decryption task. <br />
 * The <code>CipherParameter</code> subclass carrying the parameters is obtained
 * through calling the <code>spy</code> method. The resulting object is then
 * available as a specific cipher parameter.
 * 
 * @author Franco Toninato
 * @since 1.2
 */
public class AlgorithmParameters {

	/**
	 * Algorithm internal parameters.
	 */
	private I_AlgorithmParameter[] parameters;

	public AlgorithmParameters() {
		parameters = new I_AlgorithmParameter[0];
	}

	/**
	 * Sets a new parameter. If the parameter exists it is replaced by the new
	 * one.
	 * 
	 * @param parameter
	 *            the new parameter.
	 * @return this object.
	 */
	public AlgorithmParameters setParameter(I_AlgorithmParameter parameter) {
		int n = search(parameter);
		if (n > -1)
			parameters[n] = parameter;
		else {
			I_AlgorithmParameter[] p = new I_AlgorithmParameter[parameters.length + 1];
			System.arraycopy(parameters, 0, p, 0, parameters.length);
			p[parameters.length] = parameter;
			parameters = p;
		}
		return this;
	}

	/**
	 * Return the instance of the subclass implementing
	 * <code>CipherParameter</code>.
	 * 
	 * @param aType
	 *            The type of the parameter to be returned.
	 * @return The parameter of the specified type implementing the
	 *         <code>CipherParameter</code> interface.
	 * @exception IllegalArgumentException
	 *                when the specified type is not present into this
	 *                <code>CipherParameters</code> instance.
	 */

	public I_AlgorithmParameter get(Class<? extends I_AlgorithmParameter> aType)
			throws IllegalArgumentException {
		for (int i = 0; i < parameters.length; i++) {
			try {
				if (parameters[i].getClass().equals(aType)) {
					return parameters[i];
				}
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("Class cast exception ");
			}
		}
		throw new IllegalArgumentException(aType.getName()
				+ ": Invalid type parameter");
	}

	/**
	 * Return the content of the <code>CipherParameter</code> subclasses
	 * currently stored in this object.
	 * 
	 * @return the content of the <code>CipherParameters</code> subclasses
	 *         available as cipher parameters.
	 */
	public String[] getParamsName() {
		String[] parsList = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] != null) // Exit
				parsList[i] = parameters[i].getClass().getName();
			else
				parsList[i] = null;
		}
		return parsList;
	}

	private int search(I_AlgorithmParameter cipherParameter) {
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].getClass().equals(cipherParameter.getClass())) {
				return i;
			}
		}
		return -1;
	}

}
