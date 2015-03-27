/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.text.DecimalFormat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author madfist
 */
public class OptimizationResultTest {
    
    public OptimizationResultTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of set method, of class OptimizationResult.
     */
    @Test
    public void testSet() {
        System.out.println("set");
        double a = 2.0;
        double c = 3.0;
        double t = 4.0;
        double l = 5.0;
        double d = 6.0;
        OptimizationResult instance = new OptimizationResult();
        instance.set(a, c, t, l, d);
        assertEquals(a, instance.angle, 0.0);
        assertEquals(c, instance.camber, 0.0);
        assertEquals(t, instance.thickness, 0.0);
        assertEquals(l, instance.lift, 0.0);
        assertEquals(d, instance.drag, 0.0);
    }

    /**
     * Test of getLiftDrag method, of class OptimizationResult.
     */
    @Test
    public void testGetLiftDrag() {
        System.out.println("getLiftDrag");
        OptimizationResult instance = new OptimizationResult();
        instance.lift = 3.0;
        instance.drag = 1.0;
        double expResult = 3.0;
        double result = instance.getLiftDrag();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of toString method, of class OptimizationResult.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        OptimizationResult instance = new OptimizationResult();
        instance.drag = 1;
        char sep = (new DecimalFormat()).getDecimalFormatSymbols().getDecimalSeparator();
        String expResult = "Angle=0"+sep+"0000 "
                         + "Camber=1"+sep+"0000 "
                         + "Thickness=10"+sep+"0000 "
                         + "Lift=0"+sep+"0000 "
                         + "Drag=1"+sep+"0000 "
                         + "LD=0"+sep+"0000";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}