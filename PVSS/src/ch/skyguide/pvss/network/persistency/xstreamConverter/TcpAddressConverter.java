/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency.xstreamConverter;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.snmp4j.smi.TcpAddress;

/**
 *
 * @author root
 */
public class TcpAddressConverter extends AbstractSingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		return (type.equals(TcpAddress.class));
	}

	@Override
	public Object fromString(String string) {
		return new TcpAddress(string);
	}

	@Override
	public String toString(Object obj) {
		TcpAddress tcp = (TcpAddress) obj;
		return super.toString(tcp.toString());
	}
}
