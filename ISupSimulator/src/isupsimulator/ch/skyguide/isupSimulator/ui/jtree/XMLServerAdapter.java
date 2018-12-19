/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.Service;
import ch.skyguide.pvss.network.service.xml.ISupSystem;
import ch.skyguide.pvss.network.service.xml.XmlServerService;

/**
 *
 * @author caronyn
 */
public class XMLServerAdapter extends TreeNodeServiceAdapter {

	public XMLServerAdapter(Service service) {
		super(service);
	}

	@Override
	public String toString() {
		XmlServerService xmlService = (XmlServerService) super.getUserObject();
		return "XML Server [" + xmlService.getName() + " - " + xmlService.getPort() + "]";
	}
}
