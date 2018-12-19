/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.dataPointElement;

import ch.skyguide.pvss.data.dataPointType.DPType;

/**
 *
 * @author caronyn
 */
public class ConfigDistrib extends Config {

    private DPType dpType;
    private int driverId;

    public ConfigDistrib(DP dp) {
        super(dp);
    }
    
    public ConfigDistrib(DP dp, DPType dpType, int driverId) {
        super(dp);
        this.dpType = dpType;
        this.driverId = driverId;
    }

    public DPType getDpType() {
        return dpType;
    }

    public int getDriverId() {
        return driverId;
    }
    
    @Override
    public void cover(DPCoverVisitor visitor, int level) {
        visitor.ConfigDistribCover(this, level);
    }
}
