/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.pattern.item;

/**
 *
 * @author CyaNn
 */
public abstract class Item implements Evaluable {

    private Item next;

    public abstract boolean isOperator();

    public Item getNext() {
        return next;
    }

    public void setNext(Item next) {
        this.next = next;
    }

    public void removeNext() {
	this.next = null;
    }

    public boolean hasNext() {
        return (this.next != null);
    }

    public String chainSring() {
	String ret = toString();
	if (hasNext()) ret += getNext().chainSring();
	return ret;
    }

    @Override
    public abstract String toString();

}
