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
package it.tnnfnc.datamodel;

import java.lang.reflect.Array;
import java.util.Comparator;

import javax.swing.event.EventListenerList;

/**
 * Abstract class implementing default methods.
 * 
 * @author Franco Toninato
 * 
 * @param <T>
 *            the model entry type.
 */
public abstract class AbstractIndexModel<T> implements I_IndexModel<T> {
	/**
	 * Current data index
	 */
	protected Index dataIndex = new Index();

	/**
	 * Listeners registered with this indexed model.
	 */
	protected EventListenerList listenerList = new EventListenerList();

	/**
	 * Add an entry to the data model.
	 * 
	 * @param dataItem
	 *            the new entry.
	 */
	protected abstract void innerAdd(T dataItem);

	/**
	 * @param index
	 * @return
	 */
	protected abstract T innerGet(int index);

	/**
	 * Notify listeners that data or their order is changed.
	 * 
	 * @param event
	 *            the change event.
	 */
	public void fireDataChange(DataChangeEvent event) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		if (event == null)
			return;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == I_DataChangeListener.class) {
				((I_DataChangeListener) listeners[i + 1]).performChange(event);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.dimentico.apps.model.I_Index#addDataChangeListener(net.dimentico.apps
	 * .model.DataChangeListener)
	 */
	@Override
	public void addDataChangeListener(I_DataChangeListener listener) {
		listenerList.add(I_DataChangeListener.class, listener);
	}

	/**
	 * Build the index from the data model. All previous index history is lost
	 * and the current index is initialized from the current model entries.
	 * 
	 * @param modelSize
	 *            the size of the data model.
	 */
	@Override
	public void buildIndex(int modelSize) {
		// Initialize dataIndex
		dataIndex = new Index();
		dataIndex.initialize(modelSize);
		fireDataChange(new DataChangeEvent(this, DataChangeEvent.EventType.REINDEX, 0, getActiveSize()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.model.I_Index#activeSize()
	 */
	@Override
	public void setActiveSize(int s) {
		int l = dataIndex.getActiveSize();
		dataIndex.setActiveSize(s);
		// fireDataChange(new DataChangeEvent(this,
		// DataChangeEvent.EventType.REINDEX, 0, s));
		fireDataChange(new DataChangeEvent(this, DataChangeEvent.EventType.REINDEX, l, s));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.model.I_Index#clear()
	 */
	@Override
	public void clear() {
		dataIndex = new Index();
		// clearFromModel();
		fireDataChange(new DataChangeEvent(this, DataChangeEvent.EventType.DELETE, 0, getActiveSize()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.model.I_Index#indexSize()
	 */
	@Override
	public int getFullSize() {
		// return dataIndex.index.size();
		return dataIndex.getFullSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.model.I_Index#activeSize()
	 */
	@Override
	public int getActiveSize() {
		// return dataIndex.activeSize;
		return dataIndex.getActiveSize();
	}

	/**
	 * Convert the view index into the model internal index.
	 * 
	 * @param index
	 *            the view index.
	 * @return the model internal index.
	 */
	@Override
	public int indexOf(int index) {
		if (0 <= index && index < getFullSize()) {
			return dataIndex.get(index);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.model.I_Index#move(int, int, int)
	 */
	@Override
	public void move(int startSelection, int endSelection, int target) {
		int s = 0; // Start
		int e = 0; // End
		int t = 0; // Target
		// Reverse the interval
		if (endSelection < startSelection) {
			s = startSelection;
			startSelection = endSelection;
			endSelection = s;
		}
		// Move up = move down the upper block
		if (target < startSelection) {
			s = target;
			e = startSelection - 1;
			t = endSelection;
			// Move down
		} else if (target > endSelection) {
			s = startSelection;
			e = endSelection;
			t = target;
		} else {
			// Equals or between nothing to change
			return;
		}

		Index.shiftBlock(dataIndex, s, e, t);

		fireDataChange(new DataChangeEvent(this, DataChangeEvent.EventType.UPDATE, s, t));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.model.I_Index#removeDataChangeListener(net.catode
	 * .apps.model.DataChangeListener)
	 */
	@Override
	public void removeDataChangeListener(I_DataChangeListener listener) {
		listenerList.remove(I_DataChangeListener.class, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.apps.model.I_Index#removeAt(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T[] removeAt(int startFrom, int endAt) {
		int start = Math.min(startFrom, endAt);
		int end = Math.max(startFrom, endAt);
		Object d[] = new Object[endAt - startFrom + 1];
		for (int i = 0; i < endAt - startFrom + 1; i++) {
			d[i] = getEntry(i);
		}
		int ret[] = Index.remove(dataIndex, start, end);
		if (ret.length > 0) {
			fireDataChange(new DataChangeEvent(this, DataChangeEvent.EventType.DELETE, start, end));
		}
		return (T[]) d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.apps.model.I_Index#setModelIndex(int, int)
	 */
	@Override
	public void changeIndex(int index, int modelIndex) {
		dataIndex.set(index, modelIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.dimentico.apps.model.I_Index#viewIndex(int)
	 */
	@Override
	public int getIndex(int modelIndex) {
		return Index.indexFor(dataIndex, modelIndex);
	}

	@Override
	public void addEntry(T dataItem) {
		innerAdd(dataItem);
		Index.insertIndex(dataIndex, dataIndex.getActiveSize());
		fireDataChange(
				new DataChangeEvent(this, DataChangeEvent.EventType.INSERT, getActiveSize() - 1, getActiveSize() - 1));
	}

	@Override
	public T getEntry(int index) {
		return innerGet(indexOf(index));
	}

	@Override
	public int insertEntry(int index, T dataItem) {
		// int j = insert(index);
		int j = Index.insertIndex(dataIndex, index);
		innerAdd(dataItem);
		fireDataChange(new DataChangeEvent(this, DataChangeEvent.EventType.INSERT, index, index));
		return j;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] toArray(T[] a) {
		T[] newArray = (T[]) Array.newInstance(a.getClass().getComponentType(), getActiveSize());
		for (int i = 0; i < getActiveSize(); i++) {
			newArray[i] = getEntry(i);
		}
		return newArray;
	}

	/**
	 * Filter the model. Does not shot any table event.
	 * 
	 * @param cmp
	 * @param match
	 */
	@Override
	public void filter(Comparator<T> cmp, T match) {
		Index.filter(cmp, this, match);
	}

	/**
	 * Sort the model. Does not shot any table event.
	 * 
	 * @param cmp
	 * @param sortOrder
	 */
	@Override
	public void sort(Comparator<T> cmp, SortOrder sortOrder) {
		Index.sort(cmp, sortOrder, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.catode.model.I_IndexModel#getDistincts(java.util.Comparator,
	 * net.catode.model.SortOrder)
	 */
	@Override
	public void getDistincts(Comparator<T> cmp, SortOrder order) {
		Index.sort(cmp, order, this);
		Index.getDistincts(cmp, this);
	}

}