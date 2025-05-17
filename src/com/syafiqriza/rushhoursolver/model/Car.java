package com.syafiqriza.rushhoursolver.model;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private final String id;
    private final int length;
    private final boolean isHorizontal;
    private int row, col;

    public Car(String id, int length, boolean isHorizontal, int row, int col) {
        this.id = id;
        this.length = length;
        this.isHorizontal = isHorizontal;
        this.row = row;
        this.col = col;
    }

    public String getId() { return id; }
    public int getLength() { return length; }
    public boolean isHorizontal() { return isHorizontal; }
    public int getRow() { return row; }
    public int getCol() { return col; }

    public void move(int offset) {
        if (isHorizontal) col += offset;
        else row += offset;
    }

    public void undoMove(int offset) {
        move(-offset);
    }

    public List<int[]> getOccupiedCells() {
        List<int[]> cells = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            cells.add(isHorizontal ? new int[]{row, col + i} : new int[]{row + i, col});
        }
        return cells;
    }

    public Car copy() {
        return new Car(id, length, isHorizontal, row, col);
    }
}
