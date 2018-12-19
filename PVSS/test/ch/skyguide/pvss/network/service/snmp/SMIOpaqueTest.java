/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.snmp;

import org.junit.Test;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Opaque;

/**
 *
 * @author caronyn
 */
public class SMIOpaqueTest {

	GenericSMITest typeTest;

	/**
	 * Default constructor.
	 */
	public SMIOpaqueTest() {
		typeTest = new GenericSMITest(new SMIOpaque());
	}

	/**
	 * Test of toVariable method, of class SMIInteger.
	 */ @Test
	public void testToVariable() {
		 byte[] bytes = {0x05, 0x07, -0x7F};
		 typeTest.testToVariable("05:07:81", new Opaque(bytes));
	}

	/**
	 * Test of toJava method, of class SMIInteger.
	 */ @Test
	public void testToJava() {
		 byte[] bytes = {0x05, 0x07, -0x7F};
		 typeTest.testToJava(new Opaque(bytes), "05:07:81");
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