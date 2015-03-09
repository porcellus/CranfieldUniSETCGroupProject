/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package optimizer;

/**
 *
 * @author madfist
 */
public class Solver {

    public Solver() {
        nlnc = 15;
        nln2 = nlnc / 2 + 1;
        nptc = 37;
        npt2 = nptc / 2 + 1;
        deltb = .5;
        thkval = .5;

        camval = 0.0;
        angle = 5.0;
        gamval = 0.0;
        rval = 1.0;
        ycval = 0.0;
        xcval = 0.0;
        xflow = -10.0;

        aspr = 4.0;
    }
    
    public void computeLiftDrag(double a, double c, double th) {
        camber = c;
        camval = c / 25.0;
        angle = (camber < 0.0) ? -a : a; //symmetry
        thickness = th;
        thkval = th / 25.0;
        
        computeCirculation();
        liftCoeff = computeLift();
        dragCoeff = computeDrag();
    }
    
    public double getLift() {
        return liftCoeff;
    }
    
    public double getDrag() {
        return dragCoeff;
    }
    
    public double getLiftDrag() {
        return liftCoeff / dragCoeff;
    }
    
    private double getVel(double rad, double theta) {  //velocity and pressure 
        double ur, uth;
        double thrad, alfrad;

        thrad = radDegConv * theta;
        alfrad = radDegConv * angle;
        /* velocity in cylinder plane */
        ur = Math.cos(thrad - alfrad) * (1.0 - (rval * rval) / (rad * rad));
        uth = -Math.sin(thrad - alfrad) * (1.0 + (rval * rval) / (rad * rad))
                - gamval / rad;
        return ur * Math.cos(thrad) - uth * Math.sin(thrad); // MODS  20 Jul 99 
    }

    private void computeCirculation() {   // circulation from Kutta condition
        double thet, rdm, thtm;
        double beta;
        int index;

        xcval = 0.0;
        /* Juokowski geometry*/
        ycval = camval / 2.0;
        rval = thkval / 4.0 + Math.sqrt(thkval * thkval / 16.0 + ycval * ycval + 1.0);
        xcval = 1.0 - Math.sqrt(rval * rval - ycval * ycval);
        beta = Math.asin(ycval / rval) / radDegConv;     /* Kutta condition */

        gamval = 2.0 * rval * Math.sin((angle + beta) * radDegConv);

        // geometry
        for (index = 1; index <= nptc; ++index) {
            thet = (index - 1) * 360. / (nptc - 1);
            xg[0][index] = rval * Math.cos(radDegConv * thet) + xcval;
            yg[0][index] = rval * Math.sin(radDegConv * thet) + ycval;
            rg[0][index] = Math.sqrt(xg[0][index] * xg[0][index]
                    + yg[0][index] * yg[0][index]);
            thg[0][index] = Math.atan2(yg[0][index], xg[0][index]) / radDegConv;
            xm[0][index] = (rg[0][index] + 1.0 / rg[0][index])
                    * Math.cos(radDegConv * thg[0][index]);
            ym[0][index] = (rg[0][index] - 1.0 / rg[0][index])
                    * Math.sin(radDegConv * thg[0][index]);
            rdm = Math.sqrt(xm[0][index] * xm[0][index]
                    + ym[0][index] * ym[0][index]);
            thtm = Math.atan2(ym[0][index], xm[0][index]) / radDegConv;
            xm[0][index] = rdm * Math.cos((thtm - angle) * radDegConv);
            ym[0][index] = rdm * Math.sin((thtm - angle) * radDegConv);
        }
    }

