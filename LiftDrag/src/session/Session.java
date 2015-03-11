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
//fake changeS
public class Session implements SessionInfo, Logging {    
    private String name;
    //private byte[] passwordHash;
    private String passwordHash;
    private SessionControl control;
    private int id;
    private double minangle;
    private double maxangle;
    private double minthickness;
    private double maxthickness;
    private double mincamber;
    private double maxcamber;
    
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
    
    public Session(SessionControl ctrl, int id, String username, String pwd, double minangle, double maxangle, double minthickness, double maxthickness, double mincamber, double maxcamber){
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

    public double getMinangle() {
        return minangle;
    }

    public double getMaxangle() {
        return maxangle;
    }

    public double getMinthickness() {
        return minthickness;
    }

    public double getMaxthickness() {
        return maxthickness;
    }

    public double getMincamber() {
        return mincamber;
    }

    public double getMaxcamber() {
        return maxcamber;
    }

    public int getId() {
        return id;
    }
    
    public void setParameters(double minangle, double maxangle, double minthickness, double maxthickness, double mincamber, double maxcamber){
        this.minangle=minangle;
        this.maxangle=maxangle;
        this.minthickness=minthickness;
        this.maxthickness=maxthickness;
        this.mincamber=mincamber;
        this.maxcamber=maxcamber;
    }
}