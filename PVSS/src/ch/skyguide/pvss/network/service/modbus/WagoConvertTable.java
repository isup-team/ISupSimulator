/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.modbus;

import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import ch.skyguide.pvss.network.service.convertTable.Entry;
import ch.skyguide.pvss.network.service.convertTable.EnumConverter;

/**
 *
 * @author caronyn
 */
public class WagoConvertTable<T> extends ConvertTable<T> {

	@Override
	public EnumConverter getConverter() {
		return ModbusSyntax.COIL;
	}

	private WagoConvertTable() {
		super("");
	}
	
	public WagoConvertTable(String name) {
		super(name);
	}

}
