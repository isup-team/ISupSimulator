/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp;

import ch.skyguide.pvss.network.service.convertTable.TypeConverter;
import org.junit.Ignore;
import static org.junit.Assert.*;
import org.snmp4j.smi.Variable;

/**
 * Generic type test for all derived class.
 * @author caronyn
 */
@Ignore
public class GenericSMITest {

	private TypeConverter<Variable> instance;

	/**
	 * Default constructor.
	 * @param instance The type to test.
	 */
	public GenericSMITest(TypeConverter<Variable> instance) {
		this.instance = instance;
	}

	/**
	 * Test of toVariable method, of class SMIType.
	 */
	public void testToVariable(Object entry, Variable expected) {
		Variable result = instance.toVariable(entry);
		assertEquals(expected, result);
	}

	/**
	 * Test of getDefaultVariable method, of class SMIType.
	 */
	public void testGetDefaultVariable() {
		Object defaultJava = instance.getDefaultJava();
		Variable expResult = instance.toVariable(defaultJava);
		Variable result = instance.getDefaultVariable();
		assertEquals(expResult, result);
	}

	/**
	 * Test of toJava method, of class SMIType.
	 */
	public void testToJava(Variable entry, Object expected) {
		Object result = instance.toJava(entry);
		assertEquals(expected, result);
	}

	/**
	 * Test of getDefaultJava method, of class SMIType.
	 */
	public void testGetDefaultJava() {
		Variable defaultVariable = instance.getDefaultVariable();
		Object expResult = instance.toJava(defaultVariable);
		Object result = instance.getDefaultJava();
		assertEquals(expResult, result);
	}

}
