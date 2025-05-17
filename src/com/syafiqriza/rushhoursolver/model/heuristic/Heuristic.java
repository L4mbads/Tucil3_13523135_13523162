package com.syafiqriza.rushhoursolver.model.heuristic;

import com.syafiqriza.rushhoursolver.model.State;

public abstract class Heuristic {
    public abstract int getValue(State state);
}
