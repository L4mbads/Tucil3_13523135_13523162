package com.syafiqriza.rushhoursolver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Merepresentasikan papan permainan Rush Hour.
 * Papan terdiri dari grid ukuran rows x cols, sejumlah mobil (termasuk mobil utama),
 * dan posisi tujuan (goal) yang harus dicapai oleh mobil utama.
 */
public class Board {
    private final int rows;         // Jumlah baris pada papan
    private final int cols;         // Jumlah kolom pada papan
    private final char goalCarId;   // ID mobil utama (biasanya 'P')
    private final int goalRow;      // Baris tujuan yang harus dicapai mobil utama
    private final int goalCol;      // Kolom tujuan yang harus dicapai mobil utama

    private final String detail;

    private final Map<Character, Car> cars;  // Map dari ID mobil ke objek Car
    private char[][] grid;             // Representasi visual grid papan saat ini

    /**
     * Konstruktor papan.
     * @param rows jumlah baris papan
     * @param cols jumlah kolom papan
     * @param cars map ID mobil ke objek Car
     * @param goalCarId ID dari mobil utama (yang harus mencapai goal)
     * @param goalRow baris tujuan
     * @param goalCol kolom tujuan
     */
    public Board(int rows, int cols, Map<Character, Car> cars,
                 char goalCarId, int goalRow, int goalCol, String detail) {
        this.rows = rows;
        this.cols = cols;
        this.goalCarId = goalCarId;
        this.goalRow = goalRow;
        this.goalCol = goalCol;
        this.cars = cars;
        this.detail = detail;
        buildGrid();
    }

