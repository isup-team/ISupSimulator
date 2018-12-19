/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency.xstreamConverter;

import ch.skyguide.pvss.network.service.snmp.SMISyntax;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 *
 * @author caronyn
 */
public class SmiSyntaxConverter extends AbstractSingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		return type.equals(int.class);
	}

	@Override
	public Object fromString(String str) {
		return SMISyntax.valueOf(str).getSmi();
	}

	@Override
	public String toString(Object obj) {
		int smi = (Integer) obj;
		SMISyntax s = SMISyntax.valueOf(smi);
		return super.toString(s);
	}
}
