package com.syafiqriza.rushhoursolver.model.algorithm;

import com.syafiqriza.rushhoursolver.model.State;
import com.syafiqriza.rushhoursolver.model.Board;

public class UniformCostSearch extends Algorithm {

    public UniformCostSearch() {}

    @Override
    public void solve(State initialState) {
        System.out.println("solving ucs");
        int i = 0;
        for(Board board : initialState.getBoard().getAllPossibleMovement()) {
            i++;
            System.out.println(i);
            board.printBoard();
        }
    }
}
