/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp;

import org.snmp4j.smi.OID;

/**
 *
 * @author caronyn
 */
public final class OIDUtils {

	public static OID DEFAULT_PREFIX = new OID("1.3.6.1.4.1");

	/**
	 * Concat two OIDs.
	 * @param oid1 First OID to concat.
	 * @param oid2 Second OID to concat.
	 * @return Return the two OID concatenation.
	 */
	public static OID concatOID(final OID oid1, final OID oid2) {
		OID newOID = new OID();
		newOID.append(oid1);
		newOID.append(oid2);
		return newOID;
	}

}
