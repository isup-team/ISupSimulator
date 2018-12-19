/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.valueManager;

import ch.skyguide.pvss.data.PVSSException;
import ch.skyguide.pvss.data.pattern.Path;

/**
 *
 * @author CyaNn
 */
public class DpidManager extends AbstractManager<Path> {

    @Override
    public Path fromDpl(String string) {
	try {
	    return new Path(string);
	} catch (Exception ex) {
	    throw new PVSSException(String.format("Dpl String [%s] is not a valid PVSS DPID", string));
	}
    }

    @Override
    public String toDpl(Path value) {
	return value.toString();
    }

}
