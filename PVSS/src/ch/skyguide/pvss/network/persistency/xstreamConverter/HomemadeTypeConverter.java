/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency.xstreamConverter;

import ch.skyguide.pvss.network.service.homemade.HMTypeEnum;
import ch.skyguide.pvss.network.service.homemade.HMVariant;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 *
 * @author caronyn
 */
public class HomemadeTypeConverter extends AbstractSingleValueConverter {

	HMTypeEnum type;

	@Override
	public boolean canConvert(Class cls) {
		return (type.getVariantClass().equals(cls));
	}

	@Override
	public String toString(Object obj) {
		return String.valueOf(type.toJava((HMVariant) obj));
	}

	@Override
	public Object fromString(String string) {
		return type.toVariant(string);
	}

	public HomemadeTypeConverter(HMTypeEnum type) {
		this.type = type;
	}
}
