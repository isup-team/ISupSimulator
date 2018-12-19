/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.event;

import ch.skyguide.common.event.Listener;

/**
 *
 * @author caronyn
 */
public interface ChildChangedListener extends ch.skyguide.common.event.Listener {

    public void ChildAdded(ChildChangedEvent event);
    public void ChildRemoved(ChildChangedEvent event);
    
}
