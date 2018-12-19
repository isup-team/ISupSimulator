/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency.xstreamConverter;

import ch.skyguide.pvss.network.service.snmp.SMISyntax;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.snmp4j.smi.Opaque;
import org.snmp4j.smi.Variable;

/**
 *
 * @author root
 */
public class SmiTypeConverter extends AbstractSingleValueConverter {

	SMISyntax syntax;

	@Override
	public boolean canConvert(Class type) {
		return (type.equals(syntax.getVariableType()));
	}

	@Override
	public String toString(Object obj) {
		return String.valueOf(syntax.toJava((Variable) obj));
	}

	@Override
	public Object fromString(String string) {
		return syntax.toVariable(string);
	}

	public SmiTypeConverter(SMISyntax syntax) {
		this.syntax = syntax;
	}

}
