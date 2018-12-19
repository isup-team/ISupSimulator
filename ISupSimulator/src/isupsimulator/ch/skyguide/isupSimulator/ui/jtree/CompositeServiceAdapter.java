/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.Service;

/**
 *
 * @author caronyn
 */
public class CompositeServiceAdapter extends TreeNodeServiceAdapter {

	public CompositeServiceAdapter(Service service) {
		super(service);
	}
}
