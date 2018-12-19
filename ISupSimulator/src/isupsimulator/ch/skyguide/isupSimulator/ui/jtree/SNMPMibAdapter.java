/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.snmp.SNMPMIB;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author caronyn
 */
public class SNMPMibAdapter extends DefaultMutableTreeNode {

    public SNMPMibAdapter(SNMPMIB mib) {
        super(mib);
    }

    @Override
    public String toString() {
        SNMPMIB m = (SNMPMIB) super.getUserObject();
        return m.getName() + " " + m.getPrefixOID().toString();
    }

}
