/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astral;

import session.OptimizationResult;
import com.jcraft.jsch.*;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author madfist
 */
public class AstralControl implements OptimizationControl, Runnable {
    public AstralControl() {
        JLabel userLabel = new JLabel("username");
        JLabel passLabel = new JLabel("password");
        userField = new JTextField();
        passField = new JPasswordField();
        loginPanel = new JPanel(new GridLayout(2, 2));
        loginPanel.add(userLabel);
        loginPanel.add(userField);
        loginPanel.add(passLabel);
        loginPanel.add(passField);
        
        result = null;
    }

    @Override
    public OptimizationResult readResults() {
        //System.out.println("READ");
        OptimizationResult r = new OptimizationResult();
        if (result != null) {
            String[] tokens = result.split(" ");
            if (tokens.length != 6) {
                return null;
            }
            double[] data = new double[5];
            for (int i=0; i<5; ++i) {
                String[] s = tokens[i].split("=");
                data[i] = Double.parseDouble(s[1]);
            }
            r.set(data[0], data[1], data[2], data[3], data[4]);
        }
        return r;
    }

    @Override
    public void startSession(session.Session s) {
        jsch = new JSch();
        optSession = s;
        int ans = JOptionPane.showConfirmDialog(null, loginPanel,
                              "Login to Astral", JOptionPane.OK_CANCEL_OPTION);
        if (ans == JOptionPane.OK_OPTION) {
            username = userField.getText();
            password = passField.getText();
            //System.out.println("u "+username+" p "+password);
        } else {
            return;
        }
        thread = new Thread(this);
        thread.start();
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

    @Override
    public void run() {        
        openTunnelSession();
        //openMainSession();
        File jar = new File("../LdOpt/dist/LdOpt.jar");
        if (!jar.exists()) {
            System.out.println("No executable");
            stopSession();
            return;
        }
        File lib = new File("../LdOpt/dist/lib/commons-cli-1.2.jar");
        if (!lib.exists()) {
            System.out.println("No library");
            stopSession();
            return;
        }
        sendFile(jar, jar.getName(), tunnelSession);
        remoteMakeDir("lib", tunnelSession);
        sendFile(lib, "lib/"+lib.getName(), tunnelSession);
        openShell(tunnelSession);
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

    private void sendFile(File localFile, String rFile, Session session) {
        System.out.println("File...");
        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("scp -p -t "+rFile);
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();
            
            if (checkAck(in) != 0) {
                System.out.println("something's not rigth");
                return;
            }
            System.out.println("1");
            //timestamp
            String cmd = "T" + (localFile.lastModified()/1000) + " 0 " +
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
            cmd = "C0644 " + filesize + " " + localFile.getName()+"\n";
            System.out.print("size: "+filesize+" cmd: "+cmd);
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
            channel.disconnect();
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
    
    private void remoteMakeDir(String dir, Session session) {
        System.out.println("Dir...");
        try {
            Channel channel = session.openChannel("exec");
            String cmd = "mkdir "+dir+"\n";
            ((ChannelExec)channel).setCommand(cmd);
            channel.connect();
            channel.disconnect();
        } catch (JSchException e) {
            System.out.println("Exec: " + e);
        }
    }

    private void openShell(Session session) {
        System.out.println("Shell...");
        try {
            Channel channel = session.openChannel("exec");
            String command = "java -jar LdOpt.jar -s 0.1"+
                             " -a "+optSession.getMinangle()+
                             " -A "+optSession.getMaxangle()+
                             " -c "+optSession.getMincamber()+
                             " -C "+optSession.getMaxangle()+
                             " -t "+optSession.getMinthickness()+
                             " -T "+optSession.getMaxthickness()+"\n";
            ((ChannelExec)channel).setCommand(command.getBytes());
            InputStream in = channel.getInputStream();
            
            Scanner scanner = new Scanner(in);
            channel.connect(30000);
            while (scanner.hasNextLine()) {
                result = scanner.nextLine();
                System.out.println(result);
            }
            channel.disconnect();
        } catch (JSchException e) {
            System.out.println("Exec: " + e);
        } catch (IOException e) {
            System.out.println("Exec - IO: " + e);
        }
    }

    private JSch jsch;
    private Session tunnelSession;
    private Session mainSession;
    private String username;
    private String password;
    private volatile String result;
    private final static String host = "hpcgate.cranfield.ac.uk";
    private final static String localhost = "127.0.0.1";
    private final static String astral = "hpclogin-1.central.cranfield.ac.uk";
    private final static int localport = 18022;
    private final static int remoteport = 22;
    
    private final JTextField userField;
    private final JTextField passField;
    private final JPanel loginPanel;
    private session.Session optSession;
    private Thread thread;
}
