/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package optimizer;
/*Unchanged header of the original... This is a heavily stripped down and 
  modified version.

                      FoilSim III  - Airfoil  mode
   
                           A Java Applet
               to perform Kutta-Joukowski Airfoil analysis
                including drag from wind tunnel tests

                     Version 1.5b   - 3 Sep 13

                              Written by 

                               Tom Benson
                       NASA Glenn Research Center

                                 and
              
                               Anthony Vila
                          Vanderbilt University

>                                NOTICE
>This software is in the Public Domain.  It may be freely copied and used in
>non-commercial products, assuming proper credit to the author is given.  IT
>MAY NOT BE RESOLD.  If you want to use the software for commercial
>products, contact the author.
>No copyright is claimed in the United States under Title 17, U. S. Code.
>This software is provided "as is" without any warranty of any kind, either
>express, implied, or statutory, including, but not limited to, any warranty
>that the software will conform to specifications, any implied warranties of
>merchantability, fitness for a particular purpose, and freedom from
>infringement, and any warranty that the documentation will conform to the
>program, or any warranty that the software will be error free.
>In no event shall NASA be liable for any damages, including, but not
>limited to direct, indirect, special or consequential damages, arising out
>of, resulting from, or in any way connected with this software, whether or
>not based on warranty, contract, tort or otherwise, whether or not injury
>was sustained by persons or property or otherwise, and whether or not loss
>was sustained from, or arose out of the results of, or use of, the software
>or services provided hereunder.

*/

public class Foil {
    public static double rval,ycval,xcval,gamval, alfval, thkval,camval,chrd,clift;
    public static double dragCoeff,drag,liftOverDrag,reynolds,viscos ;
    public static double alfd,thkd,camd,dragco ;
    public static double leg,teg,lem,tem;
    public static double usq,vsq,alt,altmax,area,armax,armin ;
    public static double chord,span,aspr,arold,chrdold,spnold ; /* Mod 13 Jan 00 */
    public static double g0,q0,ps0,pt0,ts0,rho,rlhum,temf ;
    public static double lyg,lrg,lthg,lxgt,lygt,lrgt,lthgt;/* MOD 20 Jul */
    public static double lxm,lym,lxmt,lymt,vxdir;/* MOD 20 Jul */
    public static double deltb,xflow ;             /* MODS  20 Jul 99 */
    public static double delx,delt,vfsd,spin,spindr,yoff,radius ;
    public static double vel,pres,lift,side,omega,radcrv,relsy,angr;

    public static double rg[][]  = new double[20][40] ; 
    public static double thg[][] = new double[20][40] ; 
    public static double xg[][]  = new double[20][40] ; 
    public static double yg[][]  = new double[20][40] ; 
    public static double xm[][]  = new double[20][40] ; 
    public static double ym[][]  = new double[20][40] ; 
    public static double xpl[][]  = new double[20][40] ; 
    public static double ypl[][]  = new double[20][40] ; 
    public static double plp[]   = new double[40] ;
    public static double plv[]   = new double[40] ;

        /* units data */
    public static double vmn,almn,angmn,vmx,almx,angmx ;
    public static double camn,thkmn,camx,thkmx ;
    public static double chrdmn,spanmn,armn,chrdmx,spanmx,armx ;
    public static double radmn,spinmn,radmx,spinmx ;
    public static double vconv,vmax ;
    public static double pconv,pmax,pmin,lconv,rconv,fconv,fmax,fmaxb;
        /*  plot & probe data */
    public static double fact,xpval,ypval,pbval,factp;
    public static double prg,pthg,pxg,pyg,pxm,pym,pxpl,pypl ;
    public static double begx,endx,begy,endy ;
    public static String labx,labxu,laby,labyu ;
    public static double pltx[][]  = new double[3][40] ;
    public static double plty[][]  = new double[3][40] ;
    public static double plthg[]  = new double[2] ;
    public final double convdr = 3.1415926/180.;
    public final double pid2 = 3.1415926/2.0;
    public int inptopt;
    public int outopt;
    public int nptc;
    public int npt2;
    public int nlnc;
    public int nln2;
    public int rdflag;
    public int browflag;
    public int probflag;
    public int anflag;
    public int lftout;
    public int planet;
    public int dragOut;
    public int displ;
    public int viewflg;
    public int dispp;
    public int dout;
    public int doutb;
    public int sldloc;
    public int calcrange;
    public int arcor;
    public int indrag;
    public int recor;
    public int bdragflag;
    public int lflag;
    public int gflag;
    public int plscale;
    public int nond;
    public int pboflag;
    public int xt;
    public int yt;
    public int ntikx;
    public int ntiky;
    public int npt;
    public int xtp;
    public int ytp;
    public int xt1;
    public int yt1;
    public int xt2;
    public int yt2;
    public int spanfac;
    public int lines;
    public int nord;
    public int nabs;
    public int ntr;

