/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.dataPointElement;

/**
 *
 * @author caronyn
 */
public abstract class DPCoverVisitor {

    protected void RootCover(Root dp, int level) {}
    protected void InstanceCover(Instance dp, int level) {}
    protected void NodeCover(DPNode dp, int level) {}
    protected void ElementCover(DPElement dp, int level) {}
    protected void ConfigCommonCover(ConfigCommon config, int level) {}
    protected void ConfigValueCover(ConfigValue config, int level) {}
    protected void ConfigAlarmCover(ConfigAlarm config, int level) {}
    protected void ConfigDistribCover(ConfigDistrib config, int level) {}
    protected void ConfigAddressCover(ConfigAddress config, int level) {}
    
}
