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
public class ChildChangedEvent extends Event<DPType> {

    private DPType child;

    public DPType getChild() {
	return child;
    }

    public ChildChangedEvent(DPType iSource, DPType child) {
	super(iSource);
	this.child = child;
    }

}
