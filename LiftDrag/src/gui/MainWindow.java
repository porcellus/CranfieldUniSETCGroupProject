/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import astral.AstralControl;
import astral.OptimizationControl;
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
        
        add(userpassPanel);
        add(buttonPanel);
        pack();
    }
    
    private void start() {
        String user = userField.getText();
        String pass = passField.getText();
        if (user.length() > 0 && pass.length() > 0) {
            optControl.startSession(user, pass);
        } else {
            JOptionPane.showMessageDialog(
                    this, "Please give username or password",
                    "Missing information", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void stop() {
        optControl.stopSession();
    }
    
    private final OptimizationControl optControl;
    private final JButton startButton;
    private final JButton stopButton;
    private JTextField userField;
    private JTextField passField;
    
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
