package com.syafiqriza.rushhoursolver.model.heuristic;

import com.syafiqriza.rushhoursolver.model.Board;

public abstract class Heuristic {
    public abstract int getValue(Board board);
}
