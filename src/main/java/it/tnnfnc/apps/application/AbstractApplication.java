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
 * PARTIES PROVIDE THE PROGRAM AS-IS WITHOUT WARRANTY OF ANY KIND, EITHER 
 * EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE 
 * QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE 
 * DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION.
 */
package it.tnnfnc.apps.application; //Package

import java.io.IOException;
import java.util.Iterator;
import java.util.ListResourceBundle;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import it.tnnfnc.apps.application.document.Document;
import it.tnnfnc.apps.application.document.Documents;
import it.tnnfnc.apps.application.document.I_IOBasicOperations;
import it.tnnfnc.apps.application.undo.ObjectStatus;
import it.tnnfnc.apps.application.undo.ObjectStatusModel;
import it.tnnfnc.apps.resource.I_Resource;

/**
 * @author Franco Toninato
 * 
 * 
 * @param <D>
 *            the document subclass implementing <code>{@link Document}</code>.
 */
public abstract class AbstractApplication<D extends Document> implements I_IOBasicOperations {
	/**
	 * The property key for storing the document version.
	 */
	public static final String VERSION_KEY = "ProgramVersion";
	/**
	 * 
	 */
	protected Properties properties;
	/**
	 * 
	 */
	protected Documents<D> documents = new Documents<D>();
	/**
	 * 
	 */
	protected D document;
	/**
	 * 
	 */
	protected ApplicationEventManager eventManager;
	/**
	 * 
	 */
	protected TaskScheduler taskScheduler = new TaskScheduler();
	/**
	 * 
	 */
	protected JFrame frame;
	/**
	 * 
	 */
	protected String bundleName = this.getClass().getName();
	/**
	 * 
	 */
	protected ObjectStatusModel<String> traceModel = new ObjectStatusModel<String>();

	/**
	 * 
	 */
	protected ListResourceBundle localization;
	/**
	 * 
	 */
	private String version = "0";

	/**
	 * Creates a new document with a name and opens it.
	 * 
	 * @param r
	 *            the document resource.
	 * @throws IOException
	 *             when the document cannot be created.
	 */
	public synchronized void create(I_Resource r) throws IOException {
		D doc = newDocument();
		if (r != null && doc.setResource(r)) {
			// System.out.println("change from create" +
			// this.getClass().getName() );
			doc.setChanged(true);
			documents.add(doc);
			setActiveDocument(doc);
			loadDefaultProperties();
		} else {
			throw new IOException("The resource was not set: " + (r == null ? null : r.getURL()));
		}
	}

	/**
	 * Open a new document.
	 * 
	 * @param r
	 *            the document resource.
	 * @throws IOException
	 *             when the document can't be opened.
	 */
	public synchronized void open(I_Resource r) throws IOException {
		if (r == null) {
			throw new IOException("The invalid resource: " + (r == null ? null : r.getURL()));
		}
		D doc = newDocument();
		if (doc.setResource(r)) {
			doc.open();
			doc.setChanged(false);
			documents.add(doc);
			setActiveDocument(doc);
			loadProperties();
		} else {
			throw new IOException("The resource was not set: " + r.getURL());
		}
	}

	/**
	 * Closes the active document.
	 * 
	 * 
	 * @throws IOException
	 *             when the document can't be closed.
	 */
	public synchronized void close(I_Resource r) throws IOException {
		if (document != null) {
			closeActiveDocument();
			document.close();
			documents.remove(document);
			document = null;
		}
	}

	/**
	 * Saves the active document.
	 * 
	 * @throws IOException
	 *             when the document can't be saved.
	 */
	public synchronized void save(I_Resource r) throws IOException {
		if (getActiveDocument() != null) {
			try {
				document.setResource(r);
				saveProperties();
				document.save();
				document.setChanged(false);
			} catch (Exception e) {
				throw new IOException(e.getMessage(), e.getCause());
			}
		}
	}

