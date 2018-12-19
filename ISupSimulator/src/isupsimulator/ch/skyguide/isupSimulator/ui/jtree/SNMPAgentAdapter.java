/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.Service;
import ch.skyguide.pvss.network.service.snmp.SNMPMIB;
import ch.skyguide.pvss.network.service.snmp.SNMPAgentService;

/**
 *
 * @author caronyn
 */
public class SNMPAgentAdapter extends TreeNodeServiceAdapter {

	public SNMPAgentAdapter(Service service) {
		super(service);
	}

	@Override
	public String toString() {
		SNMPAgentService s = (SNMPAgentService) super.getUserObject();
		return "SNMP [" + s.getName() + " - " + s.getVersion() + " - " + s.getPort() + "]";
	}
}
