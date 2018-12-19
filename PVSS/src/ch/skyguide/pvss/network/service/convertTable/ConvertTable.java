/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.convertTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @param <T>
 * @author caronyn
 */
public abstract class ConvertTable<T> {

	private String name;
	private List<Entry<T>> list;

	// template methode
	public abstract EnumConverter getConverter();

	/**
	 * The name getter.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private void initializeList() {
		if (list == null) {
			list = new LinkedList<Entry<T>>();
		}
	}

	public Entry<T> get(int index) {
		initializeList();

		if (index >= list.size()) {
			return null;
		} else {
			return list.get(index);
		}
	}

	public Entry<T> getFirst() {
		initializeList();
		return get(0);
	}

	/**
	 * Add an item to the list.
	 * @param item The item to add.
	 * @return Return the obtained value list.
	 */
	public ConvertTable<T> addItem(Entry<T> item) {
		initializeList();
		list.add(item);
		return this;
	}

	/**
	 * Find a matching value by its value.
	 * @param value The value to find.
	 * @return The Matching value corresponding.
	 */
	public Entry<T> find(T value) {
		initializeList();
		for (Object o : list) {
			Entry<T> mv = (Entry<T>) o;
			if (mv.getValue().equals(value)) {
				return mv;
			}
		}
		return null;
	}

	/**
	 * Remove the specific item.
	 * @param item The item to remove.
	 * @return Return true if success.
	 */
	public boolean remove(Entry<T> item) {
		initializeList();
		return list.remove(item);
	}

	/**
	 * Get the list of possible values.
	 * @return The list.
	 */
	public List<Entry<T>> getList() {
		initializeList();
		return list;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Default constructor.
	 *
	 * @param name Convert table name.
	 */
	public ConvertTable(String name) {
		this.name = name;
		list = new ArrayList<Entry<T>>();
	}
}
