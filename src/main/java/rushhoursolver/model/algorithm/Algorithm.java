package main.java.rushhoursolver.model.algorithm;

import main.java.rushhoursolver.model.State;

public abstract class Algorithm {
    private State[] solution;

    public abstract void solve(State initialState);

    public State[] getSolution() {
        return this.solution;
    }
}
