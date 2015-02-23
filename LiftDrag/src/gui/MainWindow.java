/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author madfist
 */
public class MainWindow extends JFrame {
    public MainWindow()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Group Project, YaaaaY");
        add(label);
        pack();
    }    
}
