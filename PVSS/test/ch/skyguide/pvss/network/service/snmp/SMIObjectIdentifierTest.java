/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.snmp;

import org.junit.Test;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

/**
 *
 * @author caronyn
 */
public class SMIObjectIdentifierTest {

	GenericSMITest typeTest;

	/**
	 * Default constructor.
	 */
	public SMIObjectIdentifierTest() {
		typeTest = new GenericSMITest(new SMIObjectIdentifier());
	}

	/**
	 * Test of toVariable method, of class SMIInteger.
	 */ @Test
	public void testToVariable() {
		 typeTest.testToVariable("1.3.6.1.4", new OID("1.3.6.1.4"));
	}

	/**
	 * Test of toJava method, of class SMIInteger.
	 */ @Test
	public void testToJava() {
		 typeTest.testToJava(new OID("1.3.6.1.4"), "1.3.6.1.4");

	}

	/**
	 * Test of getDefaultVariable method, of class SMIInteger.
	 */ @Test
	public void testGetDefaultVariable() {
		typeTest.testGetDefaultVariable();
	}

	/**
	 * Test of getDefaultJava method, of class SMIInteger.
	 */ @Test
	public void testGetDefaultJava() {
		 typeTest.testGetDefaultJava();
	}

}