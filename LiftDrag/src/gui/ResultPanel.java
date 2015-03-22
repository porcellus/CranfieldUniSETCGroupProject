/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import session.OptimizationResult;

/**
 *
 * @author madfist
 */
public class ResultPanel extends JPanel {
    public ResultPanel(OptimizationResult result) {
        JLabel angleLabel = new JLabel("Angle");
        JLabel camberLabel = new JLabel("Camber");
        JLabel thicknessLabel = new JLabel("Thickness");
        JLabel liftLabel = new JLabel("Lift");
        JLabel dragLabel = new JLabel("Drag");
        JLabel liftDragLabel = new JLabel("Lift/Drag");
        
        angleDisplay = new JLabel(Double.toString(result.angle));
        camberDisplay = new JLabel(Double.toString(result.camber));
        thicknessDisplay = new JLabel(Double.toString(result.thickness));
        liftDisplay = new JLabel(Double.toString(result.lift));
        dragDisplay = new JLabel(Double.toString(result.drag));
        liftDragDisplay = new JLabel(Double.toString(result.getLiftDrag()));
        
        setLayout(new GridLayout(2, 6));
        add(angleLabel);
        add(angleDisplay);
        add(camberLabel);
        add(camberDisplay);
        add(thicknessLabel);
        add(thicknessDisplay);
        add(liftLabel);
        add(liftDisplay);
        add(dragLabel);
        add(dragDisplay);
        add(liftDragLabel);
        add(liftDragDisplay);
    }
    
    public void updateResult(OptimizationResult result) {
        angleDisplay.setText(Double.toString(result.angle));
        camberDisplay.setText(Double.toString(result.camber));
        thicknessDisplay.setText(Double.toString(result.thickness));
        liftDisplay.setText(Double.toString(result.lift));
        dragDisplay.setText(Double.toString(result.drag));
        liftDragDisplay.setText(Double.toString(result.getLiftDrag()));
    }
    
    private final JLabel angleDisplay;
    private final JLabel camberDisplay;
    private final JLabel thicknessDisplay;
    private final JLabel liftDisplay;
    private final JLabel dragDisplay;
    private final JLabel liftDragDisplay;
}
