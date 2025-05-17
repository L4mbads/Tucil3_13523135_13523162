package com.syafiqriza.rushhoursolver.view;

import com.syafiqriza.rushhoursolver.model.Board;
import com.syafiqriza.rushhoursolver.model.State;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Komponen visual untuk menampilkan papan permainan Rush Hour.
 */
public class BoardView extends GridPane {
    private static final int TILE_SIZE = 60;
    private final Map<Character, Color> colorMap = new HashMap<>();
    private final Random random = new Random();
    private char[][] currentGrid;

    public BoardView() {
        setHgap(2);
        setVgap(2);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #999; -fx-border-width: 2;");
    }

    /**
     * Menggambar ulang papan berdasarkan objek Board.
     * @param board Objek Board yang akan digambarkan.
     */
    public void draw(Board board) {
        this.currentGrid = board.getGrid();
        draw(currentGrid);
    }

    /**
     * Menggambar ulang papan berdasarkan grid.
     * @param grid Matriks karakter keadaan papan.
     */
    public void draw(char[][] grid) {
        getChildren().clear();
        int rows = grid.length;
        int cols = grid[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                char c = grid[row][col];

                tile.setFill(getColorForCar(c));
                tile.setStroke(Color.DARKGRAY);
                tile.setArcWidth(12);
                tile.setArcHeight(12);
                add(tile, col, row);
            }
        }
    }

    /**
     * Menggambar ulang papan berdasarkan State.
     * @param state State yang akan digambarkan.
     */
    public void draw(State state) {
        draw(state.getGrid());
    }

    /**
     * Menghasilkan warna berdasarkan ID mobil.
     * @param c Karakter ID mobil.
     * @return Warna tile.
     */
    private Color getColorForCar(char c) {
        if (c == '.') return Color.LIGHTGRAY;
        if (c == 'P') return Color.RED;
        if (!colorMap.containsKey(c)) {
            colorMap.put(c, Color.hsb(random.nextDouble() * 360, 0.7, 0.9));
        }
        return colorMap.get(c);
    }

    /**
     * Mendapatkan grid terakhir yang ditampilkan.
     * @return Matriks grid terakhir.
     */
    public char[][] getCurrentGrid() {
        return currentGrid;
    }
}
