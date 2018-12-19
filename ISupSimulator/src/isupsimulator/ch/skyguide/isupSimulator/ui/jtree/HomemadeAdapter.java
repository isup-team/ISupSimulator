/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.Service;
import ch.skyguide.pvss.network.service.homemade.HomemadeService;

/**
 *
 * @author caronyn
 */
public class HomemadeAdapter extends TreeNodeServiceAdapter {

	public HomemadeAdapter(Service service) {
		super(service);
	}

	@Override
	public String toString() {
		HomemadeService service = (HomemadeService) super.getUserObject();
		return "Homemade Server [" + service.getName() + " - " + service.getPort() + "]";
	}
}
