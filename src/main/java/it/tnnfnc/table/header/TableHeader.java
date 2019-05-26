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
package it.tnnfnc.table.header;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;

import it.tnnfnc.apps.application.ui.GuiUtility;
import it.tnnfnc.apps.application.ui.PopupListWindow;
import it.tnnfnc.datamodel.FilterEvent;
import it.tnnfnc.datamodel.FilterStatus;
import it.tnnfnc.datamodel.FilterType;
import it.tnnfnc.datamodel.I_FilterListener;
import it.tnnfnc.datamodel.I_IndexModel;
import it.tnnfnc.datamodel.I_SortListener;
import it.tnnfnc.datamodel.SortEvent;
import it.tnnfnc.datamodel.SortOrder;
import it.tnnfnc.table.TableOperator;
import it.tnnfnc.table.TableOperatorModel;
import it.tnnfnc.table.row.I_TableRow;

public class TableHeader extends JTableHeader {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	/**
	 * Remove filter description.
	 */
	protected static final String NOFILTER = "< no filter >";
	/**
	 * Null filter value description.
	 */
	protected static final String NULL = "<...>";
	/**
	 * Rectangle used for providing the sort button position.
	 */
	private static final Rectangle sortButtonStamp = new Rectangle();
	/**
	 * Rectangle used for providing the filter button position.
	 */
	private static final Rectangle filterButtonStamp = new Rectangle();
	/**
	 * Sort content display origin.
	 */
	private static final Point listOrigin = new Point();
	/**
	 * Max number of displayed content elements
	 */
	private static final int MAX_ROW_FILTER = 20;

	/**
	 * Pixels under the header.
	 */
	private int magic_number = 1;

	/**
	 * Current header model key.
	 */
	private int selectedCol = -1;
	/**
	 * Drop down top level window for displaying available filters.
	 */
	private PopupListWindow<Object> filterWindow;
	/**
	 * Object reference for content.
	 */
	private I_TableRow<?>[] filterList;
	/**
	 * Use the table renderer for the filter content.
	 */
	private boolean useTableRenderer = true;
	/**
	 * A customer renderer for the filter content.
	 */
	private ListCellRenderer<Object> listCellRenderer;
	/**
	 * The table control model.
	 */
	private TableOperatorModel tableOperatorModel;
	/**
	 * Operations listener.
	 */
	// private EventListenerList listenerList = new EventListenerList();