    public Foil() {
        setDefaults () ;
        getFreeStream ();
        computeFlow (0,12,5) ;
    }

    public void computeFlow(double thkinpt, double caminpt, double alfinp) { 
        camval = caminpt / 25.0;
        thkval = thkinpt / 25.0;
        alfval = alfinp;
                    
        getFreeStream () ;
        getCirc ();                   /* get circulation */
        genFlow () ;

        reynolds = vfsd/vconv * chord/lconv * rho / viscos ;


        thkd = thkinpt ;
        camd = caminpt ;
        alfd = alfval = alfinp;
    //   attempt to fix symmetry problem
        if (camd < 0.0) alfd = - alfval ;
    //
        getDrag(clift); 
        for (int ic = 0; ic <= nlnc; ++ic) {
            for (int index = 0; index <= nptc; ++index) {
                xpl[ic][index] = xm[ic][index];
                ypl[ic][index] = ym[ic][index];
            }
        }
        dragCoeff = dragco ;
    }
 
    public void setUnits() {   // Switching Units
        double ovs,chords,spans,aros,chos,spos,rads ;
        double alts,ares ;

        alts = alt / lconv ;
        chords = chord / lconv ;
        spans = span / lconv ;
        ares = area /lconv/lconv ;
        aros = arold /lconv/lconv ;
        chos = chrdold / lconv ;
        spos = spnold / lconv ;
        ovs = vfsd / vconv ;
        rads = radius / lconv ;
                           /* Metric */
        lconv = .3048;                    /* meters */
        vconv = 1.097; vmax = 400. ;   /* km/hr  */
        if (planet == 2) vmax = 80. ;
        fconv = 4.448 ; fmax = 500000.; fmaxb = 2.5; /* newtons */
        pconv = 101.3 ;               /* kilo-pascals */
 
        alt = alts * lconv ;
        chord = chords * lconv ;
        span = spans * lconv ;
        area = ares * lconv * lconv ;
        arold = aros * lconv * lconv ;
        chrdold = chos * lconv ;
        spnold = spos * lconv ;
        vfsd  = ovs * vconv;
        radius  = rads * lconv;
    }

    private void setDefaults() {
       arcor = 1 ;
       indrag = 1 ;
       recor = 1 ;
       bdragflag = 1;  // smooth ball
       planet = 0 ;
       lftout = 0 ;
       inptopt = 0 ;
       outopt = 0 ;
       nlnc = 15 ;
       nln2 = nlnc/2 + 1 ;
       nptc = 37 ;
       npt2 = nptc/2 + 1 ;
       deltb = .5 ;
       thkval = .5 ;
       camval = 0.0 ;
       gamval = 0.0 ;
       radius = 1.0 ;
       spin = 0.0 ;
       spindr = 1.0 ;
       rval = 1.0 ;
       ycval = 0.0 ;
       xcval = 0.0 ;
       displ   = 1 ;                            
       viewflg = 0 ;
       dispp = 0 ;
       calcrange = 0 ;
       dout = 0 ;
       doutb = 0 ;

       dragCoeff = 0;

       xpval = 2.1;
       ypval = -.5 ;
       pboflag = 0 ;
       xflow = -10.0;                             /* MODS  20 Jul 99 */

       pconv = 14.7;
       pmin = .5 ;
       pmax = 1.0 ;
       fconv = 1.0 ;
       fmax = 100000. ;
       fmaxb = .50 ;
       vconv = .6818 ;
       vfsd = 100. ;
       vmax = 250. ;
       lconv = 1.0 ;

       alt = 0.0 ;
       altmax = 50000. ;
       chrdold = chord = 5.0 ;
       spnold = span = 20.0 ;
       aspr = 4.0 ;
       arold = area = 100.0 ;
       armax = 2500.01 ;
       armin = .01 ;                 /* MODS 9 SEP 99 */

       xt = 170;  yt = 105; fact = 30.0 ;
       sldloc = 50 ;
       xtp = 95; ytp = 165; factp = 30.0 ;
       spanfac = (int)(2.0*fact*aspr*.3535) ;
       xt1 = xt + spanfac ;
       yt1 = yt - spanfac ;
       xt2 = xt - spanfac;
       yt2 = yt + spanfac ;
       plthg[1] = 0.0 ;

       probflag = 0 ;
       anflag = 1 ;
       vmn = 0.0;     vmx = 250.0 ;
       almn = 0.0;    almx = 50000.0 ;
       angmn = -20.0; angmx = 20.0 ;
       camn = -20.0;  camx = 20.0 ;
       thkmn = 1.0; thkmx = 20.0 ;
       chrdmn = .1 ;  chrdmx = 20.1 ;
       spanmn = .1 ;  spanmx = 125.1 ;
       armn = .01 ;  armx = 2500.01 ;
       spinmn = -1500.0;   spinmx = 1500.0 ;
       radmn = .05;   radmx = 5.0 ;
    }

