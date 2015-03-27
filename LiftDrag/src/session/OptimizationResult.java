/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author madfist
 */
public class OptimizationResult {
    public double angle;
    public double camber;
    public double thickness;
    public double lift;
    public double drag;
    
    public OptimizationResult() {
        angle = 0.0;
        camber = 1.0;
        thickness = 10.0;
        lift = 0.0;
        drag = 0.0;
    }
    
    public void set(double a, double c, double t, double l, double d) {
        angle = a;
        camber = c;
        thickness = t;
        lift = l;
        drag = d;
    }
    
    public double getLiftDrag() {
        return lift / drag;
    }
    
    @Override
    public String toString() {
        NumberFormat f = new DecimalFormat("#0.0000");
        return "Angle="+f.format(angle)+" Camber="+f.format(camber)+
               " Thickness="+f.format(thickness)+
               " Lift="+f.format(lift)+" Drag="+f.format(drag)+
               " LD="+f.format(getLiftDrag());
    }
}
