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
 * @version %I%, %G%
 * @since 1.2
 */
public class KeyBoardResources extends ListResourceBundle {

	@Override
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
	// LOCALIZE THIS

			// LoginDialog
			{ "10", new String[] { "`", "~", "", "" } }, //
			{ "11", new String[] { "1", "!", "", "" } }, //
			{ "12", new String[] { "2", "@", "", "" } }, //
			{ "13", new String[] { "3", "#", "", "" } }, //
			{ "14", new String[] { "4", "$", "", "" } }, //
			{ "15", new String[] { "5", "%", "", "" } }, //
			{ "16", new String[] { "6", "^", "", "" } }, //
			{ "17", new String[] { "7", "&", "", "" } }, //
			{ "18", new String[] { "8", "*", "", "" } }, //
			{ "19", new String[] { "9", "(", "", "" } }, //
			{ "110", new String[] { "0", ")", "", "" } }, //
			{ "111", new String[] { "-", "_", "", "" } }, //
			{ "112", new String[] { "=", "+", "", "" } }, //
			//
			{ "20", new String[] { "q", "Q", "", "" } }, //
			{ "21", new String[] { "w", "W", "", "" } }, //
			{ "22", new String[] { "e", "E", "", "" } }, //
			{ "23", new String[] { "r", "R", "", "" } }, //
			{ "24", new String[] { "t", "T", "", "" } }, //
			{ "25", new String[] { "y", "Y", "", "" } }, //
			{ "26", new String[] { "u", "U", "", "" } }, //
			{ "27", new String[] { "i", "I", "", "" } }, //
			{ "28", new String[] { "o", "O", "", "" } }, //
			{ "29", new String[] { "p", "P", "", "" } }, //
			{ "210", new String[] { "[", "{", "", "" } }, //
			{ "211", new String[] { "]", "}", "", "" } }, //
			//
			{ "30", new String[] { "a", "A", "", "" } }, //
			{ "31", new String[] { "s", "S", "", "" } }, //
			{ "32", new String[] { "d", "D", "", "" } }, //
			{ "33", new String[] { "f", "F", "", "" } }, //
			{ "34", new String[] { "g", "G", "", "" } }, //
			{ "35", new String[] { "h", "H", "", "" } }, //
			{ "36", new String[] { "j", "J", "", "" } }, //
			{ "37", new String[] { "k", "K", "", "" } }, //
			{ "38", new String[] { "l", "L", "", "" } }, //
			{ "39", new String[] { ";", ":", "", "" } }, //
			{ "310", new String[] { "'", "\"", "", "" } }, //
			{ "311", new String[] { "\\", "\\", "", "" } }, //
			//
			{ "40", new String[] { "\\", "|", "", "" } }, //
			{ "41", new String[] { "z", "Z", "", "" } }, //
			{ "42", new String[] { "x", "X", "", "" } }, //
			{ "43", new String[] { "c", "C", "", "" } }, //
			{ "44", new String[] { "v", "V", "", "" } }, //
			{ "45", new String[] { "b", "B", "", "" } }, //
			{ "46", new String[] { "n", "N", "", "" } }, //
			{ "47", new String[] { "m", "M", "", "" } }, //
			{ "48", new String[] { ",", "<", "", "" } }, //
			{ "49", new String[] { ".", ">", "", "" } }, //
			{ "410", new String[] { "/", "?", "", "" } }, //
			//			
			{ "tab", new String[] { "Tab", "", "", "" } }, //
			{ "lock", new String[] { "Lock", "", "", "" } }, //
			{ "l_Shift", new String[] { "Shift", "", "", "" } }, //
			{ "r_Shift", new String[] { "Shift", "", "", "" } }, //
			{ "enter", new String[] { "Enter", "", "", "" } }, //
			{ "space", new String[] { " ", "", "", "" } }, //
			{ "back", new String[] { "Back", "", "", "" } }, //
			{ "l_Ctrl", new String[] { "Ctrl", "", "", "" } }, //
			{ "r_Ctrl", new String[] { "Ctrl", "", "", "" } }, //
			{ "alt", new String[] { "Alt", "", "", "" } }, //
			{ "altGr", new String[] { "AltGr", "", "", "" } }, //

	// END OF MATERIAL TO LOCALIZE
	};
}
