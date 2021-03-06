/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.snmp;

import org.junit.Test;
import org.snmp4j.smi.UnsignedInteger32;

/**
 *
 * @author caronyn
 */
public class SMIUnsignedInteger32Test {

	GenericSMITest typeTest;

	/**
	 * Default constructor.
	 */
	public SMIUnsignedInteger32Test() {
		typeTest = new GenericSMITest(new SMIUnsignedInteger32());
	}

	/**
	 * Test of toVariable method, of class SMIInteger.
	 */ @Test
	public void testToVariable() {
		 typeTest.testToVariable(10, new UnsignedInteger32(10));
	}

	/**
	 * Test of toJava method, of class SMIInteger.
	 */ @Test
	public void testToJava() {
		 typeTest.testToJava(new UnsignedInteger32(10), 10);

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