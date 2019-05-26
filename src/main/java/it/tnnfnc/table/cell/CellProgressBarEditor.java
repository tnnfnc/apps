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
package it.tnnfnc.table.cell; //Package

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;


/**
 * This class implements a cell editor based on a slider. The edited value must
 * be an integer. It does not allow an accurate editing of the numeric value.
 * 
 * @author Franco Toninato
 */
public class CellProgressBarEditor extends CellEditor implements
		TableCellEditor, ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JSlider slider;

	// private int returnValue = 0;

	/**
	 * Object constructor.
	 */
	public CellProgressBarEditor() {
		slider = new JSlider();
		slider.setPaintTrack(true);
		slider.addChangeListener(this);
		setClickCountToStart(1);
	}

	/**
	 * 
	 * @param s
	 *            the slider.
	 */
	public void setSlider(JSlider s) {
		slider.removeChangeListener(this);
		slider = s;
		slider.addChangeListener(this);
	}

	/**
	 * get the slider of this editor.
	 * 
	 * @return the slider.
	 */
	public JSlider getSlider() {
		return slider;
	}

	/**
	 * Set the value from the table into the editor.
	 * 
	 * @param cell
	 *            the value.
	 */
	protected void setValue(Object cell) {
		if (cell != null && cell instanceof RangedValue) {
			RangedValue range = (RangedValue) cell;
			slider.setValue(range.getGauge());
			if (range.getMax() > range.getMin()
					&& (range.getMax() != slider.getMaximum() || range.getMin() != slider
							.getMinimum())) {
				slider.setMaximum(range.getMax());
				slider.setMinimum(range.getMin());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		// System.out.println(this.getClass().getName()
		// + value.getClass().getName());
		if (value != null && value instanceof RangedValue) {
			RangedValue range = (RangedValue) value;
			// range.setGauge(slider.getValue());
			RangedValue r = new RangedValue();
			r.setGauge(slider.getValue());
			r.setMax(range.getMax());
			r.setMin(range.getMin());
			r.setStep(range.getStep());
			r.setValue(range.getValue());
			return r;
		}
		return value;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.value = value;
		setValue(value);
		return this.slider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.catode.table.cell.CellEditor#isCellEditable(java.util.EventObject)
	 */
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		// Gets the mouse position
		if (anEvent instanceof MouseEvent) {
			MouseEvent me = (MouseEvent) anEvent;
			return me.getClickCount() >= clickCountToStart;
		}
		return false;
	}

	/**
	 * Returns if the cell must be selected as a result of this event.
	 * 
	 * @param anEvent
	 *            the event delivered to this cell.
	 *@return true when the event should perform the cell selection.
	 */
	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		// JSlider source = (JSlider) event.getSource();
		if (!slider.getValueIsAdjusting()) {
			if (value != null) {
				// returnValue = slider.getValue();
				// fireEditingStopped();
			}
		}
	}

}