/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liftdrag;

import gui.MainWindow;
import org.apache.commons.cli.*;
import optimizer.*;

/**
 *
 * @author madfist
 */
public class LiftDrag {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help");
        options.addOption("c", "camber", true, "Camber in percentage of chord");
        options.addOption("a", "angle", true, "Angle of Attack");
        options.addOption("t", "thickness", true, "Thickness in percentage of chord");
        HelpFormatter help = new HelpFormatter();
        if (args.length > 0) {
            double a = 0.0, c = 1.0, t = 0.1;
            CommandLineParser parser = new GnuParser();
            try {
                CommandLine cmd = parser.parse(options, args);
                if (cmd.hasOption("h")) {
                    help.printHelp("LiftDrag [options]\nNo options opens the GUI", options);
                    return;
                }
                if (cmd.hasOption("c")) {
                    c = Double.parseDouble(cmd.getOptionValue("c"));
                }
                if (cmd.hasOption("a")) {
                    a = Double.parseDouble(cmd.getOptionValue("a"));
                }
                if (cmd.hasOption("t")) {
                    t = Double.parseDouble(cmd.getOptionValue("t"));
                }
                Solver solver = new Solver();
                solver.computeLiftDrag(a, c, t);
                System.out.println("L  : "+solver.getLift());
                System.out.println("D  : "+solver.getDrag());
                System.out.println("L/D: "+solver.getLiftDrag());
            } catch (ParseException ex) {
                System.out.println("invalid paramter input\n"+ex);
                help.printHelp("LiftDrag [options]\nNo options opens the GUI", options);
            }
        } else {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        }
    }
}
