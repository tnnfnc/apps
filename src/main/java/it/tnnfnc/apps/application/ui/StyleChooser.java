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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import it.tnnfnc.apps.application.ui.style.ComponentStyle;
import it.tnnfnc.apps.application.ui.style.I_StyleFormatter;
import it.tnnfnc.apps.application.ui.style.PredefinedStyles;
import it.tnnfnc.apps.application.ui.style.StyleFormatter;

public class StyleChooser extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3307150353169395277L;

	private JComboBox<String> fontFamily;
	private JComboBox<String> fontStyle;
	private JComboBox<String> fontSize;
	private JColorChooser colorChooser;
	private static JLabel preview;
	private I_StyleFormatter formatter;

	// Border width
	private JSpinner spinnerLeft = new JSpinner();
	private JSpinner spinnerTop = new JSpinner();
	private JSpinner spinnerRight = new JSpinner();
	private JSpinner spinnerBottom = new JSpinner();
	private ActionListener formatButtonListener;

	/**
	 * The text size.
	 */
	private int size;
	/**
	 * The text style.
	 */
	private int style;
	/**
	 * The text family name.
	 */
	private String family;
	/**
	 * The foreground color.
	 */
	private Color foregroundColor;
	/**
	 * The background color.
	 */
	private Color backgroundColor;
	/**
	 * The border color
	 */
	private Color borderColor;
	/**
	 * The borders thickness.
	 */
	private int borders[] = new int[4];

	private JTabbedPane styleTabs;

	private JPanel predefinedStylesPanel;

	//
	/**
	 * Create a style chooser. with a default formatter for the preview panel.
	 */
	public StyleChooser() {
		// The default formatter
		this(new StyleFormatter());
	}

	/**
	 * Create a style chooser.
	 * 
	 * @param f
	 *            the formatter for the preview panel.
	 */
	public StyleChooser(I_StyleFormatter f) {
		this.formatter = f;

		// Initialization
		size = UIManager.getFont("ComboBox.font").getSize();
		style = Font.PLAIN;

		// Preview
		preview = new JLabel("Preview");
		preview.setOpaque(true);
		preview.setHorizontalAlignment(SwingConstants.CENTER);
		preview.setVerticalAlignment(SwingConstants.CENTER);

		// Creates field
		fontFamily = new JComboBox<String>(getFontFamilyNames());
		fontFamily.setRenderer(new FontComboBoxRenderer());
		fontFamily.setSelectedItem(family = getFont().getFamily());
		fontFamily.setBackground(UIManager.getColor("List.background"));
		fontFamily.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				family = event.getItem().toString();
				preview.setText(family);
				updatePreview();
			}
		});

		fontStyle = new JComboBox<String>(getFontStyle());
		fontStyle.setBackground(UIManager.getColor("List.background"));
		fontStyle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				String s = event.getItem().toString();
				if (s.equalsIgnoreCase(("Bold"))) {
					style = Font.BOLD;
				} else if (s.equalsIgnoreCase(("Italic"))) {
					style = Font.ITALIC;
				} else if (s.equalsIgnoreCase(("Plain"))) {
					style = Font.PLAIN;
				}
				updatePreview();
			}
		});

		fontSize = new JComboBox<String>(getFontSize());
		fontSize.setSelectedItem(size + "");
		fontSize.setBackground(UIManager.getColor("List.background"));
		fontSize.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				size = Integer.parseInt(event.getItem().toString());
				updatePreview();
			}
		});

		foregroundColor = getForeground();
		backgroundColor = getBackground();
		borderColor = getForeground();

		final JRadioButton selForeground = new JRadioButton("Foreground");
		final JRadioButton selBackground = new JRadioButton("Background");
		final JRadioButton selBorder = new JRadioButton("Border color");

		// Set the color in the color chooser
		selForeground.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				colorChooser.setColor(foregroundColor);
			}
		});
		selBackground.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				colorChooser.setColor(backgroundColor);
			}
		});
		selBorder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				colorChooser.setColor(borderColor);
			}
		});

		colorChooser = new JColorChooser();
		colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[1]);
		colorChooser.setColor(foregroundColor);
		colorChooser.setPreviewPanel(new JPanel());
		colorChooser.getSelectionModel().addChangeListener(
				new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						if (selForeground.isSelected()) {
							foregroundColor = colorChooser.getColor();
						} else if (selBackground.isSelected()) {
							backgroundColor = colorChooser.getColor();
						} else if (selBorder.isSelected()) {
							borderColor = colorChooser.getColor();
						}
						updatePreview();
					}
				});

		// Group the radio buttons.
		selForeground.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(selForeground);
		group.add(selBackground);
		group.add(selBorder);

		// Text panel
		JPanel textControlPanel = new JPanel();
		JPanel familyPanel = new JPanel();
		JPanel stylePanel = new JPanel();
		JPanel sizePanel = new JPanel();
		familyPanel.setBorder(new TitledBorder("Text family"));
		stylePanel.setBorder(new TitledBorder("Style"));
		sizePanel.setBorder(new TitledBorder("Size"));

		familyPanel.add(fontFamily);
		stylePanel.add(fontStyle);
		sizePanel.add(fontSize);
		textControlPanel.add(familyPanel);
		textControlPanel.add(stylePanel);
		textControlPanel.add(sizePanel);

		JPanel colorSelectorPanel = new JPanel();
		colorSelectorPanel.setLayout(new GridLayout(3, 1));
		colorSelectorPanel.add(selForeground);
		colorSelectorPanel.add(selBackground);
		colorSelectorPanel.add(selBorder);

		JPanel previewPanel = new JPanel();
		previewPanel.setLayout(new BorderLayout());
		previewPanel.setBorder(new TitledBorder("Preview"));
		previewPanel.add(preview, BorderLayout.CENTER);
		previewPanel.setPreferredSize(new Dimension((int) colorChooser
				.getPreferredSize().getWidth(), 100));

		// Border panel
		Color c = UIManager.getColor("List.background");
		final JPanel leftBorderPanel = new JPanel();
		final JPanel topBorderPanel = new JPanel();
		final JPanel rightBorderPanel = new JPanel();
		final JPanel bottomBorderPanel = new JPanel();
		leftBorderPanel.setBackground(c);
		topBorderPanel.setBackground(c);
		rightBorderPanel.setBackground(c);
		bottomBorderPanel.setBackground(c);

		ChangeListener borderListener;
		spinnerLeft.addChangeListener(borderListener = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent event) {
				borders[1] = Integer
						.parseInt(spinnerLeft.getValue().toString());
				borders[0] = Integer.parseInt(spinnerTop.getValue().toString());
				borders[3] = Integer.parseInt(spinnerRight.getValue()
						.toString());
				borders[2] = Integer.parseInt(spinnerBottom.getValue()
						.toString());
				topBorderPanel.setBorder(new MatteBorder(
						borders[0] > 0 ? 1 : 0, 0, 0, 0, Color.black));
				leftBorderPanel.setBorder(new MatteBorder(borders[0] > 0 ? 1
						: 0, borders[1] > 0 ? 1 : 0, borders[2] > 0 ? 1 : 0, 0,
						Color.black));
				bottomBorderPanel.setBorder(new MatteBorder(0, 0,
						borders[2] > 0 ? 1 : 0, 0, Color.black));
				rightBorderPanel.setBorder(new MatteBorder(borders[0] > 0 ? 1
						: 0, 0, borders[2] > 0 ? 1 : 0, borders[3] > 0 ? 1 : 0,
						Color.black));
				updatePreview();
			}
		});
		spinnerTop.addChangeListener(borderListener);
		spinnerRight.addChangeListener(borderListener);
		spinnerBottom.addChangeListener(borderListener);

		leftBorderPanel.setLayout(new GridBagLayout());
		topBorderPanel.setLayout(new GridBagLayout());
		rightBorderPanel.setLayout(new GridBagLayout());
		bottomBorderPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);

		leftBorderPanel.add(spinnerLeft, gbc);
		topBorderPanel.add(spinnerTop, gbc);
		rightBorderPanel.add(spinnerRight, gbc);
		bottomBorderPanel.add(spinnerBottom, gbc);

		JPanel bordersPanel = new JPanel();
		bordersPanel.setLayout(new GridLayout(1, 3));
		bordersPanel.setPreferredSize(new Dimension(300, 100));
		bordersPanel.setBorder(new TitledBorder("Border selector"));

		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new GridLayout(2, 1));
		centralPanel.add(topBorderPanel);
		centralPanel.add(bottomBorderPanel);

		bordersPanel.add(leftBorderPanel);
		bordersPanel.add(centralPanel);
		bordersPanel.add(rightBorderPanel);

		// Container for radio buttons & border selector
		JPanel container = new JPanel();
		container.add(colorSelectorPanel);
		container.add(bordersPanel);

		// Container for color chooser & buttons & border selector
		JPanel colorPanel = new JPanel();
		// colorPanel.setBorder(new TitledBorder("Color"));
		colorPanel.setLayout(new BorderLayout());
		colorPanel.add(colorChooser, BorderLayout.CENTER);
		colorPanel.add(container, BorderLayout.NORTH);
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout());
		textPanel.add(textControlPanel, BorderLayout.NORTH);
		textPanel.add(colorPanel, BorderLayout.CENTER);
		// predefined formats

		formatButtonListener = new FormatButtonListener();

		// Create tabs panel
		styleTabs = new JTabbedPane();
		styleTabs.addTab("User defined", textPanel);
		// tabs.addTab("Styles", stylePanel);
		setLayout(new BorderLayout());
		add(styleTabs, BorderLayout.CENTER);
		add(previewPanel, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(colorChooser.getPreferredSize().width,
				textPanel.getMinimumSize().height
						+ colorChooser.getMinimumSize().height));

		for (int i = 0; i < PredefinedStyles.getStyles().length; i++) {
			addPredefinedStyle("ab...zAB...Z", PredefinedStyles.getStyles()[i]);
		}

		// Update initials
		updatePreview();
	}

	private JButton createFormatButton(String string, String format) {
		JButton b = new JButton(string);
		formatter.applyStyle(b, format, false, false);
		b.addActionListener(formatButtonListener);
		return b;
	}

	/**
	 * Apply the format to the preview panel.
	 */
	private void updatePreview() {
		// System.out.println("formatter = " + formatter);
		if (formatter != null) {
			formatter.applyStyle(preview, getInternalStyle(), false, false);
		} else {
			preview.setBorder(new MatteBorder(borders[0], borders[1],
					borders[2], borders[3], borderColor));
			preview.setFont(new Font(family, style, size));
			preview.setForeground(foregroundColor);
			preview.setBackground(backgroundColor);
		}
		// Test
		// System.out.println(getStyle());
	}

	/**
	 * Get the available fonts from the environment.
	 * 
	 * @return the available fonts.
	 */
	private String[] getFontFamilyNames() {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		return ge.getAvailableFontFamilyNames();
	}

	/**
	 * Get the available font styles.
	 * 
	 * @return the font styles
	 */
	private String[] getFontStyle() {
		return new String[] { "Plain", "Bold", "Italic" };
	}

	/**
	 * Get a list with the font sizes.
	 * 
	 * @return the font sizes.
	 * 
	 */
	private String[] getFontSize() {
		return new String[] { "6", "7", "8", "9", "10", "11", "12", "14", "16",
				"18", "20", "22", "24", "28" };
	}

	/**
	 * Add a predefined style.
	 * 
	 * @param name
	 *            the name.
	 * @param style
	 *            the style.
	 */
	public void addPredefinedStyle(String name, String style) {
		// predefined formats
		// Create tabs panel
		if (predefinedStylesPanel == null) {
			predefinedStylesPanel = new JPanel(new FlowLayout(
					FlowLayout.LEADING, 4, 4));
			styleTabs.addTab("Predefined", predefinedStylesPanel);
		}

		predefinedStylesPanel.add(createFormatButton(name, style));
		// styleTabs.setSelectedIndex(1);
	}

	/**
	 * Get the style.
	 * 
	 * @return the style.
	 */
	public String getStyle() {
		return ComponentStyle.getComponentStyle(preview);
	}

	/**
	 * Get the style.
	 * 
	 * @return the style.
	 */
	private String getInternalStyle() {
		StringBuffer s = new StringBuffer();
		// s.append(ComponentStyle.getComponentStyle(preview));
		// Text
		s.append(ComponentStyle.getStyleElement(ComponentStyle.fontFamily,//
				family));
		s.append(ComponentStyle.getStyleElement(ComponentStyle.fontStyle,//
				style));
		s.append(ComponentStyle.getStyleElement(ComponentStyle.fontSize,//
				size));
		if (foregroundColor != null)
			s.append(ComponentStyle.getStyleElement(ComponentStyle.color,
					foregroundColor));
		if (backgroundColor != null)
			s.append(ComponentStyle.getStyleElement(
					ComponentStyle.backgroundColor, backgroundColor));
		// Border
		boolean hasBorder = false;
		if (borders[0] > 0) {
			s.append(ComponentStyle.getStyleElement(
					ComponentStyle.borderTopWidth,//
					borders[0]));
			hasBorder = true;
		}
		if (borders[1] > 0) {
			s.append(ComponentStyle.getStyleElement(
					ComponentStyle.borderLeftWidth,//
					borders[1]));
			hasBorder = true;
		}
		if (borders[2] > 0) {
			s.append(ComponentStyle.getStyleElement(
					ComponentStyle.borderBottomWidth,//
					borders[2]));
			hasBorder = true;
		}
		if (borders[3] > 0) {
			s.append(ComponentStyle.getStyleElement(
					ComponentStyle.borderRightWidth,//
					borders[3]));
			hasBorder = true;
		}
		if (hasBorder && borderColor != null) {
			s.append(ComponentStyle.getStyleElement(ComponentStyle.borderStyle,//
					"solid"));
			s.append(ComponentStyle.getStyleElement(ComponentStyle.borderColor,//
					borderColor));
		}
		return s.toString().trim();
	}

	/**
	 * Initialize the panel from a style.
	 * 
	 * @param s
	 *            the style.
	 */
	public void setStyle(String s) {
		// Colors
		foregroundColor = ComponentStyle.getForegroundColor(s);
		backgroundColor = ComponentStyle.getBackgroundColor(s);
		borderColor = ComponentStyle.getBorderColor(s);

		// Borders
		int w[] = ComponentStyle.getBorderWidth(s);
		spinnerTop.setValue(w[0]);
		spinnerLeft.setValue(w[1]);
		spinnerBottom.setValue(w[2]);
		spinnerRight.setValue(w[3]);

		// Font
		family = ComponentStyle.getFontFamily(preview.getFont(), s);
		size = Integer
				.parseInt(ComponentStyle.getFontSize(preview.getFont(), s));
		style = Integer.parseInt(ComponentStyle.getFontStyle(s));
		// - Family
		fontFamily.setSelectedItem(family);
		// - Style
		if (style == Font.BOLD) {
			fontStyle.setSelectedItem("Bold");
		} else if (style == Font.ITALIC) {
			fontStyle.setSelectedItem("Italic");
		} else if (style == Font.PLAIN) {
			fontStyle.setSelectedItem("Plain");
		}
		// - Size
		fontSize.setSelectedItem(size + "");
		// Needed when no but colors are changed
		updatePreview();
	}

	private class FormatButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			formatter.applyStyle(preview,
					ComponentStyle.getComponentStyle((JButton) e.getSource()),
					false, false);
			// System.out.println(getStyle());

		}
	}

	/**
	 * Initialize the panel from a style and a component as preview.
	 * 
	 * @param c
	 *            the preview component.
	 * 
	 * @param s
	 *            the style.
	 */
	// public void setStyle(Component c, String s) {
	// preview = c;
	//
	// this.setStyle(s);
	// }

	class FontComboBoxRenderer extends JLabel implements
			ListCellRenderer<Object> {
		private static final long serialVersionUID = 1L;
		private int s;

		public FontComboBoxRenderer() {
			setOpaque(true);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);
			s = UIManager.getFont("ComboBox.font").getSize();
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setFont(new Font(value.toString(), Font.PLAIN, s));
			setText(value.toString());
			return this;
		}
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// String format =
				// "font-family:Dialog;font-style:plain;font-size:18;color:255,51,0;background-color:255,255,0;border-top-width:2;border-bottom-width:2;border-style:solid;border-color:100,25,102;";
				JFrame f = new JFrame();
				f.setSize(600, 300);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				StyleChooser sb = new StyleChooser(new StyleFormatter());
				// sb.addPredefinedStyle("lime", format);
				// sb.setStyle(format);
				System.out.println("this " + sb.getStyle());
				System.out.println("component"
						+ ComponentStyle.getComponentStyle(preview));
				f.getContentPane().add(sb, BorderLayout.CENTER);
				f.pack();
				f.setVisible(true);
			}
		});
	}

}
