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
 * This class implements a text pattern search through a <code>toString</code>
 * object conversion.
 * 
 * @author Franco Toninato
 * 
 */
public abstract class AbstractRegexSearch implements I_RegexSearch {

	/**
	 * The model where matches are appended to.
	 */
	protected MatchResultModel matchResultModel;
	/**
	 * The matcher responsible of implementing the search.
	 */
	protected I_RegexMatcher regularSearch;
	/**
	 * The source for the search process.
	 */
	protected Object source;

	/**
	 * Class constructor.
	 * 
	 * @param s
	 *            the object implementing a regular expression search.
	 * @param source
	 *            the object where the search is performed.
	 */
	public AbstractRegexSearch(I_RegexMatcher s, Object source) {
		this.regularSearch = s;
		this.source = source;
	}

	/**
	 * Add a matches store.
	 * 
	 * @param resultStoreModel
	 *            the matches store.
	 */
	@Override
	public void setResultStoreModel(MatchResultModel resultStoreModel) {
		this.matchResultModel = resultStoreModel;
	}

	/**
	 * Search in an object.
	 * 
	 * @param pattern
	 *            the searched pattern.
	 */
	@Override
	public void search(Pattern pattern) {
		regularSearch.setPattern(pattern);
		StringBuffer result = regularSearch.lookAt(new StringBuffer(source
				.toString()));
		matchFound(result, source);
	}

	/**
	 * @param result
	 * @param object
	 */
	protected void matchFound(StringBuffer result, Object object) {

		if (result != null) {
			matchResultModel.addElement(result, object);
		}
	}
}
