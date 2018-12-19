/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.dataPointElement;

/**
 *
 * @author CyaNn
 */
public class ConfigAlarm extends Config {

    private String panel;

    public String getPanel() {
	return panel;
    }

    public void setPanel(String panel) {
	this.panel = panel;
    }

    public ConfigAlarm(DP dp) {
	super(dp);
    }

    @Override
    public void cover(DPCoverVisitor visitor, int level) {
	visitor.ConfigAlarmCover(this, level);
    }

}
