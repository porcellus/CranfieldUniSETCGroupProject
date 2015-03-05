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
    
    public double getLiftDrag() {
        return lift / drag;
    }
}
