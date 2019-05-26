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

import javax.swing.event.EventListenerList;


/**
 * This class manages a history for undoing features. The listeners are notified
 * with the undo actions.
 * 
 * @author Franco Toninato
 * 
 */
public class UndoModel implements I_UndoModel {

	protected int commandCount = 0;
	/**
	 * Undo index management: array(i) is the model index corresponding to the i
	 * history event.
	 */
	protected I_Status<?> history[] = new I_Status[0];
	private static final int MAX = 20;
	private int max = MAX;
	/**
	 * Listeners registered with this indexed model.
	 */
	protected EventListenerList listenerList = new EventListenerList();

	private I_Undoable undoListener;

	/**
	 * Add the undo listener.
	 * 
	 * @param l
	 *            the listener.
	 */
	@Override
	public void addListener(I_Undoable l) {
		undoListener = l;
	}

	/**
	 * Remove the undo listener.
	 */
	@Override
	public void removeListener() {
		undoListener = null;
	}

	/**
	 * Clear history.
	 */
	@Override
	public void clearHistory() {
		// Clear history
		history = new I_Status[0];
		commandCount = 0;
	}

	/**
	 * Get the canceled commands count.
	 * 
	 * @return the canceled commands count.
	 */
	@Override
	public int redoCount() {
		// return history.length - commandCount - 1;
		int n = history.length - commandCount - 1;
		n = (n >= 0) ? n : 0;
		return n;
	}

	/**
	 * Get the executed commands count.
	 * 
	 * @return the executed commands count.
	 */
	@Override
	public int commandCount() {
		return commandCount;
	}

	/**
	 * Cast an history index array element.
	 * 
	 * @param time
	 *            the history index.
	 * @return the casted index object.
	 */
	@Override
	public I_Status<?> getStatus(int time) {
		if (time < history.length)
			return history[time];
		return null;
	}

	/**
	 * Get the array of executed commands from the last.
	 * 
	 * @return the array of executed commands, last is first.
	 */
	@Override
	public I_Status<?>[] getCommandList() {
		I_Status<?> commands[] = new I_Status[commandCount()];
		for (int i = 0; i < commands.length; i++) {
			commands[i] = history[commandCount() - 1 - i];
		}
		return commands;
	}

	/**
	 * Get the array of the canceled commands from the last.
	 * 
	 * @return the canceled commands from the last.
	 */
	@Override
	public I_Status<?>[] getRedoList() {
		I_Status<?> commands[] = new I_Status[redoCount()];
		for (int i = 0; i < commands.length; i++) {
			commands[i] = history[commandCount() + i];
		}
		return commands;
	}

	/**
	 * Do the last canceled command.
	 */
	@Override
	public void redo() {
		if (undoListener != null) {
			// System.out.println("UndoModel.redo " + commandCount
			// + " history.length = " + history.length);
			if ((commandCount + 1) < max) {
				// undoListener.redo(getHistory(++commandCount));
				undoListener.setStatus(getStatus(++commandCount));
			}
		}
	}

	/**
	 * Undo the last command.
	 */
	@Override
	public void undo() {
		if (undoListener != null) {
			if (redoCount() == 0) {
				// append the actual status
				I_Status<?> as = undoListener.getStatus();
				// as.setCommand("");
				addStatus(as);
				// System.out.println("UndoModel.undo add actual" + commandCount
				// + " history.length = " + history.length);
				--commandCount;
			}
			if (commandCount > 0)
				undoListener.setStatus(getStatus(--commandCount));
		}
	}

	/**
	 * Add an event to the history. The repeat command content is depleted.
	 * 
	 * @param event
	 *            the event to be added.
	 * @return the time index.
	 * 
	 */
	@Override
	public int addStatus(I_Status<?> event) {
		int j = 0;
		int k = 0;
		if (commandCount < max) {
			k = 1; // Buffer has more place
		} else {
			j = 1;
		}
		I_Status<?>[] newHistory = new I_Status[commandCount + k];
		System.arraycopy(history, j, newHistory, 0, commandCount - j);
		newHistory[commandCount - j] = event;
		history = newHistory;
		commandCount += k;
		return history.length - 1;
	}

	/**
	 * Replace a past event with a new one.
	 * 
	 * @param event
	 *            the event to be added.
	 * @param time
	 *            the history index.
	 * @return the previous event.
	 */
	@Override
	public I_Status<?> replaceStatus(I_Status<?> event, int time) {
		I_Status<?> old = getStatus(time);
		history[time] = event;
		return old;
	}

	/**
	 * Set the status buffer.
	 * 
	 * @param len
	 *            the max buffer capacity.
	 */
	@Override
	public void setSize(int len) {
		if (getSize() > len) {
			// Reduce the history eldest entries are deleted
			I_Status<?>[] newHistory = new I_Status[len];
			System.arraycopy(history, getSize() - len, newHistory, 0, len);
			history = newHistory;
			commandCount = commandCount - getSize() + len;
		}
		this.max = len;
	}

	@Override
	public int getSize() {
		return this.max;
	}

	/**
	 * Get the undoes array.
	 * 
	 * @return the undo entries.
	 */
	@Override
	public I_Status<?>[] toArray() {
		return history;
	}

	@Override
	public String toStringParameter() {
		StringBuffer p = new StringBuffer();

		for (int i = 0; i < history.length; i++) {
			p.append("Item " + i + " " + history[i].getTimeStamp()
					+ "Command: " + history[i].getCommand() + "\n");
		}
		return new String(p);
	}
}
