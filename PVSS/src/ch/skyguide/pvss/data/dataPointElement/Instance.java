/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.dataPointElement;

import ch.skyguide.pvss.data.dataPointType.DPType;

/**
 *
 * @author CyaNn
 */
public class Instance extends DPNode {

    private DPType dpType;

    public DPType getDpType() {
	return dpType;
    }

    @Override
    public Instance getInstance() {
	return this;
    }

    public Instance(String name, DPType dpType) {
	super(name, dpType.getDataType());
	this.dpType = dpType;
    }

    @Override
    protected void cover(DPCoverVisitor visitor, int level) {
	visitor.InstanceCover(this, level);
	coverChildren(visitor, level);
	coverCommon(visitor, level);
    }

}
