/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualize;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import optimizer.Foil;

/**
 *
 * @author madfist
 */
public class WingPanel extends JPanel implements Runnable {
    Thread runner ;
    Point locate,anchor;
    int antim, ancol;
    Image img;
    Foil f;
    
    
    @Override
     public void update(Graphics g) {
        paint(g) ;
     }
 
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);       
        int i,j ;
        int[] xs = new int[8] ;
        int[] ys = new int[8] ;
        double slope,radvec,xvec,yvec ;

        g.setColor(Color.black) ;
        g.fillRect(0,0,500,500) ;

        if (f.vfsd > .01) {
                                           /* plot airfoil flowfield */
            radvec = .5 ;
            for (j=1; j<=f.nln2-1; ++j) {           /* lower half */
               for (i=1 ; i<= f.nptc-1; ++i) {
                  xs[0] = (int) (f.fact*f.xpl[j][i]) + f.xt ;
                  ys[0] = (int) (f.fact*(-f.ypl[j][i])) + f.yt ;
                  slope = (f.ypl[j][i+1]-f.ypl[j][i])/(f.xpl[j][i+1]-f.xpl[j][i]) ;
                  xvec = f.xpl[j][i] + radvec / Math.sqrt(1.0 + slope*slope) ;
                  yvec = f.ypl[j][i] + slope * (xvec - f.xpl[j][i]) ;
                  xs[1] = (int) (f.fact*xvec) + f.xt ;
                  ys[1] = (int) (f.fact*(-yvec)) + f.yt ;
                  if (f.displ == 0) {                   /* MODS  21 JUL 99 */
                    g.setColor(Color.yellow) ;
                    xs[1] = (int) (f.fact*f.xpl[j][i+1]) + f.xt ;
                    ys[1] = (int) (f.fact*(-f.ypl[j][i+1])) + f.yt ;
                    g.drawLine(xs[0],ys[0],xs[1],ys[1]) ;
                  }
               }
             }

            g.setColor(Color.white) ; /* stagnation */
            xs[1] = (int) (f.fact*f.xpl[f.nln2][1]) + f.xt ;
            ys[1] = (int) (f.fact*(-f.ypl[f.nln2][1])) + f.yt ;
            for (i=2 ; i<= f.npt2-1; ++i) {
                  xs[0] = xs[1] ;
                  ys[0] = ys[1] ;
                  xs[1] = (int) (f.fact*f.xpl[f.nln2][i]) + f.xt ;
                  ys[1] = (int) (f.fact*(-f.ypl[f.nln2][i])) + f.yt ;
                  if (f.displ <= 2) {             /* MODS  21 JUL 99 */
                    g.drawLine(xs[0],ys[0],xs[1],ys[1]) ;
                  }
            }
            xs[1] = (int) (f.fact*f.xpl[f.nln2][f.npt2+1]) + f.xt ;
            ys[1] = (int) (f.fact*(-f.ypl[f.nln2][f.npt2+1])) + f.yt ;
            for (i=f.npt2+2 ; i<= f.nptc; ++i) {
                xs[0] = xs[1] ;
                ys[0] = ys[1] ;
                xs[1] = (int) (f.fact*f.xpl[f.nln2][i]) + f.xt ;
                ys[1] = (int) (f.fact*(-f.ypl[f.nln2][i])) + f.yt ;
                g.drawLine(xs[0],ys[0],xs[1],ys[1]) ;
            }
        }
 
  // draw the airfoil geometry
        g.setColor(Color.white) ;
        xs[1] = (int) (f.fact*(f.xpl[0][f.npt2])) + f.xt ;
        ys[1] = (int) (f.fact*(-f.ypl[0][f.npt2])) + f.yt ;
        xs[2] = (int) (f.fact*(f.xpl[0][f.npt2])) + f.xt ;
        ys[2] = (int) (f.fact*(-f.ypl[0][f.npt2])) + f.yt ;
        for (i=1 ; i<= f.npt2-1; ++i) {
            xs[0] = xs[1] ;
            ys[0] = ys[1] ;
            xs[1] = (int) (f.fact*(f.xpl[0][f.npt2-i])) + f.xt ;
            ys[1] = (int) (f.fact*(-f.ypl[0][f.npt2-i])) + f.yt ;
            xs[3] = xs[2] ;
            ys[3] = ys[2] ;
            xs[2] = (int) (f.fact*(f.xpl[0][f.npt2+i])) + f.xt ;
            ys[2] = (int) (f.fact*(-f.ypl[0][f.npt2+i])) + f.yt ;
            g.setColor(Color.white) ;
            g.fillPolygon(xs,ys,4) ;
        }

        g.drawImage(img,0,0,this) ;   
    }
    
    
     public void start() {
        if (runner == null) {
           runner = new Thread(this) ;
           runner.start() ;
        }
        antim = 0 ;                              /* MODS  21 JUL 99 */
        ancol = 1 ;                              /* MODS  27 JUL 99 */
     }

     @Override
     public void run() { 
        while (true) {
            try {
                SwingUtilities.invokeAndWait(new Runnable(){
                    @Override
                    public void run(){
                        ++ antim ;
                        if (antim == 3) {
                            antim = 0;
                            ancol = - ancol ;
                        }
                    }
                });
            } catch (InterruptedException | InvocationTargetException exp) {
                exp.printStackTrace();
            }
        }
    }
}
