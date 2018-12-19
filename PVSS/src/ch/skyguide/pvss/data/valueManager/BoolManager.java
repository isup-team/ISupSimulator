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
public class BoolManager extends AbstractManager<Boolean> {

    public static final String STRING_TRUE = "1";
    public static final String STRING_FALSE = "0";

    @Override
    public Boolean fromDpl(String string) {
	if (string.equals(STRING_TRUE)) {
	    return true;
	} else if (string.equals(STRING_FALSE)) {
	    return false;
	}

	throw new PVSSException(String.format("Dpl String [%s] is not a valid PVSS Bool", string));
    }

    @Override
    public String toDpl(Boolean value) {
	if (value) {
	    return STRING_TRUE;
	} else {
	    return STRING_FALSE;
	}
    }

}
