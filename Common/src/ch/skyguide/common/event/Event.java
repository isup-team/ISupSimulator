/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.event;

/**
 *
 * @param <T>
 * @author CyaNn
 */
public class Event<T> {

	protected T _source = null;

	public T getSource(){
		return _source;
	}

	public Event(T iSource){
		_source = iSource;
	}

}