	/**
	 * Saves the active document as a new document.
	 * 
	 * @param r
	 *            the document resource.
	 * 
	 * @throws IOException
	 *             when the document can't be saved.
	 */
	public synchronized void saveAs(I_Resource r) throws IOException {
		if (getActiveDocument() != null) {
			document.setResource(r);
			saveProperties();
			System.out.println(this.getClass().getName() + " save document properties ok " + getProperties());
			document.save();
			System.out.println(this.getClass().getName() + " save document ok ");
			document.setChanged(false);
		}
	}

	/**
	 * Exit the application. It calls the {@link exitApplication} method.
	 * 
	 * 
	 * @throws IOException
	 *             when the application exits with errors.
	 */
	public void exit() throws IOException {
		closeAll();
		exitApplication();
	}

	/**
	 * Set the resource for the active document.
	 * 
	 * @return true if no error occurred.
	 */
	public boolean setResource(I_Resource r) {
		return document.setResource(r);
	}

	/**
	 * Get the resource of the active document.
	 * 
	 * @return the resource of the active document.
	 */
	public I_Resource getResource() {
		return document == null ? null : document.getResource();
	}

	/**
	 * Get the proposed name for default naming of the document.
	 * 
	 * @return the proposed document name.
	 */
	public abstract String getFileName();

	/**
	 * Get the application file extension.
	 * 
	 * @return the file extension.
	 */
	public abstract String getFileExtension();

	/**
	 * Saves all documents.
	 * 
	 * @throws IOException
	 *             when the document can't be saved.
	 */
	public void saveAll() throws IOException {
		StringBuffer exception = new StringBuffer();
		for (Iterator<D> iterator = documents.iterator(); iterator.hasNext();) {
			Document d = iterator.next();
			try {
				if (d != null) {
					d.save();
					d.setChanged(false);
				}
			} catch (IOException e) {
				exception.append(d.getDocumentName());
				exception.append(": ");
				exception.append(e.getLocalizedMessage());
				exception.append("/n");
				traceModel.setTrace(this, new ObjectStatus<String>(exception.toString(), "Save all"));
			}
		}
		if (exception.length() > 0)
			throw new IOException(exception.toString());
	}

	/**
	 * Closes all changed documents;
	 * 
	 * @throws IOException
	 *             when the document can't be closed.
	 */
	public void closeAll() throws IOException {
		StringBuffer exception = new StringBuffer();
		// Close first the active document
		close(getResource());
		// Close others
		for (Iterator<D> iterator = documents.iterator(); iterator.hasNext();) {
			Document d = iterator.next();
			try {
				if (d != null) {
					d.close();
					d = null;
				}
			} catch (IOException e) {
				exception.append(d.getDocumentName());
				exception.append(": ");
				exception.append(e.getLocalizedMessage());
				exception.append("/n");
				traceModel.setTrace(this, new ObjectStatus<String>(exception.toString(), "Close all"));
			}
		}
		documents.clear();
		if (exception.length() > 0)
			throw new IOException(exception.toString());
	}

	/**
	 * Get the file filter for selecting specific files.
	 * 
	 * @return the application valid file formats.
	 */
	public FileFilter getFileFilter() {
		return null;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set program current version.
	 * 
	 * @param version
	 *            the version to set
	 */
	protected void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Get program current version.
	 * 
	 * @param frame
	 *            the frame to set
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	/**
	 * Get the current localization. The localization base name is the class
	 * name with the suffix "Bundle".
	 * 
	 * @return the localization bundle.
	 */
	public ListResourceBundle getLocalization() {
		return localization;
	}

	/**
	 * Set the localization for application messages.
	 * 
	 * 
	 * @param localization
	 *            the localized message bundle.
	 */
	public void setLocalization(ListResourceBundle localization) {
		this.localization = localization;
	}

	/**
	 * Get the internationalization language. The localization base name is the
	 * class name with the suffix "Bundle".
	 * 
	 * @param l
	 *            the localization locale.
	 * @return the localization bundle.
	 */
	// public void setLocale(Locale l) {
	// try {
	// localization = (ListResourceBundle)
	// ResourceBundle.getBundle(this.bundleName + "Bundle", l);
	// } catch (Exception e) {
	// localization = new EmptyLocaleBundle(this.getClass().getName());
	// }
	// }

	/**
	 * Get the application properties.
	 * 
	 * @return the properties.
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Get the application timer for running timer threads.
	 * 
	 * @return the timer.
	 */
	public TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}

