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
public class SecurityResources extends ListResourceBundle {

	@Override
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
			// LOCALIZE THIS

			{ "Password", "Password" },//
			{ "New Password", "New Password" },//
			{ "confirm", "Confirm" },//
			{ "user", "User" },//
			{ "algorithm", "Algorithm" },//
			{ "cipher", "Cipher algorithm" },//
			{ "mac", "MAC algorithm" },//
			{ "mode", "Operation mode" },//
			{ "keylen", "Key length" },//
			{ "pad", "Block padding" },//
			{ "loginframe", "Password key" },//
			{ "random function", "Key derivation" },//
			{ "iterations", "Iterations" },//
			{ "bits", "bits" },//
			{ "Key parameters", "Key derivation parameters" },//
			{ "Cipher parameters", "Cipher parameters" },//
			// ChangePasswordPanel
			{ "Password unsettled", "Password unsettled" },//
			{ "Password mismatch", "Password mismatch" },//
			{ "Change password", "Change password" },//
			{ "Block cipher parameters", "Block cipher parameters" },//
			{ "Security provider", "Security provider" },//
			{ "XML encrypted file", "XML encrypted file" },//
			{ "Strength", "Strength" },//
	// END OF MATERIAL TO LOCALIZE
	};
}