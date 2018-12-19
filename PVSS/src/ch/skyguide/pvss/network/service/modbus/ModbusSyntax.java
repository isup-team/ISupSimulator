/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.modbus;

import ch.skyguide.pvss.network.service.convertTable.EnumConverter;
import ch.skyguide.pvss.network.service.convertTable.TypeConverter;

class CoilConverter implements TypeConverter<Object> {

    @Override
    public Class getVariableType() {
        return Boolean.class;
    }

    @Override
    public Object toVariable(Object value) {
        if (value instanceof String) {
            value = Boolean.parseBoolean((String) value);
        }
        return (Boolean) value;
    }

    @Override
    public Object getDefaultVariable() {
        return Boolean.FALSE;
    }

    @Override
    public Object toJava(Object value) {
        return value;
    }

    @Override
    public Object getDefaultJava() {
        return Boolean.FALSE;
    }

}

class RegisterConverter implements TypeConverter<Object> {

    @Override
    public Class getVariableType() {
        return Integer.class;
    }

    @Override
    public Object toVariable(Object value) {
        if (value instanceof String) {
            value = Integer.parseInt((String) value);
        }
        return (Integer) value;
    }

    @Override
    public Object getDefaultVariable() {
        return new Integer(4000);
    }

    @Override
    public Object toJava(Object value) {
        return (Integer) value;
    }

    @Override
    public Object getDefaultJava() {
        return new Integer(4000);
    }

}

public enum ModbusSyntax implements EnumConverter<Object> {

    COIL(new CoilConverter()),
    REGISTER(new RegisterConverter());
    TypeConverter<Object> converter;

    private ModbusSyntax(TypeConverter<Object> converter) {
        this.converter = converter;
    }

    @Override
    public Class getVariableType() {
        return converter.getVariableType();
    }

    @Override
    public EnumConverter find(Object value) {
        for (ModbusSyntax syn : values()) {
            if (syn.getVariableType().equals(value.getClass())) {
                return syn;
            }
        }
        throw new RuntimeException("Value not found !");
    }

    @Override
    public Object toVariable(Object value) {
        return converter.toVariable(value);
    }

    @Override
    public Object toJava(Object value) {
        return converter.toJava(value);
    }

    @Override
    public Object getDefaultVariable() {
        return converter.getDefaultVariable();
    }

    @Override
    public Object getDefaultJava() {
        return converter.getDefaultJava();
    }

    @Override
    public EnumConverter[] allValues() {
        return values();
    }
}
