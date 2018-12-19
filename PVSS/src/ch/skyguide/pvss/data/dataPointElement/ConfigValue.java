/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.dataPointElement;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author CyaNn
 */
public class ConfigValue extends Config {

	public static final int DEFAULT_STATUS = 101;
	private Object originalValue;
	private int status = DEFAULT_STATUS;
	private Date statusDate = new Date(0);

	public Object getValue() {
		return getDp().getDataType().getManager().getValue(originalValue);
	}

	public void setValue(Object value) {
		setValue(value, status, Calendar.getInstance().getTime());
	}

	public void setValue(Object value, int status, Date statusDate) {
		this.originalValue = getDp().getDataType().getManager().setValue(value);
		this.status = status;
		this.statusDate = statusDate;
	}

	public int getStatus() {
		return status;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public ConfigValue(DP dp) {
		super(dp);
	}

	public ConfigValue(DP dp, Object value) {
		super(dp);
		setValue(value);
	}

	@Override
	public String toString() {
		return String.valueOf(originalValue);
	}

	@Override
	public void cover(DPCoverVisitor visitor, int level) {
		visitor.ConfigValueCover(this, level);
	}
}
