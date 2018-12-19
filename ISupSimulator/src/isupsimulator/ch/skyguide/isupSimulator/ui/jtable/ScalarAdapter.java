/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import ch.skyguide.pvss.network.service.snmp.BuilderContainer;
import ch.skyguide.pvss.network.service.snmp.SMIAccess;
import ch.skyguide.pvss.network.service.snmp.SNMPMIB;
import ch.skyguide.pvss.network.service.snmp.builder.ManagedObjectBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.ScalarBuilder;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;

/**
 *
 * @author caronyn
 */
public class ScalarAdapter extends ListAdapter<ManagedObjectBuilder> {

	private BuilderContainer container;

	@Override
	public int getRowCount() {
		int res = 0;
		for (ManagedObjectBuilder builder : elements) {
			if (builder instanceof ScalarBuilder) {
				res++;
			}
		}
		return res;
	}

	@Override
	public ManagedObjectBuilder getElementInstance() {
		ManagedObjectBuilder builder = new ScalarBuilder("", new OID(), SMIAccess.READ_ONLY, new Integer32(0));
		builder.setParentMib(container);
		return builder;
	}
	
	// constructor
	public ScalarAdapter(TableColumnDefinition[] columnDefinitions, BuilderContainer container) {
		super(columnDefinitions, container.getBuilders());
		this.container = container;
	}

}
