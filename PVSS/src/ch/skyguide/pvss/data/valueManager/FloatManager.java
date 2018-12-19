/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.valueManager;

import ch.skyguide.pvss.data.PVSSException;

/**
 *
 * @author caronyn
 */
public class FloatManager extends AbstractManager<Float> {

    @Override
    public Float fromDpl(String string) {
	try {
	    return Float.parseFloat(string);
	} catch (Exception ex) {
	    throw new PVSSException(String.format("Dpl String [%s] is not a valid PVSS Float", string));
	}
    }

    @Override
    public String toDpl(Float value) {
	return String.valueOf(value);
    }

}
