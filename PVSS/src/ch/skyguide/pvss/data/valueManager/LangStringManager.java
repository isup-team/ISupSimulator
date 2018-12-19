/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.valueManager;

import ch.skyguide.pvss.data.PVSSException;

/**
 *
 * @author CyaNn
 */
public class LangStringManager extends AbstractManager<String> {

    private static final String FORMAT = "lt:1 LANG:1 \"%s\"";

    @Override
    public String fromDpl(String string) {
	if (string.length() >= 14) {
	    return string.substring(13, string.length() -1);
	}

	throw new PVSSException(String.format("Dpl String [%s] is not a valid PVSS Lang String", string));
    }

    @Override
    public String toDpl(String value) {
	return String.format(FORMAT, value);
    }

}
