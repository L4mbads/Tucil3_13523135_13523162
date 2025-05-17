package main.java.com.syafiqriza.rushhoursolver.model.algorithm;

import main.java.com.syafiqriza.rushhoursolver.model.State;
import main.java.com.syafiqriza.rushhoursolver.model.Board;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class UniformCostSearch extends Algorithm {

    private Map<State, Integer> costSoFar = new HashMap<>();
    Set<State> visited = new HashSet<>();

    public UniformCostSearch() {}

    @Override
    public void solve(State initialState) {
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.getTotalEstimatedCost()));

        System.out.println("solving ucs");

        queue.add(initialState);
        while(!queue.isEmpty()) {
            State currentState = queue.poll();
            if(currentState.getBoard().isSolved()) {
                System.out.println("DONE");
                currentState.getBoard().printBoard();
                break;
            }
            if (!visited.contains(currentState)) {
                visited.add(currentState);
                // currentState.printState();
                System.out.println();
                currentState.getBoard().printBoard();

                for(Board board : currentState.getBoard().getAllPossibleMovement()) {
                    State s = new State(board, currentState.getCumulativeCost() + 1, currentState.getCumulativeCost() + 1);
                    queue.add(s);
                }
            }
        }
    }
}
