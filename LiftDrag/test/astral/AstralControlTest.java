/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astral;

import javax.swing.JFrame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import session.OptimizationResult;

/**
 *
 * @author madfist
 */
public class AstralControlTest {
    private final JFrame frame;
    private final AstralControl control;
    
    public AstralControlTest() {
        frame = new JFrame();
        control = new AstralControl(frame);
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getStatus method, of class AstralControl.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        int expResult = OptimizationControl.READY;
        int result = control.getStatus();
        assertEquals(expResult, result);
    }

    /**
     * Test of readResults method, of class AstralControl.
     */
    @Test
    public void testReadResults() {
        System.out.println("readResults");
        OptimizationResult expResult = new OptimizationResult();
        OptimizationResult result = control.readResults();
        assertEquals(expResult.angle, result.angle, 0.0);
    }    
}
