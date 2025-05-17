package main.java.rushhoursolver.model.algorithm;

import main.java.rushhoursolver.model.State;
import main.java.rushhoursolver.model.Board;

import java.util.HashMap;
import java.util.Map;

public class UniformCostSearch extends Algorithm {

    private Map<State, Integer> costSoFar = new HashMap<>();

    public UniformCostSearch() {}

    @Override
    public void solve(State initialState) {
        System.out.println("solving ucs");
        initialState.printState();
        int i = 0;
        for(Board board : initialState.getBoard().getAllPossibleMovement()) {
            i++;
            System.out.println(i);
            board.printBoard();
            State s = new State(board, 1);
            s.printState();
        }
    }
}
