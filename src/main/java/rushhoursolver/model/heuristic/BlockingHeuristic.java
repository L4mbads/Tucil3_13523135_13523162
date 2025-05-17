package main.java.rushhoursolver.model.heuristic;

import main.java.rushhoursolver.model.State;

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
