/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.valueManager;

import ch.skyguide.pvss.data.PVSSException;
import ch.skyguide.pvss.data.valueManager.types.Binary;
import ch.skyguide.pvss.data.valueManager.types.Blob;

/**
 *
 * @author CyaNn
 */
public class BlobManager extends AbstractManager<Blob> {

	private static final String FORMAT = "\"%s\"";

	@Override
	public Blob fromDpl(String string) {
		try {
			if (string.length() >= 2) {
				return new Blob(Binary.valueOfHex(string.substring(1, string.length() - 1)));
			}
		} catch (Exception ex) {
		}
		throw new PVSSException(String.format("Dpl String [%s] is not a valid PVSS Blob", string));

	}

	@Override
	public String toDpl(Blob value) {
		return String.format(FORMAT, value.toHexString().toUpperCase());
	}
}
