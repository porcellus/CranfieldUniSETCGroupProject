/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

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
        thickness = 0.05;
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
        return "Angle="+angle+" Camber%="+camber+" Thickness%="+thickness+
               " Lift="+lift+" Drag"+drag;
    }

}
