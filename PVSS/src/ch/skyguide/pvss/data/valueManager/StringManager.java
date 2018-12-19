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
public class StringManager extends AbstractManager<String> {

    private static final String FORMAT = "\"%s\"";

    @Override
    public String fromDpl(String string) {
	if (string.length() >= 2) {
	    return string.substring(1, string.length() -1);
	}

	throw new PVSSException(String.format("Dpl String [%s] is not a valid PVSS String", string));
    }

    @Override
    public String toDpl(String value) {
	return String.format(FORMAT, value);
    }

}
