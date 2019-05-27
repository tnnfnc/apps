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

import java.io.IOException;
import java.text.Collator;

import javax.swing.event.EventListenerList;

import it.tnnfnc.apps.application.undo.ObjectStatusModel;
import it.tnnfnc.apps.resource.I_Resource;

/**
 * A document extends this class. The lock on document does not allow IO
 * operations. The document url is the same of its resource.
 * 
 * @author Franco Toninato
 * 
 */
public abstract class Document implements Comparable<Document> {

	// Has been changed
	private boolean changed = false;
	// Document is currently locked - no changes allowed
	private boolean locked = false;
	// Document name
	private String documentName = null;
	// Document version
	private String documentVersion = null;
	// Document current
	private boolean isCurrentDocument = false;
	/**
	 * Listeners registered with this Document.
	 */
	protected EventListenerList eventListenerList = new EventListenerList();
	/**
	 * Resource linked to the document.
	 */
	protected I_Resource resource;

	/**
	 * The trace model.
	 */
	protected ObjectStatusModel<?> traceModel;

	/**
	 * Returns the document URL. The document url is the same of its resource URL.
	 * 
	 * @return the document URL. The url of a saved document is a valid URL.
	 */
	public String getDocumentName() {

		return this.documentName;
	}

	/**
	 * Sets the document URL. The document url is the same of its resource, if no
	 * resources are linked to the document an internal url is provided.
	 * 
	 * @param name the new document URL.
	 */
	public void setDocumentName(String name) {
		this.documentName = name;
	}

	/**
	 * Get the document version.
	 * 
	 * @return the document version
	 */
	public String getVersion() {
		return documentVersion;
	}

	/**
	 * Set the document version.
	 * 
	 * @param the document version to set
	 */
	public void setVersion(String docVer) {
		this.documentVersion = docVer;
	}

	/**
	 * Returns true if something changed.
	 * 
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * Set true when a change occurred.
	 * 
	 * @param changed true when a change occurred.
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
		getResource().setChanged(changed);
		fireDocumentChange(new DocumentChangeEvent(this));
	}

	/**
	 * Returns the resource of this document.
	 * 
	 * @return the resource of this document.
	 */
	public I_Resource getResource() {
		return this.resource;
	}

	/**
	 * Returns the trace model of this document.
	 * 
	 * @return the trace model of this document.
	 */
	public ObjectStatusModel<?> getTraceModel() {
		return this.traceModel;
	}

	/**
	 * Sets the resource for this document and lock the resource.
	 * 
	 * @param r the resource for this document.
	 */
	public boolean setResource(I_Resource r) {
		if (isLocked() || r == null) {
			return false;
		}
		setLocked(true);
		this.resource = r;
		// if (docName == null) {
		documentName = resource.getURL() == null ? null : resource.getURL().toString().replaceAll("%20", " ");
		// }
		// r.setLock(true);
		setLocked(false);
		return true;
	}

	/**
	 * Returns true when this document was locked.
	 * 
	 * @return the locked.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Sets the lock status of this document.
	 * 
	 * @param locked the lock status.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * Reverts the document to its original. It does nothing.
	 */
	public void revert() {

	}

	/**
	 * Notify listeners that the document is changed.
	 * 
	 * @param event the change event.
	 */
	protected void fireDocumentChange(DocumentChangeEvent event) {
		// Guaranteed to return a non-null array
		Object[] listeners = eventListenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		if (event == null)
			return;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == I_DocumentChangeListener.class) {
				((I_DocumentChangeListener) listeners[i + 1]).performChange(event);
			}
		}
	}

	/**
	 * Add a document listener.
	 * 
	 * @param listener the data change listener.
	 */
	public void addDocumentChangeListener(I_DocumentChangeListener listener) {
		eventListenerList.add(I_DocumentChangeListener.class, listener);
	}

	/**
	 * Remove a document listener.
	 * 
	 * @param listener the data change listener.
	 */
	public void removeDocumentChangeListener(I_DocumentChangeListener listener) {
		eventListenerList.remove(I_DocumentChangeListener.class, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Document arg) {
		Collator collator = Collator.getInstance();
		collator.setDecomposition(Collator.FULL_DECOMPOSITION);
		return collator.compare(this.getDocumentName(), arg.getDocumentName());
	}

	/**
	 * Copy this document.
	 * 
	 * @return a deep copy of this document.
	 * @throws CloneNotSupportedException when the copying process returns an error.
	 */
	public abstract Document copy() throws CloneNotSupportedException;

	/**
	 * Open the document.
	 * 
	 * @throws IOException when the document is locked or the process failed on the
	 *                     IO operation.
	 */
	public abstract void open() throws IOException;

	/**
	 * Save this document to the current output resource.
	 * 
	 * @throws IOException when the document is locked or the process failed on the
	 *                     IO operation.
	 */
	public abstract void save() throws IOException;

	/**
	 * Close the document. It does nothing. Subclasses must implement it.
	 * 
	 * @throws IOException when the document is locked or the process failed on the
	 *                     IO operation.
	 */
	public abstract void close() throws IOException;

	/**
	 * @return the isCurrentDocument
	 */
	public boolean isCurrentDocument() {
		return isCurrentDocument;
	}

	/**
	 * @param isCurrentDocument the isCurrentDocument to set
	 */
	public void setCurrentDocument(boolean isCurrentDocument) {
		this.isCurrentDocument = isCurrentDocument;
	}

}