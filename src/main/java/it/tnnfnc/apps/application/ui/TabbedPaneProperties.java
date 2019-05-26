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
import java.util.Enumeration;
import java.util.Properties;

import it.tnnfnc.apps.application.ui.style.ComponentStyle;
import it.tnnfnc.apps.application.ui.style.I_StyleObject;
import it.tnnfnc.apps.application.ui.style.StyleObject;

/**
 * The class save a TabbedPane layout into a Properties object.
 * 
 * @author Franco Toninato
 * 
 */
public class TabbedPaneProperties {
	public static final String TAB = "tabPreference" + ".";
	public static final String INDEX = "position-index:";

	private TabbedPaneProperties() {
	}

	/**
	 * Save the tabbed pane layout into a Properties object.
	 * 
	 * @param tabpane
	 *            the tabbed pane.
	 * @param p
	 *            the properties.
	 */
	public static void saveTabsPaneProperties(TabbedPane tabpane, Properties p) {
		// Purge old properties
		for (Enumeration<?> keys = p.propertyNames(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			if (key.toString().startsWith(TAB)) {
				p.remove(key);
			}
		}

		for (int i = 0; i < tabpane.getTabCount(); i++) {
			if (tabpane.getTabComponentAt(i) instanceof TabComponent) {
				I_StyleObject so = ((TabComponent) tabpane.getTabComponentAt(i))
						.getTabStyle();
				p.setProperty(TAB + so.getValue(), so.getStyle() + INDEX + i
						+ ";");
			}
		}
	}

	/**
	 * Get the saved layout from the properties and apply to the tabs pane.
	 * 
	 * @param table
	 *            the tabs pane.
	 * @param p
	 *            the properties.
	 */
	public static void applyTabsPaneProperties(TabbedPane tabpane, Properties p) {

		//
		// HashMap<Integer, Component> tabs = new HashMap<Integer, Component>();

		Component[] tabs = new Component[Math.max(tabpane.getTabCount(),
				p.size())];
		for (Enumeration<?> keys = p.propertyNames(); keys.hasMoreElements();) {
			String key = keys.nextElement().toString();
			if (key.toString().startsWith(TAB)) {

				if (tabpane.indexOfTab(key.substring(TAB.length())) > -1) {
					Component c = tabpane.getTabComponentAt(tabpane
							.indexOfTab(key.substring(TAB.length())));
					int i = Integer.parseInt(ComponentStyle.parseStyle(INDEX,
							p.getProperty(key)));
					tabs[i] = c;

					// Apply the format to the existing tabs
					if (c instanceof TabComponent) {
						TabComponent tc = ((TabComponent) c);
						tc.updateTabComponent(new StyleObject(tc.getTabStyle()
								.getValue(), p.getProperty(key)));
					}
				}
			}

		}

		// for (int i = 0; i < tabs.length; i++) {
		// System.out.println(" TabComponent " + " index " + i + " category= "
		// + tabs[i]);
		// }

		// TODO Rebuild the tabs
		// if (1 < 1) {
		// for (int i = 0; i < tabs.length; i++) {
		// int tabindex = tabpane.indexOfTabComponent((tabs[i]));
		// System.out.println(" TabComponent " + " tab-index " + tabindex
		// + " category= " + tabs[i]);
		// if (tabindex > -1) {
		// String title = tabpane.getTitleAt(tabindex);
		// Icon icon = tabpane.getIconAt(tabindex);
		// Component component = tabpane.getComponentAt(tabindex);
		// String tooltip = tabpane.getToolTipTextAt(tabindex);
		// // System.out.println(" tabs[" + i + "] = " + tabs[i]);
		// if (tabs[i] instanceof TabComponent) {
		// // System.out.println(" TabComponent added  " +
		// // "style = "
		// // + ((TabComponent) tabs[i]).getTabStyle());
		// tabpane.insertTab(
		// ((TabComponent) tabs[i]).getTabStyle(), icon,
		// component, tooltip, i);
		// // tabpane.remove(tabindex);
		// } else {
		// // System.out.println(" TabComponent standard  "
		// // + "no style = " + tabs[i]);
		// tabpane.insertTab(title, icon, component, tooltip, i);
		// // tabpane.remove(tabindex);
		// }
		// }
		// }
		// }
	}
}
