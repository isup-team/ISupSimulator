/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.snmp;

import org.junit.Test;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;

/**
 *
 * @author caronyn
 */
public class SMIOctetStringTest {

	GenericSMITest typeTest;

	/**
	 * Default constructor.
	 */
	public SMIOctetStringTest() {
		typeTest = new GenericSMITest(new SMIOctetString());
	}

	/**
	 * Test of toVariable method, of class SMIInteger.
	 */ @Test
	public void testToVariable() {
		 typeTest.testToVariable("test", new OctetString("test"));
	}

	/**
	 * Test of toJava method, of class SMIInteger.
	 */ @Test
	public void testToJava() {
		 typeTest.testToJava(new OctetString("test"), "test");

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