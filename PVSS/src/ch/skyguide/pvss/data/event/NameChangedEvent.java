/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.event;

import ch.skyguide.common.event.Event;
import ch.skyguide.pvss.data.dataPointType.DPType;

/**
 *
 * @author caronyn
 */
public class NameChangedEvent extends Event<DPType> {

    public NameChangedEvent(DPType iSource) {
	super(iSource);
    }

}
