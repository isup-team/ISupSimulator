/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.xml.XmlClient;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author caronyn
 */
public class XmlClientSimulatorAdapter extends TreeNodeServiceAdapter {

	public XmlClientSimulatorAdapter(XmlClient simulator) {
		super(simulator);
	}

	@Override
	public String toString() {
		XmlClient simulator = (XmlClient) super.getUserObject();
		return simulator.getName();
	}

}
