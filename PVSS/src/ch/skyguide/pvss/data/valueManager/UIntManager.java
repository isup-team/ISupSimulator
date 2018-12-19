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
public class UIntManager extends IntManager {

    @Override
    public Integer fromDpl(String string) {
	Integer value = super.fromDpl(string);
	if (value >= 0) return value;
	throw new PVSSException(String.format("Dpl String [%s] is not a valid PVSS UInt", string));
    }

}
