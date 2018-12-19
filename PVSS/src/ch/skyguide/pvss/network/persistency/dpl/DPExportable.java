/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency.dpl;

/**
 *
 * @author caronyn
 */
public interface DPExportable {

    public int getAgentID();

    public void setAgentID(int AgentID);

    public String getDriverName();

    public void setDriverName(String driverName);

    public int getDriverID();

    public void setDriverID(int DriverID);

    public String getPollGroup();

    public void setPollGroup(String pollGroup);

    public String getDpPrefix();

    public void setDpPrefix(String prefix);
}
