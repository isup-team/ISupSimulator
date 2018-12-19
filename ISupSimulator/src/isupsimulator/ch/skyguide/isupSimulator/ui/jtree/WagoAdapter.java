/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.Service;
import ch.skyguide.pvss.network.service.modbus.WagoService;

/**
 *
 * @author caronyn
 */
public class WagoAdapter extends TreeNodeServiceAdapter {

    public WagoAdapter(Service service) {
        super(service);
    }

    @Override
    public String toString() {
        WagoService service = (WagoService) super.getUserObject();
        return "WAGO Server [" + service.getName() + " - " + service.getPort() + " - " + service.getId() + "]";
    }
}
