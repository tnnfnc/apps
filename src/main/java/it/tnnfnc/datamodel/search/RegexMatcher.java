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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class perform a text pattern search based on regular expressions.
 * 
 * 
 * @author Franco Toninato
 * 
 */
public class RegexMatcher implements I_RegexMatcher {

	/**
	 * Compiled pattern.
	 */
	private Pattern compiledRegex;
	/**
	 * The matching object.
	 */
	private Matcher regexMatcher;
	/**
	 * The line style.
	 */
	private I_RegexLineStyler lineStyler;
	/**
	 * Internal status.
	 */
	private boolean matchFound = false;
	/**
	 * The detailed search log.
	 */
	protected StringBuffer searchLog;

	// private StringBuffer searchResult;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.I_RegexMatcher#setPattern(java.util.regex. Pattern)
	 */
	@Override
	public void setPattern(Pattern regex) {
		this.compiledRegex = regex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.I_RegexMatcher#setStyler(net.catode.model
	 * .I_LineStyler)
	 */
	@Override
	public void setStyler(I_RegexLineStyler lineStyler) {
		if (lineStyler != null)
			this.lineStyler = lineStyler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.I_RegexMatcher#searchInto(java.lang.StringBuffer )
	 */
	@Override
	public StringBuffer lookAt(StringBuffer text) {
		searchLog = new StringBuffer();
		// searchResult = new StringBuffer();
		matchFound = false;
		if (text != null && compiledRegex != null) {
			regexMatcher = compiledRegex.matcher(text);
			findReplace();
		}
		// Notify listeners a new match has been found
		if (matchFound) {
			searchLog.append("No further matches");
			return lineStyler.getResult();
		} else {
			searchLog.append("No matches");
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.I_RegexMatcher#getSearchLog()
	 */
	@Override
	public StringBuffer getSearchLog() {
		return searchLog;
	}

	/**
	 * Append a log.
	 * 
	 * @param log
	 *            the logged step.
	 */
	private void log(String log) {
		searchLog.append(log);
	}

	/**
	 * Perform find and replace with a styled text.
	 */
	private void findReplace() {
		// We will store the replacement text here
		StringBuffer replaceResult = new StringBuffer();
		while (regexMatcher.find()) {
			try {
				/*
				 * Replace the regex match with the styled text. Note that
				 * appendReplacement parses the replacement text to substitute
				 * $1, $2, etc. with the contents of the corresponding capturing
				 * parenthesis just like replaceAll().
				 */
				regexMatcher.appendReplacement(replaceResult,
						lineStyler.updateMatch(regexMatcher.group()));
				matchFound = true;
			} catch (IllegalStateException ex) {
				/*
				 * appendReplacement() was called without a preceding successful
				 * call to find() This exception indicates a bug in your source
				 * code
				 */
				log("appendReplacement() called without a prior successful call to find()");
				// textReplaceResults.setText("n/a");
				return;
			} catch (IllegalArgumentException ex) {
				// Replacement text contains inappropriate dollar signs
				log("Error in the replacement text:\n" + ex.getMessage());
				// textReplaceResults.setText("n/a");
				return;
			} catch (IndexOutOfBoundsException ex) {
				/*
				 * Replacement text contains a back reference that does not
				 * exist (e.g. $4 if there are only three groups)
				 */
				log("Non-existent group in the replacement text:\n"
						+ ex.getMessage());
				// textReplaceResults.setText("n/a");
				return;
			}
		}

		if (matchFound) {
			regexMatcher.appendTail(replaceResult);
			lineStyler.finalizeLine(replaceResult);
		}
		log("n/a");
		regexMatcher.reset();
	}

}
