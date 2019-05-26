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

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import it.tnnfnc.apps.application.ui.style.ComponentStyle;
import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.ui.style.StyleFormatter;
import it.tnnfnc.apps.application.ui.style.StyleObject;

/**
 * This class implements a user interface component for rendering the tab in a
 * tab panel. An edit dialog is called with a click on the icon.
 * 
 * @author franco toninato
 * 
 */
public class TabComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JLabel iconlabel;
	private StyleFormatter formatter = new StyleFormatter();
	// private JButton editButton;
	private JToggleButton editButton;
	private JTabbedPane pane;
	private TabPanelEditor editTabPanel;
	private ListResourceBundle language = (ListResourceBundle) ResourceBundle.getBundle(LocaleBundle.class.getName());

	/**
	 * Constructor.
	 * 
	 * @param tabbedPane
	 *            the tabbed pane.
	 * @param title
	 *            title.
	 * @param icon
	 *            icon.
	 * @param tip
	 *            tip.
	 * @param index
	 *            index.
	 */
	public TabComponent(final JTabbedPane tabbedPane, I_StyleObject title, Icon icon, String tip, int index) {

		// setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(new EmptyBorder(2, 0, 0, 0));
		setOpaque(false);

		super.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		pane = tabbedPane;
		editTabPanel = new TabPanelEditor();

		label = new JLabel();
		label.setOpaque(false);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
		label.setText(title.getValue() + "");
		formatter.applyStyle(label, title.getStyle(), false, false);
		formatter.applyStyle(tabbedPane, title.getStyle(), false, false);

		iconlabel = new JLabel();
		iconlabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
		iconlabel.setOpaque(false);
		iconlabel.setBorder(null);
		iconlabel.setIcon(icon);

		editButton = new EditTabButton();

		add(label);
		add(iconlabel);
		add(editButton);
	}

	/**
	 * Constructor.
	 * 
	 * @param tabbedPane
	 *            the tabbed pane.
	 * @param title
	 *            title.
	 * @param icon
	 *            icon.
	 * @param tip
	 *            tip.
	 * @param index
	 *            index.
	 */
	public TabComponent(final JTabbedPane tabbedPane, String title, Icon icon, String tip, int index,
			TabPanelEditor edit) {
		this(tabbedPane, new StyleObject(title), icon, tip, index);
		// this(tabbedPane, tip, icon, tip, index);
		this.editTabPanel = edit;
	}

	public TabComponent(TabbedPane tabbedPane, I_StyleObject title, Icon icon, String tip, int index,
			TabPanelEditor edit) {
		this(tabbedPane, title, icon, tip, index);
		this.editTabPanel = edit;
	}

	/**
	 * @param info
	 */
	private void updateTabComponent() {
		int i = pane.indexOfTabComponent(TabComponent.this);
		if (i != -1) {
			label.setText(pane.getTitleAt(i));
		}
	}

	/**
	 * Update this tab from a new style.
	 * 
	 * @param the
	 *            style
	 */
	public void updateTabComponent(I_StyleObject o) {
		label.setText(o.getValue() + "");
		formatter.applyStyle(label, o.getStyle(), false, false);
		pane.setBackgroundAt(pane.indexOfTabComponent(TabComponent.this),
				ComponentStyle.getBackgroundColor(o.getStyle()));
	}

	/**
	 * Return the tab style.
	 * 
	 * @return the tab style.
	 */
	public I_StyleObject getTabStyle() {
		return new StyleObject(label.getText(), ComponentStyle.getComponentStyle(label));
	}

	@SuppressWarnings("serial")
	// private class ActionTabButton extends JButton implements
	private class EditTabButton extends JToggleButton implements ActionListener {
		public EditTabButton() {
			int size = 15;
			setPreferredSize(new Dimension(size, size));
			setToolTipText(language.getString("Edit tab"));
			// Make the button looks the same for all Laf's
			setUI(new BasicButtonUI());
			setContentAreaFilled(false);
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			addActionListener(this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent )
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int i = pane.indexOfTabComponent(TabComponent.this);
			if ((pane.getSelectedIndex() == i) && isSelected() && i > -1) {
				editTabPanel.showEditDialog(pane, i);
				updateTabComponent();
				setSelected(false);
				revalidate();
			} else if ((pane.getSelectedIndex() != i && i > -1)) {
				setSelected(false);
				pane.setSelectedIndex(i);
			} else {
			}
		}

		// No update UI for this button
		@Override
		public void updateUI() {
		}

		// Draws on/off icons
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			double margin = 1;
			double hspace = getPreferredSize().getHeight();
			double wspace = getPreferredSize().getWidth();
			double linespace = hspace / 4;
			if (isSelected()) {

				g2.setStroke(new BasicStroke(1));
				g2.setColor(UIManager.getColor("Button.disabledText")); // Button.foreground
				if (getModel().isRollover()) {
					g2.setColor(UIManager.getColor("Button.focus"));
				}
				Ellipse2D.Double shape = new Ellipse2D.Double(hspace / 2 - linespace - margin,
						wspace / 2 - linespace - margin, 2 * linespace, 2 * linespace);
				g2.fill(shape);
				g2.draw(shape);

			} else {

				g2.setStroke(new BasicStroke(1));
				g2.setColor(UIManager.getColor("Button.disabledText"));
				if (getModel().isRollover()) {
					g2.setColor(UIManager.getColor("Button.focus"));
				}
				Ellipse2D.Double shape = new Ellipse2D.Double(hspace / 2 - linespace - margin,
						wspace / 2 - linespace - margin, 2 * linespace, 2 * linespace);
				g2.draw(shape);

			}
			g2.dispose();
		}
	}

	private final static MouseListener buttonMouseListener = new MouseAdapter() {
		@Override
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};
}
