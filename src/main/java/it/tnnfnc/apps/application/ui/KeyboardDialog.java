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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import it.tnnfnc.apps.application.ui.KeyBoardPanel.KeyboardKey;

/**
 * This class is a dialog with a virtual keyboard.
 * 
 * @author Franco Toninato
 * 
 */
public class KeyboardDialog extends JDialog {

	// private ChangePasswordPanel loginPane_;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1912909618258011716L;
	/**
	 * The resource.
	 */
	protected ListResourceBundle language = (ListResourceBundle) ResourceBundle
			.getBundle(LocaleBundle.class.getName());
	/**
	 * The keyboard.
	 */
	protected KeyBoardPanel keyboard;

	private JComboBox<Object> localeSelector;
	private Locale[] locales;
	private JTextComponent textComponent;
	private JTextField hiddenText;

	/**
	 * Constructs a virtual keyboard for typing into a text component.
	 * 
	 */
	public KeyboardDialog() {
		setModal(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		localeSelector = new JComboBox<Object>();
		createGUI();
	}

	/**
	 * Constructs a virtual keyboard for typing into a text component.
	 * 
	 * @param textComponent
	 *            the text input component.
	 */
	public KeyboardDialog(JTextComponent textComponent) {
		this();
		this.textComponent = textComponent;
	}

	/**
	 * @param textComponent
	 */
	public void setEditedComponent(JTextComponent textComponent) {
		this.textComponent = textComponent;
	}

	/**
	 * Change the key dimension.
	 * 
	 * @param delta
	 *            the positive or negative spep.
	 */
	public void changeKeyboardSize(int delta) {
		Dimension d = new Dimension(keyboard.getKeySize().width + delta,
				keyboard.getKeySize().height + delta);
		keyboard.setKeySize(d);
		invalidate();
		repaint();
	}

	public JTextComponent getEditedComponent() {
		return textComponent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b) {
		hiddenText.setText("");
		super.setVisible(b);
	}

	private void createGUI() {
		JButton plus = new JButton(language.getString("Zoom in"));
		JButton minus = new JButton(language.getString("Zoom out"));
		plus.setActionCommand("bigger");
		minus.setActionCommand("smaller");
		ButtonListener buttonListener = new ButtonListener();
		plus.addActionListener(buttonListener);
		minus.addActionListener(buttonListener);

		keyboard = new KeyBoardPanel(Locale.getDefault());
		keyboard.addActionKeyListener(new KeyActionListener());

		hiddenText = new JTextField();
		hiddenText.setEditable(false);
		JCheckBox showText = new JCheckBox(language.getString("Show text"),
				true);
		showText.setActionCommand("show");
		showText.addActionListener(buttonListener);

		// Layout
		JPanel buttons = new JPanel(new GridLayout(0, 3));
		buttons.add(plus);
		buttons.add(minus);
		buttons.add(createLocalChooser());
//		buttons.add(showText);

		JPanel pageStart = new JPanel(new BorderLayout());
		pageStart.add(buttons, BorderLayout.PAGE_START);
		pageStart.add(hiddenText, BorderLayout.CENTER);
		pageStart.add(showText, BorderLayout.LINE_START);

		setLayout(new BorderLayout());
		add(keyboard, BorderLayout.CENTER);
		add(pageStart, BorderLayout.PAGE_START);
	}

	private JPanel createLocalChooser() {

		locales = new Locale[4];
		locales[0] = new Locale("de");
		locales[1] = new Locale("en");
		locales[2] = new Locale("fr");
		locales[3] = new Locale("it");

		// locales = Locale.getAvailableLocales();

		Object comboLocales[] = new Object[locales.length];
		String buffer = new String();
		int j = 0;
		for (int i = 0; i < comboLocales.length; i++) {
			buffer = locales[i].getDisplayName();
			comboLocales[j++] = buffer;
		}

		localeSelector = new JComboBox<Object>(comboLocales);
		localeSelector.addActionListener(new LocaleSelectorListener());
		// Set the locale selected
		Locale default_loc = Locale.getDefault();
		for (int i = 0; i < locales.length; i++) {
			if (locales[i].getISO3Language().equals(
					default_loc.getISO3Language())) {
				localeSelector.setSelectedIndex(i);
				break;
			}
		}
		//
		JPanel localChooser = new JPanel();
		localChooser.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);

		GridBagLayoutUtility.add(new JLabel(language.getString("Locale")),
				localeSelector, localChooser, gbc);
		return localChooser;
	}

