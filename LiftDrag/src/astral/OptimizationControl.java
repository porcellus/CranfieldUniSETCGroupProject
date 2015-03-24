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
    public static final int READY = 0;
    public static final int CONNECTING = 1;
    public static final int RUNNING = 2;
    public abstract OptimizationResult readResults();
    public abstract void startSession(Session s);
    public abstract void stopSession();
    public abstract int getStatus();
}
