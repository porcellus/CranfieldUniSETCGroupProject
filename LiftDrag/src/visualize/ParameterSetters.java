/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualize;

import session.OptimizationResult;

/**
 *
 * @author madfist
 */
public interface ParameterSetters {
    public abstract void setParameters(OptimizationResult result);    
    public abstract void setParameters(double t, double r, double th);
}
