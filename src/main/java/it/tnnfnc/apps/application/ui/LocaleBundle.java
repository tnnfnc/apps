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
package it.tnnfnc.apps.application.ui;

import java.util.ListResourceBundle;

/**
 * Localization for description.
 * 
 * @author Franco Toninato
 */
public class LocaleBundle extends ListResourceBundle {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListResourceBundle#getContents()
	 */
	@Override
	public Object[][] getContents() {
		return contents;
	}

	/**
	 * 
	 */
	static final Object[][] contents = {
			// LOCALIZE THIS
			{ "search history", "Search history" },//
			{ "search results", "Search results" },//
			{ "match:", "Match:" },//
			{ "find", "Find" }, //
			{ "stop", "Stop" }, //
			{ "OK", "OK" }, //
			{ "interrupted", "Interrupted" }, //
			{ "Regular expression", "Regular expression" }, //
			{ "Ignore case", "Ignore case" }, //
			{ "Multiline", "Multiline" },//
			{ "Allow dot match end line", "Allow dot match end line" },//
			{ "Search string is not valid!", "Search string is not valid!" },//
			{ "syntax error", "Pattern syntax error: " },//
			{ "Document", "Document" },//
			{ "filename", "NewDocument" },//
			{ "Default", "Default" },//
			{ "Password check", "Password check" },//

			// Resource Selection Panel
			{ "Local Backup", "Local Backup" }, //
			{ "New", "New" }, //
			{ "Open", "Open" }, //
			{ "Save", "Save" }, //
			{ "SaveAs", "Save As" }, //
			{ "Close", "Close" }, //
			{ "Exit", "Exit" }, //
			{ "Browse", "Browse" }, //
			{ "file", "file" }, //
			{ "Encrypted file", "Encrypted file" }, //
			{ "Do you want to save?", "Do you want to save?" }, //
			{ "Do you want to overwrite?", "Do you want to overwrite?" }, //
			{ "backup local path", "Browse a Backup Local Path" }, //

			{ "Resource manager exception", "Resource Manager Exception" }, //
			{ "Unexpected Exception", "Unexpected Exception" }, //
			{ "Unknown", "Unknown" }, //
			{ "No Resource Opened", "No resource is currently opened." }, //
			{ "Recent resources", "Recent resources" }, //
			{ "Unable to open", "Unable to open: " }, //
			{ "Unable to save", "Unable to save: " }, //
			{ "Unable create a new document", "Unable create a new document: " }, //
			{ "", "" }, //
			// EditTabPanel
			{ "Edit tab", "Edit tab" }, //
			{ "Remove", "Remove" }, //
			{ "Add new tab", "Add new tab" }, //
			{ "New tab", "New tab" }, //
			{ "Description", "Description" }, //
			{ "Change", "Change" }, //
			{ "Name", "Name" }, //
			{ "Format", "Format" }, //
			// Keyboard
			// Keyboard
			{ "Keyboard", "Keyboard" }, //
			{ "Zoom in", "Zoom in" }, //
			{ "Zoom out", "Zoom out" }, //
			{ "Locale", "Locale" }, //
			{ "Cancel", "Cancel" }, //
			{ "Show text", "Show text" }, //
			// PopMessame
			{ "Application exception", "Application exception: " }, //
			{ "Application information", "Application information: " }, //
			{ "Application warning", "Application warning: " }, //
			// Logs panel
			{ "Clear", "Clear all" }, //
			{ "Purge old logs", "Purge old logs" }, //
			{ "Security Exception", "Security Exception" }, //
			{ "Invalid password", "Invalid password" }, //
			{ "InputOutput Exception", "InputOutput Exception" }, //
			{ "Application Exception", "Application Exception" }, //
			{ "Create new", "Create new resource?" }, //
			{ "Unexpected file format", "Unexpected file format" }, //
			/*
			 * Tooltip
			 */
			{ "tt-new",//
					"Create a new resource for the program" }, //
			{ "tt-open",//
					"Open an existing resource to get saved data" }, //
			{ "tt-save",//
					"Save the data to the current opened resource" }, //
			{ "tt-saveas",//
					"Save the data to a new resource" }, //
			{ "tt-close",//
					"Close the current opened resource" }, //
			{ "tt-exit",//
					"Exit the program: all resources will be closed" }, //
			{ "tt-resource type",//
					"Choose the type of resource where you want to save the data" }, //
			{ "tt-Keyboard",//
					"Enter text using a keyboard on the screen. This is safer than digit" }, //

	// END OF MATERIAL TO LOCALIZE
	};
}
