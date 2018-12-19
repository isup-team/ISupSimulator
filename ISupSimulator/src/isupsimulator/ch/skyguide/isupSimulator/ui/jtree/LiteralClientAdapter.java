/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.Service;
import ch.skyguide.pvss.network.service.literal.LiteralClient;

/**
 *
 * @author caronyn
 */
public class LiteralClientAdapter extends CompositeServiceAdapter {

    public LiteralClientAdapter(Service service) {
        super(service);
    }

    @Override
    public String toString() {
        LiteralClient literalService = (LiteralClient) super.getUserObject();
        return "Literal Client [" + literalService.getName() + "]";
    }
}
