/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.convertTable;

/**
 *
 * @param <T>
 * @author caronyn
 */
public interface EnumConverter<T> {

	public Class getVariableType();
	public EnumConverter find(T value);

	public T toVariable(Object value);

	public Object toJava(T value);

	public T getDefaultVariable();

	public Object getDefaultJava();

	public EnumConverter[] allValues();


}