    private void getFreeStream() {    //  free stream conditions
        double hite,pvap,rgas,mu0 ;       /* MODS  19 Jan 00  whole routine*/

        g0 = 32.2 ;
        rgas = 1716. ;                /* ft2/sec2 R */
        hite = alt/lconv ;
        mu0 = .000000362 ;
       //Earth standard day
        if (hite <= 36152.) {           // Troposphere
            ts0 = 518.6 - 3.56 * hite/1000. ;
            ps0 = 2116. * Math.pow(ts0/518.6,5.256) ;
        }
        if (hite >= 36152. && hite <= 82345.) {   // Stratosphere
            ts0 = 389.98 ;
            ps0 = 2116. * .2236 *
               Math.exp((36000.-hite)/(53.35*389.98)) ;
        }
        if (hite >= 82345.) {
            ts0 = 389.98 + 1.645 * (hite-82345)/1000. ;
            ps0 = 2116. *.02456 * Math.pow(ts0/389.98,-11.388) ;
        }
        temf = ts0 - 459.6 ;
        if (temf <= 0.0) temf = 0.0 ; 
        rho = ps0/(rgas * ts0) ;

     /* Eq 1:6A  Domasch  - effect of humidity 
     */
        pvap = rlhum*(2.685+.00354*Math.pow(temf,2.245))/100.;
        rho = (ps0 - .379*pvap)/(rgas * ts0) ; 
        viscos = mu0 * 717.408/(ts0 + 198.72)*Math.pow(ts0/518.688,1.5) ;


        q0  = .5 * rho * vfsd * vfsd / (vconv * vconv) ;
        pt0 = ps0 + q0 ;
    }

    public void getCirc() {   // circulation from Kutta condition
        double thet,rdm,thtm ;
        double beta;

        xcval = 0.0 ;
        ycval = camval / 2.0 ;
        rval = thkval/4.0 +Math.sqrt(thkval*thkval/16.0+ycval*ycval +1.0);
        xcval = 1.0 - Math.sqrt(rval*rval - ycval*ycval) ;
        beta = Math.asin(ycval/rval)/convdr ;     /* Kutta condition */
        gamval = 2.0*rval*Math.sin((alfval+beta)*convdr) ;
                             // geometry
        for (int index =1; index <= nptc; ++index) {
            thet = (index -1)*360./(nptc-1) ;
            xg[0][index] = rval * Math.cos(convdr * thet) + xcval ;
            yg[0][index] = rval * Math.sin(convdr * thet) + ycval ;
            rg[0][index] = Math.sqrt(xg[0][index]*xg[0][index] +
                                     yg[0][index]*yg[0][index])  ;
            thg[0][index] = Math.atan2(yg[0][index],xg[0][index])/convdr;
            xm[0][index] = (rg[0][index] + 1.0/rg[0][index])*
                     Math.cos(convdr*thg[0][index]) ;
            ym[0][index] = (rg[0][index] - 1.0/rg[0][index])*
                     Math.sin(convdr*thg[0][index]) ;
            rdm = Math.sqrt(xm[0][index]*xm[0][index] +
                            ym[0][index]*ym[0][index])  ;
            thtm = Math.atan2(ym[0][index],xm[0][index])/convdr;
            xm[0][index] = rdm * Math.cos((thtm - alfval)*convdr);
            ym[0][index] = rdm * Math.sin((thtm - alfval)*convdr);
            getVel(rval,thet) ;
            plp[index] = ((ps0 + pres * q0)/2116.) * pconv ;
            plv[index] = vel * vfsd ;
        }

        xt1 = xt + spanfac ;
        yt1 = yt - spanfac ;
        xt2 = xt - spanfac;
        yt2 = yt + spanfac ;
    }

