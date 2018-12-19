/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.Service;
import ch.skyguide.pvss.network.service.snmp.SNMPMIB;
import ch.skyguide.pvss.network.service.snmp.SNMPTrapService;

/**
 *
 * @author caronyn
 */
class SNMPTrapAdapter extends TreeNodeServiceAdapter {

	public SNMPTrapAdapter(Service service) {
		super(service);
		SNMPTrapService s = (SNMPTrapService) service;
	}

	@Override
	public String toString() {
		SNMPTrapService s = (SNMPTrapService) super.getUserObject();
		return "Traps [" + s.getName() + " - " + s.getPort() + "]";
	}
}
