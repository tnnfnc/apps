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
package it.tnnfnc.table;

import java.util.ListResourceBundle;

/**
 * Localization for description.
 * 
 * @author Franco Toninato
 */
public class TableLocalBundle extends ListResourceBundle {

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
			{ "Add at", "Add row" }, //
			{ "Move row up", "Move up" },// 
			{ "Move row down", "Move down" },// 
			{ "Insert row", "Insert row" }, //
			{ "Remove row", "Remove" },//
			{ "Hide column", "Hide column" },//
			{ "Show column", "Show column" },//
			{ "Show all", "Show all columns" },//
			{ "popupMessage", "The selected row does not exist." },//
			{ "popupTitle", "Message" },//
			{ "format", "Format" },//
			{ "small", "Small on/off" },//
			{ "strong", "Strong on/off" },//
			{ "mark", "Mark on/off" },//
			{ "plain", "Plain" },//
			{ "ok", "Ok" },//
			{ "warn", "Warning on/off" },//
			{ "error", "Error on/off" },//
			{ "reset", "Reset on/off" },//
			{ "Broken link", "Broken link" },//
			{ "Browsing exception", "Browsing exception" },//
			// Table Column Chooser
			{ "TableColumn", "TableColumn" },//
			{ "Header", "Header" },//
			{ "Is visible", "Is visible" },//
			{ "Sorting on", "Sorting on" },//
			{ "Filtering on", "Filtering on" },//
			{ "Comparator", "Comparator" },//
			// Table commands
			{ "Clear History", "Clear" },//
			{ "Close", "Close" },//
			{ "Format exception", "Format exception" },//
			{ "Style is not supported", "Style is not supported" },//
			{ "Select all", "Select all" },//
			{ "Cancel", "Cancel" }, //
			{ "Set", "Set" }, //
			{ "Copy", "Copy" }, //
			{ "", "" }, //

	// END OF MATERIAL TO LOCALIZE
	};
}
