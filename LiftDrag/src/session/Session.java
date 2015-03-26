/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import database.SQLiteConnection;
import database.SessionControl;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author madfist
 */
public class Session implements SessionInfo, Logging {    
    private final String name;
    private final String password;
    private final SessionControl control;
    private final int id;
    private double minangle;
    private double maxangle;
    private double minthickness;
    private double maxthickness;
    private double mincamber;
    private double maxcamber;
    
    public Session(String n, String pw, SessionControl ctrl, int ind) {
        name = n;
        password=pw;
        control = ctrl;
        id=ind;
    }
    
    public Session(SessionControl ctrl, int id, String username, String pwd, double minangle, double maxangle, double minthickness, double maxthickness, double mincamber, double maxcamber){
        this.id=id;
        this.name=username;
        this.password=pwd;
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
        return password;
    }

    @Override
    public String getSessionName() {
        return name;
    }

    @Override
    public List<OptimizationResult> getLog() {
        //database call through control
        int N = control.getIterationNum(id);
        ArrayList<OptimizationResult> log = new ArrayList<>();
        for (int i=0; i<N; ++i) {
            log.add(control.getResult(i, id));
        }
        return log;
    }

    @Override
    public List<OptimizationResult> getLog(int a, int b) {
        //database call through control
        ArrayList<OptimizationResult> log = new ArrayList<>();
        for (int i=a; i<b; ++i) {
            log.add(control.getResult(i, id));
        }
        return log;
    }
    
    public String getLogToString(int a, int b) {
        //database call through control
    	StringBuilder stringBuilder = new StringBuilder();

        for (int i=a, num=0; i<b; ++i, ++num) {
            stringBuilder.append("Iteration=" + num + " " + control.getResult(i, id) + "\n");
        }

       String log = stringBuilder.toString();
        return log;
    }

    @Override
    public void logResult(OptimizationResult r) {
        //database call through control
        control.setResult(r, id);
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
    
    public void setParameters(double minangle, double maxangle,
                              double mincamber, double maxcamber,
                              double minthickness, double maxthickness) {
        this.minangle=minangle;
        this.maxangle=maxangle;
        this.mincamber=mincamber;
        this.maxcamber=maxcamber;
        this.minthickness=minthickness;
        this.maxthickness=maxthickness;
    }
}