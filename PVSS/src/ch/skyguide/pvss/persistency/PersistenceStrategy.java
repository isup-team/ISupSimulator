/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.persistency;

/**
 *
 * @author caronyn
 */
public interface PersistenceStrategy<T> {

    T retrieveObjectTree();
    void storeObjectTree(T object);

}
