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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A panel with a date chooser.
 * 
 * @author Franco Toninato
 * 
 */
public class CalendarPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color WEEK_BACKGROUND = UIManager
			.getColor("Label.background");
	private static final Color WEEK_FOREGROUND = UIManager
			.getColor("Label.foreground");
	private static final Color STANDARD_BACKGROUND = UIManager
			.getColor("Table.background");
	private static final Color STANDARD_FOREGROUND = UIManager
			.getColor("Table.foreground");
	private static final Color FESTIVE_BACKGROUND = UIManager
			.getColor("Table.selectionBackground");
	private static final Color FESTIVE_FOREGROUND = UIManager
			.getColor("Table.selectionForeground");
	private static final Color DISABLED_FOREGROUND = UIManager
			.getColor("Label.disabledForeground");

	private static final int INNER_PADDING = 2;
	private static final int OUTER_PADDING = 1;
	private static final Border DAY_BORDER = new CompoundBorder(UIManager
			.getBorder("Table.focusCellHighlightBorder"),
			new CompoundBorder(new MatteBorder(OUTER_PADDING, OUTER_PADDING,
					OUTER_PADDING, OUTER_PADDING, STANDARD_BACKGROUND),
					new EmptyBorder(INNER_PADDING, INNER_PADDING,
							INNER_PADDING, INNER_PADDING)));

	/**
	 * Calendar days.
	 */
	protected DayLabel days[];
	/**
	 * Calendar weeks.
	 */
	protected DayLabel weeks[];
	/**
	 * Selected day.
	 */
	protected DayLabel selected;
	/**
	 * Today day.
	 */
	protected DayLabel today;
	/**
	 * 
	 */
	// protected DayLabel today;
	/**
	 * The selected date.
	 */
	protected Date date;
	/**
	 * The days container.
	 */
	JPanel canvas;
	/**
	 * Mouse listener for cells.
	 */
	private MouseListener mouseListener;

	/**
	 * The current calendar instance.
	 */
	private GregorianCalendar calendar;

	private JSpinner monthSelector;
	private JSpinner yearSelector;

	/**
	 * Create a calendar from the current date.
	 */
	public CalendarPanel() {
		this(new Date());
	}

	/**
	 * Create a calendar from a date.
	 * 
	 * @param d
	 *            the selected date.
	 */
	public CalendarPanel(Date d) {
		calendar = new GregorianCalendar();
		selected = new DayLabel("");
		// init(d);
		createPanel();
		setDate(d);
		updateCalendar();
	}

	/**
	 * Initialize the date for the calendar.
	 * 
	 * @param d
	 *            the new date;
	 */
	private void init(Date d) {
		calendar.setTime(d);
		today.date = d;
		today.day = calendar.get(Calendar.DAY_OF_MONTH);
		today.month = calendar.get(Calendar.MONTH);
		today.year = calendar.get(Calendar.YEAR);
		today.day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

		today.setText("<html><a href=>" + getDayStrings()[today.day_of_week]
				+ " " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(d)
				+ "</a>");

		// today.setAlignmentX(CENTER_ALIGNMENT);
		// today.setAlignmentY(CENTER_ALIGNMENT);
		selected = null;
	}

	/*
	 * Create the empty calendar panel.
	 */
	private void createPanel() {
		this.setLayout(new BorderLayout());

		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(1, 0, 0, 0));

		SpinnerModel monthModel = new CyclingSpinnerListModel(getMonthStrings());
		monthSelector = new JSpinner(monthModel);

		int currentYear = calendar.get(Calendar.YEAR);
		SpinnerModel yearModel = new SpinnerNumberModel(currentYear,
				currentYear - 100, //
				currentYear + 100, //
				1);

		// Link the models
		((CyclingSpinnerListModel) monthModel).setLinkedModel(yearModel);

		yearSelector = new JSpinner(yearModel);
		yearSelector.setEditor(new JSpinner.NumberEditor(yearSelector, "#"));
		JTextField ftf = ((JSpinner.NumberEditor) yearSelector.getEditor())
				.getTextField();
		// ftf.setEditable(false);
		GuiUtility.changeFontStyle(ftf, Font.PLAIN);

		controls.add(monthSelector);
		controls.add(yearSelector);

		canvas = new JPanel();
		canvas.setFocusable(true);
		canvas.setLayout(new GridLayout(7, 8, 0, 0));

		// Listeners
		KeyListener keyListener = new ThisKeyListener();
		canvas.addKeyListener(keyListener);
		ChangeListener spinnerListener = new ThisChangeListener();
		// Canvas listeners
		monthSelector.addChangeListener(spinnerListener);
		yearSelector.addChangeListener(spinnerListener);
		mouseListener = new ThisMouseListener();
		// Make space
		DayLabel header[] = new DayLabel[8];
		header[0] = new DayLabel(" ");
		GuiUtility.changeFontSize(header[0], 0.9F);
		GuiUtility.changeFontStyle(header[0], Font.PLAIN);
		header[0].setOpaque(true);
		header[0].setHorizontalAlignment(SwingConstants.CENTER);
		header[0].index = 0;
		header[0].day_of_week = 0;
		formatWeekDay(header[0]);
		canvas.add(header[0]);

		// Make day names
		String dayNames[] = getDayShortStrings();
		for (int col = 1; col < 8; col++) {
			// The days array has the slot 0 empty - so the first is 1!)
			int day = (col - 1 + calendar.getFirstDayOfWeek())
					% dayNames.length == 0 ? 1 : (col - 1 + calendar
					.getFirstDayOfWeek())
					% dayNames.length;
			header[col] = new DayLabel(dayNames[day]);
			header[col].setOpaque(true);
			header[col].setHorizontalAlignment(SwingConstants.CENTER);
			GuiUtility.changeFontSize(header[col], 0.9F);
			GuiUtility.changeFontStyle(header[col], Font.BOLD);
			header[col].index = col;
			header[col].day_of_week = day;
			formatWeekDay(header[col]);
			canvas.add(header[col]);
		}

		// Make day numbers and week numbers
		days = new DayLabel[42];
		weeks = new DayLabel[6];
		int n = 0;
		int w = 0;
		for (int row = 1; row < 7; row++) {
			for (int col = 0; col < 8; col++) {
				DayLabel box = new DayLabel("");
				box.setOpaque(true);
				box.setHorizontalAlignment(SwingConstants.CENTER);
				GuiUtility.changeFontSize(box, 0.9F);
				// Days
				if (row > 0 && col > 0 && col < 8) {
					GuiUtility.changeFontStyle(box, Font.PLAIN);
					box.addMouseListener(mouseListener);
					box.index = n;
					box.day_of_week = header[n % 7 + 1].day_of_week;
					days[n++] = box;
				}
				// Weeks
				else if (col == 0) {
					GuiUtility.changeFontStyle(box, Font.ITALIC);
					weeks[w++] = box;
					box.index = n;
					box.day_of_week = 0;
					formatWeekNumber(box);
				}
				canvas.add(box);
			}
		}
		this.add(controls, BorderLayout.NORTH);
		this.add(canvas, BorderLayout.CENTER);
		today = new DayLabel("");
		formatWeekNumber(today);
		today.setCursor(new Cursor(Cursor.HAND_CURSOR));
		today.addMouseListener(mouseListener);
		this.add(today, BorderLayout.SOUTH);
	}

	/*
	 * Give a format to the weeks column.
	 * 
	 * @param l the day label.
	 */
	private void formatWeekNumber(DayLabel l) {
		l.setBackground(STANDARD_BACKGROUND);
	}

	/*
	 * Give a format to the days.
	 * 
	 * @param l the day label.
	 */
	private void formatDayOfMonth(DayLabel l) {
		l.setBorder(DAY_BORDER);
		if (l.day_of_week == Calendar.SUNDAY
				|| l.day_of_week == Calendar.SATURDAY) {
			l.setBackground(FESTIVE_BACKGROUND);
			if (l.enabled) {
				l.setForeground(FESTIVE_FOREGROUND);
			} else {
				l.setForeground(DISABLED_FOREGROUND);
			}
		} else {
			if (l.enabled) {
				l.setBackground(STANDARD_BACKGROUND);
				l.setForeground(STANDARD_FOREGROUND);
			} else {
				l.setForeground(DISABLED_FOREGROUND);
				l.setBackground(STANDARD_BACKGROUND);
			}
		}
	}

	/*
	 * Give a format to the days of a week.
	 * 
	 * @param l the day label.
	 */
	private void formatWeekDay(DayLabel l) {
		l.setBackground(WEEK_BACKGROUND);
		l.setForeground(WEEK_FOREGROUND);
	}

	/**
	 * Get the selected date.
	 * 
	 * @return the selected date.
	 */
	public Date getDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, selected.year);
		c.set(Calendar.MONTH, selected.month);
		c.set(Calendar.DAY_OF_MONTH, selected.day);
		date = c.getTime();
		return date;
	}

	/**
	 * Set the date.
	 * 
	 * @return the selected date.
	 */
	public void setDate(Date d) {
		// Set Today
		init(d);
		// Update selectors
		yearSelector.getModel().setValue(calendar.get(Calendar.YEAR));
		monthSelector.getModel().setValue(
				getMonthStrings()[calendar.get(Calendar.MONTH)]);
		// Update the view
		updateCalendar();
	}

	/**
	 * Get an array of the day short names.
	 * 
	 * @return an array of the day short names in the current locale.
	 */
	static protected String[] getDayShortStrings() {
		String[] days = new java.text.DateFormatSymbols().getShortWeekdays();
		int lastIndex = days.length - 1;
		// for (int i = 0; i < days.length; i++) {
		// // System.out.println("i = " + i + " " + days[i]);
		// }
		if (days[lastIndex] == null || days[lastIndex].length() <= 0) {
			String[] monthStrings = new String[lastIndex];
			System.arraycopy(days, 0, monthStrings, 0, lastIndex);
			return monthStrings;
		} else {
			return days;
		}
	}

	/**
	 * Get an array of the day short names.
	 * 
	 * @return an array of the day short names in the current locale.
	 */
	static protected String[] getDayStrings() {
		String[] days = new java.text.DateFormatSymbols().getWeekdays();
		int lastIndex = days.length - 1;
		// for (int i = 0; i < days.length; i++) {
		// // System.out.println("i = " + i + " " + days[i]);
		// }
		if (days[lastIndex] == null || days[lastIndex].length() <= 0) {
			String[] monthStrings = new String[lastIndex];
			System.arraycopy(days, 0, monthStrings, 0, lastIndex);
			return monthStrings;
		} else {
			return days;
		}
	}

	/**
	 * Get an array of the months name.
	 * 
	 * @return an array of the months in the current locale.
	 */
	static protected String[] getMonthStrings() {
		String[] months = new java.text.DateFormatSymbols().getMonths();
		int lastIndex = months.length - 1;
		if (months[lastIndex] == null || months[lastIndex].length() <= 0) {
			String[] monthStrings = new String[lastIndex];
			System.arraycopy(months, 0, monthStrings, 0, lastIndex);
			return monthStrings;
		} else {
			return months;
		}
	}

	/*
	 * Change the month selector to the next one.
	 */
	private void nextMonth() {
		monthSelector.getModel().setValue(
				monthSelector.getModel().getNextValue());
	}

	/*
	 * Change the month selector to the previous one.
	 */
	private void previousMonth() {
		monthSelector.getModel().setValue(
				monthSelector.getModel().getPreviousValue());
	}

	/*
	 * Select a day.
	 * 
	 * @param l the day label.
	 */
	private void selectDay(DayLabel source) {
		// if (selected != source) {
		if (selected != null) {
			formatDayOfMonth(selected);
		}
		this.selected = source;

		// today = Integer.parseInt(selected.getText());
		source.setForeground(STANDARD_BACKGROUND);
		source.setBackground(STANDARD_FOREGROUND);

		// }
	}

	/**
	 * Redraw the calendar.
	 */
	public void updateCalendar() {
		GregorianCalendar c = (GregorianCalendar) calendar.clone();
		// First day
		c.set(Calendar.DAY_OF_MONTH, 1);
		// Current month
		int first_month = c.get(Calendar.MONTH);
		// Week number
		int week_of_year = c.getActualMaximum(Calendar.WEEK_OF_YEAR);

		// Make day numbers
		int first_day = c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK);
		if (first_day < 0) {
			first_day = -first_day;
		} else {
			first_day = 7 - first_day;
		}

		// Days
		c.add(Calendar.DAY_OF_MONTH, -first_day);
		int first_week = c.get(Calendar.WEEK_OF_YEAR);

		// The first working day
		for (int col = 0; col < days.length; col++) {
			days[col].setText("" + c.get(Calendar.DAY_OF_MONTH));
			days[col].enabled = c.get(Calendar.MONTH) == first_month;
			// Calendar date
			days[col].day = c.get(Calendar.DAY_OF_MONTH);
			days[col].month = c.get(Calendar.MONTH);
			days[col].year = c.get(Calendar.YEAR);

			formatDayOfMonth(days[col]);

			if (calendar.get(Calendar.DAY_OF_MONTH) == days[col].day //
					&& calendar.get(Calendar.MONTH) == days[col].month //
					&& calendar.get(Calendar.YEAR) == days[col].year) {
				selectDay(days[col]);
			}
			c.add(Calendar.DAY_OF_MONTH, 1);
		}

		for (int i = 0; i < weeks.length; i++) {
			weeks[i].setText(first_week + "");
			if (first_week >= week_of_year) {
				first_week = 0;
			}
			first_week++;
		}
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame("Calendar");
				CalendarPanel up = new CalendarPanel();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.getContentPane().setLayout(new BorderLayout());
				f.getContentPane().add(up, BorderLayout.CENTER);
				up.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
					}
				});
				f.pack();
				f.setVisible(true);
			}
		});
	}

	/**
	 * The day of month or the day of week and the week of year box.
	 */
	private class DayLabel extends JLabel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DayLabel(String string) {
			super(string);
		}

		/**
		 * The day.
		 */
		int day_of_week;
		/**
		 * The day.
		 */
		int day;
		/**
		 * The month.
		 */
		int month;
		/**
		 * The year.
		 */
		int year;
		/**
		 * The index in its array.
		 */
		int index;
		/**
		 * Enabled.
		 */
		boolean enabled;
		/**
		 * Date.
		 */
		Date date;

	}

	private class ThisMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent event) {
			DayLabel day = null;
			if (event.getSource() instanceof DayLabel) {
				day = (DayLabel) event.getSource();
				if (day.equals(today)) {
					// System.out.println(this.getClass().getName() +
					// " day is today ");
					setDate(today.date);
				} else {
					// System.out.println(this.getClass().getName() +
					// " day is not today ");
					selectDay(day);
					canvas.requestFocus();
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent event) {

		}

		@Override
		public void mouseExited(MouseEvent event) {
		}

		@Override
		public void mousePressed(MouseEvent event) {
		}

		@Override
		public void mouseReleased(MouseEvent event) {
		}
	}

	private class ThisKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent event) {
			switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (selected != null) {
					if (selected.index > 0) {
						selectDay(days[selected.index - 1]);
					} else {
						// previous month
						previousMonth();
						selectDay(days[days.length - 1]);
					}
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (selected != null) {
					if (selected.index + 1 < days.length) {
						selectDay(days[selected.index + 1]);
					} else {
						// next month
						nextMonth();
						selectDay(days[0]);
					}
				}
				break;
			case KeyEvent.VK_UP:
				if (selected != null) {
					if (selected.index - 6 > 0) {
						selectDay(days[selected.index - 7]);
					} else {
						// previous month
						previousMonth();
						selectDay(days[selected.index + days.length - 7]);
					}
				}
				break;
			case KeyEvent.VK_DOWN:
				if (selected != null) {
					if (selected.index + 7 < days.length) {
						selectDay(days[selected.index + 7]);
					} else {
						// next month
						nextMonth();
						selectDay(days[selected.index - days.length + 7]);
					}
				}
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent event) {
		}

		@Override
		public void keyTyped(KeyEvent event) {
		}
	}

	private class ThisChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent event) {
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			// System.out.println("before update selectors: Now.year= " + year
			// + " now.month= " + month);
			if (event.getSource().equals(yearSelector)) {
				year = Integer.parseInt(yearSelector.getModel().getValue()
						.toString());
				// selectDay(source);
			} else if (event.getSource().equals(monthSelector)) {
				String months[] = getMonthStrings();
				for (int m = 0; m < months.length; m++) {
					if (months[m].equalsIgnoreCase(monthSelector.getModel()
							.getValue().toString())) {
						month = m;
						break;
					}
				}
			}

			if (year != calendar.get(Calendar.YEAR)
					|| month != calendar.get(Calendar.MONTH)) {
				// System.out.println("after update selectors: Now.year= " +
				// year
				// + " now.month= " + month);
				calendar.set(Calendar.MONTH, month);
				calendar.set(Calendar.YEAR, year);
				updateCalendar();
			}
		}
	}

	private class CyclingSpinnerListModel extends SpinnerListModel {

		private static final long serialVersionUID = 1L;
		Object firstValue, lastValue;
		SpinnerModel linkedModel = null;

		public CyclingSpinnerListModel(Object[] values) {
			super(values);
			firstValue = values[0];
			lastValue = values[values.length - 1];
		}

		public void setLinkedModel(SpinnerModel linkedModel) {
			this.linkedModel = linkedModel;
		}

		@Override
		public Object getNextValue() {
			Object value = super.getNextValue();
			if (value == null) {
				value = firstValue;
				if (linkedModel != null) {
					linkedModel.setValue(linkedModel.getNextValue());
				}
			}
			return value;
		}

		@Override
		public Object getPreviousValue() {
			Object value = super.getPreviousValue();
			if (value == null) {
				value = lastValue;
				if (linkedModel != null) {
					linkedModel.setValue(linkedModel.getPreviousValue());
				}
			}
			return value;
		}
	}

}
