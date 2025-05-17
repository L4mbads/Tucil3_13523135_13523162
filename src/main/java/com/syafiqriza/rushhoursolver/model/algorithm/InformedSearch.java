package com.syafiqriza.rushhoursolver.model.algorithm;

import com.syafiqriza.rushhoursolver.model.heuristic.Heuristic;

public abstract class InformedSearch extends Algorithm {
    protected Heuristic heuristicModel = null;

    public void setHeuristicModel(Heuristic heuristicModel) {
        this.heuristicModel = heuristicModel;
    }
}
