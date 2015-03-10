/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualize;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import optimizer.Foil;
import session.OptimizationResult;

/**
 *
 * @author madfist
 */
public class WingPanel extends JPanel implements Runnable, ParameterSetters {

    Thread runner;
    Point locate, anchor;
    int antim, ancol;
    Image img;
    Foil f;

    public WingPanel() {
        f = new Foil();
        setPreferredSize(new Dimension(400, 200));
    }

    //@Override
    //public void update(Graphics g) {
    //    paint(g);
    //}

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        Color col = Color.cyan;
        g.setColor(Color.black);
        g.fillRect(0, 0, 500, 500);

        if (f.viewflg == 0 || f.viewflg == 2) {  // edge View
            if (Foil.vfsd > .01) {
                /* plot airfoil flowfield */
                drawLower(.5, g, col);

                drawStagnationLine(g);

                drawUpper(.5, g, col);

                drawWing(g);
            }
        }
    }

    private void drawWing(Graphics g) {
        // draw the airfoil geometry
        int[] xs = new int[4];
        int[] ys = new int[4];
        g.setColor(Color.white);
        xs[1] = (int) (Foil.fact * (Foil.xpl[0][f.npt2])) + f.xt;
        ys[1] = (int) (Foil.fact * (-Foil.ypl[0][f.npt2])) + f.yt;
        xs[2] = (int) (Foil.fact * (Foil.xpl[0][f.npt2])) + f.xt;
        ys[2] = (int) (Foil.fact * (-Foil.ypl[0][f.npt2])) + f.yt;
        for (int i = 1; i <= f.npt2 - 1; ++i) {
            xs[0] = xs[1];
            ys[0] = ys[1];
            xs[1] = (int) (Foil.fact * (Foil.xpl[0][f.npt2 - i])) + f.xt;
            ys[1] = (int) (Foil.fact * (-Foil.ypl[0][f.npt2 - i])) + f.yt;
            xs[3] = xs[2];
            ys[3] = ys[2];
            xs[2] = (int) (Foil.fact * (Foil.xpl[0][f.npt2 + i])) + f.xt;
            ys[2] = (int) (Foil.fact * (-Foil.ypl[0][f.npt2 + i])) + f.yt;
            //f.camx[i] = (xs[1] + xs[2]) / 2;
            //f.camy[i] = (ys[1] + ys[2]) / 2;
            g.setColor(Color.white);
            g.fillPolygon(xs, ys, 4);
        }
    }

    private void drawUpper(double radvec, Graphics g, Color col) {
        double slope;
        double xvec;
        double yvec;
        int[] xs = new int[2];
        int[] ys = new int[2];
        for (int j = f.nln2 + 1; j <= f.nlnc; ++j) {          /* upper half */
            
            for (int i = 1; i <= f.nptc - 1; ++i) {
                xs[0] = (int) (Foil.fact * Foil.xpl[j][i]) + f.xt;
                ys[0] = (int) (Foil.fact * (-Foil.ypl[j][i])) + f.yt;
                slope = (Foil.ypl[j][i + 1] - Foil.ypl[j][i]) / (Foil.xpl[j][i + 1] - Foil.xpl[j][i]);
                xvec = Foil.xpl[j][i] + radvec / Math.sqrt(1.0 + slope * slope);
                yvec = Foil.ypl[j][i] + slope * (xvec - Foil.xpl[j][i]);
                xs[1] = (int) (Foil.fact * xvec) + f.xt;
                ys[1] = (int) (Foil.fact * (-yvec)) + f.yt;
                if (f.displ == 0) {                     /* MODS  21 JUL 99 */
                    
                    g.setColor(col);
                    xs[1] = (int) (Foil.fact * Foil.xpl[j][i + 1]) + f.xt;
                    ys[1] = (int) (Foil.fact * (-Foil.ypl[j][i + 1])) + f.yt;
                    g.drawLine(xs[0], ys[0], xs[1], ys[1]);
                }
                if (f.displ == 2 && (i / 3 * 3 == i)) {
                    g.setColor(col);   /* MODS  27 JUL 99 */
                    
                    for (int n = 1; n <= 4; ++n) {
                        if (i == 6 + (n - 1) * 9) {
                            g.setColor(Color.yellow);
                        }
                    }
                    if (i / 9 * 9 == i) {
                        g.setColor(Color.white);
                    }
                    g.drawLine(xs[0], ys[0], xs[1], ys[1]);
                }
                if (f.displ == 1 && ((i - antim) / 3 * 3 == (i - antim))) {
                    if (ancol == -1) {          /* MODS  27 JUL 99 */
                        
                        if ((i - antim) / 6 * 6 == (i - antim)) {
                            g.setColor(col);
                        }
                        if ((i - antim) / 6 * 6 != (i - antim)) {
                            g.setColor(Color.white);
                        }
                    }
                    if (ancol == 1) {          /* MODS  27 JUL 99 */
                        
                        if ((i - antim) / 6 * 6 == (i - antim)) {
                            g.setColor(Color.white);
                        }
                        if ((i - antim) / 6 * 6 != (i - antim)) {
                            g.setColor(col);
                        }
                    }
                    g.drawLine(xs[0], ys[0], xs[1], ys[1]);
                }
            }
        }
    }

    private void drawStagnationLine(Graphics g) {
        int[] xs = new int[2];
        int[] ys = new int[2];
        g.setColor(Color.white); /* stagnation */
        
        xs[1] = (int) (Foil.fact * Foil.xpl[f.nln2][1]) + f.xt;
        ys[1] = (int) (Foil.fact * (-Foil.ypl[f.nln2][1])) + f.yt;
        for (int i = 2; i <= f.npt2 - 1; ++i) {
            xs[0] = xs[1];
            ys[0] = ys[1];
            xs[1] = (int) (Foil.fact * Foil.xpl[f.nln2][i]) + f.xt;
            ys[1] = (int) (Foil.fact * (-Foil.ypl[f.nln2][i])) + f.yt;
            if (f.displ <= 2) {             /* MODS  21 JUL 99 */
                
                g.drawLine(xs[0], ys[0], xs[1], ys[1]);
            }
        }
        xs[1] = (int) (Foil.fact * Foil.xpl[f.nln2][f.npt2 + 1]) + f.xt;
        ys[1] = (int) (Foil.fact * (-Foil.ypl[f.nln2][f.npt2 + 1])) + f.yt;
        for (int i = f.npt2 + 2; i <= f.nptc; ++i) {
            xs[0] = xs[1];
            ys[0] = ys[1];
            xs[1] = (int) (Foil.fact * Foil.xpl[f.nln2][i]) + f.xt;
            ys[1] = (int) (Foil.fact * (-Foil.ypl[f.nln2][i])) + f.yt;
            if (f.displ <= 2) {                         /* MODS  21 JUL 99 */
                
                g.drawLine(xs[0], ys[0], xs[1], ys[1]);
            }
        }
    }

    private void drawLower(double radvec, Graphics g, Color col) {
        double slope;
        double xvec;
        double yvec;
        int[] xs = new int[2];
        int[] ys = new int[2];
        for (int j = 1; j <= f.nln2 - 1; ++j) {           /* lower half */
            for (int i = 1; i <= f.nptc - 1; ++i) {
                xs[0] = (int) (Foil.fact * Foil.xpl[j][i]) + f.xt;
                ys[0] = (int) (Foil.fact * (-Foil.ypl[j][i])) + f.yt;
                slope = (Foil.ypl[j][i + 1] - Foil.ypl[j][i]) / (Foil.xpl[j][i + 1] - Foil.xpl[j][i]);
                xvec = Foil.xpl[j][i] + radvec / Math.sqrt(1.0 + slope * slope);
                yvec = Foil.ypl[j][i] + slope * (xvec - Foil.xpl[j][i]);
                xs[1] = (int) (Foil.fact * xvec) + f.xt;
                ys[1] = (int) (Foil.fact * (-yvec)) + f.yt;
                if (f.displ == 0) {                   /* MODS  21 JUL 99 */
                    
                    g.setColor(Color.yellow);
                    xs[1] = (int) (Foil.fact * Foil.xpl[j][i + 1]) + f.xt;
                    ys[1] = (int) (Foil.fact * (-Foil.ypl[j][i + 1])) + f.yt;
                    g.drawLine(xs[0], ys[0], xs[1], ys[1]);
                }
                if (f.displ == 2 && (i / 3 * 3 == i)) {
                    g.setColor(col);
                    for (int n = 1; n <= 4; ++n) {
                        if (i == 6 + (n - 1) * 9) {
                            g.setColor(Color.yellow);
                        }
                    }
                    if (i / 9 * 9 == i) {
                        g.setColor(Color.white);
                    }
                    g.drawLine(xs[0], ys[0], xs[1], ys[1]);
                }
                if (f.displ == 1 && ((i - antim) / 3 * 3 == (i - antim))) {
                    
                    g.setColor(Color.white);
                    if (ancol == -1) {          /* MODS  27 JUL 99 */
                        
                        if ((i - antim) / 6 * 6 == (i - antim)) {
                            g.setColor(col);
                        } else {
                            g.setColor(Color.white);
                        }
                    }
                    if (ancol == 1) {          /* MODS  27 JUL 99 */
                        
                        if ((i - antim) / 6 * 6 == (i - antim)) {
                            g.setColor(Color.white);
                        } else {
                            g.setColor(col);
                        }
                    }
                    g.drawLine(xs[0], ys[0], xs[1], ys[1]);
                }
            }
        }
    }

    public void start() {
        if (runner == null) {
            runner = new Thread(this);
            runner.start();
        }
        antim = 0;                              /* MODS  21 JUL 99 */

        ancol = 1;                              /* MODS  27 JUL 99 */

    }

    @Override
    public void run() {
        int timer;

        timer = 100;
        while (true) {
            ++antim;
            try {
                Thread.sleep(timer);
            } catch (InterruptedException e) {
            }
            repaint();
            if (antim == 3) {
                antim = 0;
                ancol = -ancol;               /* MODS 27 JUL 99 */

            }
            timer = 135 - (int) (.227 * f.vfsd / f.vconv);
            // make the ball spin
        }
    }

    @Override
    public void setParameters(OptimizationResult result) {
        f.computeFlow(result.thickness, result.camber, result.angle);
    }

    @Override
    public void setParameters(double thickness, double camber, double angle) {
        f.computeFlow(thickness, camber, angle);
    }
}
