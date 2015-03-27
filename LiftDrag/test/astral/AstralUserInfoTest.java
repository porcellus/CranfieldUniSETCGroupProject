/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astral;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author madfist
 */
public class AstralUserInfoTest {
    private final AstralUserInfo userInfo;
    private final String password = "password";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final String eol = System.getProperty("line.separator");
    
    public AstralUserInfoTest() {
        userInfo = new AstralUserInfo(password);
    }
    
    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    
    @After
    public void tearDown() {
        System.setOut(null);
        System.setErr(null);
    }

    /**
     * Test of getPassphrase method, of class AstralUserInfo.
     */
    @Test
    public void testGetPassphrase() {
        String expResult = "";
        String result = userInfo.getPassphrase();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPassword method, of class AstralUserInfo.
     */
    @Test
    public void testGetPassword() {
        String expResult = password;
        String result = userInfo.getPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of promptPassword method, of class AstralUserInfo.
     */
    @Test
    public void testPromptPassword() {
        String message = "";
        boolean expResult = true;
        boolean result = userInfo.promptPassword(message);
        assertEquals(expResult, result);
        assertEquals("pass?"+eol, outContent.toString());
    }

    /**
     * Test of promptPassphrase method, of class AstralUserInfo.
     */
    @Test
    public void testPromptPassphrase() {
        String message = "";
        boolean expResult = false;
        boolean result = userInfo.promptPassphrase(message);
        assertEquals(expResult, result);
        assertEquals("passphrase?"+eol, outContent.toString());
    }

    /**
     * Test of promptYesNo method, of class AstralUserInfo.
     */
    @Test
    public void testPromptYesNo() {
        String message = "";
        boolean expResult = true;
        boolean result = userInfo.promptYesNo(message);
        assertEquals(expResult, result);
    }

    /**
     * Test of showMessage method, of class AstralUserInfo.
     */
    @Test
    public void testShowMessage() {
        String message = "message";
        userInfo.showMessage(message);
        assertEquals(message+eol, outContent.toString());
    }

    /**
     * Test of promptKeyboardInteractive method, of class AstralUserInfo.
     */
    @Test
    public void testPromptKeyboardInteractive() {
        String destination = "";
        String name = "";
        String instruction = "";
        String[] prompt = {"Password"};
        boolean[] echo = {false};
        String[] expResult = {password};
        String[] result = userInfo.promptKeyboardInteractive(destination, name, instruction, prompt, echo);
        assertArrayEquals(expResult, result);
        assertEquals("Skipping keyboard interactive password prompt"+eol, outContent.toString());
    }    
}
