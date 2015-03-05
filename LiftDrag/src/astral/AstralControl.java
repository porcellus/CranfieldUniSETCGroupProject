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
        return new OptimizationResult();
    }

    @Override
    public void startSession(String user, String pass) {
        jsch = new JSch();
        username = user;
        password = pass;
        openTunnelSession();
        openMainSession();
        openShell();
    }

    @Override
    public void stopSession() {
        if (mainChannel != null) mainChannel.disconnect();
        if (mainSession != null) mainSession.disconnect();
        if (tunnelSession != null) tunnelSession.disconnect();
    }
    
    private void openTunnelSession() {
        System.out.println("Tunnel...");
        UserInfo userInfo = new AstralUserInfo(password);
        try {
            tunnelSession = jsch.getSession(username, host);            
            tunnelSession.setUserInfo(userInfo);
            tunnelSession.connect(30000);
            tunnelSession.setPortForwardingL(localport, astral, remoteport);
        } catch (JSchException e) {
            System.out.println("Tunnel: "+e);
        }
    }
    
    private void openMainSession() {
        System.out.println("Main...");
        UserInfo userInfo = new AstralUserInfo(password);
        try {
            mainSession = jsch.getSession(username, localhost, localport);
            mainSession.setUserInfo(userInfo);
            mainSession.connect(30000);
        } catch (JSchException e) {
            System.out.println("MainSession: "+e);
        }
    }
    
    private void openShell() {
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
    
    private JSch jsch;
    private Session tunnelSession;
    private Channel mainChannel;
    private Session mainSession;
    private String username;
    private String password;
    private final static String host = "hpcgate.cranfield.ac.uk";
    private final static String localhost = "127.0.0.1";
    private final static String astral = "hpclogin-1.central.cranfield.ac.uk";
    private final static int localport = 18022;
    private final static int remoteport = 22;
}
