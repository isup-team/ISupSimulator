/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.structure;

import java.util.Enumeration;

/**
 *
 * @author CyaNn
 */
public interface Composite<T> extends Item<T>, Iterable<Item<T>>, Enumeration<Item<T>> {

    void addChild(Item<T> child);
    void removeChild(Item<T> child);
    Item<T> getChild(int i);
    boolean hasChild();
    Iterable<Item<T>> getDepthFirstIterable();
    Iterable<Item<T>> getBreadthFirstIterable();

}