	/**
	 * Remove filter element. When selected causes any filter be removed.
	 */
	public final JTextField removeAll = new JTextField(NOFILTER) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public String toString() {
			return new String(NOFILTER);
		}
	};

	/**
	 * Creates a new default instance.
	 */
	public TableHeader() {
		this(null);
	}

	/**
	 * Constructor for an header.
	 * 
	 * @param columnModel
	 *            the model containing columns.
	 */
	public TableHeader(TableColumnModel columnModel) {
		super(columnModel);
		createFilterListDropdown();
		this.addMouseListener(new HeaderMouseListener());
		this.addSortListener(tableOperatorModel);
		this.addFilterListener(tableOperatorModel);
	}

	/**
	 * Create the window for the content.
	 */
	@SuppressWarnings("serial")
	private void createFilterListDropdown() {
		if (filterWindow == null) {
			filterWindow = new PopupListWindow<Object>() {

				@Override
				public void windowsLostFocus() {
					if (isFiltering(selectedCol)) {
						cancelFilter(selectedCol);
					}
					filterWindow.setVisible(false);
					filterWindow.dispose();
					TableHeader.this.repaint();
				}

				@Override
				public void windowsGainFocus() {
				}

				@Override
				public void performOnClick(MouseEvent e) {
					int selIndex = filterWindow.getList().getSelectedIndex();
					removeFilter(selectedCol);
					if (selIndex == -1 && isFiltering(selectedCol)) {
						filterWindow.getList().clearSelection();
					} else if (selIndex >= 0) {// && isFiltering(selectedCol)) {
						doFilter(selectedCol, getListSelectedValue(selIndex));
					}
					filterWindow.setVisible(false);
					filterWindow.dispose();
					TableHeader.this.repaint();
				}
			};
		}
		removeAll.setEditable(false);
		removeAll.setBackground(UIManager.getColor("List.background"));
		filterWindow.addItem(removeAll);
		// Set the default renderer for the content
		listCellRenderer = filterWindow.getList().getCellRenderer();
	}

	/**
	 * Create a default listWindow.getList() model with a first remove all
	 * entry.
	 * 
	 * @param data
	 *            the model data.
	 */
	private void createStringListModel(int column) {
		Object listdata[] = new Object[filterList.length];
		for (int slot = 0; slot < listdata.length; slot++) {
			Object value = filterList[slot].get(column);
			String s = new String();
			if (value == null) {
				s = NULL;
			} else {
				s = value.toString();
			}
			listdata[slot] = s;
		}
		filterWindow.getList().setListData(listdata);
	}

	/**
	 * Create a default listWindow.getList() model with a first remove all
	 * entry.
	 * 
	 * @param data
	 *            the model data.
	 */
	private void createObjectListModel(int column) {
		Object listdata[] = new Object[filterList.length];
		for (int slot = 0; slot < listdata.length; slot++) {
			listdata[slot] = filterList[slot].get(column);
		}
		filterWindow.getList().setListData(listdata);
	}

	/**
	 * Get the header value.
	 * 
	 * @param columnIndex
	 *            the column identifier.
	 * @return the column header value.
	 */
	private TableOperator getColumnControl(int columnIndex) {
		// System.out
		// .println("TableHeader.getColumnControl() col= " + columnIndex);

		return tableOperatorModel.getColumnControl(columnModel
				.getColumn(columnIndex));
	}

	/**
	 * Return the object.
	 * 
	 * @param selected
	 * @return
	 */
	private Object getListSelectedValue(int selected) {
		return filterList[selected];
	}

	/**
	 * Cancel when exit from the window.
	 * 
	 * @param col
	 *            the column index.
	 */
	private void cancelFilter(int col) {
		// replace the selected with the previous one
		TableOperator control = getColumnControl(col);
		if (control.getFilterMatch() == null) {
			control.setFilterStatus(FilterStatus.OFF);
			control.setFilterMatch(null);
			control.setFilterType(FilterType.ANY);
		} else {
			control.setFilterStatus(FilterStatus.ON);
		}
		// Needed for trigger repaint in the table header
		fireFilterData(new FilterEvent(this.columnModel.getColumn(col), control
				.getFilterType(), control.getFilterMatch(), getColumnControl(
				col).getComparator()));
	}

	/**
	 * Display filter data listWindow.getList().
	 * 
	 * @param column
	 *            the selected column in the table view.
	 * @param options
	 *            the allowed values.
	 */
	private void filter(int column, final I_TableRow<?>[] options) {
		// Check if filter enabled
		TableOperator control = getColumnControl(column);
		if (control.getFilterStatus() == FilterStatus.DISABLED) {
			return;
		}
		filterList = options;
		filterWindow.getList().setCellRenderer(listCellRenderer);
		// The column index changes when table columns are moved
		createStringListModel(table.convertColumnIndexToModel(column));
		// set the filtering status
		control.setFilterStatus(FilterStatus.FILTERING);
		// set the selected
		if (control.getFilterMatch() == null) {
			filterWindow.getList().clearSelection();
		} else {
			filterWindow.getList().setSelectedValue(control.getFilterMatch(),
					true);
		}
		// pop up the listWindow.getList()
		Point hL = this.getLocationOnScreen();
		Rectangle hR = this.getHeaderRect(column);
		listOrigin.setLocation(hL.x + hR.x, hL.y + hR.y + hR.height
				+ magic_number);

		filterWindow.setMinimumSize(new Dimension(hR.width, 0));
		filterWindow.setVisibleRowCount(MAX_ROW_FILTER);
		filterWindow.pack();
		filterWindow.setLocation(listOrigin);
		this.filterWindow.setVisible(true);
	}

	/**
	 * Display filter data listWindow.getList().
	 * 
	 * @param column
	 *            the selected column in the table view.
	 * @param options
	 *            the allowed values.
	 * @param renderer
	 *            the renderer for the listWindow.getList().
	 */
	private void filter(int column, final I_TableRow<?>[] options,
			ListCellRenderer<Object> renderer) {
		// System.out.println(this.getClass().getName() + " filter event at "
		// + column);
		// Check if filter enabled
		TableOperator control = getColumnControl(column);
		if (control.getFilterStatus() == FilterStatus.DISABLED) {
			return;
		}
		filterList = options;
		filterWindow.getList().setCellRenderer(renderer);
		// The column index changes when table colums are moved
		createObjectListModel(table.convertColumnIndexToModel(column));
		// set the filtering status
		control.setFilterStatus(FilterStatus.FILTERING);
		// set the selected
		if (control.getFilterMatch() == null) {
			filterWindow.getList().clearSelection();
		} else {
			filterWindow.getList().setSelectedValue(control.getFilterMatch(),
					true);
		}
		// pop up the listWindow.getList()
		Point hL = this.getLocationOnScreen();
		Rectangle hR = this.getHeaderRect(column);
		listOrigin.setLocation(hL.x + hR.x, hL.y + hR.y + hR.height
				+ magic_number);

		filterWindow.setMinimumSize(new Dimension(hR.width, 0));
		filterWindow.setVisibleRowCount(MAX_ROW_FILTER);

		filterWindow.pack();
		filterWindow.setLocation(listOrigin);
		this.filterWindow.setVisible(true);
	}

	/**
	 * Get the header renderer for the column.
	 * 
	 * @param column
	 *            the table column.
	 * @return the renderer.
	 */
	protected TableCellRenderer getHeaderRenderer(int column) {
		TableCellRenderer hcr = null;
		if (table != null) {
			hcr = table.getColumnModel().getColumn(column).getHeaderRenderer();
		}
		if (hcr == null) {
			hcr = getDefaultRenderer();
		}
		return hcr;
	}

	/**
	 * Creates a default renderer.
	 * 
	 * @return A default renderer.
	 */
	@Override
	protected TableCellRenderer createDefaultRenderer() {
		HeaderRenderer hr = new HeaderRenderer(tableOperatorModel);
		return hr;
	}

	/**
	 * Initializes the fields and properties of this class with default values.
	 * This is called by the constructors.
	 */
	@Override
	protected void initializeLocalVars() {
		this.tableOperatorModel = new TableOperatorModel(null);
		super.initializeLocalVars();
	}

	/**
	 * Add a filter listener.
	 * 
	 * @param listener
	 *            the filter listener.
	 */
	public void addFilterListener(I_FilterListener listener) {
		this.listenerList.add(I_FilterListener.class, listener);
	}

	/**
	 * Add a sort listener.
	 * 
	 * @param listener
	 *            the sort listener.
	 */
	public void addSortListener(I_SortListener listener) {
		this.listenerList.add(I_SortListener.class, listener);
	}

	/**
	 * Return true when a filter is active.
	 * 
	 * @param columnIndex
	 *            the column index.
	 * @return true when is editing.
	 */
	public boolean isFiltering(int columnIndex) {
		return getColumnControl(columnIndex).getFilterStatus() == FilterStatus.FILTERING;
	}

	/**
	 * Stop: hide list, update the table column model and exit.
	 * 
	 * @param col
	 *            the filtered column index.
	 * @param match
	 *            filter match.
	 */
	public void doFilter(int col, Object match) {
		TableOperator control = getColumnControl(col);
		control.setFilterMatch(match);
		control.setFilterType(FilterType.MATCH);
		control.setFilterStatus(FilterStatus.ON);
	
		fireFilterData(new FilterEvent(this.columnModel.getColumn(col), control
				.getFilterType(), control.getFilterMatch(), control
				.getComparator()));
	}

	/**
	 * Remove the active filter.
	 * 
	 * @param col
	 *            the column index.
	 */
	public void removeFilter(int col) {
		if (col > -1) {
			TableOperator control = getColumnControl(col);
			control.setFilterStatus(FilterStatus.OFF);
			control.setFilterMatch(null);
			control.setFilterType(FilterType.ANY);
			fireFilterData(new FilterEvent(this.columnModel.getColumn(col),
					control.getFilterType(), null, control.getComparator()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.JTableHeader#setTable(javax.swing.JTable)
	 */
	@Override
	public void setTable(JTable table) {
		super.setTable(table);
		tableOperatorModel.setTable(table);
	}

	/**
	 * Set the comparator for sorting the visible column.
	 * 
	 * @param col
	 *            the index of table column.
	 * @param comparator
	 *            the comparator.
	 */
	public void setComparatorForColumn(int col,
			Comparator<I_TableRow<?>> comparator) {
		getColumnControl(col).setComparator(comparator);
	}

	/**
	 * Get the comparator for sorting the visible column.
	 * 
	 * @param col
	 *            the index of table column.
	 * @return comparator the comparator.
	 */
//	public Comparator<? super I_TableRow<Object>> getComparatorForColumn(int col) {
	public Comparator<I_TableRow<?>> getComparatorForColumn(int col) {
		return getColumnControl(col).getComparator();
	}

	/**
	 * Prepare the content for filtering. It displays sorted values
	 * corresponding to all active filters but not the passed column one.
	 * 
	 * @param col
	 *            the visible column.
	 * @return the filtered and ascending sorted object content.
	 */
	@SuppressWarnings("unchecked")
	public I_TableRow<?>[] getFilterList(int col) {
		// Apply active filters but the current column's one
		if (table.getModel() instanceof I_IndexModel<?>) {
			I_IndexModel<I_TableRow<?>> indexModel = ((I_IndexModel<I_TableRow<?>>) table
					.getModel()).copy();
			indexModel.setActiveSize(indexModel.getFullSize());
			// System.out.println(indexModel.getActiveSize());
			// Apply active filters but the current column's one
			TableOperator ref_c = getColumnControl(col);
			for (int i = 0; i < columnModel.getColumnCount(); i++) {
				TableOperator c = getColumnControl(i);
				// System.out.println("TableHeader.getFilterList model column = "
				// + i + " = " + c.toStringParam());
				// if (c != ref_c && !c.getFilterType().equals(FilterType.ANY) )
				// {
				if (c != ref_c && c.isFilter()) {
					indexModel.filter(c.getComparator(), (I_TableRow<?>) c
							.getFilterMatch());
					// System.out.println("match for column = "
					// + i + " = " + c.getFilterMatch());
					// System.out.println(indexModel.getActiveSize());
				}
			}

			indexModel.getDistincts(ref_c.getComparator(), SortOrder.ASCENDING);

			return indexModel.toArray(new I_TableRow[0]);
		}
		return new I_TableRow[0];

	}

	/**
	 * Get the operator model.
	 * 
	 * @return the operator model.
	 */
	public TableOperatorModel getOperatorModel() {
		return tableOperatorModel;
	}

	/**
	 * Fire a filter event when a filter is activated.
	 * 
	 * @param event
	 *            the filter event.
	 */
	public void fireFilterData(FilterEvent event) {
		// Repaint cells
		this.repaint();
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		if (event == null) {
			return;
		}
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == I_FilterListener.class) {
				((I_FilterListener) listeners[i + 1]).filterPerformed(event);
			}
		}
	}

	/**
	 * Fire a sort listWindow.getList() event.
	 * 
	 * @param event
	 *            the sort event.
	 */
	public void fireSortData(SortEvent event) {
		this.repaint();
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		if (event == null)
			return;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == I_SortListener.class) {
				((I_SortListener) listeners[i + 1]).sortPerformed(event);
			}
		}
	}

	/**
	 * Use the table cell renderer for displaying the filter list content,
	 * default is true.
	 * 
	 * @param b
	 *            when true it enables the table cell renderer, when false it
	 *            uses a default plain text renderer.
	 */
	public void useTableCellRenderer(boolean b) {
		useTableRenderer = b;
	}

	/**
	 * Get the rectangle where the filer button is placed for a column.
	 * 
	 * @param column
	 *            the header column.
	 * @return the rectangle where the filter button is placed.
	 */
	protected Rectangle getFilterButtonRect(int column) {
		TableCellRenderer hcr = getHeaderRenderer(column);

		if (hcr instanceof HeaderComponent) {
			// Is filter enabled?
			// Is sort enabled?
			HeaderComponent hr = (HeaderComponent) hcr;

			// get the header rectangle
			Rectangle hRect = getHeaderRect(column);
			// get the buttons rectangle
			Rectangle bRect = hr.getButtonsRect();
			// get filter button
			Rectangle fRect = hr.getFilterButtonRect();

			// Algo for west:
			int x = hRect.x + hRect.width - bRect.width + fRect.x;
			int y = fRect.y + bRect.y;
			int w = fRect.width;
			int h = fRect.height;
			filterButtonStamp.setBounds(x, y, w, h);
			return filterButtonStamp;
		}
		return null;
	}

	/**
	 * Get the rectangle where the sort button is placed for a column.
	 * 
	 * @param column
	 *            the header column.
	 * @return the rectangle where the sort button is placed.
	 */
	protected Rectangle getSortButtonRect(int column) {
		TableCellRenderer hcr = getHeaderRenderer(column);
		if (hcr instanceof HeaderComponent) {
			// Is filter enabled?
			// Is sort enabled?
			HeaderComponent hr = (HeaderComponent) hcr;
			// get the header rectangle
			Rectangle hRect = getHeaderRect(column);
			// get the buttons rectangle
			Rectangle bRect = hr.getButtonsRect();
			// get filter button
			Rectangle sRect = hr.getSortButtonRect();

			// Algo:
			int x = hRect.x + hRect.width - bRect.width + sRect.x;
			int y = sRect.y + bRect.y;
			int w = sRect.width;
			int h = sRect.height;
			sortButtonStamp.setBounds(x, y, w, h);
			return sortButtonStamp;
		}
		return null;
	}

	/**
	 * Return true if the point is inside a filter button.
	 * 
	 * @param column
	 *            the header column.
	 * @param p
	 *            the point.
	 * 
	 * @return true if the point is inside a filter button.
	 */
	protected boolean insideFilterRect(int column, Point p) {
		Rectangle r = getFilterButtonRect(columnAtPoint(p));
		if (r == null)
			return false;
		return r.contains(p);
	}

	/**
	 * Return true if the point is inside a sort button.
	 * 
	 * @param column
	 *            the header column.
	 * @param p
	 *            the point.
	 * 
	 * @return true if the point is inside a sort button.
	 */
	protected boolean insideSortRect(int column, Point p) {
		Rectangle r = getSortButtonRect(columnAtPoint(p));
		if (r == null)
			return false;
		return r.contains(p);
	}

	/**
	 * Remove a filter listener.
	 * 
	 * @param listener
	 *            the filter listener.
	 */
	public void removeFilterListener(I_FilterListener listener) {
		this.listenerList.remove(I_FilterListener.class, listener);
	}

	/**
	 * Remove a sort listener.
	 * 
	 * @param listener
	 *            the sort listener.
	 */
	public void removeSortListener(I_SortListener listener) {
		this.listenerList.remove(I_SortListener.class, listener);
	}

	/**
	 * Set the filter content cell renderer.
	 * 
	 * @param listener
	 *            the sort listener.
	 */
	public void setListRenderer(ListCellRenderer<Object> renderer) {
		this.listCellRenderer = renderer;
	}

	/**
	 * This listWindow.getList() cell renderer cellEditor rendering to the table
	 * cell renderer.
	 * 
	 * @author Franco Toninato
	 * 
	 */
	protected class ListRendererAdapter implements ListCellRenderer<Object> {
		private int column;

		public ListRendererAdapter(int column) {
			this.column = column;
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Component c = table.getCellRenderer(index, column)
					.getTableCellRendererComponent(table, value, isSelected,
							cellHasFocus, index, column);
			if (c instanceof JTextComponent || c instanceof JLabel) {
				GuiUtility.changeFontSize(c, 0.9F);
			}
			if (c.getPreferredSize().height < removeAll.getPreferredSize().height) {
				c.setPreferredSize(removeAll.getPreferredSize());
			}
			return c;
		}

	}

	/**
	 * Command for the mouse clicks on the header.
	 * 
	 */
	protected class HeaderMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			// Column
			selectedCol = columnAtPoint(p);
			// Identifier
			if (TableHeader.this.insideFilterRect(selectedCol, p)) {
				if (useTableRenderer) {
					filter(selectedCol, getFilterList(selectedCol),
							new ListRendererAdapter(selectedCol));
				} else {
					filter(selectedCol, getFilterList(selectedCol));
				}
			} else if (insideSortRect(selectedCol, p)) {
				fireSortData(new SortEvent(TableHeader.this.columnModel
						.getColumn(selectedCol), null, null));
			} else {
			}
		}
	}
}
