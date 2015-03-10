/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import astral.AstralControl;
import astral.OptimizationControl;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import session.OptimizationResult;
import visualize.WingPanel;

/**
 *
 * @author madfist
 */
public class MainWindow extends JFrame {
    public MainWindow()
    {
        optControl = new AstralControl();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Group Project, YaaaaY");
        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");
        userField = new JTextField(16);
        passField = new JPasswordField(16);
        startButton = new JButton(start);
        stopButton = new JButton(stop);
        
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        add(label);
        JPanel userpassPanel = new JPanel(new GridLayout(2, 2));
        userpassPanel.add(userLabel);
        userpassPanel.add(userField);
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
    }
    
    private void start() {
        String user = userField.getText();
        String pass = passField.getText();
        if (user.length() > 0 && pass.length() > 0) {
            optControl.startSession(user, pass);
            lastResult = optControl.readResults("", "");
            System.out.println(lastResult);
            visualizer.setParameters(lastResult);
        } else {
            JOptionPane.showMessageDialog(
                    this, "Please give username or password",
                    "Missing information", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void stop() {
        visualizer.setParameters(Math.random()*19+1,Math.random()*20,Math.random()*30-15);
        optControl.stopSession();
    }
    
    private final OptimizationControl optControl;
    private final JButton startButton;
    private final JButton stopButton;
    private final WingPanel visualizer;
    private final JTextField userField;
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
}
