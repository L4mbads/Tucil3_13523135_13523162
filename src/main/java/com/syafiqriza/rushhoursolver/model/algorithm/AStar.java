package com.syafiqriza.rushhoursolver.model.algorithm;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import com.syafiqriza.rushhoursolver.model.Board;
import com.syafiqriza.rushhoursolver.model.State;

public class AStar extends InformedSearch {

    public AStar() {}

    @Override
    public void solve(State initialState) {
        Set<State> visited = new HashSet<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.getState().getEstimatedCost() + n.getState().getCumulativeCost()));

        long startTime = System.nanoTime();
        queue.add(new Node(initialState, 0, null));

        while(!queue.isEmpty()) {
            Node currentNode = queue.poll();
            State currentState = currentNode.getState();

            solutionData.nodeCount++;

            // berhenti jika board sudah solved
            if(currentState.getBoard().isSolved()) {
                // resize array solusi
    			this.solutionData.states = new State[currentNode.getDepth() + 1];

    			Node pathNode = currentNode;

                // isi array solusi dari indeks akhir ke awal menuju root node
    			while (pathNode != null) {
    				this.solutionData.states[pathNode.getDepth()] = pathNode.getState();
    				pathNode = pathNode.getParent();
    			}
                break;
            }

            // hanya proses state jika belum visited
            if (!visited.contains(currentState)) {
                visited.add(currentState);

                // enqueue semua possible state
                for(Board board : currentState.getBoard().getAllPossibleMovement()) {
                    State s = new State(board, currentState.getCumulativeCost() + 1, heuristicModel.getValue(board));
                    // s.getBoard().printBoard();
                    queue.add(new Node(s, currentNode.getDepth() + 1, currentNode));
                    //
                }
            }
        }

        long timeElapsedNs = System.nanoTime() - startTime;
        double timeElapsedMs = timeElapsedNs / 1_000_000.0;
        solutionData.timeElapsedMs = timeElapsedMs;
    }
}

