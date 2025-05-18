package com.syafiqriza.rushhoursolver.view;

import java.io.IOException;
import java.util.Scanner;

import com.syafiqriza.rushhoursolver.model.Board;
import com.syafiqriza.rushhoursolver.model.State;
import com.syafiqriza.rushhoursolver.model.Utils;
import com.syafiqriza.rushhoursolver.model.algorithm.AStar;
import com.syafiqriza.rushhoursolver.model.algorithm.Algorithm;
import com.syafiqriza.rushhoursolver.model.algorithm.GreedyBestFirstSearch;
import com.syafiqriza.rushhoursolver.model.algorithm.InformedSearch;
import com.syafiqriza.rushhoursolver.model.algorithm.UniformCostSearch;
import com.syafiqriza.rushhoursolver.model.algorithm.Algorithm.SolutionData;
import com.syafiqriza.rushhoursolver.model.heuristic.BlockingHeuristic;
import com.syafiqriza.rushhoursolver.model.heuristic.DistanceHeuristic;
import com.syafiqriza.rushhoursolver.model.heuristic.Heuristic;

public class CLI {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String filePath;

            if (args.length == 0) {
                System.out.print("Masukkan path ke file puzzle (.txt): ");
                filePath = sc.nextLine();
            } else {
                filePath = args[0];
            }

            Board board;
            try {
                board = Utils.readRushHourPuzzleFromFile(filePath);
                board.printBoard();
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

            System.out.println();

            int chosenAlgorithm = 0;
            System.out.println("""
                    Pilih algoritma:
                    1. Uniform Cost Search
                    2. Greedy Best First Search
                    3. A*
                        """);

            while (true) {
                System.out.print(">>");
                if (!sc.hasNextInt()) {
                    System.out.println("Masukkan pilihan angka yang valid");
                    sc.nextLine();
                    continue;
                }
                chosenAlgorithm = sc.nextInt();
                if (chosenAlgorithm > 0 && chosenAlgorithm < 4) {
                    break;
                }
                System.out.println("Masukkan pilihan angka yang valid");
            }

            Algorithm algorithm = null;
            switch (chosenAlgorithm) {
                case 1:
                    algorithm = new UniformCostSearch();
                    break;
                case 2:
                    algorithm = new GreedyBestFirstSearch();
                    break;
                case 3:
                    algorithm = new AStar();
                    break;

                default:
                    assert false;
            }

            if (algorithm instanceof InformedSearch alg) {
                int chosenHeuristic = 0;
                System.out.println("""
                        Pilih heuristik:
                        1. Blocking Cars
                        2. Distance to Goal
                            """);

                while (true) {
                    System.out.print(">>");
                    if (!sc.hasNextInt()) {
                        System.out.println("Masukkan pilihan angka yang valid");
                        sc.nextLine();
                        continue;
                    }
                    chosenHeuristic = sc.nextInt();
                    if (chosenHeuristic > 0 && chosenHeuristic < 3) {
                        break;
                    }

                    System.out.println("Masukkan pilihan angka yang valid");
                }

                Heuristic heuristic = null;
                switch (chosenHeuristic) {
                    case 1:
                        heuristic = new BlockingHeuristic();
                        break;
                    case 2:
                        heuristic = new DistanceHeuristic();
                        break;

                    default:
                        assert false;
                }
                alg.setHeuristicModel(heuristic);
            }

            algorithm.solve(new State(board, 0, 0));

            SolutionData solutionData = algorithm.getSolution();

            System.out.println();

            if (solutionData.states != null) {
                System.out.println("Solusi: ");
                for (State state : solutionData.states) {
                    System.out.println(state.getBoard().getDetail());
                    state.getBoard().printBoard();
                    System.out.println();
                }

                System.out.println("Solusi ditemukan");
            } else {
                System.out.println("Solusi tidak ditemukan");
            }
            System.out.println();

            System.out.println("Waktu (ms)      : " + solutionData.timeElapsedMs);
            System.out.println("Node dikunjungi : " + solutionData.nodeCount);

            if (solutionData.states != null)
                System.out.println("Jumlah langkah  : " + solutionData.states.length);

            System.out.println();

        }
    }
}