/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency.xstreamConverter;

import com.thoughtworks.xstream.converters.enums.EnumSingleValueConverter;

/**
 *
 * @param <T>
 * @author caronyn
 */
public class EnumInstanceConverter<T extends Enum> extends EnumSingleValueConverter {

	private T instance;
	private String replace;

	@Override
	public Object fromString(String str) {
		if (str.equals(replace)) {
			return instance;
		} else {
			return super.fromString(str);
		}
	}

	@Override
	public String toString(Object obj) {
		T value = (T) obj;
		if (value == instance) {
			return replace;
		} else {
			return super.toString(obj);
		}
	}

	public EnumInstanceConverter(Class type, T instance, String replace) {
		super(type);
		this.instance = instance;
		this.replace = replace;
	}
}
