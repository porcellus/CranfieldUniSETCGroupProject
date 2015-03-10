/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import database.SessionControl;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
/**
 *
 * @author madfist
 */
public class Session implements SessionInfo, Logging {    
    private String name;
    //private byte[] passwordHash;
    private String passwordHash;
    private SessionControl control;
    private int id;
    private float minangle;
    private float maxangle;
    private float minthickness;
    private float maxthickness;
    private float mincamber;
    private float maxcamber;
    
    public Session(String n, String pw, SessionControl ctrl, int ind) {
        name = n;
        /*try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pw.getBytes());
            passwordHash = md.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("no md5");
        }*/
        passwordHash=pw;
        control = ctrl;
        id=ind;
    }
    
    public Session(SessionControl ctrl, int id, String username, String pwd, float minangle, float maxangle, float minthickness, float maxthickness, float mincamber, float maxcamber){
        this.id=id;
        this.name=username;
        this.passwordHash=pwd;
        this.minangle=minangle;
        this.maxangle=maxangle;
        this.minthickness=minthickness;
        this.maxthickness=maxthickness;
        this.mincamber=mincamber;
        this.maxcamber=maxcamber;
        control = ctrl;
    }
    
    @Override
    public String getPassHash() {        
        //return new String(passwordHash);
        return passwordHash;
    }

    @Override
    public String getSessionName() {
        return name;
    }

    @Override
    public List<OptimizationResult> getLog() {
        //database call through control
        //parsing
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OptimizationResult> getLog(int a, int b) {
        //database call through control
        //parsing
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void logResult(OptimizationResult r) {
        //database call through control
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public float getMinangle() {
        return minangle;
    }

    public float getMaxangle() {
        return maxangle;
    }

    public float getMinthickness() {
        return minthickness;
    }

    public float getMaxthickness() {
        return maxthickness;
    }

    public float getMincamber() {
        return mincamber;
    }

    public float getMaxcamber() {
        return maxcamber;
    }

    public int getId() {
        return id;
    }
    
    public void setParameters(float minangle, float maxangle, float minthickness, float maxthickness, float mincamber, float maxcamber){
        this.minangle=minangle;
        this.maxangle=maxangle;
        this.minthickness=minthickness;
        this.maxthickness=maxthickness;
        this.mincamber=mincamber;
        this.maxcamber=maxcamber;
    }
}