package com.syafiqriza.rushhoursolver.model.algorithm;

import com.syafiqriza.rushhoursolver.model.State;

public abstract class Algorithm {

    public class SolutionData {
        public int nodeCount;
        public State[] states = null;
        public double timeElapsedMs;
    }

    SolutionData solutionData = new SolutionData();

    public abstract void solve(State initialState);

    public SolutionData getSolution() {
        return this.solutionData;
    }
}
