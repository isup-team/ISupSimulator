/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.snmp;

import org.junit.Test;
import org.snmp4j.smi.Null;

/**
 *
 * @author caronyn
 */
public class SMINullTest {

	GenericSMITest typeTest;

	/**
	 * Default constructor.
	 */
	public SMINullTest() {
		typeTest = new GenericSMITest(new SMINull());
	}

	/**
	 * Test of toVariable method, of class SMIInteger.
	 */ @Test
	public void testToVariable() {
		 typeTest.testToVariable(null, new Null());
	}

	/**
	 * Test of toJava method, of class SMIInteger.
	 */ @Test
	public void testToJava() {
		 typeTest.testToJava(new Null(), new Null().toString());
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