    public void genFlow() {   // generate flowfield
        double rnew,thet,psv,fxg,stfact;
        int k,index;
                             /* all lines of flow  except stagnation line*/
        for (k=1; k<=nlnc; ++k) {
            psv = -.5*(nln2-1) + .5*(k-1) ;
            fxg = xflow ;
            for (index =1; index <=nptc; ++ index) {
              getPoints (fxg,psv) ;
              xg[k][index]  = lxgt ;
              yg[k][index]  = lygt ;
              rg[k][index]  = lrgt ;
              thg[k][index] = lthgt ;
              xm[k][index]  = lxmt ;
              ym[k][index]  = lymt ;
              if (anflag == 1) {           // stall model
                 if (alfval > 10.0 && psv > 0.0) {
                      if (xm[k][index] > 0.0) {
                         ym[k][index] = ym[k][index -1] ;
                      }
                 }
                 if (alfval < -10.0 && psv < 0.0) {
                      if (xm[k][index] > 0.0) {
                         ym[k][index] = ym[k][index -1] ;
                      }
                 }
              }
              getVel(lrg,lthg) ;
              fxg = fxg + vxdir*deltb ;
            }
        }
        /*  stagnation line */
        k = nln2 ;
        psv = 0.0 ;
        /*  incoming flow */
        for (index =1; index <= npt2; ++ index) {
            rnew = 10.0 - (10.0 - rval)*Math.sin(pid2*(index-1)/(npt2-1)) ;
            thet = Math.asin(.999*(psv - gamval*Math.log(rnew/rval))/
                                    (rnew - rval*rval/rnew)) ;
            fxg =  - rnew * Math.cos(thet) ;
            getPoints (fxg,psv) ;
            xg[k][index]  = lxgt ;
            yg[k][index]  = lygt ;
            rg[k][index]  = lrgt ;
            thg[k][index] = lthgt ;
            xm[k][index]  = lxmt ;
            ym[k][index]  = lymt ;
        }
                                             /*  downstream flow */
        for (index = 1; index <= npt2; ++ index) {
            rnew = 10.0 + .01 - (10.0 - rval)*Math.cos(pid2*(index-1)/(npt2-1)) ;
            thet = Math.asin(.999*(psv - gamval*Math.log(rnew/rval))/
                                       (rnew - rval*rval/rnew)) ;
            fxg =   rnew * Math.cos(thet) ;
            getPoints (fxg,psv) ;
            xg[k][npt2+index]  = lxgt ;
            yg[k][npt2+index]  = lygt ;
            rg[k][npt2+index]  = lrgt ;
            thg[k][npt2+index] = lthgt ;
            xm[k][npt2+index]  = lxmt ;
            ym[k][npt2+index]  = lymt ;
        }
                                             /*  stagnation point */
        xg[k][npt2]  = xcval ;
        yg[k][npt2]  = ycval ;
        rg[k][npt2]  = Math.sqrt(xcval*xcval+ycval*ycval) ;
        thg[k][npt2] = Math.atan2(ycval,xcval)/convdr ;
        xm[k][npt2]  = (xm[k][npt2+1] + xm[k][npt2-1])/2.0 ;
        ym[k][npt2]  = (ym[0][nptc/4+1] + ym[0][nptc/4*3+1])/2.0 ;
                                 /*  compute lift coefficient */
        leg = xcval - Math.sqrt(rval*rval - ycval*ycval) ;
        teg = xcval + Math.sqrt(rval*rval - ycval*ycval) ;
        lem = leg + 1.0/leg ;
        tem = teg + 1.0/teg ;
        chrd = tem - lem ;
        clift = gamval*4.0*3.1415926/chrd ;
                         // stall model
        stfact = 1.0 ;
        if (anflag == 1) {
            if (alfval > 10.0 ) {
                stfact = .5 + .1 * alfval - .005 * alfval * alfval ;
            }
            if (alfval < -10.0 ) {
                stfact = .5 - .1 * alfval - .005 * alfval * alfval ;
            }
            clift = clift * stfact ;
        }

        if (arcor == 1) {  // correction for low aspect ratio
             clift = clift /(1.0 + Math.abs(clift)/(3.14159*aspr)) ;
        }
    }

