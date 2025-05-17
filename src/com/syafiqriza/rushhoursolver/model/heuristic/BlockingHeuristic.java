package com.syafiqriza.rushhoursolver.model.heuristic;

import com.syafiqriza.rushhoursolver.model.State;

public class BlockingHeuristic extends Heuristic{

    public BlockingHeuristic() {}

    @Override
    public int getValue(State state) {
        if(state.getBoard().isSolved()) {
            return 0;
        }
            return 1;
    }
}
