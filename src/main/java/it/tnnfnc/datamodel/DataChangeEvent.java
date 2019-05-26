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
package it.tnnfnc.datamodel;

import java.util.EventObject;

/**
 * This class propagates a generic event from a data model with index to the MVC
 * model. The MVC model must be a listener of this event and than can convert
 * this generic event to the specific MVC event to notify registered views that
 * data has changed.
 * 
 * @author Franco Toninato
 * 
 */
public class DataChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This enumeration stands for operations event type on content of data.
	 * 
	 * @author Franco Toninato
	 * 
	 */
	public enum EventType {
		// FILTER,
		// SORT,
		DELETE, // 
		UPDATE, //
		INSERT, //
		REINDEX,// Sort and filter
	}

	private int selectionStart = -1;
	private int selectionEnd = -1;
	private int column = -1;
	private EventType event = null;

	/**
	 * Event constructor.
	 * 
	 * @param source
	 *            event originator - the index model.
	 * @param eventType
	 *            the specific event type.
	 * @param startFrom
	 *            the data content starting index affected by changes.
	 * @param endTo
	 *            the data content ending index affected by changes.
	 */
	public DataChangeEvent(Object source, EventType eventType, int startFrom,
			int endTo) {
		super(source);
		this.selectionStart = startFrom;
		this.selectionEnd = endTo;
		this.event = eventType;
	}

	/**
	 * Event constructor.
	 * 
	 * @param source
	 *            event originator - the index model.
	 * @param eventType
	 *            the specific event type.
	 * @param startFrom
	 *            the data content starting block index affected by changes.
	 * @param endTo
	 *            the data content ending block index affected by changes.
	 * @param column
	 *            the data content inner index affected by changes.
	 */

	public DataChangeEvent(Object source, EventType eventType, int startFrom,
			int endTo, int column) {
		super(source);
		this.selectionStart = startFrom;
		this.selectionEnd = endTo;
		this.column = column;
		this.event = eventType;
	}

	/**
	 * Get the data content starting index affected by changes.
	 * 
	 * @return the data content starting index affected by changes.
	 */
	public int getFrom() {
		return selectionStart;
	}

	/**
	 * Get the data content ending index affected by changes.
	 * 
	 * @return the data content ending index affected by changes.
	 */
	public int getTo() {
		return selectionEnd;
	}

	/**
	 * Get the data content inner index affected by changes.
	 * 
	 * @return the data content inner index affected by changes.
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Get the specific event type.
	 * 
	 * @return the specific event type.
	 */
	public EventType getEventType() {
		return event;
	}

}
