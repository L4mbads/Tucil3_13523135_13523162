package com.syafiqriza.rushhoursolver;

import com.syafiqriza.rushhoursolver.view.GUI;
import com.syafiqriza.rushhoursolver.view.CLI;

import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
       if (args.length > 0 && args[0].equalsIgnoreCase("cli")) {
           CLI.main(Arrays.copyOfRange(args, 1, args.length));
       } else {
           GUI.main(args);
       }
    }
}