    /**
     * Membangun ulang grid dari posisi mobil yang ada.
     */
    private void buildGrid() {
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], '.'); // '.' berarti sel kosong
        }
        for (Car car : cars.values()) {
            char idChar = car.getId();
            for (int[] cell : car.getOccupiedCells()) {
                grid[cell[0]][cell[1]] = idChar;
            }
        }
    }

    /**
     * Mengecek apakah mobil dengan ID tertentu bisa bergerak sebanyak offset.
     * @param id ID mobil yang ingin digerakkan
     * @param offset jarak perpindahan
     * @return true jika gerakan valid, false jika tidak
     */
    public boolean canMove(char id, int offset) {
        Car car = cars.get(id);
        Car temp = car.copy();
        temp.move(offset);

        for (int[] cell : temp.getOccupiedCells()) {
            int r = cell[0], c = cell[1];
            if (r < 0 || r >= rows || c < 0 || c >= cols) return false;
            if (grid[r][c] != '.' && grid[r][c] != id) return false;
        }
        return true;
    }

    /**
     * Menerapkan gerakan mobil ke grid.
     * @param id ID mobil
     * @param offset langkah gerakan
     */
    public void applyMove(char id, int offset) {
        cars.get(id).move(offset);
        buildGrid();
    }

    /**
     * Membatalkan gerakan mobil.
     * @param id ID mobil
     * @param offset offset yang ingin dibatalkan
     */
    public void undoMove(char id, int offset) {
        cars.get(id).undoMove(offset);
        buildGrid();
    }

    /**
     * Mengecek apakah mobil utama telah mencapai posisi goal.
     * @return true jika posisi tujuan tercapai
     */
    public boolean isSolved() {
        Car car = cars.get(goalCarId);
        for (int[] cell : car.getOccupiedCells()) {
            if(car.isHorizontal()) {
                cell[1] += goalCol < cell[1] ? -1 : 1;
            } else {
                cell[0] += goalRow < cell[0] ? -1 : 1;
            }
            if (cell[0] == goalRow && cell[1] == goalCol) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mengembalikan salinan dari semua mobil pada papan.
     * @return map ID ke salinan Car
     */
    public Map<Character, Car> getCarsCopy() {
        Map<Character, Car> copy = new HashMap<>();
        for (Map.Entry<Character, Car> entry : cars.entrySet()) {
            copy.put(entry.getKey(), entry.getValue().copy());
        }
        return copy;
    }


    /**
     * Format ANSI untuk simbol Board
     * @param carID
     * @return string berisi ID kendaraan dengan warna
     */
    private String getFormattedBoardSymbol(char carID) {
        StringBuilder formatted = new StringBuilder();
        formatted.append("\u001B[");
        if(carID == getDetail().charAt(0)) {
            formatted.append("1;"); // bold
        }
        if (carID == 'P') {
            formatted.append("31"); // hijau
        } else if (carID == 'K') {
            formatted.append("32"); // hijau
        } else if (carID == getDetail().charAt(0)) {
            formatted.append("34"); // biru
        } else {
            formatted.append("0");
        }

        formatted.append("m").append(carID).append("\u001B[0m");
        return formatted.toString();
    }

    /**
     * Menampilkan papan ke console.
     */
    public void printBoard() {
        var grid = getGrid();
        if(goalRow == -1) {
            for(int j = 0; j < cols; j++) {
                String symbol = getFormattedBoardSymbol(goalCol == j ? 'K' : ' ');
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < rows; i++) {
            if(goalCol == -1) {
                String symbol = getFormattedBoardSymbol(goalRow == i ? 'K' : ' ');
                System.out.print(symbol + " ");
            }
            for (int j = 0; j < cols; j++) {
                String symbol = getFormattedBoardSymbol(grid[i][j]);
                System.out.print(symbol + " ");
            }
            if(goalCol == cols) {
                String symbol = getFormattedBoardSymbol(goalRow == i ? 'K' : ' ');
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
        if(goalRow == rows) {
            for(int j = 0; j < cols; j++) {
                String symbol = getFormattedBoardSymbol(goalCol == j ? 'K' : ' ');
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        char[][] grid = getGrid();

        if (goalRow == -1) {
            for (int j = 0; j < cols; j++) {
                char c = (goalCol == j) ? 'K' : ' ';
                sb.append(c).append(" ");
            }
            sb.append("\n");
        }

        for (int i = 0; i < rows; i++) {
            if (goalCol == -1) {
                char c = (goalRow == i) ? 'K' : ' ';
                sb.append(c).append(" ");
            }

            for (int j = 0; j < cols; j++) {
                sb.append(grid[i][j]).append(" ");
            }

            if (goalCol == cols) {
                char c = (goalRow == i) ? 'K' : ' ';
                sb.append(c).append(" ");
            }
            sb.append("\n");
        }

        if (goalRow == rows) {
            for (int j = 0; j < cols; j++) {
                char c = (goalCol == j) ? 'K' : ' ';
                sb.append(c).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }


    /**
     * Mendapatkan semua kemungkinan gerakan Car dari Board saat ini.
     * @return Array berisi kemungkinan kondisi Board selanjutnya
     */
    public Board[] getAllPossibleMovement() {
        List<Board> possibleBoards = new ArrayList<>();

        for (char carId : cars.keySet()) {
            Car car = cars.get(carId);

            // coba gerakan ke arah negatif (mundur)
            int offset = -1;
            while (canMove(carId, offset)) {
                String movement = carId + (car.isHorizontal() ? "-kiri" : "-atas");
                Board newBoard = new Board(rows, cols, getCarsCopy(), goalCarId, goalRow, goalCol, movement);
                newBoard.applyMove(carId, offset);
                possibleBoards.add(newBoard);
                offset--; // coba gerakan lebih jauh
            }

            // coba gerakan ke arah positif (maju)
            offset = 1;
            while (canMove(carId, offset)) {
                String movement = carId + (car.isHorizontal() ? "-kanan" : "-bawah");
                Board newBoard = new Board(rows, cols, getCarsCopy(), goalCarId, goalRow, goalCol, movement);
                newBoard.applyMove(carId, offset);
                possibleBoards.add(newBoard);
                offset++; // coba gerakan lebih jauh
            }
        }

        return possibleBoards.toArray(new Board[0]);
    }

    // Getter standar
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public Map<Character, Car> getCars() { return cars; }
    public char[][] getGrid() { return grid; }
    public char getGoalCarId() { return goalCarId; }
    public int getGoalRow() { return goalRow; }
    public int getGoalCol() { return goalCol; }
    public int getGoalRowClamped() { return Math.max(0, Math.min(goalRow, rows - 1)); }
    public int getGoalColClamped() { return Math.max(0, Math.min(goalCol, cols - 1)); }
    public String getDetail() { return detail; }
}
