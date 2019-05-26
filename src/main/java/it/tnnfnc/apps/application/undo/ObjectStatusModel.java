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
package it.tnnfnc.apps.application.undo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class traces the change of the internal status of changing objects.
 * 
 * @author franco toninato
 * 
 */
public class ObjectStatusModel<T> {

	/**
	 * The key is the traced object, the array list contains the history of
	 * changes.
	 */
	protected Map<Object, ArrayList<I_Status<T>>> tracedObjects //
	= new HashMap<Object, ArrayList<I_Status<T>>>();

	protected int size = 0;

	/**
	 * 
	 */
	public ObjectStatusModel() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * @param size
	 */
	public ObjectStatusModel(int size) {
		if (size > 0)
			this.size = size;
	}

	/**
	 * Set the old changed internal object status. It is not allow more than one
	 * status with the same time stamp. The trace is ordered from the first to
	 * the last time stamp.
	 * 
	 * @param tracedObjectID
	 *            the object changed.
	 * @param oldValue
	 *            the old internal object status.
	 */
	public void setTrace(Object tracedObjectID, I_Status<T> oldValue) {
		ArrayList<I_Status<T>> c = tracedObjects.get(tracedObjectID);
		if (c == null) {
			c = new ArrayList<I_Status<T>>();
			tracedObjects.put(tracedObjectID, c);
		}
		for (int i = 0; i < c.size(); i++) {
			if (oldValue.getTimeStamp().after(c.get(i).getTimeStamp())) {
				c.add(i, oldValue);
				return;
			} else if (c.get(i).getTimeStamp().equals(oldValue.getTimeStamp())) {
				return;
			}
		}
		c.add(oldValue);
		trimToSize(tracedObjectID, size);
	}

	/**
	 * Set the old changed internal object status. It is not allow more than one
	 * status with the same time stamp. The trace is ordered from the first to
	 * the last time stamp.
	 * 
	 * @param tracedObject
	 *            the object changed.
	 * @param newObject
	 *            the new object.
	 * @param oldObject
	 *            the old object.
	 * @param log
	 *            the string log.
	 */
	public void setTrace(Object tracedObject, T newObject, T oldObject,
			String log) {
		if (oldObject == null) {
			// no trace
		} else if (newObject != null && !newObject.equals(oldObject)) {
			log = log == null ? "" : log;
			I_Status<T> s = new ObjectStatus<T>(oldObject, log);
			ArrayList<I_Status<T>> c = tracedObjects.get(tracedObject);
			if (c == null) {
				c = new ArrayList<I_Status<T>>();
				tracedObjects.put(tracedObject, c);
			}
			for (int i = 0; i < c.size(); i++) {
				if (s.getTimeStamp().after(c.get(i).getTimeStamp())) {
					c.add(i, s);
					return;
				} else if (c.get(i).getTimeStamp().equals(s.getTimeStamp())) {
					return;
				}
			}
			c.add(s);
			trimToSize(tracedObject, size);
		}
	}

	/**
	 * Get the history from the last to the former.
	 * 
	 * @param tracedObject
	 *            the object whose internal status is traced.
	 * @return the array of previous values.
	 */
	public I_Status<?>[] getHistory(Object tracedObject) {
		ArrayList<I_Status<T>> c = tracedObjects.get(tracedObject);

		I_Status<?> o[] = new ObjectStatus<?>[0];

		if (c != null) {
			return tracedObjects.get(tracedObject).toArray(o);
		}
		return o;
	}

	/**
	 * Trim the whole trace data up to the size. Eldest traced items are dropped
	 * out.
	 * 
	 * @param tracedObject
	 *            the object whose internal status is traced.
	 * @param size
	 *            the traced number of entries for each traced object.
	 */
	public void trimToSize(Object tracedObject, int size) {
		ArrayList<?> array;
		if ((array = tracedObjects.get(tracedObject)) == null)
			return;
		while (array.size() > size) {
			array.remove(size);
		}
		array.trimToSize();
	}

	/**
	 * Trim the whole trace data up to the size and set the new trace size.
	 * 
	 * @param size
	 *            the traced number of entries for each traced object.
	 */
	public void trimToSize(int size) {
		for (Entry<Object, ArrayList<I_Status<T>>> entry : tracedObjects
				.entrySet()) {
			if (entry.getValue().size() > size) {
				trimToSize(entry.getKey(), size);
			}
		}
	}

	/**
	 * Remove all entries from the model.
	 */
	public void purge() {
		for (Entry<Object, ArrayList<I_Status<T>>> entry : tracedObjects
				.entrySet()) {
			entry.getValue().clear();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return tracedObjects.toString();
	}

}
