/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import astral.AstralControl;
import astral.OptimizationControl;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
        startButton = new JButton(start);
        stopButton = new JButton(stop);
        
        setLayout(new FlowLayout());
        add(label);
        add(startButton);
        add(stopButton);
        pack();
    }
    
    private void start() {
        optControl.startSession("s226144", "*****"); ///TODO add password, or handle prompt
    }
    
    private void stop() {
        optControl.stopSession();
    }
    
    private final OptimizationControl optControl;
    private final JButton startButton;
    private final JButton stopButton;
    
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
