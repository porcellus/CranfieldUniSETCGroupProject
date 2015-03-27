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

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OptimizationResult other = (OptimizationResult) obj;
		if (Double.doubleToLongBits(angle) != Double
				.doubleToLongBits(other.angle))
			return false;
		if (Double.doubleToLongBits(camber) != Double
				.doubleToLongBits(other.camber))
			return false;
		if (Double.doubleToLongBits(drag) != Double
				.doubleToLongBits(other.drag))
			return false;
		if (Double.doubleToLongBits(lift) != Double
				.doubleToLongBits(other.lift))
			return false;
		if (Double.doubleToLongBits(thickness) != Double
				.doubleToLongBits(other.thickness))
			return false;
		return true;
	}
}
