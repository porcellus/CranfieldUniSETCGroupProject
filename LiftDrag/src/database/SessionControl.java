/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import session.*;

/**
 *
 * @author madfist
 */
public interface SessionControl {
    public abstract Session createSession(String username, String password);    
    public abstract Session loginSession(String username, String password);
    public abstract void setSessionParameters(Session s);
    public abstract void setResult(OptimizationResult result, int id);
    public abstract OptimizationResult getResult(int number, int id);
    public abstract int maxIteration(int id);
}