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
package it.tnnfnc.datamodel.search;

import java.util.regex.Pattern;

/**
 * A regular search.
 * 
 * @author Franco Toninato
 * 
 */
public interface I_RegexMatcher {

	/**
	 * Set the search regular expression.
	 * 
	 * @param regex
	 *            the search regular expression.
	 */
	public void setPattern(Pattern regex);

	/**
	 * Set the search line formatter.
	 * 
	 * @param lineStyler
	 *            the line styler.
	 */
	public void setStyler(I_RegexLineStyler lineStyler);

	/**
	 * Search a regular expression in the text.
	 * 
	 * @param text
	 *            the text
	 * @return the search result with the matches.
	 */
	public StringBuffer lookAt(StringBuffer text);

	/**
	 * Get the search log.
	 * 
	 * @return the search log.
	 */
	public StringBuffer getSearchLog();

}