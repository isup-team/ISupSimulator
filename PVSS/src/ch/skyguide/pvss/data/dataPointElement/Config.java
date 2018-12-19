/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.dataPointElement;

/**
 *
 * @author CyaNn
 */
public abstract class Config {

    private DP dp;

    public abstract void cover(DPCoverVisitor visitor, int level);

    public DP getDp() {
	return dp;
    }

    public Config(DP dp) {
	this.dp = dp;
    }

}
