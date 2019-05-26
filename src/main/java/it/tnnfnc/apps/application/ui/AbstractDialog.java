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
package it.tnnfnc.apps.application.ui; //Package

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Dialog supporting the basic user dialog with YES - NO - CANCEL options.
 * Subclasses must add option buttons calling
 * {@link #enableOption(Options, String)} method, the further behaviour is added
 * with {@link #performOK()}, {@link #performNO()}, {@link #performClose()}.
 * More buttons can be added with {@link #addButton(JButton, String)}.
 * 
 * @author Franco Toninato
 */
public abstract class AbstractDialog extends JDialog implements ActionListener,
		WindowListener {

	protected ListResourceBundle language = (ListResourceBundle) ResourceBundle
			.getBundle(LocaleBundle.class.getName());

	/**
	 * Standard dialog options.
	 * 
	 * @author Franco Toninato
	 * 
	 */
	public static enum Options {
		OK_OPTION, NO_OPTION, CLOSE_OPTION,
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The <code>JPanel</code> component where user fields are placed.
	 */
	protected JPanel inputPanel = new JPanel();
	/**
	 * The <code>JPanel</code> component where user buttons are placed.
	 */
	protected JPanel buttonPanel = new JPanel();
	/**
	 * Return option internal field.
	 */
	protected Options returnOption = null;
	/**
	 * OK-option button reference.
	 */
	protected JButton okButton = null;
	/**
	 * NO-option button reference.
	 */
	protected JButton noButton = null;
	/**
	 * Close-option button reference.
	 */
	protected JButton closeButton = null;

	public AbstractDialog() {
		super();
		setModalityType(DEFAULT_MODALITY_TYPE);
		addWindowListener(this);
		JComponent dialogPanel = GuiUtility.createInputPanel(inputPanel, buttonPanel);
		setContentPane(dialogPanel);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	/* Closes the dialog with an option */
	private void close(Options option) {
		returnOption = option;
		setVisible(false);
		dispose();
	}

	/**
	 * Perform action when the OK button is pressed and exit. The default
	 * implementation returns true.
	 * 
	 * @return true when the action completes and the dialog can exit, otherwise
	 *         returns false the dialog stay visible.
	 */
	protected boolean performOK() {
		return true;
	}

	/**
	 * Perform action when the NO button is pressed and exit. The default
	 * implementation returns true.
	 * 
	 * @return true when the action completes and the dialog can exit, otherwise
	 *         returns false the dialog stay visible.
	 */
	protected boolean performNO() {
		return true;
	}

	/**
	 * Perform action when the CANCEL button is pressed and exit. The default
	 * implementation returns true.
	 * 
	 * @return true when the action completes and the dialog can exit, otherwise
	 *         returns false the dialog stay visible.
	 */
	protected boolean performClose() {
		return true;
	}

	/**
	 * Perform the user action action defined in the implementing class.
	 * 
	 * @param actionCommand
	 *            the action command.
	 * @return true when the action completes and the dialog can exit, otherwise
	 *         returns false the dialog stay visible. The default is false.
	 */
	protected boolean actionPerformed(String actionCommand) {
		return false;
	}

	/**
	 * Adds a button to the buttons panel. This object is added as key listener
	 * and action listener.
	 * 
	 * @param button
	 *            the new button.
	 * @param actionName
	 *            the button action name.
	 */
	public void addButton(JButton button, String actionName) {
		button.setActionCommand(actionName);
		button.addActionListener(this);
		buttonPanel.add(button);
	}

	/**
	 * Displays only the needed buttons.
	 * 
	 * @param option
	 *            displays / hides the button.
	 * @param name
	 *            button name.
	 */
	public void enableOption(Options option, String name) {
		switch (option) {
		case OK_OPTION:
			addButton(okButton = new JButton(name), "" + Options.OK_OPTION);
			break;
		case NO_OPTION:
			addButton(noButton = new JButton(name), "" + Options.NO_OPTION);
			break;
		case CLOSE_OPTION:
			addButton(closeButton = new JButton(name), ""
					+ Options.CLOSE_OPTION);
		}
	}

	/**
	 * Shows the dialog located in the center of the screen. The default
	 * implementation returns true.
	 * 
	 * @return true when the dialog can be closed and the returned value set to
	 *         {@link #CLOSE_OPTION CANCEL_OPTION}).
	 */
	public Options showDialog() {
		if (!this.isVisible()) {
			this.pack();
			setVisible(true);
		}
		return returnOption;
	}

	/**
	 * Shows the dialog located relative to a prent component.
	 * 
	 * @param parent
	 *            the parent component of this dialog.
	 * @return the user's selected option.
	 */
	public Options showDialog(Component parent) {
		setLocationRelativeTo(parent);
		if (!this.isVisible()) {
			this.pack();
			setVisible(true);
		}
		return returnOption;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		// System.out.println("Abstract dialog - actionPerformed " + action);
		if (action.equals("" + Options.OK_OPTION) && performOK()) {
			close(Options.OK_OPTION);
		} else if (action.equals("" + Options.NO_OPTION) && performNO()) {
			close(Options.NO_OPTION);
		} else if (action.equals("" + Options.CLOSE_OPTION) && performClose()) {
			close(Options.CLOSE_OPTION);
		} else {
			if (actionPerformed(action)) {
				close(Options.OK_OPTION);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent
	 * )
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		if (performClose())
			close(Options.CLOSE_OPTION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent
	 * )
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	/*
	 * KeyboardKey binding.
	 */
	// /**
	// * @author Franco Toninato
	// *
	// */
	// private class ThisKeyAction extends AbstractAction {
	// /**
	// *
	// */
	// private static final long serialVersionUID = -3312118604906774876L;
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
	// * )
	// */
	// public void actionPerformed(ActionEvent e) {
	// switch (e.getActionCommand().charAt(0)) {
	// case KeyEvent.VK_ENTER:
	// if (yesButton.isEnabled() && performYes())
	// close(YES_OPTION);
	// break;
	// case KeyEvent.VK_ESCAPE:
	// if (cancButton.isEnabled() && performCancel())
	// close(CANCEL_OPTION);
	// break;
	// }
	// }
	// }
}