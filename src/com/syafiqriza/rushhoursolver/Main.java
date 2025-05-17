package com.syafiqriza.rushhoursolver;

import com.syafiqriza.rushhoursolver.model.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String filePath;

        if (args.length == 0) {
            System.out.print("Masukkan path ke file puzzle (.txt): ");
            filePath = sc.nextLine();
        } else {
            filePath = args[0];
        }

        try {
            Board board = Utils.readRushHourPuzzleFromFile(filePath);
            board.printBoard();
            System.out.println("- Tujuan: (" + board.getGoalRow() + ", " + board.getGoalCol() + ")");

        } catch (IOException e) {
            System.err.println("File tidak ditemukan: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Puzzle tidak valid: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
