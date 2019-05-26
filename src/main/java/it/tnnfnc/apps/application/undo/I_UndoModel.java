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


public interface I_UndoModel {

	/**
	 * Add the undo listener.
	 * 
	 * @param l
	 *            the listener.
	 */
	public void addListener(I_Undoable l);

	/**
	 * Remove the undo listener.
	 */
	public void removeListener();

	/**
	 * Clear history.
	 */
	public void clearHistory();

	/**
	 * Get the canceled commands count.
	 * 
	 * @return the canceled commands count.
	 */
	public int redoCount();

	/**
	 * Get the executed commands count.
	 * 
	 * @return the executed commands count.
	 */
	public int commandCount();

	/**
	 * Get the array of executed commands from the last.
	 * 
	 * @return the array of executed commands, last is first.
	 */
	public I_Status<?>[] getCommandList();

	/**
	 * Get the array of the canceled commands from the last.
	 * 
	 * @return the canceled commands from the last.
	 */
	public I_Status<?>[] getRedoList();

	/**
	 * Do the last canceled command.
	 */
	public void redo();

	/**
	 * Undo the last command.
	 */
	public void undo();

	/**
	 * Cast an history index array element.
	 * 
	 * @param time
	 *            the history index.
	 * @return the casted index object.
	 */
	public I_Status<?> getStatus(int time);

	/**
	 * Add an event to the history. The repeat command content is depleted.
	 * 
	 * @param event
	 *            the event to be added.
	 * @return the time index.
	 * 
	 */
	public int addStatus(I_Status<?> event);

	/**
	 * Replace a past event with a new one.
	 * 
	 * @param event
	 *            the event to be added.
	 * @param time
	 *            the history index.
	 * @return the previous event.
	 */
	public I_Status<?> replaceStatus(I_Status<?> event, int time);

	/**
	 * Set the history status capacity.
	 * 
	 * @param len
	 *            the history status capacity.
	 */
	public void setSize(int len);

	/**
	 * Get the history status length.
	 * 
	 * @return the history status length.
	 */
	public int getSize();

	/**
	 * Get the undoes array.
	 * 
	 * @return the undo entries.
	 */
	public I_Status<?>[] toArray();

	public String toStringParameter();

}