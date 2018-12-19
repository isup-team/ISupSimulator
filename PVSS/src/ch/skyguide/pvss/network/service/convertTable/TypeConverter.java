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
public interface TypeConverter<T> {

	Class getVariableType();

	T toVariable(Object value);

	T getDefaultVariable();

	Object toJava(T value);

	Object getDefaultJava();

}
