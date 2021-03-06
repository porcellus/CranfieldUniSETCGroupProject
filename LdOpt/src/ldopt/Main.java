/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldopt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import org.apache.commons.cli.*;
/**
 *
 * @author madfist
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help");
        options.addOption("c", "camber_min", true, "Camber in percentage of chord");
        options.addOption("a", "angle_min", true, "Angle of Attack");
        options.addOption("t", "thickness_min", true, "Thickness in percentage of chord");
        options.addOption("C", "camber_max", true, "Camber in percentage of chord");
        options.addOption("A", "angle_max", true, "Angle of Attack");
        options.addOption("T", "thickness_max", true, "Thickness in percentage of chord");
        options.addOption("s", "step", true, "Step size for optimizer");
        options.addOption("o", "output", true, "Output filename");
        HelpFormatter help = new HelpFormatter();
        if (args.length > 0) {
            double a = -20.0, c = -20.0, t = 1.0;
            double A = 19.6, C = 19.6, T = 19.81;
            double s = 0.1;
            String o = null;
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
                if (cmd.hasOption("C")) {
                    C = Double.parseDouble(cmd.getOptionValue("C"));
                }
                if (cmd.hasOption("A")) {
                    A = Double.parseDouble(cmd.getOptionValue("A"));
                }
                if (cmd.hasOption("T")) {
                    T = Double.parseDouble(cmd.getOptionValue("T"));
                }
                if (cmd.hasOption("s")) {
                    s = Double.parseDouble(cmd.getOptionValue("s"));
                }
                if (cmd.hasOption("o")) {
                    o = cmd.getOptionValue("o");
                }
                Optimizer optimizer;
                if (o != null) {
                    File f = new File(o);
                    try {
                        optimizer = new Optimizer(new PrintStream(f));
                    } catch (FileNotFoundException e) {
                        optimizer = new Optimizer(System.out);
                    }
                } else {
                    optimizer = new Optimizer(System.out);
                }
                optimizer.set(c, C, t, T, a, A, s);
                optimizer.optimize();
            } catch (ParseException ex) {
                System.out.println("invalid paramter input\n"+ex);
                help.printHelp("LiftDrag [options]", options);
            }
        } else {
            help.printHelp("LiftDrag [options]", options);
        }
    }
    
}
