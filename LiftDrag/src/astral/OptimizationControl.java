/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astral;

import session.OptimizationResult;

/**
 *
 * @author madfist
 */
public interface OptimizationControl {
    public abstract OptimizationResult readResults(String s1, String  s2);
    public abstract void startSession(String username, String password);
    public abstract void stopSession();
}
