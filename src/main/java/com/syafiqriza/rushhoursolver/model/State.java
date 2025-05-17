package com.syafiqriza.rushhoursolver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class State {
    private final Board board;
    private final int cumCost;
    private final int totalEstimatedCost;
    private final List<String> carStates;

    public State(Board board, int cumCost, int totalEstimatedCost) {
        this.board = board;
        this.cumCost = cumCost;
        this.totalEstimatedCost = totalEstimatedCost;

        carStates = new ArrayList<>();
        for (char carID : board.getCars().keySet()) {
            Car car = board.getCars().get(carID);
            carStates.add(carID + "-" + car.getRow() + "-" + car.getCol() + "-" + (car.isHorizontal() ? 1 : 0));
        }
        Collections.sort(carStates); // normalize order
    }

    public Board getBoard() {
        return board;
    }

    public int getCumulativeCost() {
        return cumCost;
    }

    public int getTotalEstimatedCost() {
        return totalEstimatedCost;
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
                cumCost,
                totalEstimatedCost
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State other)) return false;
        return carStates.equals(other.carStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carStates);
    }

    public void printState() {
        for (String string : carStates) {
            System.out.println(string);
        }
    }
}

