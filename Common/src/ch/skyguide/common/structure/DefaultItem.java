/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.structure;

/**
 *
 * @author CyaNn
 */
public class DefaultItem<T> implements Item<T> {

    private T object;
    private Item<T> parent;

    @Override
    public T getObject() {
	return object;
    }

    public DefaultItem(T object) {
	this.object = object;
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

}
