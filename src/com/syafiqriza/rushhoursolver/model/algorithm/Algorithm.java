package com.syafiqriza.rushhoursolver.model.algorithm;

import com.syafiqriza.rushhoursolver.model.State;

public abstract class Algorithm {
    private State[] solution;

    public abstract void solve(State initialState);

    public State[] getSolution() {
        return this.solution;
    }
}