    private void getPoints(double fxg, double psv) {   // flow in x-psi
        double radm, thetm;                /* MODS  20 Jul 99  whole routine*/

        double fnew, ynew, yold, rfac, deriv;
        int iter;
        /* get variables in the generating plane */
        /* iterate to find value of yg */
        ynew = 10.0;
        yold = 10.0;
        if (psv < 0.0) {
            ynew = -10.0;
        }
        if (Math.abs(psv) < .001 && angle < 0.0) {
            ynew = rval;
        }
        if (Math.abs(psv) < .001 && angle >= 0.0) {
            ynew = -rval;
        }
        fnew = 0.1;
        iter = 1;
        while (Math.abs(fnew) >= .00001 && iter < 25) {
            ++iter;
            rfac = fxg * fxg + ynew * ynew;
            if (rfac < rval * rval) {
                rfac = rval * rval + .01;
            }
            fnew = psv - ynew * (1.0 - rval * rval / rfac)
                    - gamval * Math.log(Math.sqrt(rfac) / rval);
            deriv = -(1.0 - rval * rval / rfac)
                    - 2.0 * ynew * ynew * rval * rval / (rfac * rfac)
                    - gamval * ynew / rfac;
            yold = ynew;
            ynew = yold - .5 * fnew / deriv;
        }
        lyg = yold;
        /* rotate for angle of attack */
        lrg = Math.sqrt(fxg * fxg + lyg * lyg);
        lthg = Math.atan2(lyg, fxg) / radDegConv;
        lxgt = lrg * Math.cos(radDegConv * (lthg + angle));
        lygt = lrg * Math.sin(radDegConv * (lthg + angle));
        /* translate cylinder to generate airfoil */
        lxgt = lxgt + xcval;
        lygt = lygt + ycval;
        lrgt = Math.sqrt(lxgt * lxgt + lygt * lygt);
        lthgt = Math.atan2(lygt, lxgt) / radDegConv;
        /*  Kutta-Joukowski mapping */
        lxm = (lrgt + 1.0 / lrgt) * Math.cos(radDegConv * lthgt);
        lym = (lrgt - 1.0 / lrgt) * Math.sin(radDegConv * lthgt);
        /* tranforms for view fixed with free stream */
        /* take out rotation for angle of attack mapped and cylinder */
        radm = Math.sqrt(lxm * lxm + lym * lym);
        thetm = Math.atan2(lym, lxm) / radDegConv;
        lxmt = radm * Math.cos(radDegConv * (thetm - angle));
        lymt = radm * Math.sin(radDegConv * (thetm - angle));

        lxgt = lxgt - xcval;
        lygt = lygt - ycval;
        lrgt = Math.sqrt(lxgt * lxgt + lygt * lygt);
        lthgt = Math.atan2(lygt, lxgt) / radDegConv;
        lxgt = lrgt * Math.cos((lthgt - angle) * radDegConv);
        lygt = lrgt * Math.sin((lthgt - angle) * radDegConv);
    }

