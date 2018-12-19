/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.dataPointType;

import ch.skyguide.common.event.EventDispatcher;
import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.event.ChildChangedEvent;
import ch.skyguide.pvss.data.event.ChildChangedListener;
import ch.skyguide.pvss.data.event.NameChangedEvent;
import ch.skyguide.pvss.data.event.NameChangedListener;
import ch.skyguide.pvss.data.valueManager.DataType;

/**
 *
 * @author CyaNn
 */
public abstract class DPType {

    // attributes
    protected String name;
    protected DPType parent;
    // Event dispatcher
    private EventDispatcher<NameChangedListener> nameDispatcher = new EventDispatcher<NameChangedListener>();
    private EventDispatcher<ChildChangedListener> childDispatcher = new EventDispatcher<ChildChangedListener>();

    public final void addNameChangedListener(NameChangedListener listener) {
	nameDispatcher.addListener(listener);
    }

    public final void removeNameChangedListener(NameChangedListener listener) {
	nameDispatcher.removeListener(listener);
    }

    public final void addChildChangedListener(ChildChangedListener listener) {
	childDispatcher.addListener(listener);
    }

    public final void removeChildChangedListener(ChildChangedListener listener) {
	childDispatcher.removeListener(listener);
    }

    protected final void invokeNameChanged() {
	nameDispatcher.invoke("NameChanged", new NameChangedEvent(this));
    }

    protected final void invokeChildAdded(ChildChangedEvent event) {
	childDispatcher.invoke("ChildAdded", event);
    }

    protected final void invokeChildRemoved(ChildChangedEvent event) {
	childDispatcher.invoke("ChildRemoved", event);
    }

    // public attributes
    public abstract DataType getDataType();

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
	invokeNameChanged();
    }

    public DPType(String name) {
	this.name = name;
    }

    public abstract DP createDataPoint(String name);

    public void cover(DPTypeCoverVisitor visitor) {
	cover(visitor, 0);
    }

    public abstract void cover(DPTypeCoverVisitor visitor, int level);

    @Override
    public String toString() {
	return getName();
    }

}
