/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service;

import ch.skyguide.common.event.Event;
import ch.skyguide.common.event.Listener;

/**
 *
 * @author caronyn
 */
public interface ServiceListener extends Listener {
    void serviceStarted(Event<ServiceLeaf> event);
    void serviceStopped(Event<ServiceLeaf> event);
    void serviceChanged(Event<ServiceLeaf> event);

}