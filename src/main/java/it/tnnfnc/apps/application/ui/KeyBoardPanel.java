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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;


/**
 * A keyboard for user input.
 * 
 * @author Franco Toninato
 * 
 */
public class KeyBoardPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2816525979512131879L;

	// private Locale locale;
	private ListResourceBundle localization;

	private KeyboardKey row_a[] = new KeyboardKey[13];
	private KeyboardKey row_b[] = new KeyboardKey[12];
	private KeyboardKey row_c[] = new KeyboardKey[12];
	private KeyboardKey row_d[] = new KeyboardKey[11];
	private KeyboardKey modifiers[] = new KeyboardKey[11];

	private KeyboardKey tab;
	private KeyboardKey lock;
	private KeyboardKey l_Shift;
	private KeyboardKey r_Shift;
	private KeyboardKey enter;
	private KeyboardKey space;
	private KeyboardKey back;
	private KeyboardKey l_Ctrl;
	private KeyboardKey r_Ctrl;
	private KeyboardKey alt;
	private KeyboardKey altGr;

	// --O-- //
	private static final int OFF = 0;
	private static final int SHIFT = 1;
	private static final int ALTGR = 2;
	private static final int CTRL = 3;
	private static final int ALT = 4;
	private int keyModifier = OFF;
	// --O-- //
	protected Dimension size = new Dimension(35, 30);

	static final FlowLayout f = new FlowLayout(0, 0, 0);

	/**
	 * Creates a keyboard with the locale key set.
	 * 
	 * @param l
	 *            the locale.
	 */
	public KeyBoardPanel(Locale l) {
		// this.locale = l;
		localization = (ListResourceBundle) ResourceBundle.getBundle(
				KeyBoardResources.class.getName(), l);

		setLayout(new BorderLayout());
		
		initModifiers();
		initKeys();
		localize();

		JPanel a = new JPanel(f);
		a.add(addKeyToKeyboard(row_a));
		a.add(back);

		JPanel b = new JPanel(f);
		b.add(tab);
		b.add(addKeyToKeyboard(row_b));

		JPanel c = new JPanel(f);
		c.add(lock);
		c.add(addKeyToKeyboard(row_c));

		JPanel bc = new JPanel(f);
		JPanel _bc1 = new JPanel(new GridLayout(2, 0, 0, 0));
		_bc1.add(b);
		_bc1.add(c);
		bc.add(_bc1);
		bc.add(enter);

		JPanel d = new JPanel(f);
		d.add(l_Shift);
		d.add(addKeyToKeyboard(row_d));
		d.add(r_Shift);

		JPanel e = new JPanel(f);
		e.add(l_Ctrl);
		e.add(alt);
		e.add(space);
		e.add(altGr);
		e.add(r_Ctrl);

		// Keyboard
		JPanel container = new JPanel();
		container.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);
		
		gbc.insets.set(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0; 
		gbc.weighty = 1.0; 
		
		container.add(a, gbc);
		GridBagLayoutUtility.down(gbc);
		container.add(bc, gbc);
		GridBagLayoutUtility.down(gbc);
		container.add(d, gbc);
		GridBagLayoutUtility.down(gbc);
		container.add(e, gbc);

		this.add(container, BorderLayout.CENTER);

		addKeyListener(modifiers, new KeyModifierListener());

		setActiveKeys();
		activateKeys(modifiers);
	}

	/**
	 * Add a listener to the keyboard keys.
	 * 
	 * @param l
	 *            the listener.
	 */
	public void addActionKeyListener(ActionListener l) {
		addKeyListener(row_a, l);
		addKeyListener(row_b, l);
		addKeyListener(row_c, l);
		addKeyListener(row_d, l);
		addKeyListener(modifiers, l);
	}

	/**
	 * Remove a listener from the keyboard keys.
	 * 
	 * @param l
	 *            the listener.
	 */
	public void removeKeyListener(ActionListener l) {
		removeKeyListener(row_a, l);
		removeKeyListener(row_b, l);
		removeKeyListener(row_c, l);
		removeKeyListener(row_d, l);
		// removeKeyListener(modifiers, l);
	}

	/**
	 * Change the key size.
	 * 
	 * @param d
	 *            the size.
	 */
	public void setKeySize(Dimension d) {
		size = d;
		setPreferredSize(row_a);
		setPreferredSize(row_b);
		setPreferredSize(row_c);
		setPreferredSize(row_d);
		setModifiersPreferredSize();
		// Invalidate the keyboard container
		invalidate();
		
	}

	/**
	 * Return the key size.
	 * 
	 * @return the size.
	 */
	public Dimension getKeySize() {
		return this.size;
	}

	/**
	 * Change the keyboard locale.
	 * 
	 * @param l
	 *            the locale.
	 */
	public void setKeyboardLocale(Locale l) {
		try {
			localization = (ListResourceBundle) ResourceBundle.getBundle(
					KeyBoardResources.class.getName() + "_" + l, l);
		} catch (MissingResourceException e) {
			localization = (ListResourceBundle) ResourceBundle
					.getBundle(KeyBoardResources.class.getName());
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				localize();
				setActiveKeys();
			}
		});
	}

	private void initModifiers() {
		tab = new KeyboardKey("Tab");
		tab.setName("tab");
		lock = new KeyboardKey("Lock");
		lock.setName("lock");
		l_Shift = new KeyboardKey("Shift");
		l_Shift.setName("l_Shift");
		r_Shift = new KeyboardKey("Shift");
		r_Shift.setName("r_Shift");
		enter = new KeyboardKey("Enter");
		enter.setName("enter");
		space = new KeyboardKey("space");
		space.setName("space");
		back = new KeyboardKey("Back");
		back.setName("back");
		// back.setEnabled(false);
		l_Ctrl = new KeyboardKey("Ctrl");
		l_Ctrl.setName("l_Ctrl");
		r_Ctrl = new KeyboardKey("Ctrl");
		r_Ctrl.setName("r_Ctrl");
		alt = new KeyboardKey("alt");
		alt.setName("alt");
		altGr = new KeyboardKey("altGr");
		altGr.setName("altGr");

		modifiers = new KeyboardKey[] { tab, lock, l_Shift, r_Shift, enter,
				space, back, l_Ctrl, r_Ctrl, alt, altGr, };
		setModifiersPreferredSize();
	}

	private void initKeys() {
		for (int i = 0; i < row_a.length; i++) {
			row_a[i] = new KeyboardKey();
			row_a[i].setName("1" + i);
			row_a[i].setPreferredSize(size);
			// row_a[i].setValue("#" + i, "n" + i, null, null);
		}
		for (int i = 0; i < row_b.length; i++) {
			row_b[i] = new KeyboardKey();
			row_b[i].setName("2" + i);
			row_b[i].setPreferredSize(size);
			// row_b[i].setValue("k" + i, "K" + i, null, "a" + i);
		}
		for (int i = 0; i < row_c.length; i++) {
			row_c[i] = new KeyboardKey();
			row_c[i].setName("3" + i);
			row_c[i].setPreferredSize(size);
			// row_c[i].setValue("k" + i, "K" + i, null, "a" + i);
		}
		for (int i = 0; i < row_d.length; i++) {
			row_d[i] = new KeyboardKey();
			row_d[i].setName("4" + i);
			row_d[i].setPreferredSize(size);
			// row_d[i].setValue("k" + i, "K" + i, null, "a" + i);
		}
	}

	private void localize() {
		try {
			for (KeyboardKey k : row_a) {
				k.setValue(localization.getStringArray(k.getName()));
			}
			for (KeyboardKey k : row_b) {
				k.setValue(localization.getStringArray(k.getName()));
			}
			for (KeyboardKey k : row_c) {
				k.setValue(localization.getStringArray(k.getName()));
			}
			for (KeyboardKey k : row_d) {
				k.setValue(localization.getStringArray(k.getName()));
			}
			for (KeyboardKey k : modifiers) {
				k.setValue(localization.getStringArray(k.getName()));
			}
		} catch (MissingResourceException e) {
		}
	}

	/*
	 * Add a listener.
	 */
	private void addKeyListener(KeyboardKey[] keys, ActionListener l) {
		for (KeyboardKey keyboardKey : keys) {
			keyboardKey.addActionListener(l);
		}
	}

	/*
	 * Set the active text according to the modifier.
	 */
	private void activateKeys(KeyboardKey[] keys) {
		for (KeyboardKey keyboardKey : keys) {
			keyboardKey.setActiveText(keyModifier);
		}
	}

	/*
	 * Add a key component to the keyboard component.
	 */
	private JPanel addKeyToKeyboard(KeyboardKey[] keys) {
		JPanel p = new JPanel(new GridLayout(1, keys.length, 0, 0));
		for (KeyboardKey k : keys) {
			p.add(k);
		}
		return p;
	}

	/*
	 * Remove a listener.
	 */
	private void removeKeyListener(KeyboardKey[] keys, ActionListener l) {
		for (KeyboardKey keyboardKey : keys) {
			keyboardKey.removeActionListener(l);
		}
	}

	private void setModifiersPreferredSize() {
		l_Shift.setPreferredSize(new Dimension(size.width, size.height));
		l_Ctrl.setPreferredSize(new Dimension(2 * size.width, size.height));
		alt.setPreferredSize(new Dimension(size.width, size.height));
		space.setPreferredSize(new Dimension(9 * size.width, size.height));
		altGr.setPreferredSize(new Dimension(size.width, size.height));
		r_Ctrl.setPreferredSize(new Dimension(2 * size.width, size.height));

		r_Shift.setPreferredSize(new Dimension(3 * size.width, size.height));
		enter.setPreferredSize(new Dimension(size.width, 2 * size.height));
		lock.setPreferredSize(new Dimension(2 * size.width, size.height));
		back.setPreferredSize(new Dimension(2 * size.width, size.height));
		tab.setPreferredSize(new Dimension(2 * size.width, size.height));
		// Invalidate the key components
		for (KeyboardKey k : modifiers) {
			k.invalidate();
		}
	}

	private void setPreferredSize(KeyboardKey[] keys) {
		for (KeyboardKey k : keys) {
			k.setPreferredSize(size);
			// Invalidate the key component
			k.invalidate();
		}
	}

	private void setActiveKeys() {
		activateKeys(row_a);
		activateKeys(row_b);
		activateKeys(row_c);
		activateKeys(row_d);
	}

	private class KeyModifierListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource().equals(enter)) {
			} else if (event.getSource().equals(tab)) {
			} else if (event.getSource().equals(lock)) {
				keyModifier = keyModifier == SHIFT ? OFF : SHIFT;
			} else if (event.getSource().equals(l_Ctrl)) {
				keyModifier = keyModifier == CTRL ? OFF : CTRL;
			} else if (event.getSource().equals(r_Ctrl)) {
				keyModifier = keyModifier == CTRL ? OFF : CTRL;
			} else if (event.getSource().equals(space)) {
			} else if (event.getSource().equals(altGr)) {
				keyModifier = keyModifier == ALTGR ? OFF : ALTGR;
			} else if (event.getSource().equals(alt)) {
				keyModifier = keyModifier == ALT ? OFF : ALT;
			} else if (event.getSource().equals(back)) {
			} else if (event.getSource().equals(l_Shift)) {
				keyModifier = keyModifier == SHIFT ? OFF : SHIFT;
			} else if (event.getSource().equals(r_Shift)) {
				keyModifier = keyModifier == SHIFT ? OFF : SHIFT;
			}
			setActiveKeys();
		}
	}
	/**
	 * A key of in a keyboard. Different characters can be stored and activated
	 * according to the key modifier currently selected.
	 * 
	 * @author Franco Toninato
	 * 
	 */
	public static class KeyboardKey extends JButton {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4237648630680883567L;

		private String labels[] = new String[] { //
		new String(), new String(), new String(), new String() //
		};

		/**
		 * Class constructor. The default key stores up to four different values
		 * 
		 * @param text
		 *            the key values stored. This can extends the number of stored
		 *            keys over the default.
		 */
		public KeyboardKey(String... text) {
			this();
			if (text.length > labels.length)
				labels = new String[text.length];
			setValue(text);
		}

		/**
		 * Class constructor. The default key stores up to four different
		 * characters.
		 */
		public KeyboardKey() {
			super();
			super.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
			setFocusable(false);
			GuiUtility.changeFontStyle(this, Font.PLAIN);
		}

		/**
		 * Set the key values.
		 * 
		 * @param text
		 *            the values.
		 */
		public void setValue(String... text) {
			for (int i = 0; i < text.length; i++) {
				labels[i] = text[i];
			}
		}

		/**
		 * Set the key preferred size. Also the key label changes its font size
		 * accordingly.
		 * 
		 * @param d
		 *            the new key dimension.
		 */
		@Override
		public void setPreferredSize(Dimension d) {
			// Dimension old_size = super.getPreferredSize();
			if (getPreferredSize().width > d.width) {
				// Less than
				GuiUtility.changeFontSize(this, 0.9F);
			} else {
				// Greater than
				GuiUtility.changeFontSize(this, 1.1F);
			}
			super.setPreferredSize(d);
		}

		/**
		 * Set the active text for this key.
		 * 
		 * @param m
		 *            the modifier value.
		 */
		public void setActiveText(int m) {
			if (m < labels.length) {
				this.setText(labels[m]);
				this.repaint();
			}
		}

		/**
		 * Get the key values.
		 * 
		 * @return the key values.
		 */
		public String[] getKeyValue() {
			String ret[] = new String[labels.length];
			for (int i = 0; i < labels.length; i++) {
				ret[i] = labels[i];
			}
			return ret;
		}

	}
}