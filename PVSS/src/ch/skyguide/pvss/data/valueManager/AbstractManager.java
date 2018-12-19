/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.valueManager;

/**
 *
 * @author caronyn
 */
public abstract class AbstractManager<T> {

    // TODO : Reflechir si besoin ici ou directement dans le ConfigValue (Generic)
    // value accessor
    public Object setValue(T value) {
	return (T) value;
    }

    public T getValue(Object value) {
	return (T) value;
    }

    // value parser
    public abstract T fromDpl(String string);
    public abstract String toDpl(T value);

}