    public void getPoints(double fxg, double psv) {   // flow in x-psi
      double radm,thetm ;                /* MODS  20 Jul 99  whole routine*/
      double fnew,ynew,yold,rfac,deriv ;
      int iter;
                      /* get variables in the generating plane */
                          /* iterate to find value of yg */
      ynew = 10.0 ;
      yold = 10.0 ;
      if (psv < 0.0) ynew = -10.0 ;
      if (Math.abs(psv) < .001 && alfval < 0.0) ynew = rval ;
      if (Math.abs(psv) < .001 && alfval >= 0.0) ynew = -rval ;
      fnew = 0.1 ;
      iter = 1 ;
      while (Math.abs(fnew) >= .00001 && iter < 25) {
          ++iter ;
          rfac = fxg*fxg + ynew*ynew ;
          if (rfac < rval*rval) rfac = rval*rval + .01 ;
          fnew = psv - ynew*(1.0 - rval*rval/rfac)
                 - gamval*Math.log(Math.sqrt(rfac)/rval) ;
          deriv = - (1.0 - rval*rval/rfac)
                  - 2.0 * ynew*ynew*rval*rval/(rfac*rfac)
                  - gamval * ynew / rfac ;
          yold = ynew ;
          ynew = yold  - .5*fnew/deriv ;
      }
      lyg = yold ;
                                    /* rotate for angle of attack */
      lrg = Math.sqrt(fxg*fxg + lyg*lyg) ;
      lthg = Math.atan2(lyg,fxg)/convdr ;
      lxgt = lrg * Math.cos(convdr*(lthg + alfval)) ;
      lygt = lrg * Math.sin(convdr*(lthg + alfval)) ;
                             /* translate cylinder to generate airfoil */
      lxgt = lxgt + xcval ;
      lygt = lygt + ycval ;
      lrgt = Math.sqrt(lxgt*lxgt + lygt*lygt) ;
      lthgt = Math.atan2(lygt,lxgt)/convdr ;
                              /*  Kutta-Joukowski mapping */
      lxm = (lrgt + 1.0/lrgt)*Math.cos(convdr*lthgt) ;
      lym = (lrgt - 1.0/lrgt)*Math.sin(convdr*lthgt) ;
                             /* tranforms for view fixed with free stream */
               /* take out rotation for angle of attack mapped and cylinder */
      radm = Math.sqrt(lxm*lxm+lym*lym) ;
      thetm = Math.atan2(lym,lxm)/convdr ;
      lxmt = radm*Math.cos(convdr*(thetm-alfval)) ;
      lymt = radm*Math.sin(convdr*(thetm-alfval)) ;

      lxgt = lxgt - xcval ;
      lygt = lygt - ycval ;
      lrgt = Math.sqrt(lxgt*lxgt + lygt*lygt)  ;
      lthgt = Math.atan2(lygt,lxgt)/convdr;
      lxgt = lrgt * Math.cos((lthgt - alfval)*convdr);
      lygt = lrgt * Math.sin((lthgt - alfval)*convdr);
    }

    public void getVel(double rad, double theta) {  //velocity and pressure 
       double ur,uth,jake1,jake2,jakesq ;
       double xloc,yloc,thrad,alfrad ;

       thrad = convdr * theta ;
       alfrad = convdr * alfval ;
                                 /* get x, y location in cylinder plane */
       xloc = rad * Math.cos(thrad) ;
       yloc = rad * Math.sin(thrad) ;
                                 /* velocity in cylinder plane */
       ur  = Math.cos(thrad-alfrad)*(1.0-(rval*rval)/(rad*rad)) ;
       uth = -Math.sin(thrad-alfrad)*(1.0+(rval*rval)/(rad*rad))
                             - gamval/rad;
       usq = ur*ur + uth*uth ;
       vxdir = ur * Math.cos(thrad) - uth * Math.sin(thrad) ; // MODS  20 Jul 99 
                                 /* translate to generate airfoil  */
       xloc = xloc + xcval ;
       yloc = yloc + ycval ;
                                    /* compute new radius-theta  */
       rad = Math.sqrt(xloc*xloc + yloc*yloc) ;
       thrad  = Math.atan2(yloc,xloc) ;
                                    /* compute Joukowski Jacobian  */
       jake1 = 1.0 - Math.cos(2.0*thrad)/(rad*rad) ;
       jake2 = Math.sin(2.0*thrad)/(rad*rad) ;
       jakesq = jake1*jake1 + jake2*jake2 ;
       if (Math.abs(jakesq) <= .01) jakesq = .01 ;  /* protection */
       vsq = usq / jakesq ;
         /* vel is velocity ratio - pres is coefficient  (p-p0)/q0   */
       vel = Math.sqrt(vsq) ;
       pres = 1.0 - vsq ;
    }

