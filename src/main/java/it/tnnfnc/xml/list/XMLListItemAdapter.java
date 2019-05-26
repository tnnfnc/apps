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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Provides a wrapper class for serialize/deserialize the wrapped object in the
 * writing/reading process to/from a XML document. Adapters must subclass it.
 * @see XMLListReader
 * @see XMLListWriter
 * @author Franco Toninato
 * 
 */
public abstract class XMLListItemAdapter {

	/**
	 * The adapted object.
	 */
	protected Object delegate;
	private String[] attributes;
	/**
	 * The setter method starts with "set".
	 */
	public static final String START_WITH_SET = "set";
	/**
	 * The getter method starts with "get".
	 */
	public static final String START_WITH_GET = "get";
	/**
	 * The getter method for boolean attributes starts with "is".
	 */
	public static final String START_WITH_IS = "is";

	/**
	 * Creates a new instance of the wrapped class.
	 */
	public abstract void newInstance();

	/**
	 * Returns the instance of the adapted object.
	 * 
	 * @return the instance of the adapted object
	 */
	public Object getInstance() {
		return this.delegate;
	}

	/**
	 * Creates a new instance of the wrapped class from an existing object.
	 * 
	 * @param o
	 *            the object instance.
	 */
	public void newInstance(Object o) {
		this.delegate = o;
	}

	/**
	 * Gets the adapted object field value. Builds the XML dump from the adapted
	 * object.
	 * 
	 * @param attribute
	 *            the object attribute name.
	 * @return the value returned by the wrapped class getter.
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public Object getField(String attribute) throws IllegalArgumentException,
			IllegalAccessException {
		Object value = null;
		try {
			Method method = delegate.getClass().getDeclaredMethod(
					getGetterMethod(attribute));
			value = method.invoke(delegate, (Object[]) null);
		} catch (SecurityException e) {
			throw new IllegalAccessException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalAccessException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new IllegalAccessException(e.getMessage());
		}
		return value;
	}

	/**
	 * Sets the adapted object field value. Builds the adapted object starting
	 * from XML.
	 * 
	 * @param setterName
	 *            the setter name. Overloading is not managed.
	 * @param value
	 *            the value passed to the setter method.
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void setField(String attribute, Object value)
			throws IllegalArgumentException, IllegalAccessException {
		try {
			Method method = delegate.getClass().getDeclaredMethod(
					getSetterMethod(attribute));
			method.invoke(delegate, value);
		} catch (SecurityException e) {
			throw new IllegalAccessException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalAccessException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new IllegalAccessException(e.getMessage());
		}

	}

	/**
	 * Returns the named attributes of the adapted object. Builds the XML dump
	 * from the adapted object.
	 * 
	 * @return the getters content.
	 */
	public String[] getFields() {
		if (attributes == null) {
			// getSomething or isTrueSomething is checked as getters...
			Method[] methods = delegate.getClass().getMethods();
			String[] g = new String[methods.length];
			int i = 0;
			for (int j = 0; j < methods.length; j++) {
				if (methods[j].getName().startsWith(START_WITH_GET)) {
					g[i] = methods[j].getName().substring(
							START_WITH_GET.length());
					i++;
				} else if (methods[j].getName().startsWith(START_WITH_IS)) {
					g[i] = methods[j].getName().substring(
							START_WITH_IS.length());
					i++;
				}
			}
			attributes = new String[i];
			System.arraycopy(g, 0, attributes, 0, i);
		}
		return attributes;
	}

	/**
	 * Returns the getter method name from its accessed field name.
	 * 
	 * @param field
	 *            the attribute accessed by the getter.
	 * @return the getter method.
	 */
	protected String getGetterMethod(String field) {
		Method[] methods = delegate.getClass().getMethods();
		for (int j = 0; j < methods.length; j++) {
			if (methods[j].getName().endsWith(field)
					&& !methods[j].getName().startsWith(START_WITH_SET)) {
				return methods[j].getName();
			}
		}
		return null;
	}

	/**
	 * Returns the setter method name from its accessed field name.
	 * 
	 * @param field
	 *            the attribute accessed by the setter.
	 * @return the setter method.
	 */
	protected String getSetterMethod(String field) {
		Method[] methods = delegate.getClass().getMethods();
		for (int j = 0; j < methods.length; j++) {
			if (methods[j].getName().endsWith(field)
					&& methods[j].getName().startsWith(START_WITH_SET)) {
				return methods[j].getName();
			}
		}
		return null;
	}
}
