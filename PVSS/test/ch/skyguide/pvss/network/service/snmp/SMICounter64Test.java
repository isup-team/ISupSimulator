/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.snmp;

import org.junit.Test;
import org.snmp4j.smi.Counter64;

/**
 *
 * @author caronyn
 */
public class SMICounter64Test {

	GenericSMITest typeTest;

	/**
	 * Default constructor.
	 */
	public SMICounter64Test() {
		typeTest = new GenericSMITest(new SMICounter64());
	}

	/**
	 * Test of toVariable method, of class SMIInteger.
	 */ @Test
	public void testToVariable() {
		 typeTest.testToVariable(10L, new Counter64(10));
	}

	/**
	 * Test of toJava method, of class SMIInteger.
	 */ @Test
	public void testToJava() {
		 typeTest.testToJava(new Counter64(10), 10L);

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