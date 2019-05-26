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

import java.awt.Point;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import it.tnnfnc.datamodel.FilterEvent;
import it.tnnfnc.datamodel.I_FilterListener;
import it.tnnfnc.datamodel.I_IndexModel;
import it.tnnfnc.datamodel.I_SortListener;
import it.tnnfnc.datamodel.SortEvent;
import it.tnnfnc.datamodel.SortOrder;
import it.tnnfnc.table.row.I_TableRow;
import it.tnnfnc.table.row.RowComparatorFactory;

/**
 * This class implements ordering operations for rows in a table.
 * 
 * @author franco toninato
 * 
 */
public class TableOperatorModel implements I_SortListener, I_FilterListener {

	protected JTable table;
	protected Point selected_cell;
	/**
	 * Current sorted column header, it can be null.
	 */
	private TableOperator sortedColumn;
	/**
	 * Column model - (identifier, index) mapping set at initialization also for
	 * columns deleted from the <code>ColumnModel</code>.
	 */
	protected HashMap<Object, TableOperator> tableOperators = new HashMap<Object, TableOperator>();

	/**
	 * Object constructor.
	 * 
	 * @param t
	 *            the table.
	 */
	public TableOperatorModel(JTable t) {
		setTable(t);
	}

	/**
	 * Set the table.
	 * 
	 * @param t
	 *            the table.
	 */
	public void setTable(JTable t) {
		tableOperators.clear();
		this.table = t;
	}

	/**
	 * Set the comparator for sorting the visible column.
	 * 
	 * @param col
	 *            the visible table column.
	 * @param comparator
	 *            the comparator.
	 */
	public void setComparatorForColumn(TableColumn col, Comparator<I_TableRow<?>> comparator) {
		getColumnControl(col).setComparator(comparator);
	}

	/**
	 * Get the table operator, if it does not exist, the method lazily creates a
	 * default new one.
	 * 
	 * @param col
	 *            the table column.
	 * @return the table column operator.
	 */
	public TableOperator getColumnControl(TableColumn col) {
		if (tableOperators.containsKey(col)) {
			return tableOperators.get(col);
		} else {
			TableOperator c = new TableOperator();
			tableOperators.put(col, c);
			setLazilyDefaultComparator(col);
			return c;
		}
	}

	@Override
	public void sortPerformed(SortEvent e) {
		if (e.getSource() instanceof TableColumn) {
			TableColumn tc = (TableColumn) e.getSource();
			// Initialize the table column control
			prepareSort(getColumnControl(tc));
			// System.out.println(e.getSource().getClass()
			// + e.getType().toString());
			I_IndexModel<I_TableRow<?>> m = castToIndexModel(table.getModel());
			setSelectedCell(m);
			if (m != null) {
				order(m);
			}
			setTableSelection(m);
		}
	}

	@Override
	public void filterPerformed(FilterEvent e) {
		if (e.getSource() instanceof TableColumn) {
			TableColumn tc = (TableColumn) e.getSource();
			TableOperator col = getColumnControl(tc);
			col.setFilterType(e.getType());
			col.setFilterMatch(e.getMatch());
			// System.out.println(e.getSource().getClass()
			// + e.getType().toString());
			I_IndexModel<I_TableRow<?>> m = castToIndexModel(table.getModel());
			setSelectedCell(m);
			if (m != null) {
				order(m);
			}

			setTableSelection(m);
		}
	}

