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
        System.out.println("Tunnel...");
        JSch tunnelJSch = new JSch();
        JSch mainJSch = new JSch();
        UserInfo tunnelUserInfo = new AstralUserInfo(password);
        UserInfo mainUserInfo = new AstralUserInfo(password);
        try {
            tunnelSession = tunnelJSch.getSession(username, host);            
            tunnelSession.setUserInfo(tunnelUserInfo);
            tunnelSession.connect(30000);
            tunnelSession.setPortForwardingL(localport, astral, remoteport);
        } catch (JSchException e) {
            System.out.println("Tunnel: "+e);
        }
        
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        try {
            System.out.println("Main...");
            mainSession = tunnelSession = tunnelJSch.getSession(username, localhost, localport);
            mainSession.setUserInfo(mainUserInfo);
            mainSession.connect(30000);
        } catch (JSchException e) {
            System.out.println("MainSession: "+e);
            return;
        }
        
        try {
            mainChannel = mainSession.openChannel("shell");
            String command = "ls -la\n";
            InputStream input = new ByteArrayInputStream(command.getBytes());
            mainChannel.setInputStream(input);
            mainChannel.setOutputStream(System.out);
            mainChannel.connect(30000);
        } catch (JSchException e) {
            System.out.println("MainChannel: "+e);
        }
    }

    @Override
    public void stopSession() {
        mainChannel.disconnect();
        mainSession.disconnect();
        tunnelSession.disconnect();
    }
    
    private Session tunnelSession;
    private Channel mainChannel;
    private Session mainSession;
    private final static String host = "hpcgate.cranfield.ac.uk";
    private final static String localhost = "127.0.0.1";
    private final static String astral = "hpclogin-1.central.cranfield.ac.uk";
    private final static int localport = 18022;
    private final static int remoteport = 22;
}
