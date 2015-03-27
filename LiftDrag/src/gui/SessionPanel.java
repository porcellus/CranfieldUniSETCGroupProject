/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author madfist
 */
public class SessionPanel extends JPanel {
    public SessionPanel() {
        JLabel minAngleLabel = new JLabel("min Angle");
        JLabel maxAngleLabel = new JLabel("max Angle");
        JLabel minCamberLabel = new JLabel("min Camber");
        JLabel maxCamberLabel = new JLabel("max Camber");
        JLabel minThicknessLabel = new JLabel("min Thickness");
        JLabel maxThicknessLabel = new JLabel("max Thickness");
        minAngleField = new JTextField("0.0");
        maxAngleField = new JTextField("20.0");
        minCamberField = new JTextField("1.0");
        maxCamberField = new JTextField("20.0");
        minThicknessField = new JTextField("1.0");
        maxThicknessField = new JTextField("20.0");
        
        setLayout(new GridLayout(6, 2));
        add(minAngleLabel);
        add(minAngleField);
        add(maxAngleLabel);
        add(maxAngleField);
        add(minCamberLabel);
        add(minCamberField);
        add(maxCamberLabel);
        add(maxCamberField);
        add(minThicknessLabel);
        add(minThicknessField);
        add(maxThicknessLabel);
        add(maxThicknessField);
    }
    
    public double getMinAngle() {
        return Double.parseDouble(minAngleField.getText());
    }
    
    public double getMaxAngle() {
        return Double.parseDouble(maxAngleField.getText());
    }
    
    public double getMinCamber() {
        return Double.parseDouble(minCamberField.getText());
    }
    
    public double getMaxCamber() {
        return Double.parseDouble(maxCamberField.getText());
    }
    
    public double getMinThickness() {
        return Double.parseDouble(minThicknessField.getText());
    }
    
    public double getMaxThickness() {
        return Double.parseDouble(maxThicknessField.getText());
    }
    
    public void setParameters(double minangle, double maxangle,
                              double mincamber, double maxcamber,
                              double minthickness, double maxthickness) {
        minAngleField.setText(Double.toString(minangle));
        maxAngleField.setText(Double.toString(maxangle));
        minCamberField.setText(Double.toString(mincamber));
        maxCamberField.setText(Double.toString(maxcamber));
        minThicknessField.setText(Double.toString(minthickness));
        maxThicknessField.setText(Double.toString(maxthickness));
    }
    
    private final JTextField minAngleField;
    private final JTextField maxAngleField;
    private final JTextField minCamberField;
    private final JTextField maxCamberField;
    private final JTextField minThicknessField;
    private final JTextField maxThicknessField;
}
