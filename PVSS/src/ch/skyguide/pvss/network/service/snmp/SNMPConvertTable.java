/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.snmp;

import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import ch.skyguide.pvss.network.service.convertTable.EnumConverter;
import ch.skyguide.pvss.network.service.snmp.SMISyntax;
import org.snmp4j.smi.Variable;

/**
 *
 * @param <T> 
 * @author caronyn
 */
public class SNMPConvertTable<T extends Variable> extends ConvertTable<T> {
	
	// methode implementation
	@Override
	public EnumConverter getConverter() {
		return SMISyntax.NULL;
	}

	// constructor
	private SNMPConvertTable() {
		super("");
	}
	
	public SNMPConvertTable(String name) {
		super(name);
	}

	
}
