/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astral;

import session.OptimizationResult;
import session.Session;

/**
 *
 * @author madfist
 */
public interface OptimizationControl {
    public abstract OptimizationResult readResults();
    public abstract void startSession(Session s);
    public abstract void stopSession();
}
