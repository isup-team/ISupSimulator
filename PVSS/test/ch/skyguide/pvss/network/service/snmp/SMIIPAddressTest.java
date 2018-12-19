/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.snmp;

import org.junit.Test;
import org.snmp4j.smi.IpAddress;

/**
 *
 * @author caronyn
 */
public class SMIIPAddressTest {

	GenericSMITest typeTest;

	/**
	 * Default constructor.
	 */
	public SMIIPAddressTest() {
		typeTest = new GenericSMITest(new SMIIPAddress());
	}

	/**
	 * Test of toVariable method, of class SMIInteger.
	 */ @Test
	public void testToVariable() {
		 typeTest.testToVariable("127.0.0.1", new IpAddress("127.0.0.1"));
	}

	/**
	 * Test of toJava method, of class SMIInteger.
	 */ @Test
	public void testToJava() {
		 typeTest.testToJava(new IpAddress("127.0.0.1"), "127.0.0.1");

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