/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml;

import ch.skyguide.pvss.network.service.convertTable.SystemStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author caronyn
 */
public class ISupSubSystemTest {

    public ISupSubSystemTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setLabel1 method, of class ISupSubSystem.
     */
    @Test
    public void testLabel() {
        ISupSubSystem subSystem = new ISupSubSystem("Test", SystemStatus.OPS);

        subSystem.setLabel1("label1");
        assertEquals("Label 1 ?", "label1", subSystem.getLabel1());
        assertEquals("nothing ?", "", subSystem.getLabel2());
        assertEquals("nothing ?", "", subSystem.getLabel3());

        subSystem.setLabel3("label3");
        assertEquals("Label 1 ?", "label1", subSystem.getLabel1());
        assertEquals("nothing ?", "", subSystem.getLabel2());
        assertEquals("Label 3 ?", "label3", subSystem.getLabel3());

        subSystem.setLabel2("label2");
        assertEquals("Label 1 ?", "label1", subSystem.getLabel1());
        assertEquals("Label 2 ?", "label2", subSystem.getLabel2());
        assertEquals("Label 3 ?", "label3", subSystem.getLabel3());

    }
}
