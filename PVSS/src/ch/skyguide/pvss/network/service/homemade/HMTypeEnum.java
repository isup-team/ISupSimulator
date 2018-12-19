/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.homemade;

import ch.skyguide.pvss.network.service.homemade.HMStatusEnum;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.snmp4j.util.EnumerationIterator;

/**
 *
 * @author caronyn
 */
interface HMType<T> {

	Class getVariantClass();

	Class getJavaClass();

	HMVariant toVariant(Object value);

	HMVariant getDefaultVariant();

	Object toJava(HMVariant value);

	Object getDefaultJava();

	T[] getValues();

}

class HMTypeStatus implements HMType<HMStatusEnum> {

	@Override
	public Class getVariantClass() {
		return HMStatus.class;
	}

	@Override
	public Class getJavaClass() {
		return HMStatusEnum.class;
	}

	@Override
	public HMVariant toVariant(Object value) {
		if (value instanceof String) {
			value = HMStatusEnum.valueOf((String) value);
		}

		return new HMStatus((HMStatusEnum) value);
	}

	@Override
	public HMVariant getDefaultVariant() {
		return new HMStatus();
	}

	@Override
	public Object toJava(HMVariant value) {
		return ((HMStatus) value).getValue();
	}

	@Override
	public Object getDefaultJava() {
		return new HMStatus().getValue();
	}

	@Override
	public HMStatusEnum[] getValues() {
		return HMStatusEnum.values();
	}

}

class HMTypeLabel implements HMType<String> {

	@Override
	public Class getVariantClass() {
		return HMLabel.class;
	}

	@Override
	public Class getJavaClass() {
		return String.class;
	}

	@Override
	public HMVariant toVariant(Object value) {
		return new HMLabel((String) value);
	}

	@Override
	public HMVariant getDefaultVariant() {
		return new HMLabel();
	}

	@Override
	public Object toJava(HMVariant value) {
		return value.toString();
	}

	@Override
	public Object getDefaultJava() {
		return new HMLabel().toString();
	}

	@Override
	public String[] getValues() {
		return null;
	}

}

class HMTypeChain implements HMType<Integer> {

	@Override
	public Class getVariantClass() {
		return HMChanel.class;
	}

	@Override
	public Class getJavaClass() {
		return Integer.class;
	}

	@Override
	public HMVariant toVariant(Object value) {
		if (value instanceof String) {
			value = Integer.valueOf((String) value);
		}

		return new HMChanel((Integer) value);
	}

	@Override
	public HMVariant getDefaultVariant() {
		return new HMChanel();
	}

	@Override
	public Object toJava(HMVariant value) {
		return ((HMChanel) value).getValue();
	}

	@Override
	public Object getDefaultJava() {
		return new HMChanel().getValue();
	}

	@Override
	public Integer[] getValues() {
		Integer[] array = {1, 2, 3, 4};
		return array;
	}
}

class HMTypeSwitch implements HMType<HMSwitchEnum> {

	@Override
	public Class getVariantClass() {
		return HMSwitch.class;
	}

	@Override
	public Class getJavaClass() {
		return HMSwitchEnum.class;
	}

	@Override
	public HMVariant toVariant(Object value) {
		if (value instanceof String) {
			value = HMSwitchEnum.valueOf((String) value);
		}

		return new HMSwitch((HMSwitchEnum) value);
	}

	@Override
	public HMVariant getDefaultVariant() {
		return new HMSwitch();
	}

	@Override
	public Object toJava(HMVariant value) {
		return ((HMSwitch) value).getValue();
	}

	@Override
	public Object getDefaultJava() {
		return new HMSwitch().getValue();
	}

	@Override
	public HMSwitchEnum[] getValues() {
		return HMSwitchEnum.values();
	}
}

public enum HMTypeEnum {

	STATUS(new HMTypeStatus()),
	LABEL(new HMTypeLabel()),
	CHANEL(new HMTypeChain()),
	SWITCH(new HMTypeSwitch());

	private HMType typeConverter;

	public Class<?> getVariantClass() {
		return typeConverter.getVariantClass();
	}

	public Class<?> geJavaClass() {
		return typeConverter.getJavaClass();
	}

	public Object toJava(HMVariant variant) {
		return typeConverter.toJava(variant);
	}

	public HMVariant toVariant(Object obj) {
		return typeConverter.toVariant(obj);
	}

	public Object[] getValues() {
		return typeConverter.getValues();
	}

	public Object getDefaultJava() {
		return typeConverter.getDefaultJava();
	}

	public HMVariant getDefaultType() {
		return typeConverter.getDefaultVariant();
	}

	private HMTypeEnum(HMType typeConverter) {
		this.typeConverter = typeConverter;
	}
}
