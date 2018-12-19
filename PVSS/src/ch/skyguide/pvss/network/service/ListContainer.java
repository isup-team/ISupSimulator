/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service;

/**
 *
 * @param <T> 
 * @author caronyn
 */
public interface ListContainer<T> {
	
	void lazyInitializeSubSystems();

	boolean addSubSystem(T e);

	void addSubSystem(int index, T e);

	boolean removeSubSystem(T o);

	T removeSubSystem(int i);

	T getSubSystem(int index);

	Iterable<T> getSubSystems();

}
