/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author madfist
 */
public class SessionPanelTest {
    
    public SessionPanelTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getMinAngle method, of class SessionPanel.
     */
    @Test
    public void testGetMinAngle() {
        System.out.println("getMinAngle");
        SessionPanel instance = new SessionPanel();
        double expResult = 0.0;
        double result = instance.getMinAngle();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMaxAngle method, of class SessionPanel.
     */
    @Test
    public void testGetMaxAngle() {
        System.out.println("getMaxAngle");
        SessionPanel instance = new SessionPanel();
        double expResult = 0.0;
        double result = instance.getMaxAngle();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMinCamber method, of class SessionPanel.
     */
    @Test
    public void testGetMinCamber() {
        System.out.println("getMinCamber");
        SessionPanel instance = new SessionPanel();
        double expResult = 0.0;
        double result = instance.getMinCamber();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMaxCamber method, of class SessionPanel.
     */
    @Test
    public void testGetMaxCamber() {
        System.out.println("getMaxCamber");
        SessionPanel instance = new SessionPanel();
        double expResult = 0.0;
        double result = instance.getMaxCamber();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMinThickness method, of class SessionPanel.
     */
    @Test
    public void testGetMinThickness() {
        System.out.println("getMinThickness");
        SessionPanel instance = new SessionPanel();
        double expResult = 0.0;
        double result = instance.getMinThickness();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMaxThickness method, of class SessionPanel.
     */
    @Test
    public void testGetMaxThickness() {
        System.out.println("getMaxThickness");
        SessionPanel instance = new SessionPanel();
        double expResult = 0.0;
        double result = instance.getMaxThickness();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParameters method, of class SessionPanel.
     */
    @Test
    public void testSetParameters() {
        System.out.println("setParameters");
        double minangle = 0.0;
        double maxangle = 0.0;
        double mincamber = 0.0;
        double maxcamber = 0.0;
        double minthickness = 0.0;
        double maxthickness = 0.0;
        SessionPanel instance = new SessionPanel();
        instance.setParameters(minangle, maxangle, mincamber, maxcamber, minthickness, maxthickness);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
