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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import it.tnnfnc.apps.application.ui.style.IconsFactory;
import it.tnnfnc.datamodel.FilterStatus;
import it.tnnfnc.datamodel.SortOrder;
import it.tnnfnc.table.header.TableHeader;

/**
 * Panel for managing table columns.
 * 
 * @author franco toninato
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableColumnChooser extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ListResourceBundle language = (ListResourceBundle) ResourceBundle
			.getBundle(TableLocalBundle.class.getName());

	private JTable table;
	private TableOperatorModel operatorModel;
	private JTable tableOfColumns;
	private JDialog dialog;

	// Columns position
	private final static int tableColumn = 0;// TableColumn - no display
	private final static int headerLabel = 1;// Header label
	private final static int display = 2;// Visible
	private final static int sort = 3;// Sort
	private final static int filter = 4;// Filter
	private final static int comparator = 5;// Select comparator (combo box)

	// Command actions
	private static final String UP = "UP";
	private static final String DOWN = "DOWN";
	private static final String OK = "OK";
	private static final String CANCEL = "CANCEL";
	private static final String SELECT_ALL = "SELECT_ALL";

	/**
	 * Creates a table column panel.
	 * 
	 * @param t
	 *            the table.
	 * 
	 */
	public TableColumnChooser(JTable t) {
		this.table = t;
		tableOfColumns = new JTable(new InternalModel());

		refresh();

		createGUI();

	}

	private void createGUI() {

		tableOfColumns.setRowSelectionAllowed(true);
		tableOfColumns.setColumnSelectionAllowed(false);
		tableOfColumns.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tableOfColumns.getColumnModel().removeColumn(tableOfColumns.getColumnModel().getColumn(tableColumn));

		tableOfColumns.setFillsViewportHeight(false);

		JScrollPane scrollpane = new JScrollPane(tableOfColumns);

		// Set tooltip
		tableOfColumns.getColumnModel().getColumn(filter).setCellRenderer(new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			/*
			 * (non-Javadoc)
			 * 
			 * @seejavax.swing.table.DefaultTableCellRenderer#
			 * getTableCellRendererComponent(javax.swing.JTable,
			 * java.lang.Object, boolean, boolean, int, int)
			 */
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				setToolTipText(value + "");
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}

		});

		setLayout(new BorderLayout());
		add(scrollpane, BorderLayout.CENTER);

		Icon dummy = new IconsFactory.VoidIcon();

		JButton row_up = new JButton(new IconsFactory.UpArrow(getForeground()));
		row_up.setPreferredSize(new Dimension(2 * dummy.getIconWidth(), 2 * dummy.getIconHeight()));
		row_up.setActionCommand(UP);
		row_up.addActionListener(this);

		JButton row_down = new JButton(new IconsFactory.DownArrow(getForeground()));
		row_down.setPreferredSize(new Dimension(2 * dummy.getIconWidth(), 2 * dummy.getIconHeight()));
		row_down.setActionCommand(DOWN);
		row_down.addActionListener(this);

		JButton ok = new JButton(language.getString("ok"));
		ok.setActionCommand(OK);
		ok.addActionListener(this);

		JButton cancel = new JButton(language.getString("Cancel"));
		cancel.setActionCommand(CANCEL);
		cancel.addActionListener(this);

		JCheckBox selectAll = new JCheckBox(language.getString("Show all"));
		selectAll.setActionCommand(SELECT_ALL);
		selectAll.addActionListener(this);
		selectAll.setSelected(false);

		JPanel p_south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel p_north = new JPanel(new FlowLayout(FlowLayout.LEFT));

		p_south.add(ok);
		p_south.add(cancel);

		p_north.add(row_up);
		p_north.add(row_down);
		p_north.add(selectAll);

		add(p_north, BorderLayout.NORTH);
		add(p_south, BorderLayout.SOUTH);

	}

	/**
	 * @return
	 */
	private InternalModel getModel() {
		return ((InternalModel) tableOfColumns.getModel());
	}

	/**
	 * Update the panel from the table header.
	 */
	private void update() {
		getModel().update();
	}

	/**
	 * Refresh the displayed data.
	 * 
	 */
	public void refresh() {
		getModel().clear();
		TableColumnModel tcm = table.getColumnModel();
		// Fill the model
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			getModel().addRow(new Object[] { //
					tcm.getColumn(i), // Column id
					table.getColumnName(i), // Header name
					true, // Visible
					true, // Enable sort
					true, // Enable filter
					new Object(), // Comparator
			});
		}

		// tcm.addTableModelListener(this);

		try {
			operatorModel = ((TableHeader) table.getTableHeader()).getOperatorModel();
		} catch (ClassCastException e) {
			operatorModel = new TableOperatorModel(table);
		}

		update(); // Reordering!!
	}

	/**
	 * Update the table header.
	 */
	public void applyChanges() {
		getModel().updateColumnHeaders();
	}

	/**
	 * Show an edit tab dialog.
	 * 
	 * @param value
	 *            the access.
	 * 
	 */
	public void showDialog() {
		class InternalWindowsListener extends WindowAdapter {
			@Override
			public void windowClosing(WindowEvent event) {
			}
		}
		if (dialog == null) {
			dialog = new JDialog();
			dialog.setTitle("Title");
			dialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
			dialog.addWindowListener(new InternalWindowsListener());
			dialog.getContentPane().setLayout(new BorderLayout());
			dialog.getContentPane().add(this, BorderLayout.CENTER);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		}
		update();

		// dialog.setLocationRelativeTo(table);
		dialog.pack();
		dialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		// System.out.println("TableColumnChooser action = " + command);
		if (command.equalsIgnoreCase(UP)) {
			getModel().up();
		} else if (command.equalsIgnoreCase(DOWN)) {
			getModel().down();
		} else if (command.equalsIgnoreCase(OK)) {
			applyChanges();
			if (dialog != null) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		} else if (command.equalsIgnoreCase(CANCEL)) {
			if (dialog != null) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		} else if (command.equalsIgnoreCase(SELECT_ALL)) {
			// JCheckBox cb = ((JCheckBox) e.getSource());
			getModel().displayAll(((JCheckBox) e.getSource()).isSelected());
		} else {
		}

	}

	private class InternalModel extends DefaultTableModel implements TableModelListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Check if the table contains the column.
		 * 
		 * @param t
		 *            the column.
		 * @return true if the table contains the column.
		 */
		private int contains(TableColumn t) {
			for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
				if (table.getColumnModel().getColumn(i).equals(t))
					return i;
			}
			return -1;
		}

		public void displayAll(boolean selected) {
			// System.out.println(selected);
			for (int i = 0; i < dataVector.size(); i++) {
				Vector<Object> row = (Vector<Object>) dataVector.get(i);
				// Display
				row.set(display, selected);
			}
			this.fireTableRowsUpdated(0, dataVector.size());
		}

		/**
		 * 
		 */
		public void down() {
			int sel = tableOfColumns.getSelectedRow();
			if (sel > -1 && sel < tableOfColumns.getRowCount() - 1) {
				dataVector.set(sel, dataVector.set(sel + 1, dataVector.get(sel)));
				tableOfColumns.getSelectionModel().setSelectionInterval(sel + 1, sel + 1);
				this.fireTableRowsUpdated(sel, sel + 1);
			}
		}

		/**
		 * 
		 */

		public void up() {
			int sel = tableOfColumns.getSelectedRow();
			if (sel > -1 && sel > 0) {
				dataVector.set(sel, dataVector.set(sel - 1, dataVector.get(sel)));
				tableOfColumns.getSelectionModel().setSelectionInterval(sel - 1, sel - 1);
				this.fireTableRowsUpdated(sel - 1, sel);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return (column == 2 || column == 3 || column == 4);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.DefaultTableModel#getColumnCount()
		 */
		@Override
		public int getColumnCount() {
			return 6;
		}

		/**
		 * Clear all entries from the model.
		 */
		public void clear() {
			// Display or hide
			if (dataVector.size() > 0) {
				dataVector.clear();
			}
			// Change order
			fireTableDataChanged();
		}

		/**
		 * Update the model from the table column model.
		 */
		public void update() {
			// Display or hide
			for (int i = 0; i < dataVector.size(); i++) {

				Vector<Object> row = (Vector) dataVector.get(i);
				TableColumn t_i = (TableColumn) row.get(tableColumn);
				// Display
				row.set(display, contains(t_i) > -1);
				//
				TableOperator op = operatorModel.getColumnControl(t_i);
				if (op != null) {
					// Sort
					row.set(sort, op.getSortingOrder() != SortOrder.UNSORTABLE);
					// Filter
					row.set(filter, op.getFilterStatus() != FilterStatus.DISABLED);
					// Comparator
					row.set(comparator, op.getComparator().toString());
				} else {
				}
			}

			// reordering - OK
			TableColumnModel tcm = table.getColumnModel();
			for (int i = 0; i < tcm.getColumnCount(); i++) {
				TableColumn t = tcm.getColumn(i);
				// System.out.println("TableColumnChooser.update" + " move " + i
				// + " = " + t.getIdentifier());
				for (int j = i; j < dataVector.size(); j++) {
					Vector<Object> row = null;
					if ((row = (Vector) dataVector.get(j)).get(tableColumn).equals(t)) {
						// System.out.println("TableColumnChooser.update"
						// + " move " + i + " to " + j);
						dataVector.setElementAt(dataVector.set(i, row), j);
						break;
					}
				}

			}

			// Change order
			super.fireTableDataChanged();
		}

		/**
		 * Update the table column model from the model.
		 */
		// @SuppressWarnings("unchecked")
		public void updateColumnHeaders() {
			TableColumnModel tcm = table.getColumnModel();
			ArrayList<TableColumn> removedColumns = new ArrayList<TableColumn>();
			for (int i = 0; i < dataVector.size(); i++) {
				Vector<Object> row = (Vector) dataVector.get(i);

				TableColumn t_i = (TableColumn) row.get(tableColumn);

				// Enable or disable sorting
				if ((Boolean) row.get(sort)
						&& operatorModel.getColumnControl(t_i).getSortingOrder() == SortOrder.UNSORTABLE) {
					operatorModel.getColumnControl(t_i).setSortingOrder(SortOrder.UNSORTED);
				} else if (!(Boolean) row.get(sort)) {
					operatorModel.getColumnControl(t_i).setSortingOrder(SortOrder.UNSORTABLE);
				}

				// Enable or disable filtering
				if ((Boolean) row.get(filter)
						&& operatorModel.getColumnControl(t_i).getFilterStatus() == FilterStatus.DISABLED) {
					operatorModel.getColumnControl(t_i).setFilterStatus(FilterStatus.OFF);
				} else if (!(Boolean) row.get(filter)) {
					operatorModel.getColumnControl(t_i).setFilterStatus(FilterStatus.DISABLED);
				}

				if ((Boolean) row.get(display)) {
					int j = 0;
					try {
						j = tcm.getColumnIndex(t_i.getIdentifier());
					} catch (IllegalArgumentException e) {
						j = tcm.getColumnCount();
						tcm.addColumn(t_i);
					}
					if (i < j) {
						tcm.moveColumn(j, i);
					}
				} else {
					removedColumns.add(t_i);
				}
			}

			for (TableColumn tableColumn : removedColumns) {
				tcm.removeColumn(tableColumn);
			}
			// Repaint the header
			table.getTableHeader().repaint();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.DefaultTableModel#getColumnName(int)
		 */
		@Override
		public String getColumnName(int column) {
			switch (column) {
			case tableColumn:
				return language.getString("TableColumn");
			case headerLabel:
				return language.getString("Header");
			case display:
				return language.getString("Is visible");
			case sort:
				return language.getString("Sorting on");
			case filter:
				return language.getString("Filtering on");
			case comparator:
				return language.getString("Comparator");
			default:
				return language.getString("");
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case tableColumn:
				return TableColumn.class;
			case headerLabel:
				return String.class;
			case display:
				return Boolean.class;
			case sort:
				return Boolean.class;
			case filter:
				return Boolean.class;
			case comparator:
				return Object.class;
			default:
				return Object.class;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
		 */
		@Override
		public Object getValueAt(int row, int column) {

			return super.getValueAt(row, column);
		}

		@Override
		public void tableChanged(TableModelEvent event) {
			// System.out.println("tableChanged");
			update();
		}

	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				JTable tab = new JTable(new Object[][] { {} },
						new Object[] { "Col 1", "Col 2", "Col 3", "Col 4", "Col 5" });

				TableColumnChooser t = new TableColumnChooser(tab);
				t.showDialog();
			}
		});

	}

}