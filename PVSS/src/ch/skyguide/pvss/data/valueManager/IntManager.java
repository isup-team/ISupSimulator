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
public class IntManager extends AbstractManager<Integer> {

    @Override
    public Integer fromDpl(String string) {
	try {
	    return Integer.parseInt(string);
	} catch (Exception ex) {
	    throw new PVSSException(String.format("Dpl String [%s] is not a valid PVSS Int", string));
	}
    }

    @Override
    public String toDpl(Integer value) {
	return String.valueOf(value);
    }

}
