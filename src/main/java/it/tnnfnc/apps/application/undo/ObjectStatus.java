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

import java.text.DateFormat;
import java.util.Date;


/**
 * Generic object status.
 * 
 * @author Franco Toninato
 * 
 */
public class ObjectStatus<T> implements I_Status<T> {

	private T memento;
	private Object command;
	private long timestamp = new Date().getTime();

	/**
	 * Construct an empty object with null reference.
	 */
	public ObjectStatus() {
	}

	/**
	 * Construct an object from a status, and a command.
	 * 
	 * @param status
	 *            the memento status.
	 * @param command
	 *            the command.
	 */
	public ObjectStatus(T status, Object command) {
		setStatus(status, command);
	}

	/**
	 * Construct an object from a status, a command and a time stamp.
	 * 
	 * @param status
	 *            the object status.
	 * @param command
	 *            the command.
	 * @param timestamp
	 *            the time stamp when the status was changed.
	 */
	public ObjectStatus(T status, Object command, Date timestamp) {
		this.timestamp = timestamp.getTime();
		setStatus(status, command);
	}

	private void setStatus(T status, Object command) {
		this.memento = status;
		this.command = command;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.I_ObjectTrace#getStatus()
	 */
	@Override
	public T getStatus() {
		return this.memento;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.I_ObjectTrace#getCommand()
	 */
	@Override
	public Object getCommand() {
		return this.command;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.I_ObjectTrace#getTimeStamp()
	 */
	@Override
	public Date getTimeStamp() {
		Date d = new Date();
		d.setTime(timestamp);
		return d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return DateFormat.getDateInstance(DateFormat.SHORT).format(
				getTimeStamp())
				+ "-"
				+ DateFormat.getTimeInstance(DateFormat.MEDIUM).format(
						getTimeStamp())
				+ " "
				+ getCommand()
				+ " "
				+ getStatus().toString();
	}

}
