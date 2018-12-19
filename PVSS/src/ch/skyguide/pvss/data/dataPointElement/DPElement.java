package ch.skyguide.pvss.data.dataPointElement;

import ch.skyguide.pvss.data.dataPointType.DPType;
import ch.skyguide.pvss.data.event.NameChangedEvent;
import ch.skyguide.pvss.data.valueManager.DataType;

public class DPElement extends DP {

    // variable
    private DataType dataType;
    private ConfigValue value;
    private ConfigAlarm alarm;
    protected ConfigDistrib distrib = null;
    protected ConfigAddress address = null;

    // accessors
    @Override
    public DataType getDataType() {
	return dataType;
    }

    public boolean hasValue() {
	return (value != null);
    }

    public ConfigValue getValueInstance() {
	if (!hasValue()) {
	    value = new ConfigValue(this);
	}
	return value;
    }

    public ConfigAlarm getAlarmInstance() {
	if (alarm == null) {
	    alarm = new ConfigAlarm(this);
	}
	return alarm;
    }

    public ConfigAlarm getAlarm() {
	return alarm;
    }

    public ConfigAddress getAddress() {
        return address;
    }

    public void setAddress(DPType dpType, int dataType, int agentId, int direction, String address, String pollGroup, String driverName) {
        this.address = new ConfigAddress(this, dpType, dataType, agentId, direction, address, pollGroup, driverName);
    }

    public ConfigDistrib getDistrib() {
        return distrib;
    }

    public void setDistrib(DPType dpType, int driverId) {
        this.distrib = new ConfigDistrib(this, dpType, driverId);
    }
    
    // constructor
    public DPElement(String name, DataType datatype) {
	super(name);
	this.dataType = datatype;
    }

    @Override
    protected void cover(DPCoverVisitor visitor, int level) {
	visitor.ElementCover(this, level);
	coverCommon(visitor, level);
	if (value != null) {
	    value.cover(visitor, level);
	}
	if (alarm != null) {
	    alarm.cover(visitor, level);
	}
        if (distrib != null) {
            distrib.cover(visitor, level);
        }
        if (address != null) {
            address.cover(visitor, level);
        }
    }

    // event handling
    @Override
    public void NameChanged(NameChangedEvent event) {
	name = event.getSource().getName();
    }

}