    /**
     * Generate flow field, compute lift
     * @return lift coefficient
     */
    private double computeLift() {
        double rnew, thet, psv, fxg, stfact, clift;
        int k, index;
        /* all lines of flow  except stagnation line*/
        for (k = 1; k <= nlnc; ++k) {
            psv = -.5 * (nln2 - 1) + .5 * (k - 1);
            fxg = xflow;
            for (index = 1; index <= nptc; ++index) {
                getPoints(fxg, psv);
                xg[k][index] = lxgt;
                yg[k][index] = lygt;
                rg[k][index] = lrgt;
                thg[k][index] = lthgt;
                xm[k][index] = lxmt;
                ym[k][index] = lymt;
                if (angle > 10.0 && psv > 0.0) {
                    if (xm[k][index] > 0.0) {
                        ym[k][index] = ym[k][index - 1];
                    }
                }
                if (angle < -10.0 && psv < 0.0) {
                    if (xm[k][index] > 0.0) {
                        ym[k][index] = ym[k][index - 1];
                    }
                }
                fxg = fxg + getVel(lrg, lthg) * deltb;
            }
        }
        /*  stagnation line */
        k = nln2;
        psv = 0.0;
        /*  incoming flow */
        for (index = 1; index <= npt2; ++index) {
            rnew = 10.0 - (10.0 - rval) * Math.sin(halfPi * (index - 1) / (npt2 - 1));
            thet = Math.asin(.999 * (psv - gamval * Math.log(rnew / rval))
                    / (rnew - rval * rval / rnew));
            fxg = -rnew * Math.cos(thet);
            getPoints(fxg, psv);
            xg[k][index] = lxgt;
            yg[k][index] = lygt;
            rg[k][index] = lrgt;
            thg[k][index] = lthgt;
            xm[k][index] = lxmt;
            ym[k][index] = lymt;
        }
        /*  downstream flow */
        for (index = 1; index <= npt2; ++index) {
            rnew = 10.0 + .01 - (10.0 - rval) * Math.cos(halfPi * (index - 1) / (npt2 - 1));
            thet = Math.asin(.999 * (psv - gamval * Math.log(rnew / rval))
                    / (rnew - rval * rval / rnew));
            fxg = rnew * Math.cos(thet);
            getPoints(fxg, psv);
            xg[k][npt2 + index] = lxgt;
            yg[k][npt2 + index] = lygt;
            rg[k][npt2 + index] = lrgt;
            thg[k][npt2 + index] = lthgt;
            xm[k][npt2 + index] = lxmt;
            ym[k][npt2 + index] = lymt;
        }
        /*  stagnation point */
        xg[k][npt2] = xcval;
        yg[k][npt2] = ycval;
        rg[k][npt2] = Math.sqrt(xcval * xcval + ycval * ycval);
        thg[k][npt2] = Math.atan2(ycval, xcval) / radDegConv;
        xm[k][npt2] = (xm[k][npt2 + 1] + xm[k][npt2 - 1]) / 2.0;
        ym[k][npt2] = (ym[0][nptc / 4 + 1] + ym[0][nptc / 4 * 3 + 1]) / 2.0;
        /*  compute lift coefficient */
        double leg = xcval - Math.sqrt(rval * rval - ycval * ycval);
        double teg = xcval + Math.sqrt(rval * rval - ycval * ycval);
        double lem = leg + 1.0 / leg;
        double tem = teg + 1.0 / teg;
        double chrd = tem - lem;
        clift = gamval * 4.0 * 3.1415926 / chrd;
        // stall model
        stfact = 1.0;
        if (angle > 10.0) {
            stfact = .5 + .1 * angle - .005 * angle * angle;
        }
        if (angle < -10.0) {
            stfact = .5 - .1 * angle - .005 * angle * angle;
        }
        clift = clift * stfact;

        // correction for low aspect ratio
        return clift / (1.0 + Math.abs(clift) / (3.14159 * aspr));
    }

