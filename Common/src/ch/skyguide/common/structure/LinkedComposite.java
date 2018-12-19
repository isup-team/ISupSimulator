/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author CyaNn
 */
public class LinkedComposite<T> implements Composite<T> {

	private T object;
	private List<Item<T>> children;
	private Item<T> parent;

	@Override
	public void addChild(Item<T> child) {
		child.setParent(this);
		children.add(child);
	}

	@Override
	public void removeChild(Item<T> child) {
		child.removeParent();
		children.remove(child);
	}

	@Override
	public Item<T> getChild(int i) {
		return children.get(i);
	}

	@Override
	public T getObject() {
		return object;
	}

	public LinkedComposite(T object) {
		this.object = object;
		this.children = new LinkedList<Item<T>>();
	}

	@Override
	public void setParent(Item<T> parent) {
		this.parent = parent;
	}

	@Override
	public void removeParent() {
		this.parent = null;
	}

	@Override
	public Item<T> getParent() {
		return this.parent;
	}

	@Override
	public boolean isRoot() {
		return (this.parent == null);
	}

	@Override
	public boolean hasChild() {
		return (!children.isEmpty());
	}

	@Override
	public Iterable<Item<T>> getDepthFirstIterable() {
		LinkedList<Item<T>> list = new LinkedList<Item<T>>();
		appendDepthFirstToList(list);
		return list;
	}

	private void appendDepthFirstToList(List<Item<T>> list) {

		list.add(this);
		for (Item<T> child : children) {
			if (child instanceof LinkedComposite) {
				LinkedComposite composite = (LinkedComposite) child;
				composite.appendDepthFirstToList(list);
			} else {
				list.add(child);
			}
		}
	}

	@Override
	public Iterable<Item<T>> getBreadthFirstIterable() {
		LinkedList<Item<T>> list = new LinkedList<Item<T>>();
		LinkedList<Item<T>> cover = new LinkedList<Item<T>>();

		cover.add(this);

		while (!cover.isEmpty()) {
			list.add(cover.getFirst());

			if (cover.getFirst() instanceof LinkedComposite) {
				LinkedComposite<T> composite = (LinkedComposite<T>) cover.getFirst();
				for (Item<T> child : composite.children) {
					cover.add(child);
				}
			}

			cover.remove(0);
		}

		return list;

	}

	@Override
	public Iterator<Item<T>> iterator() {
		return children.iterator();
	}

	@Override
	public boolean hasMoreElements() {
		return children.iterator().hasNext();
	}

	@Override
	public Item<T> nextElement() {
		return children.iterator().next();
	}
}
