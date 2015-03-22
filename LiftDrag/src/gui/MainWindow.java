/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import astral.AstralControl;
import astral.OptimizationControl;
import database.SQLiteConnection;
import database.SessionControl;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import session.OptimizationResult;
import session.Session;
import visualize.WingPanel;

/**
 *
 * @author madfist
 */
public class MainWindow extends JFrame implements ActionListener {
    public MainWindow()
    {
        optControl = new AstralControl(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Group Project, Alpha");
        
        JLabel sessionLabel = new JLabel("Session name");
        JLabel passLabel = new JLabel("Password");
        sessionField = new JTextField(16);
        passField = new JPasswordField(16);
        startButton = new JButton(start);
        stopButton = new JButton(stop);
        createButton = new JButton(create);
        loginButton = new JButton(login);
        
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        JPanel userpassPanel = new JPanel(new GridLayout(2, 2));
        userpassPanel.add(sessionLabel);
        userpassPanel.add(sessionField);
        userpassPanel.add(passLabel);
        userpassPanel.add(passField);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(createButton);
        buttonPanel.add(loginButton);
        
        startStopButtonPanel = new JPanel(new GridLayout(1, 2));
        startStopButtonPanel.add(startButton);
        startStopButtonPanel.add(stopButton);
        
        visualizer = new WingPanel();
        
        sessionPanel = new SessionPanel();
        resultPanel = new ResultPanel(optControl.readResults());
        
        add(userpassPanel);
        add(buttonPanel);
        pack();
        
        Timer timer = new Timer(100, this);
        timer.start();
        sessionControl = new SQLiteConnection(this);
        SQLiteConnection.connection();
        session = null;
        started = false;
    }
    
    private void start() {
        if (session == null) return;
            session.setParameters(sessionPanel.getMinAngle(),
                                  sessionPanel.getMaxAngle(),
                                  sessionPanel.getMinCamber(),
                                  sessionPanel.getMaxCamber(),
                                  sessionPanel.getMinThickness(),
                                  sessionPanel.getMaxThickness());
            sessionControl.setSessionParameters(session);
            add(visualizer);
            add(resultPanel);
            visualizer.start();
            pack();
        started = true;
        optControl.startSession(session); //should be session given
    }
    
    private void stop() {
        visualizer.setParameters(optControl.readResults());
        optControl.stopSession();
    }
    
    private void create() {
        String user = sessionField.getText();
        String pass = passField.getText();
        if (user.length() > 0 && pass.length() > 0) {
            session = sessionControl.createSession(user, pass);
            if (session == null) return;
        } else {
            JOptionPane.showMessageDialog(
                    this, "Please give username or password",
                    "Missing information", JOptionPane.ERROR_MESSAGE);
        }
        add(sessionPanel);
        add(startStopButtonPanel);
        pack();
        
    }
    
    private void login() {
        String user = sessionField.getText();
        String pass = passField.getText();
        if (user.length() > 0 && pass.length() > 0) {
            session = sessionControl.loginSession(user, pass);
            if (session == null) {
                System.out.println("HEHE");
                return;
            }
            sessionPanel.setParameters(session.getMinangle(), session.getMaxangle(),
                                       session.getMincamber(), session.getMaxcamber(),
                                       session.getMinthickness(), session.getMaxthickness());
        } else {
            JOptionPane.showMessageDialog(
                    this, "Please give username or password",
                    "Missing information", JOptionPane.ERROR_MESSAGE);
        }
        add(sessionPanel);
        add(startStopButtonPanel);
        pack();
    }
    
    private final OptimizationControl optControl;
    private final ResultPanel resultPanel;
    private final SessionPanel sessionPanel;
    private final JPanel startStopButtonPanel;
    private final JButton startButton;
    private final JButton stopButton;
    private final JButton createButton;
    private final JButton loginButton;
    private final WingPanel visualizer;
    private final JTextField sessionField;
    private final JTextField passField;
    private final SessionControl sessionControl;
    private Session session;
    private boolean started;
    
    private final AbstractAction start = new AbstractAction("Start") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            start();
        }
    };
    
    private final AbstractAction stop = new AbstractAction("Stop") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            stop();
        }
    };
    
    private final AbstractAction create = new AbstractAction("Create") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            create();
        }
    };
    
    private final AbstractAction login = new AbstractAction("Login") {  
        @Override
        public void actionPerformed(ActionEvent ae) {
            login();
        }
    };

    @Override
    public void actionPerformed(ActionEvent e) {
        if (optControl.getStatus() == OptimizationControl.RUNNING) {
            visualizer.setParameters(optControl.readResults());
            resultPanel.updateResult(optControl.readResults());
            if (session != null) session.logResult(optControl.readResults());
            repaint();
        }
    }
}
