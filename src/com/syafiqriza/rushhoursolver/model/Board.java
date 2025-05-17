package com.syafiqriza.rushhoursolver.model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int rows;
    private final int cols;
    private final String goalCarId;
    private final int goalRow;
    private final int goalCol;

    private Map<String, Car> cars;
    private char[][] grid;

    public Board(int rows, int cols, Map<String, Car> cars,
            String goalCarId, int goalRow, int goalCol) {
        this.rows = rows;
        this.cols = cols;
        this.goalCarId = goalCarId;
        this.goalRow = goalRow;
        this.goalCol = goalCol;
        this.cars = cars;
        buildGrid();
    }

    private void buildGrid() {
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], '.'); // '.' artinya sel kosong
        }
        for (Car car : cars.values()) {
            char idChar = car.getId(); // asumsi ID satu huruf
            for (int[] cell : car.getOccupiedCells()) {
                grid[cell[0]][cell[1]] = idChar;
            }
        }
    }

    public boolean canMove(String id, int offset) {
        Car car = cars.get(id);
        Car temp = car.copy();
        temp.move(offset);

        char idChar = id.charAt(0);

        for (int[] cell : temp.getOccupiedCells()) {
            int r = cell[0], c = cell[1];
            if (r < 0 || r >= rows || c < 0 || c >= cols)
                return false;
            if (grid[r][c] != '.' && grid[r][c] != idChar)
                return false;
        }
        return true;
    }

    public void applyMove(String id, int offset) {
        cars.get(id).move(offset);
        buildGrid();
    }

    public void undoMove(String id, int offset) {
        cars.get(id).undoMove(offset);
        buildGrid();
    }

    public boolean isSolved() {
        Car car = cars.get(goalCarId);
        for (int[] cell : car.getOccupiedCells()) {
            if (cell[0] == goalRow && cell[1] == goalCol) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Car> getCarsCopy() {
        Map<String, Car> copy = new HashMap<>();
        for (Map.Entry<String, Car> entry : cars.entrySet()) {
            copy.put(entry.getKey(), entry.getValue().copy());
        }
        return copy;
    }

    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Board[] getAllPossibleMovement() {
        List<Board> possibleBoards = new ArrayList<>();

        for (String carId : cars.keySet()) {
            Car car = cars.get(carId);

            // coba gerakan ke arah negatif (mundur)
            int offset = -1;
            while (canMove(carId, offset)) {
                Board newBoard = new Board(rows, cols, getCarsCopy(), goalCarId, goalRow, goalCol);
                newBoard.applyMove(carId, offset);
                possibleBoards.add(newBoard);
                offset--; // Coba gerakan lebih jauh
            }

            // coba gerakan ke arah positif (maju)
            offset = 1;
            while (canMove(carId, offset)) {
                Board newBoard = new Board(rows, cols, getCarsCopy(), goalCarId, goalRow, goalCol);
                newBoard.applyMove(carId, offset);
                possibleBoards.add(newBoard);
                offset++; // coba gerakan lebih jauh
            }
        }

        return possibleBoards.toArray(new Board[0]);
    }

    // Getters
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Map<String, Car> getCars() {
        return cars;
    }

    public char[][] getGrid() {
        return grid;
    }

    public String getGoalCarId() {
        return goalCarId;
    }

    public int getGoalRow() {
        return goalRow;
    }

    public int getGoalCol() {
        return goalCol;
    }
}
