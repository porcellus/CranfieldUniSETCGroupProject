/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldopt;

import java.io.PrintStream;


/**
 *
 * @author a.koleszar@cranfield.ac.uk
 */
public class Optimizer {
    public Optimizer(PrintStream o) {
        minCamber = -20.0;
        maxCamber = 19.6;
        minThickness = 1.0;
        maxThickness = 19.81;
        minAngle = -20.0;
        maxAngle = 19.6;
        stepSize = 0.1;
        out = o;
    }
    
    public void set(double minc, double maxc, double mint, double maxt,
                    double mina, double maxa, double step) {
        minCamber = minc;
        maxCamber = maxc;
        minThickness = mint;
        maxThickness = maxt;
        minAngle = mina;
        maxAngle = maxa;
        stepSize = step;
    }
    
    //sequencial
    public OptimizationResult optimize() {
        OptimizationResult result = new OptimizationResult();
        double maxLiftDrag = -50.0;
        Solver solver = new Solver();
        double a = minAngle;
        double c = minCamber;
        double t = maxThickness;
        int i = 0;
        for (; t>minThickness; t-=stepSize) {
            solver.computeLiftDrag(a, c, t);
            if (solver.getLiftDrag() >= maxLiftDrag && solver.getDrag() > 0.0) {
                maxLiftDrag = solver.getLiftDrag();
                result.set(a, c, t, solver.getLift(), solver.getDrag());
            }
            if (i++ % 100 == 0) {
                out.println(result);
            }
        }
        out.println(result);
        for (; c<=maxCamber; c+=stepSize) {
            solver.computeLiftDrag(a, c, result.thickness);
            if (solver.getLiftDrag() >= maxLiftDrag && solver.getDrag() > 0.0) {
                maxLiftDrag = solver.getLiftDrag();
                result.set(a, c, result.thickness, solver.getLift(), solver.getDrag());
            }
            if (i++ % 100 == 0) {
                out.println(result);
            }
        }
        out.println(result);
        for (; a<=maxAngle; a+=stepSize) {
            solver.computeLiftDrag(a, result.camber, result.thickness);
            if (solver.getLiftDrag() >= maxLiftDrag && solver.getDrag() > 0.0) {
                maxLiftDrag = solver.getLiftDrag();
                result.set(a, result.camber, result.thickness, solver.getLift(), solver.getDrag());
            }
            if (i++ % 100 == 0) {
                out.println(result);
            }
        }
        out.println(result);
        return result;
    }
    
    private double minCamber;
    private double maxCamber;
    private double minThickness;
    private double maxThickness;
    private double minAngle;
    private double maxAngle;
    private double stepSize;
    private PrintStream out;
}

