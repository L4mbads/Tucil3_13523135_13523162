package com.syafiqriza.rushhoursolver.view;

import java.io.IOException;
import java.util.Scanner;

import com.syafiqriza.rushhoursolver.model.Board;
import com.syafiqriza.rushhoursolver.model.State;
import com.syafiqriza.rushhoursolver.model.Utils;
import com.syafiqriza.rushhoursolver.model.algorithm.Algorithm;
import com.syafiqriza.rushhoursolver.model.algorithm.UniformCostSearch;
import com.syafiqriza.rushhoursolver.model.heuristic.BlockingHeuristic;
import com.syafiqriza.rushhoursolver.model.heuristic.Heuristic;

public class CLI {
    public void run() {
        try (Scanner sc = new Scanner(System.in)) {
            String filePath;

            // if (args.length == 0) {
            System.out.print("Masukkan path ke file puzzle (.txt): ");
            filePath = sc.nextLine();
            // } else {
            //     filePath = args[0];
            // }

            Board board;
            try {
                board = Utils.readRushHourPuzzleFromFile(filePath);
                board.printBoard();
                System.out.println("- Tujuan: (" + board.getGoalRow() + ", " + board.getGoalCol() + ")");
            } catch (IOException e) {
                System.err.println("File tidak ditemukan: " + e.getMessage());
                return;
            } catch (IllegalArgumentException e) {
                System.err.println("Puzzle tidak valid: " + e.getMessage());
                return;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                return;
            }

            int chosenHeuristic = 0;
            System.out.println("""
                Pilih heuristik:
                1. Blocking Cars
                    """);

            while(true) {
                while(!sc.hasNextInt()) {
                    sc.nextLine();
                    System.out.println("Masukkan pilihan angka yang valid");
                    continue;
                }
                chosenHeuristic = sc.nextInt();
                if(chosenHeuristic > 0 && chosenHeuristic < 2) {
                    break;
                }
                System.out.println("Masukkan pilihan angka yang valid");
            }

            Heuristic heuristic;
            switch (chosenHeuristic) {
                case 1:
                    heuristic = new BlockingHeuristic();
                    break;

                default:
                    assert false;
            }

            int chosenAlgorithm = 0;
            System.out.println("""
                Pilih algoritma:
                1. Uniform Cost Search
                2. Greedy Best First Search
                3. A*
                    """);

            while(!sc.hasNextInt()) {
                System.out.println("Masukkan pilihan angka yang valid");
                sc.nextLine();
            }
            while(chosenAlgorithm <= 0 || chosenAlgorithm > 3) {
                chosenAlgorithm = sc.nextInt();
            }

            Algorithm algorithm = null;
            switch (chosenAlgorithm) {
                case 1:
                case 2:
                case 3:
                    algorithm = new UniformCostSearch();
                    break;

                default:
                    assert false;
            }


            algorithm.solve(new State(board, 0, 0));

            for(State state : algorithm.getSolution()) {
                System.out.println(state.getBoard().getDetail());
                state.getBoard().printBoard();;
            }

        }
    }
}