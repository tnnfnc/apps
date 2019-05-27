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
package it.tnnfnc.apps.application.document;

import java.util.ArrayList;

/**
 * @author Franco Toninato
 * 
 * 
 */
public class Documents<D extends Document> extends ArrayList<D> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Gets the last opened document.
	 * 
	 * @return the last opened document.
	 */
	public D getLast() {
		if (!this.isEmpty())
			return this.get(this.size() - 1);
		return null;
	}

	/**
	 * Gets a document.
	 * 
	 * @param name the document name.
	 * @return the document.
	 */
	public D get(String name) {
		for (D d : this) {
			if (d.getDocumentName().equalsIgnoreCase(name)) {
				return d;
			}
		}
		return null;
	}

	/**
	 * Returns a deep copy of a document.
	 * 
	 * @param index the index.
	 * @return the document.
	 * @throws CloneNotSupportedException when the copying process returns an error.
	 */
	@SuppressWarnings("unchecked")
	public D copy(int index) throws CloneNotSupportedException {
		if (this.size() > index) {
			return (D) this.get(index).copy();
		} else {
			return null;
		}
	}

	/**
	 * Removes all documents from the content.
	 */
	public void removeAll() {
		this.clear();
		this.trimToSize();
	}

	/**
	 * Locks the documents from being accessed.
	 * 
	 * @return true when locked.
	 */
	public boolean lock() {
		for (D doc : this) {
			doc.setLocked(true);
		}
		return true;
	}

	/**
	 * Unlocks the documents.
	 * 
	 * @return true when unlocked.
	 */
	public boolean unlock() {
		for (D doc : this) {
			doc.setLocked(false);
		}
		return true;
	}
}
