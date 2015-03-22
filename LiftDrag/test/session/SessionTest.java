/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import database.SessionControl;
import java.util.ArrayList;
import java.util.List;
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
public class SessionTest {
    private Session session;
    private final SessionControl control;
    private final String name;
    private final String password;
    private final double min;
    private final double max;
    private final int index;
    private final OptimizationResult optResult;
    
    public SessionTest() {
        name = "name";
        password = "password";
        min = 1.0;
        max = 10.0;
        index = 0;
        control = new SessionControlImpl();
        optResult = new OptimizationResult();
        optResult.set(2.5, 3.5, 4.5, 4.4, 2.2);
        session = new Session(name, password, control, index);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        session.setParameters(min, max, min, max, min, max);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPassHash method, of class Session.
     */
    @Test
    public void testGetPassHash() {
        System.out.println("getPassHash");
        String expResult = password;
        String result = session.getPassHash();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSessionName method, of class Session.
     */
    @Test
    public void testGetSessionName() {
        System.out.println("getSessionName");
        String expResult = name;
        String result = session.getSessionName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLog method, of class Session.
     */
    @Test
    public void testGetLog_0args() {
        System.out.println("getLog");
        List<OptimizationResult> expResult = new ArrayList<>();
        expResult.add(new OptimizationResult());
        List<OptimizationResult> result = session.getLog();
        assertEquals(expResult.size(), result.size());
    }

    /**
     * Test of getLog method, of class Session.
     */
    @Test
    public void testGetLog_int_int() {
        System.out.println("getLog");
        List<OptimizationResult> expResult = new ArrayList<>();
        expResult.add(new OptimizationResult());
        List<OptimizationResult> result = session.getLog(0, 1);
        assertEquals(expResult.size(), result.size());
    }

    /**
     * Test of logResult method, of class Session.
     */
    @Test
    public void testLogResult() {
        System.out.println("logResult");
        session.logResult(optResult);
        assertEquals(optResult.thickness, ((SessionControlImpl)control).optResult.thickness, 0.0);
    }

    /**
     * Test of getMinangle method, of class Session.
     */
    @Test
    public void testGetMinangle() {
        System.out.println("getMinangle");
        double result = session.getMinangle();
        assertEquals(min, result, 0.0);
    }

    /**
     * Test of getMaxangle method, of class Session.
     */
    @Test
    public void testGetMaxangle() {
        System.out.println("getMaxangle");
        double result = session.getMaxangle();
        assertEquals(max, result, 0.0);
    }

    /**
     * Test of getMinthickness method, of class Session.
     */
    @Test
    public void testGetMinthickness() {
        System.out.println("getMinthickness");
        double result = session.getMinthickness();
        assertEquals(min, result, 0.0);
    }

    /**
     * Test of getMaxthickness method, of class Session.
     */
    @Test
    public void testGetMaxthickness() {
        System.out.println("getMaxthickness");
        double result = session.getMaxthickness();
        assertEquals(max, result, 0.0);
    }

    /**
     * Test of getMincamber method, of class Session.
     */
    @Test
    public void testGetMincamber() {
        System.out.println("getMincamber");
        double result = session.getMincamber();
        assertEquals(min, result, 0.0);
    }

    /**
     * Test of getMaxcamber method, of class Session.
     */
    @Test
    public void testGetMaxcamber() {
        System.out.println("getMaxcamber");
        double result = session.getMaxcamber();
        assertEquals(max, result, 0.0);
    }

    /**
     * Test of getId method, of class Session.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        int result = session.getId();
        assertEquals(index, result);
    }

    /**
     * Test of setParameters method, of class Session.
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
        session.setParameters(minangle, maxangle, mincamber, maxcamber, minthickness, maxthickness);
        assertEquals(session.getMinangle(), minangle, 0.0);
        assertEquals(session.getMaxangle(), maxangle, 0.0);
        assertEquals(session.getMincamber(), mincamber, 0.0);
        assertEquals(session.getMaxcamber(), maxcamber, 0.0);
        assertEquals(session.getMinthickness(), minthickness, 0.0);
        assertEquals(session.getMaxthickness(), maxthickness, 0.0);
    }
    
}

class SessionControlImpl implements SessionControl {
    public SessionControlImpl() {
        optResult = new OptimizationResult();
    }
    
    @Override
    public Session createSession(String username, String password) {
        return new Session(username, password, this, 0);
    }

    @Override
    public Session loginSession(String username, String password) {
        return new Session(username, password, this, 0);
    }

    @Override
    public void setSessionParameters(Session s) {
    }

    @Override
    public OptimizationResult getResult(int number, int id) {
        return new OptimizationResult();
    }

    @Override
    public void setResult(OptimizationResult result, int id) {
        this.optResult = result;
    }

    @Override
    public int getIterationNum(int id) {
        return 1;
    }
    
    public OptimizationResult optResult;
}
