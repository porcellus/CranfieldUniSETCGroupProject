/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astral;

import session.OptimizationResult;
import com.jcraft.jsch.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author madfist
 */
public class AstralControl implements OptimizationControl {    
    @Override
    public OptimizationResult readResults(String s1, String s2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void startSession(String username, String password) {
        JSch jsch = new JSch();
        
        try {
        session = jsch.getSession(username, host);
        UserInfo userInfo = new AstralUserInfo(password);
        
        session.setUserInfo(userInfo);
        session.connect(30000);
        channel = session.openChannel("shell");
        String command = "ls -la\n";
        InputStream input = new ByteArrayInputStream(command.getBytes());
        channel.setInputStream(input);
        channel.setOutputStream(System.out);
        channel.connect(30000);
        } catch (JSchException je) {
            System.out.println(je);
        }
    }

    @Override
    public void stopSession() {
        channel.disconnect();
        session.disconnect();
    }
    
    private Channel channel;
    private Session session;
    private final static String host = "hpcgate.cranfield.ac.uk";
}
