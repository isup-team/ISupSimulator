/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.modbus;

import net.wimpi.modbus.Modbus;

/**
 *
 * @author caronyn
 */
public enum Encoding {

	ASCII (Modbus.SERIAL_ENCODING_ASCII),
	BIN (Modbus.SERIAL_ENCODING_BIN);

	String value;

	public String getValue() {
		return value;
	}

	private Encoding(String value) {
		this.value = value;
	}

}