	/**
	 * Write into the text component.
	 * 
	 * @param t
	 *            the text.
	 * @throws BadLocationException
	 *             when the writing process fails.
	 */
	private void writeOnText(final String t) throws BadLocationException {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// if (hiddenText.isEnabled()) {
				try {
					hiddenText.getDocument().insertString(
							hiddenText.getCaretPosition(), t, null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
			// }
		});
	}

	private void backOnText() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// if (hiddenText.isEnabled()) {
				try {
					int offs = hiddenText.getCaretPosition() - 1;
					offs = offs > -1 ? offs : 0;
					hiddenText.getDocument().remove(offs, 1);
				} catch (BadLocationException e) {
					// e.printStackTrace();
				}
			}
			// }
		});
	}

	protected void backOnTextComponent() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (textComponent != null) {
					try {
						int offs = textComponent.getCaretPosition() - 1;
						offs = offs > -1 ? offs : 0;
						textComponent.getDocument().remove(offs, 1);
					} catch (BadLocationException e) {
						// e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * Write into a text component.
	 * 
	 * @param t
	 *            the text.
	 * @throws BadLocationException
	 *             when the writing process fails.
	 */
	protected void writeOnTextComponent(final String t)
			throws BadLocationException {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (textComponent != null) {
					try {
						textComponent.getDocument().insertString(
								textComponent.getCaretPosition(), t, null);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private class LocaleSelectorListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			@SuppressWarnings("unchecked")
			JComboBox<Object> combo = (JComboBox<Object>) event.getSource();
			if (locales[combo.getSelectedIndex()] != null) {
				keyboard.setKeyboardLocale(locales[combo.getSelectedIndex()]);
			}
		}
	}

	/*
	 * InternalKeys listener.
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getActionCommand() == "bigger") {
				changeKeyboardSize(5);
				// adjust the dimension
				setVisible(false);
				pack();
				setVisible(true);

			} else if (event.getActionCommand() == "smaller") {
				changeKeyboardSize(-5);
				// adjust the dimension
				setVisible(false);
				pack();
				setVisible(true);
			} else if (event.getActionCommand() == "show") {
				JCheckBox cb = (JCheckBox) event.getSource();
				hiddenText.setVisible(cb.isSelected());
				// hiddenText.setEnabled(cb.isSelected());
			}
			// keyboard.invalidate();

		}
	}

	/*
	 * InternalKeys listener.
	 */
	private class KeyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			KeyboardKey keyboardKey = (KeyboardKey) event.getSource();
			String command = keyboardKey.getName();
			String character = null;
			if (command == "tab") {
				// character = new String(new char[] { 0x09 });
				character = new String("	");
			} else if (command == "lock") {
			} else if (command == "l_Shift") {
			} else if (command == "r_Shift") {
			} else if (command == "enter") {

			} else if (command == "space") {
				// character = new String(new char[] { 0x20 });
				character = new String(" ");
			} else if (command == "back") {
				backOnText();
				backOnTextComponent();
			} else if (command == "l_Ctrl") {
			} else if (command == "r_Ctrl") {
			} else if (command == "alt") {
			} else if (command == "altGr") {
			} else {
				character = keyboardKey.getText();
			}
			if (character != null) {
				try {
					writeOnText(character);
					writeOnTextComponent(character);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				KeyboardDialog osk = new KeyboardDialog();
				osk.pack();
				osk.setVisible(true);
			}
		});
	}

}
