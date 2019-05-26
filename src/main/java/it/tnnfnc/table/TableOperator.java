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

import java.io.Serializable;
import java.util.Comparator;

import it.tnnfnc.datamodel.FilterStatus;
import it.tnnfnc.datamodel.FilterType;
import it.tnnfnc.datamodel.SortOrder;
import it.tnnfnc.table.row.I_TableRow;

/**
 * This class represents a table column model to manage the ordering operation
 * according to the {@link it.tnnfnc.table.header.TableHeader} class.
 * 
 * @author Franco Toninato
 * 
 */
public class TableOperator implements Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2791515437767403456L;
	/**
	 * Current filter status - editing.
	 */
	protected FilterStatus filterStatus = FilterStatus.OFF;
	/**
	 * Current filter type - editing
	 */
	protected FilterType filterType = FilterType.ANY;
	/**
	 * Object match. Used for filtering.
	 */
	protected Object filterMatch = null;
	/**
	 * Show - hide
	 */
	private boolean visible = true;
	/**
	 * column comparator
	 */
	protected Comparator<I_TableRow<?>> comparator = null;
//	protected Comparator<? super I_TableRow> comparator = null;
	/**
	 * The internal current sorting status.
	 */
	protected SortOrder sortingOrder = SortOrder.UNSORTED;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public TableOperator clone() {
		TableOperator tcl = new TableOperator();
		// Filtering
		tcl.setFilterStatus(getFilterStatus());
		tcl.setFilterType(getFilterType());
		tcl.setFilterMatch(getFilterMatch());
		tcl.setComparator(getComparator());
		tcl.setSortingOrder(getSortingOrder());
		tcl.setVisible(isVisible());
		// tcl.setTableColumn(getTableColumn());

		return tcl;
	}

	/**
	 * @return the match.
	 */
	public Object getFilterMatch() {
		return filterMatch;
	}

	/**
	 * @return
	 */
	public FilterStatus getFilterStatus() {
		return filterStatus;
	}

	/**
	 * @return
	 */
	public FilterType getFilterType() {
		return filterType;
	}

	/**
	 * @return the comparator
	 */
	public Comparator<I_TableRow<?>> getComparator() {
//		public Comparator<? super I_TableRow> getComparator() {
		return comparator;
	}

	/**
	 * @return
	 */
	public SortOrder getSortingOrder() {
		return sortingOrder;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param comparator
	 *            the comparator to set
	 */
	public void setComparator(Comparator<I_TableRow<?>> comparator) {
//		public void setComparator(Comparator<? super I_TableRow> comparator) {
		this.comparator = comparator;
	}

	/**
	 * @param isVisible
	 * 
	 */
	public void setVisible(boolean isVisible) {
		this.visible = isVisible;
	}

	/**
	 * @param match
	 *            the match to set
	 */
	public void setFilterMatch(Object match) {
		this.filterMatch = match;
	}

	/**
	 * @param filterStatus
	 */
	public void setFilterStatus(FilterStatus filterStatus) {
		this.filterStatus = filterStatus;
	}

	/**
	 * @param filterType
	 */
	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}

	/**
	 * @param sortingOrder
	 */
	public void setSortingOrder(SortOrder sortingOrder) {
		this.sortingOrder = sortingOrder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toStringParam();
	}

	/**
	 * @return
	 */
	public String toStringParam() {
		StringBuffer b = new StringBuffer();
		// b.append("\n   - filter " + getFilter());
		b.append("\n   - filterStatus " + getFilterStatus());
		b.append("\n   - filterType " + getFilterType());
		b.append("\n   - filterMatch " + getFilterMatch());
		b.append("\n   - comparator " + getComparator());
		b.append("\n   - sortOrdering " + getSortingOrder());
		// b.append("\n   - visible " + isVisible());
		return b.toString();
	}

	/**
	 * Is true when the filtering is possible.
	 * 
	 * @return true when the filtering is possible.
	 */
	public boolean isFilter() {
		return getFilterType() != null
				&& !getFilterType().equals(FilterType.ANY)
				&& !getFilterStatus().equals(FilterStatus.DISABLED)
				&& getComparator() != null;
	}

	/**
	 * Is true when the sorting is possible.
	 * 
	 * @return true when the sorting is possible.
	 */
	public boolean isSort() {
		return getSortingOrder() != null
				&& !getSortingOrder().equals(SortOrder.UNSORTABLE)
				&& !getSortingOrder().equals(SortOrder.UNSORTED)
				&& getComparator() != null;
	}
}