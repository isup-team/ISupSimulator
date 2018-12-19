/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.snmp;

import ch.skyguide.pvss.network.service.snmp.builder.ManagedObjectBuilder;
import java.util.List;
import org.snmp4j.smi.OID;

/**
 *
 * @param <T> 
 * @author caronyn
 */
public interface BuilderContainer<T extends ManagedObjectBuilder> {

	// property
	OID getPrefixOID();

	SNMPConvertTable getConvertTable(String name);

	public List<SNMPConvertTable> getConvertTables();

	T getBuilder(int i);

	// methodes
	void addBuilder(T builder);

	void removeBuilder(T builder);

	List<T> getBuilders();

}
