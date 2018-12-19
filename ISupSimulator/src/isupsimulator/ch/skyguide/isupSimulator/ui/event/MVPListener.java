/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isupsimulator.ch.skyguide.isupSimulator.ui.event;

import ch.skyguide.common.event.Event;
import ch.skyguide.common.event.Listener;
import javax.swing.JComponent;

/**
 * The default action listener.
 * @author caronyn
 */
public interface MVPListener extends Listener {

	/**
	 * The observer methode.
	 * @param event
	 */
	public void actionPerformed(Event<JComponent> event);

}
