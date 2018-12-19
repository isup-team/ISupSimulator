/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency.xstreamConverter;

import ch.skyguide.pvss.network.service.xml.ISupLabel;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 *
 * @author caronyn
 */
public class LabelConverter implements Converter {

	@Override
	public boolean canConvert(Class type) {
		return type.equals(ISupLabel.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		ISupLabel label = (ISupLabel) source;
		writer.addAttribute("Name", label.getName());
                
                String value = label.getValue();
                if ("[empty]".equalsIgnoreCase(value))
                    writer.setValue("");
                else                
                    writer.setValue(value);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String name = reader.getAttribute("Name");
		String value = reader.getValue();
		return new ISupLabel(name, value);
	}
}
