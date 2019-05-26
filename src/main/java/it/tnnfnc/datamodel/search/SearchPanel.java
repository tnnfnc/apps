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
package it.tnnfnc.datamodel.search;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import it.tnnfnc.apps.application.ui.GridBagLayoutUtility;
import it.tnnfnc.apps.application.ui.LocaleBundle;
import it.tnnfnc.datamodel.I_HyperLinkListener;
import it.tnnfnc.datamodel.I_Visitable;

/**
 * @author franco toninato
 * 
 */
public class SearchPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SEARCH = "find";
	private static final String STOP = "stop";
	private ListResourceBundle language = (ListResourceBundle) ResourceBundle
			.getBundle(LocaleBundle.class.getName());

	private JComboBox<Object> findTextCombo;
	private JTextField matchCount;
	private JTextField message;
	// Scan for a regular expression, if not flagged it searches for a literal.
	private JCheckBox regex;
	// Case insensitive.
	private JCheckBox ignoreCase;
	// Multilines search.
	private JCheckBox multiline;
	// The regular expression "." matches any character except a line terminator
	// unless the DOTALL flag is specified.
	private JCheckBox dotAll;
	private JButton find;
	// private JButton close;
	private JButton stop;
	private MatchDisplayPanel matchPanel;
	//
	private JDialog dialog;

	// private MatchResultModel resultListModel;

	private static GridBagLayout gridBagLayout = new GridBagLayout();
	/**
	 * The searchable data stores.
	 */
	protected ArrayList<SearchItem> searches;
	/**
	 * 
	 */
	protected ArrayList<Thread> searchThreads;

	private class SearchItem {
		public I_HyperLinkListener linkListener;
		public I_RegexSearch search;
		public String searchName;

		public SearchItem(I_HyperLinkListener l, I_RegexSearch search,
				String name) {
			this.linkListener = l;
			this.search = search;
			this.searchName = name;
		}
	}

	/**
	 * Creates a find panel.
	 * 
	 */
	public SearchPanel() {
		initialize();
		regex.setSelected(false);
		createGUI();
	}

	/**
	 * Creates a find panel with a running match.
	 * 
	 * @param fullSearch
	 *            true when regular expression syntax is enabled.
	 * @param match
	 *            the initial match text.
	 */
	public SearchPanel(boolean fullSearch, String match) {
		this();
		regex.setSelected(fullSearch);
		findTextCombo.setSelectedItem(match);
		// find.doClick();
	}

	private void initialize() {
		matchCount = new JTextField(3);
		message = new JTextField(10);
		findTextCombo = new JComboBox<Object>();
		regex = new JCheckBox(language.getString("Regular expression"));
		ignoreCase = new JCheckBox(language.getString("Ignore case"));
		multiline = new JCheckBox(language.getString("Multiline"));
		dotAll = new JCheckBox(language.getString("Allow dot match end line"));
		searchThreads = new ArrayList<Thread>();
		searches = new ArrayList<SearchItem>();
		find = new JButton(language.getString("find"));
		stop = new JButton(language.getString("stop"));
	}

	/* Init the find dialog */
	private void createGUI() {
		// split pane
		JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		splitPanel.setOneTouchExpandable(true);
		// splitPanel.setResizeWeight(0.0);
		splitPanel.setBorder(null);

		// JScrollPane;
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel topContainer = new JPanel(new BorderLayout());
		topContainer.add(createPatternPanel(), BorderLayout.LINE_START);

		topContainer.add(createButtonPanel(), BorderLayout.PAGE_END);

		topPanel.add(topContainer, BorderLayout.PAGE_START);

		JScrollPane matchPanel = new JScrollPane(createMatchPanel());
		matchPanel.setBorder(null);

		Dimension size = topPanel.getPreferredSize();
		matchPanel.setPreferredSize(size);

		splitPanel.setTopComponent(topPanel);
		splitPanel.setBottomComponent(matchPanel);
		this.setLayout(new BorderLayout());
		this.add(splitPanel, BorderLayout.CENTER);
	}

	private JPanel createPatternPanel() {
		matchCount.setText("0");
		matchCount.setEditable(false);
		matchCount.setBorder(null);

		message.setEditable(false);
		message.setBorder(null);

		// Add a search history
		JTextField dim = new JTextField(25);
		findTextCombo.setPreferredSize(dim.getPreferredSize());
		findTextCombo.requestFocusInWindow();
		findTextCombo.setEditable(true);

		// Options
		// Mnemonic
		regex.setMnemonic('R');
		ignoreCase.setMnemonic('I');
		multiline.setMnemonic('M');
		dotAll.setMnemonic('D');

		JPanel container = new JPanel(gridBagLayout);
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayoutUtility.initConstraints(gbc);

		JPanel comboPanel = new JPanel(gridBagLayout);
		comboPanel.add(new JLabel(language.getString("find")), gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.1;
		comboPanel.add(findTextCombo, GridBagLayoutUtility.right(gbc));
		GridBagLayoutUtility.initConstraints(gbc);
		container.add(comboPanel, GridBagLayoutUtility.spanBlock(gbc, 2, 1));

		GridBagLayoutUtility.refreshConstraints(gbc);
		container.add(regex, GridBagLayoutUtility.newLine(gbc));
		container.add(ignoreCase, GridBagLayoutUtility.right(gbc));
		container.add(multiline, GridBagLayoutUtility.newLine(gbc));
		container.add(dotAll, GridBagLayoutUtility.right(gbc));

		JPanel reportPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		reportPanel.add(new JLabel(language.getString("match:")));
		reportPanel.add(matchCount);

		GridBagLayoutUtility.newLine(gbc);
		GridBagLayoutUtility.refreshConstraints(gbc);
		container.add(reportPanel, GridBagLayoutUtility.spanBlock(gbc, 1, 1));

		GridBagLayoutUtility.refreshConstraints(gbc);
		GridBagLayoutUtility.right(gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		container.add(message, gbc);
		return container;
	}

	private Component createButtonPanel() {
		find.setActionCommand(SEARCH);
		stop.setActionCommand(STOP);

		ActionListener buttonsListener = new ButtonsListener();
		find.addActionListener(buttonsListener);
		stop.addActionListener(buttonsListener);

		JPanel container = new JPanel(new GridLayout(1, 0));

		container.add(find);
		container.add(stop);

		JPanel container1 = new JPanel(new BorderLayout());
		container1.add(container, BorderLayout.LINE_START);
		return container1;
	}

	private JPanel createMatchPanel() {
		matchPanel = new MatchDisplayPanel();
		return matchPanel;
	}

	private void addPattern(Object selectedItem) {
		if (contains(selectedItem)) {
		} else {
			findTextCombo.addItem(selectedItem);
		}
	}

	/**
	 * Search history for a pattern.
	 * 
	 * @param pattern
	 *            the search object.
	 * @return true when found.
	 */
	private boolean contains(Object pattern) {
		for (int i = 0; i < findTextCombo.getItemCount(); i++) {
			if (findTextCombo.getItemAt(i).equals(pattern))
				return true;
		}
		return false;
	}

	/**
	 * Build the search options to be passed to the search engine.
	 * 
	 * @return the search options bitmap.
	 */
	private int getRegexOptions() {
		int options = 0;
		if (!regex.isSelected()) {
			options |= Pattern.LITERAL;
		}
		if (ignoreCase.isSelected()) {
			options |= Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
		}
		if (dotAll.isSelected()) {
			options |= Pattern.DOTALL;
		}
		if (multiline.isSelected()) {
			options |= Pattern.MULTILINE;
		}
		return options;
	}

	/**
	 * Run a search for the match.
	 * 
	 * @param fullSearch
	 *            true when regular expression syntax is enabled.
	 * @param match
	 *            the initial match text.
	 */
	public void search(boolean fullSearch, String match) {
		regex.setSelected(fullSearch);
		findTextCombo.setSelectedItem(match);
		find.doClick();
	}

	/**
	 * Add an object for being searched for the regular expression. When the
	 * object class also implements the interface it is added also to the link
	 * listener content allowing navigation.
	 * 
	 * @param s
	 *            the listener notified when the link to a match is selected.
	 * @param search
	 *            the search.
	 * @param searchName
	 *            the search name.
	 */
	public void addSearch(I_HyperLinkListener s, I_RegexSearch search,
			String searchName) {
		searches.add(new SearchItem(s, search, searchName));
		matchPanel.addLinkListener(s);
	}

	/**
	 * Remove a source.
	 * 
	 * @see net.apps.AbstractFindDialog#addSearch(I_Visitable, RegexMatcher,
	 *      String)
	 * @param listener
	 *            the search listener.
	 */
	public void removeSearch(String searchName) {
		for (int i = 0; i < searches.size(); i++) {
			if (searches.get(i).searchName.equalsIgnoreCase(searchName)) {
				matchPanel.removeLinkListener(searches.get(i).linkListener);
				searches.remove(i);
				return;
			}
		}
	}

	/**
	 * Clear the matches list.
	 */
	public void clear() {
		matchPanel.clear();
	}

	/**
	 * Get the result model.
	 * 
	 * @return the search matches found.
	 */
	public ArrayList<SearchItem> getSearches() {
		return searches;
	}

	/**
	 * Display the matches found.
	 * 
	 * @param matchModel
	 *            the matches found in the search.
	 */
	public void displaySearch(MatchResultModel matchModel) {
		matchPanel.updateModel(matchModel);
	}

	/**
	 * Compile the search pattern.
	 * 
	 * @param regex
	 *            the regular expression.
	 * @param options
	 *            the search options.
	 * @return the compiled regular expression.
	 */
	public Pattern compilePattern() {
		try {
			int options = getRegexOptions();
			Object pattern = findTextCombo.getSelectedItem();
			if (pattern == null || pattern.equals("")) {
				message.setText(language
						.getString("Search string is not valid!"));
				return null;
			}
			if (options < 1) {
				return Pattern.compile(pattern.toString());
			} else {
				return Pattern.compile(pattern.toString(), options);
			}
		} catch (PatternSyntaxException ex) {
			String error = language.getString("syntax error") + ex.getMessage();
			message.setText(error);
			message.setToolTipText(message.getText());
			message.setCaretPosition(0);
			matchCount.setText("0");
		} catch (IllegalArgumentException ex) {
			String error = language.getString("syntax error") + ex.getMessage();
			message.setText(error);
			message.setCaretPosition(0);
			message.setText("0");
			matchCount.setToolTipText(message.getText());
		}
		return null;
	}

	/**
	 * Show a find dialog.
	 */
	public void showSearchDialog() {
		if (dialog == null) {
			dialog = new JDialog();
			dialog.addWindowListener(new InternalWindowsListener());
			dialog.getContentPane().setLayout(new BorderLayout());
			dialog.getContentPane().add(this, BorderLayout.CENTER);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(this);
			// close.setVisible(false);
		}

		dialog.pack();
		dialog.setVisible(true);
	}

	/**
	 * Search for the pattern.
	 */
	public void find() {
		if (compilePattern() != null) {
			message.setText("");
			message.setToolTipText(message.getText());
			addPattern(findTextCombo.getSelectedItem());
			Runnable r = new SearchBuilder(SearchPanel.this);
			Thread t = new Thread(r);
			searchThreads.add(t);
			t.start();
		}
	}

	/**
	 * Stop long running search.
	 */
	public void stop() {
		for (Thread t : searchThreads) {
			t.interrupt();
		}
		searchThreads.clear();
	}

	/**
	 * @author franco toninato
	 * 
	 */
	private class InternalWindowsListener extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent event) {
			stop();
		}

	}

	/**
	 * Buttons listener.
	 * 
	 */
	private class ButtonsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equalsIgnoreCase(SEARCH)) {
				find();
			} else if (e.getActionCommand().equalsIgnoreCase(STOP)) {
				stop();
			} else {
			}
		}
	}

	/**
	 * @author franco toninato
	 * 
	 */
	public static class SearchBuilder implements Runnable {
		// private Thread searchThread;
		private SearchPanel searchPanel;

		/**
		 * @param p
		 */
		public SearchBuilder(SearchPanel p) {
			this.searchPanel = p;
		}

		@Override
		public void run() {
			performSearch();
		}

		/**
		 * Finds a pattern.
		 */
		protected void performSearch() {
			// Creates a SearchListModel
			MatchResultModel matchModel = new MatchResultModel();
			// searchPanel.getMatches().clear();
			Pattern compiledPattern = searchPanel.compilePattern();
			for (int i = 0; i < searchPanel.getSearches().size(); i++) {
				begin();
				try {
					Thread.sleep(10);
					// Creates a text searcher
					I_RegexSearch _searcher = searchPanel.getSearches().get(i).search;
					_searcher.setResultStoreModel(matchModel);
					// Add an object header here!
					matchModel.addElement(formatSearchTitle(searchPanel
							.getSearches().get(i)), null);
					// Search the text
					_searcher.search(compiledPattern);
				} catch (InterruptedException e) {
					// notifyAll();
					end();
				}
			}
			displaySearch(matchModel);
			end();
		}

		/**
		 * Display the search results into the swing event thread.
		 */
		protected void displaySearch(final MatchResultModel matchModel) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					searchPanel.displaySearch(matchModel);
				}
			});
		}

		private void begin() {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					searchPanel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					searchPanel.find.setEnabled(false);
					searchPanel.stop.setEnabled(true);
				}
			});

		}

		private void end() {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					searchPanel.setCursor(Cursor.getDefaultCursor());
					searchPanel.find.setEnabled(true);
					searchPanel.stop.setEnabled(false);
				}
			});

		}

		private StringBuffer formatSearchTitle(SearchItem i) {
			StringBuffer sb = new StringBuffer();
			sb.append("<html><div align=center><font size=+1><i>");
			sb.append(i.searchName);
			sb.append("</i></font></div>");

			return sb;
		}
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
				SearchPanel demo = new SearchPanel(true, "www.google.com");
				demo.showSearchDialog();
			}
		});
	}
}
