/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency.xstreamConverter;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.snmp4j.smi.UdpAddress;

/**
 *
 * @author root
 */
public class UdpAddressConverter extends AbstractSingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		return (type.equals(UdpAddress.class));
	}

	@Override
	public Object fromString(String string) {
		return new UdpAddress(string);
	}

	@Override
	public String toString(Object obj) {
		UdpAddress Udp = (UdpAddress) obj;
		return super.toString(Udp.toString());
	}
}
