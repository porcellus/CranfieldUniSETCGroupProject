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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;

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
        File jar = new File("LiftDrag.jar");
        //sendFile(jar);
        openShell();
    }

    @Override
    public void stopSession() {
        if (mainSession != null) {
            mainSession.disconnect();
        }
        if (tunnelSession != null) {
            tunnelSession.disconnect();
        }
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
            System.out.println("Tunnel: " + e);
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
            System.out.println("MainSession: " + e);
        }
    }

    private void sendFile(File localFile) {
        System.out.println("File...");
        try {
            mainChannel = mainSession.openChannel("exec");
            ((ChannelExec) mainChannel).setCommand("scp -p -t "+localFile.getName());
            OutputStream out = mainChannel.getOutputStream();
            InputStream in = mainChannel.getInputStream();

            mainChannel.connect();
            
            if (checkAck(in) != 0) {
                System.out.println("something's not rigth");
                return;
            }
            System.out.println("1");
            //timestamp
            String cmd = "T " + (localFile.lastModified()/1000) + " 0 " +
                                (localFile.lastModified()/1000) + " 0\n";
            out.write(cmd.getBytes());
            out.flush();
            
            if (checkAck(in) != 0) {
                System.out.println("something's not rigth");
                return;
            }
            System.out.println("2");
            //size
            long filesize = localFile.length();
            cmd = "C0644 " + filesize + " " + localFile.getName();
            out.write(cmd.getBytes());
            out.flush();
            
            if (checkAck(in) != 0) {
                System.out.println("something's not rigth");
                return;
            }
            System.out.println("3");
            //file
            FileInputStream f = new FileInputStream(localFile);
            byte[] buf = new byte[1024];
            while (true) {
                int len = f.read(buf, 0, buf.length);
                if (len <= 0) {
                    break;
                }
                out.write(buf, 0, buf.length);
            }
            f.close();
            // \0
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            
            if (checkAck(in) != 0) {
                System.out.println("something's not rigth");
                return;
            }
            System.out.println("4");
            out.close();
            mainChannel.disconnect();
        } catch (JSchException e) {
            System.out.println("sendFile - JSch:" + e);
        } catch (IOException e) {
            System.out.println("sendFile - IO: " + e);
        }
    }

    private int checkAck(InputStream in) throws IOException {
        int b = in.read();
    // b may be 0 for success,
    //          1 for error,
    //          2 for fatal error,
    //          -1
        if (b == 0) {
            return b;
        }
        if (b == -1) {
            return b;
        }
        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            } while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }

        return b;
    }

    private void openShell() {
        System.out.println("Shell...");
        try {
            mainChannel = mainSession.openChannel("shell");
            String command = "ls | grep LiftDrag\n";
            InputStream input = new ByteArrayInputStream(command.getBytes());
            mainChannel.setInputStream(input);
            mainChannel.setOutputStream(System.out);
            mainChannel.connect(30000);
        } catch (JSchException e) {
            System.out.println("MainChannel: " + e);
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
