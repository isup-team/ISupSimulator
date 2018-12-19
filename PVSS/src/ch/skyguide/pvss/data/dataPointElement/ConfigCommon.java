/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.dataPointElement;

/**
 *
 * @author CyaNn
 */
public class ConfigCommon extends Config {

    private String alias;

    public String getAlias() {
	return alias;
    }

    public void setAlias(String alias) {
	this.alias = alias;
    }

    public ConfigCommon(DP dp) {
	super(dp);
    }

    public ConfigCommon(DP dp, String alias) {
	super(dp);
	this.alias = alias;
    }

    @Override
    public String toString() {
	return alias;
    }

    @Override
    public void cover(DPCoverVisitor visitor, int level) {
	visitor.ConfigCommonCover(this, level);
    }

}