    /**
     * Compute drag
     * @return drag coefficient
     */
    private double computeDrag()
    {
        double dragco = 0.0;
        double dragCam0Thk5, dragCam5Thk5, dragCam10Thk5, dragCam15Thk5, dragCam20Thk5;
        double dragCam0Thk10, dragCam5Thk10, dragCam10Thk10, dragCam15Thk10, dragCam20Thk10;
        double dragCam0Thk15, dragCam5Thk15, dragCam10Thk15, dragCam15Thk15, dragCam20Thk15;
        double dragCam0Thk20, dragCam5Thk20, dragCam10Thk20, dragCam15Thk20, dragCam20Thk20;
        double dragThk5, dragThk10, dragThk15, dragThk20;
        
        //airfoil drag logic
        dragCam0Thk5 = -9E-07 * Math.pow(angle, 3) + 0.0007 * Math.pow(angle, 2) + 0.0008 * angle + 0.015;
        dragCam5Thk5 = 4E-08 * Math.pow(angle, 5) - 7E-07 * Math.pow(angle, 4) - 1E-05 * Math.pow(angle, 3) + 0.0009 * Math.pow(angle, 2) + 0.0033 * angle + 0.0301;
        dragCam10Thk5 = -9E-09 * Math.pow(angle, 6) - 6E-08 * Math.pow(angle, 5) + 5E-06 * Math.pow(angle, 4) + 3E-05 * Math.pow(angle, 3) - 0.0001 * Math.pow(angle, 2) - 0.0025 * angle + 0.0615;
        dragCam15Thk5 = 8E-10 * Math.pow(angle, 6) - 5E-08 * Math.pow(angle, 5) - 1E-06 * Math.pow(angle, 4) + 3E-05 * Math.pow(angle, 3) + 0.0008 * Math.pow(angle, 2) - 0.0027 * angle + 0.0612;
        dragCam20Thk5 = 8E-9 * Math.pow(angle, 6) + 1E-8 * Math.pow(angle, 5) - 5E-6 * Math.pow(angle, 4) + 6E-6 * Math.pow(angle, 3) + 0.001 * Math.pow(angle, 2) - 0.001 * angle + 0.1219;

        dragCam0Thk10 = -1E-08 * Math.pow(angle, 6) + 6E-08 * Math.pow(angle, 5) + 6E-06 * Math.pow(angle, 4) - 2E-05 * Math.pow(angle, 3) - 0.0002 * Math.pow(angle, 2) + 0.0017 * angle + 0.0196;
        dragCam5Thk10 = 3E-09 * Math.pow(angle, 6) + 6E-08 * Math.pow(angle, 5) - 2E-06 * Math.pow(angle, 4) - 3E-05 * Math.pow(angle, 3) + 0.0008 * Math.pow(angle, 2) + 0.0038 * angle + 0.0159;
        dragCam10Thk10 = -5E-09 * Math.pow(angle, 6) - 3E-08 * Math.pow(angle, 5) + 2E-06 * Math.pow(angle, 4) + 1E-05 * Math.pow(angle, 3) + 0.0004 * Math.pow(angle, 2) - 3E-05 * angle + 0.0624;
        dragCam15Thk10 = -2E-09 * Math.pow(angle, 6) - 2E-08 * Math.pow(angle, 5) - 5E-07 * Math.pow(angle, 4) + 8E-06 * Math.pow(angle, 3) + 0.0009 * Math.pow(angle, 2) + 0.0034 * angle + 0.0993;
        dragCam20Thk10 = 2E-09 * Math.pow(angle, 6) - 3E-08 * Math.pow(angle, 5) - 2E-06 * Math.pow(angle, 4) + 2E-05 * Math.pow(angle, 3) + 0.0009 * Math.pow(angle, 2) + 0.0023 * angle + 0.1581;

        dragCam0Thk15 = -5E-09 * Math.pow(angle, 6) + 7E-08 * Math.pow(angle, 5) + 3E-06 * Math.pow(angle, 4) - 3E-05 * Math.pow(angle, 3) - 7E-05 * Math.pow(angle, 2) + 0.0017 * angle + 0.0358;
        dragCam5Thk15 = -4E-09 * Math.pow(angle, 6) - 8E-09 * Math.pow(angle, 5) + 2E-06 * Math.pow(angle, 4) - 9E-07 * Math.pow(angle, 3) + 0.0002 * Math.pow(angle, 2) + 0.0031 * angle + 0.0351;
        dragCam10Thk15 = 3E-09 * Math.pow(angle, 6) + 3E-08 * Math.pow(angle, 5) - 2E-06 * Math.pow(angle, 4) - 1E-05 * Math.pow(angle, 3) + 0.0009 * Math.pow(angle, 2) + 0.004 * angle + 0.0543;
        dragCam15Thk15 = 3E-09 * Math.pow(angle, 6) + 5E-08 * Math.pow(angle, 5) - 2E-06 * Math.pow(angle, 4) - 3E-05 * Math.pow(angle, 3) + 0.0008 * Math.pow(angle, 2) + 0.0087 * angle + 0.1167;
        dragCam20Thk15 = 3E-10 * Math.pow(angle, 6) - 3E-08 * Math.pow(angle, 5) - 6E-07 * Math.pow(angle, 4) + 3E-05 * Math.pow(angle, 3) + 0.0006 * Math.pow(angle, 2) + 0.0008 * angle + 0.1859;

        dragCam0Thk20 = -3E-09 * Math.pow(angle, 6) + 2E-08 * Math.pow(angle, 5) + 2E-06 * Math.pow(angle, 4) - 8E-06 * Math.pow(angle, 3) - 4E-05 * Math.pow(angle, 2) + 0.0003 * angle + 0.0416;
        dragCam5Thk20 = -3E-09 * Math.pow(angle, 6) - 7E-08 * Math.pow(angle, 5) + 1E-06 * Math.pow(angle, 4) + 3E-05 * Math.pow(angle, 3) + 0.0004 * Math.pow(angle, 2) + 5E-05 * angle + 0.0483;
        dragCam10Thk20 = 1E-08 * Math.pow(angle, 6) + 4E-08 * Math.pow(angle, 5) - 6E-06 * Math.pow(angle, 4) - 2E-05 * Math.pow(angle, 3) + 0.0014 * Math.pow(angle, 2) + 0.007 * angle + 0.0692;
        dragCam15Thk20 = 3E-09 * Math.pow(angle, 6) - 9E-08 * Math.pow(angle, 5) - 3E-06 * Math.pow(angle, 4) + 4E-05 * Math.pow(angle, 3) + 0.001 * Math.pow(angle, 2) + 0.0021 * angle + 0.139;
        dragCam20Thk20 = 3E-09 * Math.pow(angle, 6) - 7E-08 * Math.pow(angle, 5) - 3E-06 * Math.pow(angle, 4) + 4E-05 * Math.pow(angle, 3) + 0.0012 * Math.pow(angle, 2) + 0.001 * angle + 0.1856;

        if (-20.0 <= camber && camber < -15.0) {
            dragThk5 = (camber / 5 + 4) * (dragCam15Thk5 - dragCam20Thk5) + dragCam20Thk5;
            dragThk10 = (camber / 5 + 4) * (dragCam15Thk10 - dragCam20Thk10) + dragCam20Thk10;
            dragThk15 = (camber / 5 + 4) * (dragCam15Thk15 - dragCam20Thk15) + dragCam20Thk15;
            dragThk20 = (camber / 5 + 4) * (dragCam15Thk20 - dragCam20Thk20) + dragCam20Thk20;

            if (1.0 <= thickness && thickness <= 5.0) {
                dragco = dragThk5;
            } else if (5.0 < thickness && thickness <= 10.0) {
                dragco = (thickness / 5 - 1) * (dragThk10 - dragThk5) + dragThk5;
            } else if (10.0 < thickness && thickness <= 15.0) {
                dragco = (thickness / 5 - 2) * (dragThk15 - dragThk10) + dragThk10;
            } else if (15.0 < thickness && thickness <= 20.0) {
                dragco = (thickness / 5 - 3) * (dragThk20 - dragThk15) + dragThk15;
            }
        } else if (-15.0 <= camber && camber < -10.0) {
            dragThk5 = (camber / 5 + 3) * (dragCam10Thk5 - dragCam15Thk5) + dragCam15Thk5;
            dragThk10 = (camber / 5 + 3) * (dragCam10Thk10 - dragCam15Thk10) + dragCam15Thk10;
            dragThk15 = (camber / 5 + 3) * (dragCam10Thk15 - dragCam15Thk15) + dragCam15Thk15;
            dragThk20 = (camber / 5 + 3) * (dragCam10Thk20 - dragCam15Thk20) + dragCam15Thk20;

            if (1.0 <= thickness && thickness <= 5.0) {
                dragco = dragThk5;
            } else if (5.0 < thickness && thickness <= 10.0) {
                dragco = (thickness / 5 - 1) * (dragThk10 - dragThk5) + dragThk5;
            } else if (10.0 < thickness && thickness <= 15.0) {
                dragco = (thickness / 5 - 2) * (dragThk15 - dragThk10) + dragThk10;
            } else if (15.0 < thickness && thickness <= 20.0) {
                dragco = (thickness / 5 - 3) * (dragThk20 - dragThk15) + dragThk15;
            }
        } else if (-10.0 <= camber && camber < -5.0) {
            dragThk5 = (camber / 5 + 2) * (dragCam5Thk5 - dragCam10Thk5) + dragCam10Thk5;
            dragThk10 = (camber / 5 + 2) * (dragCam5Thk10 - dragCam10Thk10) + dragCam10Thk10;
            dragThk15 = (camber / 5 + 2) * (dragCam5Thk15 - dragCam10Thk15) + dragCam10Thk15;
            dragThk20 = (camber / 5 + 2) * (dragCam5Thk20 - dragCam10Thk20) + dragCam10Thk20;

            if (1.0 <= thickness && thickness <= 5.0) {
                dragco = dragThk5;
            } else if (5.0 < thickness && thickness <= 10.0) {
                dragco = (thickness / 5 - 1) * (dragThk10 - dragThk5) + dragThk5;
            } else if (10.0 < thickness && thickness <= 15.0) {
                dragco = (thickness / 5 - 2) * (dragThk15 - dragThk10) + dragThk10;
            } else if (15.0 < thickness && thickness <= 20.0) {
                dragco = (thickness / 5 - 3) * (dragThk20 - dragThk15) + dragThk15;
            }
        } else if (-5.0 <= camber && camber < 0) {
            dragThk5 = (camber / 5 + 1) * (dragCam0Thk5 - dragCam5Thk5) + dragCam5Thk5;
            dragThk10 = (camber / 5 + 1) * (dragCam0Thk10 - dragCam5Thk10) + dragCam5Thk10;
            dragThk15 = (camber / 5 + 1) * (dragCam0Thk15 - dragCam5Thk15) + dragCam5Thk15;
            dragThk20 = (camber / 5 + 1) * (dragCam0Thk20 - dragCam5Thk20) + dragCam5Thk20;

            if (1.0 <= thickness && thickness <= 5.0) {
                dragco = dragThk5;
            } else if (5.0 < thickness && thickness <= 10.0) {
                dragco = (thickness / 5 - 1) * (dragThk10 - dragThk5) + dragThk5;
            } else if (10.0 < thickness && thickness <= 15.0) {
                dragco = (thickness / 5 - 2) * (dragThk15 - dragThk10) + dragThk10;
            } else if (15.0 < thickness && thickness <= 20.0) {
                dragco = (thickness / 5 - 3) * (dragThk20 - dragThk15) + dragThk15;
            }
        } else if (0 <= camber && camber < 5) {
            dragThk5 = (camber / 5) * (dragCam5Thk5 - dragCam0Thk5) + dragCam0Thk5;
            dragThk10 = (camber / 5) * (dragCam5Thk10 - dragCam0Thk10) + dragCam0Thk10;
            dragThk15 = (camber / 5) * (dragCam5Thk15 - dragCam0Thk15) + dragCam0Thk15;
            dragThk20 = (camber / 5) * (dragCam5Thk20 - dragCam0Thk20) + dragCam0Thk20;

            if (1.0 <= thickness && thickness <= 5.0) {
                dragco = dragThk5;
            } else if (5.0 < thickness && thickness <= 10.0) {
                dragco = (thickness / 5 - 1) * (dragThk10 - dragThk5) + dragThk5;
            } else if (10.0 < thickness && thickness <= 15.0) {
                dragco = (thickness / 5 - 2) * (dragThk15 - dragThk10) + dragThk10;
            } else if (15.0 < thickness && thickness <= 20.0) {
                dragco = (thickness / 5 - 3) * (dragThk20 - dragThk15) + dragThk15;
            }
        } else if (5 <= camber && camber < 10) {
            dragThk5 = (camber / 5 - 1) * (dragCam10Thk5 - dragCam5Thk5) + dragCam5Thk5;
            dragThk10 = (camber / 5 - 1) * (dragCam10Thk10 - dragCam5Thk10) + dragCam5Thk10;
            dragThk15 = (camber / 5 - 1) * (dragCam10Thk15 - dragCam5Thk15) + dragCam5Thk15;
            dragThk20 = (camber / 5 - 1) * (dragCam10Thk20 - dragCam5Thk20) + dragCam5Thk20;

            if (1.0 <= thickness && thickness <= 5.0) {
                dragco = dragThk5;
            } else if (5.0 < thickness && thickness <= 10.0) {
                dragco = (thickness / 5 - 1) * (dragThk10 - dragThk5) + dragThk5;
            } else if (10.0 < thickness && thickness <= 15.0) {
                dragco = (thickness / 5 - 2) * (dragThk15 - dragThk10) + dragThk10;
            } else if (15.0 < thickness && thickness <= 20.0) {
                dragco = (thickness / 5 - 3) * (dragThk20 - dragThk15) + dragThk15;
            }
        } else if (10 <= camber && camber < 15) {
            dragThk5 = (camber / 5 - 2) * (dragCam15Thk5 - dragCam10Thk5) + dragCam10Thk5;
            dragThk10 = (camber / 5 - 2) * (dragCam15Thk10 - dragCam10Thk10) + dragCam10Thk10;
            dragThk15 = (camber / 5 - 2) * (dragCam15Thk15 - dragCam10Thk15) + dragCam10Thk15;
            dragThk20 = (camber / 5 - 2) * (dragCam15Thk20 - dragCam10Thk20) + dragCam10Thk20;

            if (1.0 <= thickness && thickness <= 5.0) {
                dragco = dragThk5;
            } else if (5.0 < thickness && thickness <= 10.0) {
                dragco = (thickness / 5 - 1) * (dragThk10 - dragThk5) + dragThk5;
            } else if (10.0 < thickness && thickness <= 15.0) {
                dragco = (thickness / 5 - 2) * (dragThk15 - dragThk10) + dragThk10;
            } else if (15.0 < thickness && thickness <= 20.0) {
                dragco = (thickness / 5 - 3) * (dragThk20 - dragThk15) + dragThk15;
            }
        } else if (15 <= camber && camber <= 20) {
            dragThk5 = (camber / 5 - 3) * (dragCam20Thk5 - dragCam15Thk5) + dragCam15Thk5;
            dragThk10 = (camber / 5 - 3) * (dragCam20Thk10 - dragCam15Thk10) + dragCam15Thk10;
            dragThk15 = (camber / 5 - 3) * (dragCam20Thk15 - dragCam15Thk15) + dragCam15Thk15;
            dragThk20 = (camber / 5 - 3) * (dragCam20Thk20 - dragCam15Thk20) + dragCam15Thk20;

            if (1.0 <= thickness && thickness <= 5.0) {
                dragco = dragThk5;
            } else if (5.0 < thickness && thickness <= 10.0) {
                dragco = (thickness / 5 - 1) * (dragThk10 - dragThk5) + dragThk5;
            } else if (10.0 < thickness && thickness <= 15.0) {
                dragco = (thickness / 5 - 2) * (dragThk15 - dragThk10) + dragThk10;
            } else if (15.0 < thickness && thickness <= 20.0) {
                dragco = (thickness / 5 - 3) * (dragThk20 - dragThk15) + dragThk15;
            }
        }
        return dragco;
    }
    
    //input
    private double angle, camber, thickness;
    //output
    private double liftCoeff, dragCoeff;
    
    private final int nptc, npt2, nlnc, nln2;    
    private double xcval, ycval, rval, gamval;
    private double camval, thkval;
    private final double xflow, deltb, aspr;
    private double lyg, lrg, lthg, lxgt, lygt, lrgt, lthgt;
    private double lxm, lym, lxmt, lymt;
    
    private final static double radDegConv = 3.1415926/180. ;
    private final static double halfPi = 3.1415926/2.0 ;
    private final double rg[][]  = new double[20][40]; 
    private final double thg[][] = new double[20][40]; 
    private final double xg[][]  = new double[20][40]; 
    private final double yg[][]  = new double[20][40]; 
    private final double xm[][]  = new double[20][40]; 
    private final double ym[][]  = new double[20][40]; 
} // end Solver
