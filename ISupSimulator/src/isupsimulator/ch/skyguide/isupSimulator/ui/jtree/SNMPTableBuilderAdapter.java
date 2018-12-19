/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.snmp.builder.TableBuilder;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author caronyn
 */
public class SNMPTableBuilderAdapter extends DefaultMutableTreeNode {

	public SNMPTableBuilderAdapter(TableBuilder builder) {
		super(builder);
	}

	@Override
	public String toString() {
		TableBuilder b = (TableBuilder) super.getUserObject();
		return b.getName() + " " + b.getOID();
	}
}
