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
        setHgap(3);
        setVgap(3);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: linear-gradient(to bottom right, #1c1c1c, #2a2a2a);"
                + "-fx-border-color: yellow; -fx-border-width: 3; -fx-border-radius: 10;");
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
                tile.setStroke(Color.web("#cccccc"));
                tile.setStrokeWidth(1.5);
                tile.setArcWidth(16);
                tile.setArcHeight(16);

                add(tile, col, row);
            }
        }
    }

    /**
     * Menggambar ulang papan berdasarkan State.
     * @param state State yang akan digambarkan.
     */
    public void draw(State state) {
        draw(state.getBoard().getGrid());
    }


    private static final Color[] PRESET_COLORS = {
            Color.hsb(150, 0.8, 0.85),  // Spring Green
            Color.hsb(210, 0.9, 0.9),   // Azure
            Color.hsb(330, 0.8, 0.8),   // Pink
            Color.hsb(270, 0.9, 0.85),  // Purple
            Color.hsb(75, 0.8, 0.85),   // Lime
            Color.hsb(285, 0.8, 0.85),  // Orchid
            Color.hsb(180, 0.9, 0.9),   // Cyan
            Color.hsb(255, 0.8, 0.9),   // Violet
            Color.hsb(60, 0.8, 0.9),    // Yellow
            Color.hsb(135, 0.8, 0.8),   // Forest Green
            Color.hsb(300, 0.9, 0.85),  // Magenta
            Color.hsb(195, 0.8, 0.85),  // Sky Blue
            Color.hsb(30, 0.9, 0.9),    // Orange
            Color.hsb(120, 0.9, 0.9),   // Green
            Color.hsb(165, 0.7, 0.85),  // Aqua Green
            Color.hsb(90, 0.7, 0.8),    // Yellow-Green
            Color.hsb(225, 0.8, 0.85),  // Blue
            Color.hsb(45, 0.8, 0.85),   // Yellow-Orange
            Color.hsb(240, 0.9, 0.85),  // Indigo
            Color.hsb(315, 0.7, 0.85)   // Rose
    };


    /**
     * Menghasilkan warna berdasarkan ID mobil.
     * @param c Karakter ID mobil.
     * @return Warna tile.
     */
    private int colorIndex = 0;

    private Color getColorForCar(char c) {
        if (c == '.') return Color.web("#444444");
        if (c == 'P') return Color.web("#ff3b3b");

        if (!colorMap.containsKey(c)) {
            Color color = PRESET_COLORS[colorIndex % PRESET_COLORS.length];
            colorMap.put(c, color);
            colorIndex++;
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
