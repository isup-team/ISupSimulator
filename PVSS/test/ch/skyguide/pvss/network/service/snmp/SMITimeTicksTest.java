/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import org.snmp4j.smi.TimeTicks;

/**
 *
 * @author caronyn
 */
public class SMITimeTicksTest {

	GenericSMITest typeTest;

	/**
	 * Default constructor.
	 */
	public SMITimeTicksTest() {
		typeTest = new GenericSMITest(new SMITimeTicks());
	}

	/**
	 * Test of toVariable method, of class SMIInteger.
	 * @throws Exception Throw formating exception.
	 */
	@Test
	public void testToVariable() throws Exception {
		DateFormat timeF = new SimpleDateFormat(SMITimeTicks.FORMAT);
		Date time = timeF.parse("01:15:37.47");
		typeTest.testToVariable(timeF.format(time), new TimeTicks(time.getTime()));
	}

	/**
	 * Test of toJava method, of class SMIInteger.
	 * @throws Exception Throw formating exception.
	 */
	@Test
	public void testToJava() throws Exception {
		DateFormat timeF = new SimpleDateFormat(SMITimeTicks.FORMAT);
		Date time = timeF.parse("01:15:37.47");
		typeTest.testToJava(new TimeTicks(time.getTime()), timeF.format(time));
	}

	/**
	 * Test of getDefaultVariable method, of class SMIInteger.
	 */
	@Test
	public void testGetDefaultVariable() {
		// TODO à faire
		throw new RuntimeException("Not yet implemented !");
	}


	/**
	 * Test of getDefaultJava method, of class SMIInteger.
	 */
	@Test
	public void testGetDefaultJava() {
		typeTest.testGetDefaultJava();
	}
}
