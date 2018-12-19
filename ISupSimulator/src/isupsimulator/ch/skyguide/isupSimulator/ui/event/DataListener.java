/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isupsimulator.ch.skyguide.isupSimulator.ui.event;

/**
 * The open file action listener.
 * @param <T>
 * @author caronyn
 */
public interface DataListener<T> extends ch.skyguide.common.event.Listener {

	/**
	 * The observer methode.
	 * @param event
	 */
	public void changed(DataMVPEvent<T> event);

}
