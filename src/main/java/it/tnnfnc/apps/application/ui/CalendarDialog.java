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

import java.awt.Component;
import java.util.Date;

import javax.swing.WindowConstants;

/**
 * A dialog for editing the date based on a gregorian calendar.
 * 
 * @author Franco Toninato
 * 
 */
public class CalendarDialog extends AbstractDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CalendarPanel calendarPanel;
	private Date date;

	public CalendarDialog() {
		super();
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		enableOption(AbstractDialog.Options.OK_OPTION, "Ok");
		enableOption(AbstractDialog.Options.CLOSE_OPTION, "Cancel");
		enableOption(AbstractDialog.Options.NO_OPTION, "Today");
		setResizable(false);
		calendarPanel = new CalendarPanel();
		inputPanel.add(calendarPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.apps.AbstractDialog#performOK()
	 */
	@Override
	protected boolean performOK() {
		date = calendarPanel.getDate();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.AbstractDialog#performNO()
	 */
	@Override
	protected boolean performNO() {
		calendarPanel.setDate(new Date());
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.apps.AbstractDialog#performClose()
	 */
	@Override
	protected boolean performClose() {
		return true;
	}

	/**
	 * Shows the calendar dialog relative to the parent component.
	 * 
	 * @param parentComponent
	 *            the parent component.
	 * @param d
	 *            the date.
	 * @return the new date selected by the user.
	 */
	public Date showCalendarDialog(Component parentComponent, final Date d) {
		this.date = d;
		calendarPanel.setDate(d);

		setLocationRelativeTo(parentComponent);

		pack();
		setVisible(true);
		return date;
	}

	/**
	 * Test the class.
	 * 
	 * @param args
	 *            none.
	 */
	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				CalendarDialog c = new CalendarDialog();
				c.pack();
				c.setResizable(false);
				Date date = c.showCalendarDialog(null, new Date());
				System.out.println("selected date dialog " + date);
			}
		});
	}

}
