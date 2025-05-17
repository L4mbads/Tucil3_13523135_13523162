package com.syafiqriza.rushhoursolver.model.heuristic;

import com.syafiqriza.rushhoursolver.model.Board;
import com.syafiqriza.rushhoursolver.model.Car;


public class DistanceHeuristic extends Heuristic{

    public DistanceHeuristic() {}

    @Override
    public int getValue(Board board) {
        if(board.isSolved()) {
            return 0;
        }

        Car goalCar = board.getCars().get(board.getGoalCarId());
        int dirToFinish;
        if(goalCar.isHorizontal()) {
            if(board.getGoalCol() > goalCar.getCol()) {
                dirToFinish = 1;
            } else {
                dirToFinish = -1;
            }
        } else {
            if(board.getGoalRow() > goalCar.getRow()) {
                dirToFinish = 1;
            } else {
                dirToFinish = -1;
            }

        }

        char[][] grid = board.getGrid();

        int distance = 0;
        if(goalCar.isHorizontal()) {
            int carRow = goalCar.getRow();
            for(int j = goalCar.getCol() + dirToFinish; j != board.getGoalCol(); j += dirToFinish) {
                char carId = grid[carRow][j];
                if(carId != 'P') {
                    distance++;
                }
            }
        } else {
            int carCol = goalCar.getCol();
            for(int i = goalCar.getRow() + dirToFinish; i != board.getGoalRow(); i += dirToFinish) {
                char carId = grid[i][carCol];
                if(carId != 'P') {
                    distance++;
                }
            }
        }

        // System.out.println("BLOCKING: " + blockingCarCounter);
        return distance;
    }
}

