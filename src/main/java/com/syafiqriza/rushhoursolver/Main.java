package main.java.com.syafiqriza.rushhoursolver;

import main.java.com.syafiqriza.rushhoursolver.view.CLI;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
            System.out.println("GUI anjay gurinjay");
            // GUI gui = new GUI();
            // gui.run();
        } else {
            CLI cli = new CLI();
            cli.run();
        }
    }
}
