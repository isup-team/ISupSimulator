/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.Service;
import ch.skyguide.pvss.network.service.snmp.SNMPMIB;
import ch.skyguide.pvss.network.service.xml.XmlClient;
import ch.skyguide.pvss.network.service.xml.XmlClientService;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author caronyn
 */
public class XMLClientAdapter extends CompositeServiceAdapter {

	public XMLClientAdapter(Service service) {
		super(service);
	}

	@Override
	public String toString() {
		XmlClientService xmlService = (XmlClientService) super.getUserObject();
		return "XML Client [" + xmlService.getName() + "]";
	}
}
