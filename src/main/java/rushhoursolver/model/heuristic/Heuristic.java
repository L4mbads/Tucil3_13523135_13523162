package main.java.rushhoursolver.model.heuristic;

import main.java.rushhoursolver.model.State;

public abstract class Heuristic {
    public abstract int getValue(State state);
}
