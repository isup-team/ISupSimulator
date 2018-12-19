/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.valueManager;

import ch.skyguide.pvss.data.valueManager.types.Binary;
import ch.skyguide.pvss.data.valueManager.types.Bit32;

/**
 *
 * @author caronyn
 */
public class Bit32Manager extends AbstractManager<Bit32> {

    private static final String PREFIX = "0x";

    @Override
    public Bit32 fromDpl(String string) {
	//try {
	    String v = string.substring(PREFIX.length());
	    return new Bit32(Binary.valueOfHex(v));
	/*} catch (Exception ex) {
	    throw new PVSSException(String.format("Dpl String [%s] is not a valid PVSS Bit32", string));
	}*/
    }

    @Override
    public String toDpl(Bit32 value) {
	return PREFIX + value.toHexString();
    }

}
