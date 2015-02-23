/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.List;

/**
 *
 * @author madfist
 */
public abstract class Logging {
    public abstract List<OptimizationResult> getLog();    
    public abstract List<OptimizationResult> getLog(int a, int b);    
    public abstract void logResult(OptimizationResult r);
}
