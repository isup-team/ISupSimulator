/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.pattern;

import ch.skyguide.pvss.data.pattern.item.Item;

/**
 *
 * @author caronyn
 */
public class Path {

    private Item firstItem;

    public Item getFirstItem() {
	return firstItem;
    }

    private Item getLastItem(Item item) {
	if (item.hasNext()) {
	    return getLastItem(item.getNext());
	}
	return item;
    }

    public Path add(Item item) {
	getLastItem(firstItem).setNext(item);
	return this;
    }

    public Path(String path) {
	firstItem = PatternParser.parse(path);
    }

    public Path(Item firstItem) {
	this.firstItem = firstItem;
    }

    @Override
    public String toString() {
	return firstItem.chainSring();
    }

}