    public void getDrag(double cldin)     //Drag Interpolator
       { 
       double dragCam0Thk5, dragCam5Thk5, dragCam10Thk5, dragCam15Thk5, dragCam20Thk5;
       double dragCam0Thk10, dragCam5Thk10, dragCam10Thk10, dragCam15Thk10, dragCam20Thk10;
       double dragCam0Thk15, dragCam5Thk15, dragCam10Thk15, dragCam15Thk15, dragCam20Thk15;
       double dragCam0Thk20, dragCam5Thk20, dragCam10Thk20, dragCam15Thk20, dragCam20Thk20;
       double dragThk5, dragThk10, dragThk15, dragThk20;

       dragCam0Thk5 = -9E-07*Math.pow(alfd,3) + 0.0007*Math.pow(alfd,2) + 0.0008*alfd + 0.015;
       dragCam5Thk5 = 4E-08*Math.pow(alfd,5) - 7E-07*Math.pow(alfd,4) - 1E-05*Math.pow(alfd,3) + 0.0009*Math.pow(alfd,2) + 0.0033*alfd + 0.0301;
       dragCam10Thk5 = -9E-09*Math.pow(alfd,6) - 6E-08*Math.pow(alfd,5) + 5E-06*Math.pow(alfd,4) + 3E-05*Math.pow(alfd,3) - 0.0001*Math.pow(alfd,2) - 0.0025*alfd + 0.0615;
       dragCam15Thk5 = 8E-10*Math.pow(alfd,6) - 5E-08*Math.pow(alfd,5) - 1E-06*Math.pow(alfd,4) + 3E-05*Math.pow(alfd,3) + 0.0008*Math.pow(alfd,2) - 0.0027*alfd + 0.0612;
       dragCam20Thk5 = 8E-9*Math.pow(alfd,6) + 1E-8*Math.pow(alfd,5) - 5E-6*Math.pow(alfd,4) + 6E-6*Math.pow(alfd,3) + 0.001*Math.pow(alfd,2) - 0.001*alfd + 0.1219;

       dragCam0Thk10 = -1E-08*Math.pow(alfd,6) + 6E-08*Math.pow(alfd,5) + 6E-06*Math.pow(alfd,4) - 2E-05*Math.pow(alfd,3) - 0.0002*Math.pow(alfd,2) + 0.0017*alfd + 0.0196;
       dragCam5Thk10 = 3E-09*Math.pow(alfd,6) + 6E-08*Math.pow(alfd,5) - 2E-06*Math.pow(alfd,4) - 3E-05*Math.pow(alfd,3) + 0.0008*Math.pow(alfd,2) + 0.0038*alfd + 0.0159;
       dragCam10Thk10 = -5E-09*Math.pow(alfd,6) - 3E-08*Math.pow(alfd,5) + 2E-06*Math.pow(alfd,4) + 1E-05*Math.pow(alfd,3) + 0.0004*Math.pow(alfd,2) - 3E-05*alfd + 0.0624;
       dragCam15Thk10 = -2E-09*Math.pow(alfd,6) - 2E-08*Math.pow(alfd,5) - 5E-07*Math.pow(alfd,4) + 8E-06*Math.pow(alfd,3) + 0.0009*Math.pow(alfd,2) + 0.0034*alfd + 0.0993;
       dragCam20Thk10 = 2E-09*Math.pow(alfd,6) - 3E-08*Math.pow(alfd,5) - 2E-06*Math.pow(alfd,4) + 2E-05*Math.pow(alfd,3) + 0.0009*Math.pow(alfd,2) + 0.0023*alfd + 0.1581;

       dragCam0Thk15 = -5E-09*Math.pow(alfd,6) + 7E-08*Math.pow(alfd,5) + 3E-06*Math.pow(alfd,4) - 3E-05*Math.pow(alfd,3) - 7E-05*Math.pow(alfd,2) + 0.0017*alfd + 0.0358;
       dragCam5Thk15 = -4E-09*Math.pow(alfd,6) - 8E-09*Math.pow(alfd,5) + 2E-06*Math.pow(alfd,4) - 9E-07*Math.pow(alfd,3) + 0.0002*Math.pow(alfd,2) + 0.0031*alfd + 0.0351;
       dragCam10Thk15 = 3E-09*Math.pow(alfd,6) + 3E-08*Math.pow(alfd,5) - 2E-06*Math.pow(alfd,4) - 1E-05*Math.pow(alfd,3) + 0.0009*Math.pow(alfd,2) + 0.004*alfd + 0.0543;
       dragCam15Thk15 = 3E-09*Math.pow(alfd,6) + 5E-08*Math.pow(alfd,5) - 2E-06*Math.pow(alfd,4) - 3E-05*Math.pow(alfd,3) + 0.0008*Math.pow(alfd,2) + 0.0087*alfd + 0.1167;
       dragCam20Thk15 = 3E-10*Math.pow(alfd,6) - 3E-08*Math.pow(alfd,5) - 6E-07*Math.pow(alfd,4) + 3E-05*Math.pow(alfd,3) + 0.0006*Math.pow(alfd,2) + 0.0008*alfd + 0.1859;

       dragCam0Thk20 = -3E-09*Math.pow(alfd,6) + 2E-08*Math.pow(alfd,5) + 2E-06*Math.pow(alfd,4) - 8E-06*Math.pow(alfd,3) - 4E-05*Math.pow(alfd,2) + 0.0003*alfd + 0.0416;
       dragCam5Thk20 = -3E-09*Math.pow(alfd,6) - 7E-08*Math.pow(alfd,5) + 1E-06*Math.pow(alfd,4) + 3E-05*Math.pow(alfd,3) + 0.0004*Math.pow(alfd,2) + 5E-05*alfd + 0.0483;
       dragCam10Thk20 = 1E-08*Math.pow(alfd,6) + 4E-08*Math.pow(alfd,5) - 6E-06*Math.pow(alfd,4) - 2E-05*Math.pow(alfd,3) + 0.0014*Math.pow(alfd,2) + 0.007*alfd + 0.0692;
       dragCam15Thk20 = 3E-09*Math.pow(alfd,6) - 9E-08*Math.pow(alfd,5) - 3E-06*Math.pow(alfd,4) + 4E-05*Math.pow(alfd,3) + 0.001*Math.pow(alfd,2) + 0.0021*alfd + 0.139;
       dragCam20Thk20 = 3E-09*Math.pow(alfd,6) - 7E-08*Math.pow(alfd,5) - 3E-06*Math.pow(alfd,4) + 4E-05*Math.pow(alfd,3) + 0.0012*Math.pow(alfd,2) + 0.001*alfd + 0.1856;

       if (-20.0 <= camd && camd < -15.0)
           {
           dragThk5 = (camd/5 + 4)*(dragCam15Thk5 - dragCam20Thk5) + dragCam20Thk5;
           dragThk10 = (camd/5 + 4)*(dragCam15Thk10 - dragCam20Thk10) + dragCam20Thk10;
           dragThk15 = (camd/5 + 4)*(dragCam15Thk15 - dragCam20Thk15) + dragCam20Thk15;
           dragThk20 = (camd/5 + 4)*(dragCam15Thk20 - dragCam20Thk20) + dragCam20Thk20;

           if (1.0 <= thkd && thkd <= 5.0)
               {
               dragco = dragThk5;
               }
           else if (5.0 < thkd && thkd <= 10.0)
               {
               dragco = (thkd/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
               }
           else if (10.0 < thkd && thkd <= 15.0)
               {
               dragco = (thkd/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
               }
           else if (15.0 < thkd && thkd <= 20.0)
               {
               dragco = (thkd/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
               }
           }
       else if (-15.0 <= camd && camd < -10.0)
           {
           dragThk5 = (camd/5 + 3)*(dragCam10Thk5 - dragCam15Thk5) + dragCam15Thk5;
           dragThk10 = (camd/5 + 3)*(dragCam10Thk10 - dragCam15Thk10) + dragCam15Thk10;
           dragThk15 = (camd/5 + 3)*(dragCam10Thk15 - dragCam15Thk15) + dragCam15Thk15;
           dragThk20 = (camd/5 + 3)*(dragCam10Thk20 - dragCam15Thk20) + dragCam15Thk20;

           if (1.0 <= thkd && thkd <= 5.0)
               {
               dragco = dragThk5;
               }
           else if (5.0 < thkd && thkd <= 10.0)
               {
               dragco = (thkd/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
               }
           else if (10.0 < thkd && thkd <= 15.0)
               {
               dragco = (thkd/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
               }
           else if (15.0 < thkd && thkd <= 20.0)
               {
               dragco = (thkd/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
               }
           }
       else if (-10.0 <= camd && camd < -5.0)
           {
           dragThk5 = (camd/5 + 2)*(dragCam5Thk5 - dragCam10Thk5) + dragCam10Thk5;
           dragThk10 = (camd/5 + 2)*(dragCam5Thk10 - dragCam10Thk10) + dragCam10Thk10;
           dragThk15 = (camd/5 + 2)*(dragCam5Thk15 - dragCam10Thk15) + dragCam10Thk15;
           dragThk20 = (camd/5 + 2)*(dragCam5Thk20 - dragCam10Thk20) + dragCam10Thk20;

           if (1.0 <= thkd && thkd <= 5.0)
               {
               dragco = dragThk5;
               }
           else if (5.0 < thkd && thkd <= 10.0)
               {
               dragco = (thkd/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
               }
           else if (10.0 < thkd && thkd <= 15.0)
               {
               dragco = (thkd/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
               }
           else if (15.0 < thkd && thkd <= 20.0)
               {
               dragco = (thkd/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
               }
           }
       else if (-5.0 <= camd && camd < 0)
           {
           dragThk5 = (camd/5 + 1)*(dragCam0Thk5 - dragCam5Thk5) + dragCam5Thk5;
           dragThk10 = (camd/5 + 1)*(dragCam0Thk10 - dragCam5Thk10) + dragCam5Thk10;
           dragThk15 = (camd/5 + 1)*(dragCam0Thk15 - dragCam5Thk15) + dragCam5Thk15;
           dragThk20 = (camd/5 + 1)*(dragCam0Thk20 - dragCam5Thk20) + dragCam5Thk20;

           if (1.0 <= thkd && thkd <= 5.0)
               {
               dragco = dragThk5;
               }
           else if (5.0 < thkd && thkd <= 10.0)
               {
               dragco = (thkd/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
               }
           else if (10.0 < thkd && thkd <= 15.0)
               {
               dragco = (thkd/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
               }
           else if (15.0 < thkd && thkd <= 20.0)
               {
               dragco = (thkd/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
               }
           }
       else if (0 <= camd && camd < 5)
           {
           dragThk5 = (camd/5)*(dragCam5Thk5 - dragCam0Thk5) + dragCam0Thk5;
           dragThk10 = (camd/5)*(dragCam5Thk10 - dragCam0Thk10) + dragCam0Thk10;
           dragThk15 = (camd/5)*(dragCam5Thk15 - dragCam0Thk15) + dragCam0Thk15;
           dragThk20 = (camd/5)*(dragCam5Thk20 - dragCam0Thk20) + dragCam0Thk20;

           if (1.0 <= thkd && thkd <= 5.0)
               {
               dragco = dragThk5;
               }
           else if (5.0 < thkd && thkd <= 10.0)
               {
               dragco = (thkd/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
               }
           else if (10.0 < thkd && thkd <= 15.0)
               {
               dragco = (thkd/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
               }
           else if (15.0 < thkd && thkd <= 20.0)
               {
               dragco = (thkd/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
               }
           }
       else if (5 <= camd && camd < 10)
           {
           dragThk5 = (camd/5 - 1)*(dragCam10Thk5 - dragCam5Thk5) + dragCam5Thk5;
           dragThk10 = (camd/5 - 1)*(dragCam10Thk10 - dragCam5Thk10) + dragCam5Thk10;
           dragThk15 = (camd/5 - 1)*(dragCam10Thk15 - dragCam5Thk15) + dragCam5Thk15;
           dragThk20 = (camd/5 - 1)*(dragCam10Thk20 - dragCam5Thk20) + dragCam5Thk20;

           if (1.0 <= thkd && thkd <= 5.0)
               {
               dragco = dragThk5;
               }
           else if (5.0 < thkd && thkd <= 10.0)
               {
               dragco = (thkd/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
               }
           else if (10.0 < thkd && thkd <= 15.0)
               {
               dragco = (thkd/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
               }
           else if (15.0 < thkd && thkd <= 20.0)
               {
               dragco = (thkd/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
               }
           }
       else if (10 <= camd && camd < 15)
           {
           dragThk5 = (camd/5 - 2)*(dragCam15Thk5 - dragCam10Thk5) + dragCam10Thk5;
           dragThk10 = (camd/5 - 2)*(dragCam15Thk10 - dragCam10Thk10) + dragCam10Thk10;
           dragThk15 = (camd/5 - 2)*(dragCam15Thk15 - dragCam10Thk15) + dragCam10Thk15;
           dragThk20 = (camd/5 - 2)*(dragCam15Thk20 - dragCam10Thk20) + dragCam10Thk20;

           if (1.0 <= thkd && thkd <= 5.0)
               {
               dragco = dragThk5;
               }
           else if (5.0 < thkd && thkd <= 10.0)
               {
               dragco = (thkd/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
               }
           else if (10.0 < thkd && thkd <= 15.0)
               {
               dragco = (thkd/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
               }
           else if (15.0 < thkd && thkd <= 20.0)
               {
               dragco = (thkd/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
               }
           }
       else if (15 <= camd && camd <= 20)
           {
           dragThk5 = (camd/5 - 3)*(dragCam20Thk5 - dragCam15Thk5) + dragCam15Thk5;
           dragThk10 = (camd/5 - 3)*(dragCam20Thk10 - dragCam15Thk10) + dragCam15Thk10;
           dragThk15 = (camd/5 - 3)*(dragCam20Thk15 - dragCam15Thk15) + dragCam15Thk15;
           dragThk20 = (camd/5 - 3)*(dragCam20Thk20 - dragCam15Thk20) + dragCam15Thk20;

           if (1.0 <= thkd && thkd <= 5.0)
               {
               dragco = dragThk5;
               }
           else if (5.0 < thkd && thkd <= 10.0)
               {
               dragco = (thkd/5 - 1)*(dragThk10 - dragThk5) + dragThk5;
               }
           else if (10.0 < thkd && thkd <= 15.0)
               {
               dragco = (thkd/5 - 2)*(dragThk15 - dragThk10) + dragThk10;
               }
           else if (15.0 < thkd && thkd <= 20.0)
               {
               dragco = (thkd/5 - 3)*(dragThk20 - dragThk15) + dragThk15;
               }
           }
       }
}
