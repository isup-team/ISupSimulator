/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.structure;

/**
 *
 * @author CyaNn
 */
public interface Item<T> {

    T getObject();
    void setParent(Item<T> parent);
    void removeParent();
    Item<T> getParent();
    boolean isRoot();

}