	/**
	 * Apply all active filters and the active sort. Rearrange the table
	 * applying active filters and sorting. When an undo manager is set the
	 * rearrangement should be preceded by restoring the reference status of the
	 * model.
	 * 
	 * @param model
	 *            the data model.
	 */
	protected void order(I_IndexModel<I_TableRow<?>> model) {
		// protected void order(I_IndexModel<I_TableRow<?>> model) {

		/*
		 * Recover the index inactive rows, but not the ones corresponding to
		 * deleted data.
		 */
		model.setActiveSize(model.getFullSize());

		// Apply filter
		for (int i = 0; i < table.getColumnCount(); i++) {
			TableOperator c = tableOperators.get(table.getColumnModel().getColumn(i));
			// System.out.println("TableR.order " + c + " filter "+
			// c.filterAllowed());
			if (c != null && c.isFilter()) {
				model.filter(c.getComparator(), (I_TableRow<?>) c.getFilterMatch());
			}
		}
		// Apply sort
		for (int i = 0; i < table.getColumnCount(); i++) {
			TableOperator c = tableOperators.get(table.getColumnModel().getColumn(i));
			if (c != null && c.isSort()) {
				model.sort(c.getComparator(), c.getSortingOrder());
			}
		}
		

	}

	/**
	 * @param m
	 * 
	 */
	private void setSelectedCell(I_IndexModel<I_TableRow<?>> m) {
		selected_cell = new Point(m.indexOf(table.getSelectedRow()), table.getSelectedColumn());
	}

	/**
	 * @param m
	 */
	private void setTableSelection(I_IndexModel<I_TableRow<?>> m) {
		int x = m.getIndex(selected_cell.x);
		if (x > -1 && x < m.getActiveSize()) {
			table.getSelectionModel().setSelectionInterval(x, x);
			table.getColumnModel().getSelectionModel().setSelectionInterval(0, table.getColumnCount());
		} else {
			table.getSelectionModel().setSelectionInterval(0, 0);
			table.getColumnModel().getSelectionModel().setSelectionInterval(0, table.getColumnCount());
		}

	}

	/**
	 * Cast to an index model.
	 * 
	 * @param m
	 *            the table model.
	 * @return the index model.
	 */
	@SuppressWarnings("unchecked")
	private I_IndexModel<I_TableRow<?>> castToIndexModel(TableModel m) {
		if (m instanceof I_IndexModel<?>) {
			return (I_IndexModel<I_TableRow<?>>) m;
		}
		return null;
	}

	/**
	 * Set the default comparator for a table column.
	 * 
	 * @param col
	 *            the table column.
	 */
	private void setLazilyDefaultComparator(TableColumn col) {
		for (int j = 0; j < table.getColumnModel().getColumnCount(); j++) {
			if (table.getColumnModel().getColumn(j).equals(col)) {
				setComparatorForColumn(col, RowComparatorFactory.getDefaultComparator(col.getModelIndex(),
						table.getModel().getColumnClass(j)));
				return;
			}
		}
		// System.out.println(" comparator not found for class");
		setComparatorForColumn(col, RowComparatorFactory.getDefaultComparator(col.getModelIndex()));
	}

	/**
	 * Perform a cyclic change of the sorting order. Unsorted to ascending to
	 * descending to unsorted.
	 * 
	 * @param column
	 *            the header column index.
	 */
	private void prepareSort(TableOperator control) {
		/*
		 * Switch the sort status through headers. When a header sort status is
		 * switched on the previous sorted header status is switched off.
		 */
		if (sortedColumn == null || sortedColumn.equals(control)) {
		} else {
			// Cancel the previous sorted header to unsorted.
			sortedColumn.setSortingOrder(SortOrder.UNSORTED);
		}
		sortedColumn = control;

		SortOrder sortingOrder = control.getSortingOrder();
		switch (sortingOrder) {
		case UNSORTED:
			control.setSortingOrder(SortOrder.ASCENDING);
			break;
		case ASCENDING:
			control.setSortingOrder(SortOrder.DESCENDING);
			break;
		case DESCENDING:
			// Clear the sorted header.
			control.setSortingOrder(SortOrder.UNSORTED);
			sortedColumn = null;
			break;
		case UNSORTABLE:
			// Clear the sorted header.
			sortedColumn = null;
			return;
		default:
			control.setSortingOrder(SortOrder.UNSORTED);
			// Clear the sorted header.
			sortedColumn = null;
			break;
		}
	}
}
