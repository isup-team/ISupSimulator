/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.dataPointType;

import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.valueManager.DataType;

/**
 *
 * @author CyaNn
 */
public class ReferenceType extends DPType {

    private DPType dpType;

    @Override
    public DataType getDataType() {
	return DataType.TYPEREF;
    }

    public DPType getDpType() {
	return dpType;
    }

    @Override
    public DP createDataPoint(String name) {
	DP dp = dpType.createDataPoint(super.name);
	addNameChangedListener(dp);
	return dp;
    }

    public ReferenceType(String name, DPType dpType) {
	super(name);
	this.dpType = dpType;
    }

    @Override
    public void cover(DPTypeCoverVisitor visitor, int level) {
	visitor.ReferenceTypeCover(this, level);
	
	if (visitor.linkReference()) {
	    dpType.cover(visitor);
	}
    }

}
