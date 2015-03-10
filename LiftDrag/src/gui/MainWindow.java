/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import astral.AstralControl;
import astral.OptimizationControl;
import java.awt.Graphics;
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
        optControl = new AstralControl();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Group Project, YaaaaY");
        JLabel sessionLabel = new JLabel("Session name");
        JLabel passLabel = new JLabel("Password");
        sessionField = new JTextField(16);
        passField = new JPasswordField(16);
        startButton = new JButton(start);
        stopButton = new JButton(stop);
        
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        add(label);
        JPanel userpassPanel = new JPanel(new GridLayout(2, 2));
        userpassPanel.add(sessionLabel);
        userpassPanel.add(sessionField);
        userpassPanel.add(passLabel);
        userpassPanel.add(passField);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        visualizer = new WingPanel();        
        
        add(userpassPanel);
        add(buttonPanel);
        add(visualizer);
        visualizer.start();
        pack();
        
        Timer timer = new Timer(100, this);
        timer.start();
    }
    
    private void start() {
        String user = sessionField.getText();
        String pass = passField.getText();
        if (user.length() > 0 && pass.length() > 0) {
            Session session = new Session(null, 0, "user", "pass", 0.0, 20.0, 1.0, 20.0, 1.0, 20.0);
            optControl.startSession(session); //should be session given
            //lastResult = optControl.readResults("", "");
            //System.out.println(lastResult);
            //visualizer.setParameters(lastResult);
        } else {
            JOptionPane.showMessageDialog(
                    this, "Please give username or password",
                    "Missing information", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void stop() {
        visualizer.setParameters(optControl.readResults());
        optControl.stopSession();
    }
    
    private final OptimizationControl optControl;
    private final JButton startButton;
    private final JButton stopButton;
    private final WingPanel visualizer;
    private final JTextField sessionField;
    private final JTextField passField;
    private OptimizationResult lastResult;
    
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

    @Override
    public void actionPerformed(ActionEvent e) {
        visualizer.setParameters(optControl.readResults());
        repaint();
    }
}
