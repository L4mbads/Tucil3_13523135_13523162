package com.syafiqriza.rushhoursolver.model;

import java.util.Map;

public class State {
    private final Board board;
    private final int cost;

    public State(Board board, int cost) {
        this.board = board;
        this.cost = cost;
    }

    public Board getBoard() {
        return board;
    }

    public int getCost() {
        return cost;
    }

    public State copy() {
        return new State(
                new Board(
                        board.getRows(),
                        board.getCols(),
                        board.getCarsCopy(),
                        board.getGoalCarId(),
                        board.getGoalRow(),
                        board.getGoalCol()
                ),
                cost
        );
    }
}

