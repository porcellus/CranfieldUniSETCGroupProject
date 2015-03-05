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
//fake changes
public interface SessionControl {
    public abstract Session createSession(String username, String password);    
    public abstract Session loginSession(String username, String password);
    public abstract void setSessionParameters(Session s);
    public abstract void setResult(float result, int id);
    public abstract float getResult(int number, int id);
    
}