	/**
	 * Get the application frame.
	 * 
	 * @return the frame.
	 */
	public JFrame getFrame() {

		return frame;
	}

	/**
	 * Get the application event manager.
	 * 
	 * @return the event manager, if any.
	 */
	public ApplicationEventManager getEventManager() {
		return eventManager;
	}

	/**
	 * @param laf
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws UnsupportedLookAndFeelException
	 */
	public void changeLookAndFeel(String laf) {

		// boolean exists = false;
		try {

			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				// System.out.println("laf = " + info.getName());
				if (laf.equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					// MetalLookAndFeel.setCurrentTheme(new
					// DefaultMetalTheme());
					MetalLookAndFeel.setCurrentTheme(new OceanTheme());
					SwingUtilities.updateComponentTreeUI(frame);
					frame.pack();
					// exists = true;
					break;
				}
			}

		} catch (UnsupportedLookAndFeelException e) {

		} catch (ClassNotFoundException e) {

		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		}
		// if (!exists) {
		// try {
		// UIManager.setLookAndFeel(UIManager
		// .getSystemLookAndFeelClassName());
		// SwingUtilities.updateComponentTreeUI(frame);
		// frame.pack();
		// exists = true;
		// } catch (ClassNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (InstantiationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (UnsupportedLookAndFeelException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

	}

	/**
	 * Set the active document.
	 * 
	 * @param url
	 *            the document name.
	 */
	public synchronized void setActiveDocument(D d) {
		if (documents.contains(d)) {
			document = d;
			setActiveDocument();
		}
	}

	/**
	 * Gets the active document.
	 * 
	 * @return the current document.
	 */
	public D getActiveDocument() {
		return document;
	}

	/**
	 * Compare the version to the current program version.
	 * 
	 * @param version
	 *            the version.
	 * @return the value 0 if version = current program version or when they
	 *         cannot be matched; a value less than 0 if version &lt; program
	 *         version; and a value greater than 0 if version &gt; program
	 *         version
	 * 
	 */
	public int checkVersion(String version) {
		try {
			String v = version.replace(".", "").trim();
			String v_current = getVersion().replace(".", "").trim();

			int diff = Math.abs(v.length() - v_current.length());

			if (v.length() > v_current.length()) {
				int number = Integer.parseInt(v);
				int number_current = Integer.parseInt(v_current) * 10 ^ diff;
				return Integer.compare(number, number_current);
			} else if (v.length() == v_current.length()) {
				int number = Integer.parseInt(v);
				int number_current = Integer.parseInt(v_current);
				return Integer.compare(number, number_current);
			} else {
				int number = Integer.parseInt(v) * 10 ^ diff;
				int number_current = Integer.parseInt(v_current);
				return Integer.compare(number, number_current);
			}
		} catch (Exception err) {
			return 0;
		}

	}

	/**
	 * Load the default application properties at the creation of a new
	 * document.
	 * 
	 */
	protected abstract void loadDefaultProperties();

	/**
	 * Load the application properties.
	 * 
	 */
	protected abstract void loadProperties() throws IOException;

	/**
	 * Save the application properties.
	 * 
	 */
	protected abstract void saveProperties() throws IOException;

	/**
	 * Creates a document. This method is called when open or create a document.
	 * Subclasses must implement this method.
	 * 
	 * @param r
	 *            the document resource.
	 * @return the new document.
	 */
	protected abstract D newDocument();

	/**
	 * Close the active document. Subclasses must implement this method.
	 * 
	 */
	protected abstract void closeActiveDocument();

	/**
	 * Sets the active document. Subclasses must implement this method.
	 * 
	 */
	protected abstract void setActiveDocument();

	/**
	 * Exit the application. Subclasses must implement this method.
	 * 
	 */
	protected abstract void exitApplication() throws IllegalStateException;
}