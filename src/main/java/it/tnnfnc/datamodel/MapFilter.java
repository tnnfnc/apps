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
package it.tnnfnc.datamodel; //Package

import java.util.Iterator;
import java.util.Map;

/**
 * Map entries are can be grouped into different categories. This class
 * implements a one key to many groups grouping. For example the grouping object
 * can be a resource relevant for the associated keys.
 * 
 * @param <T>
 *            the grouping object.
 * 
 * @author Franco Toninato
 */
public class MapFilter<T> {
	/* The filters */
	private Object[][] filters = new Object[0][0];

	/**
	 * Creates a filter map.
	 */
	public MapFilter() {
	}

	/**
	 * @param group
	 *            the key group for filtering.
	 * @return returns the group index or -1 il the filter does not exist.
	 */
	public int searchFilter(T group) {
		for (int i = 0; i < filters.length; i++) {
			if (filters[i][0].equals(group)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Searches for a key in a group.
	 * 
	 * @param index
	 *            the group index.
	 * @param key
	 *            the key.
	 * @return returns the index of the key or -1 when the key does not exist.
	 */
	public int searchKey(int index, Object key) {
		for (int j = 0; index > -1 && j < filters[index].length; j++) {
			if (filters[index][j].equals(key)) {
				return j;
			}
		}
		return -1;
	}

	/**
	 * Sets a filter for gathering all properties from the keys into one group.
	 * Properties can be filtered for being stored and retrieved from different
	 * resources.
	 * 
	 * @param key
	 *            the property key to include in the filter.
	 * @param group
	 *            the name of the filter.
	 */
	public void addKey(Object key, T group) {
		// key = PropertiesPoolModel.key(id, key);
		int i = searchFilter(group);
		if (i < 0) {
			// add a new filter
			Object[][] temp = new Object[1 + filters.length][];
			System.arraycopy(filters, 0, temp, 0, filters.length);
			temp[filters.length] = new Object[1];
			temp[filters.length][0] = group;
			filters = temp;
			i = filters.length - 1;
		}
		int j = searchKey(i, key);
		if (j < 0) {
			// add a new key
			Object[] temp = new Object[1 + filters[i].length];
			System.arraycopy(filters[i], 0, temp, 0, filters[i].length);
			temp[filters[i].length] = key;
			filters[i] = temp;
		}
		// already present
	}

	/**
	 * Includes all map keys into one filter group.
	 * 
	 * @param group
	 *            the name of the filter.
	 * @param keys
	 *            the map keys to include in the filter.
	 */
	public void setFilter(Map<?, ?> map, T group) {
		Object[] k = map.keySet().toArray(new Object[0]);
		for (Object s : k) {
			addKey(s, group);
		}
	}

	/**
	 * Filters an input map to a filtered output map.
	 * 
	 * @param <K>
	 *            the map key type.
	 * @param <V>
	 *            the map value type.
	 * 
	 * @param map
	 *            the input map.
	 * @param filteredMap
	 *            the filtered output map. This contains only the filtered keys.
	 * 
	 * @param group
	 *            the name of the filter group.
	 */
	public <K, V> void doFilter(Map<K, V> map, Map<K, V> filteredMap, T group) {
		int i = searchFilter(group);
		for (Iterator<K> iterator = map.keySet().iterator(); iterator.hasNext();) {
			K key = iterator.next();
			int j = searchKey(i, key);
			if (j < 0) {
				continue;
			} else {
				filteredMap.put(key, map.get(key));
			}
		}
	}

	/**
	 * Get a value from a map. The value is returned only if the key belongs to
	 * the filter.
	 * 
	 * @param <K>
	 *            the map key type.
	 * @param <V>
	 *            the map value type.
	 * @param map
	 *            the input map.
	 * @param key
	 *            the key to be filtered.
	 * @param group
	 *            the name of the filter group.
	 * @return the value.
	 */
	public <K, V> V get(Map<K, V> map, K key, T group) {
		int i = searchFilter(group);
		if (i >= 0 && searchKey(i, key) > 0) {
			return map.get(key);
		}
		return null;
	}

	/**
	 * Returns a dump of this object.
	 * 
	 *@return a string with a dump of this object.
	 */
	@Override
	public String toString() {
		int i = 0;
		int j = 0;
		String dump = "{Filters: ";
		for (Object[] ss : filters) {
			dump += "\n [";
			for (Object s : ss) {
				dump += "\n   key(" + i + "," + j + "):" + s;
				j++;
			}
			j = 0;
			i++;
			dump += "\n ]";
		}
		dump += "}";

		return dump;
	}
}