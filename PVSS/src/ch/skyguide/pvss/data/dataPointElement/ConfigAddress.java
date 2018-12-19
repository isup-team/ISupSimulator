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
public class ConfigAddress extends Config {

    private DPType dpType;
    private int dataType;
    private int agentId;
    private int direction;
    private String address;
    private String pollGroup;
    private String driverName;

    public ConfigAddress(DP dp, DPType dpType, int dataType, int agentId, int direction, String address, String pollGroup, String driverName) {
        super(dp);
        this.dpType = dpType;
        this.dataType = dataType;
        this.agentId = agentId;
        this.direction = direction;
        this.address = address;
        this.pollGroup = pollGroup;
        this.driverName = driverName;
    }

    public int getDataType() {
        return dataType;
    }
    
    public String getAddress() {
        return address;
    }

    public int getAgentId() {
        return agentId;
    }

    public int getDirection() {
        return direction;
    }

    public DPType getDpType() {
        return dpType;
    }

    public String getPollGroup() {
        return pollGroup;
    }
    
    public String getDriverName() {
        return driverName;
    }
    
    @Override
    public void cover(DPCoverVisitor visitor, int level) {
        visitor.ConfigAddressCover(this, level);
    }
}
