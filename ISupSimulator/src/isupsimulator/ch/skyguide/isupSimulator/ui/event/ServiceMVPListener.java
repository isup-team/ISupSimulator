/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isupsimulator.ch.skyguide.isupSimulator.ui.event;

import ch.skyguide.common.event.Listener;

/**
 * The open file action listener.
 * @author caronyn
 */
public interface ServiceMVPListener extends Listener {

	/**
	 * The observer methode.
	 * @param event
	 */
	public void actionPerformed(ServiceMVPEvent event);

}
