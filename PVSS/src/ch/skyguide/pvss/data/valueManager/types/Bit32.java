/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.valueManager.types;

import ch.skyguide.pvss.data.PVSSException;

/**
 *
 * @author caronyn
 */
public class Bit32 extends Binary {

    public Bit32(byte value) {
	super(value);
    }

    private static final byte[] validate(byte[] b) {
	if (b.length > 8) throw new PVSSException("Bad bit32 value, bit32 has 4 bits digits");
	return b;
    }

    public Bit32(byte[] value) {
	super(validate(value));
    }

}