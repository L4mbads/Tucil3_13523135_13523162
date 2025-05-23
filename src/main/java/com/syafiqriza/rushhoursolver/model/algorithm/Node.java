package com.syafiqriza.rushhoursolver.model.algorithm;

import com.syafiqriza.rushhoursolver.model.*;

public class Node {

	private final State state;
	private final int depth;
	private final Node parent;

	public Node(State state, int depth, Node parent) {
		this.state = state;
		this.depth = depth;
		this.parent = parent;
	}

	public State getState() { return state; }

	public Node getParent() { return parent; }

	public int getDepth() { return depth; }
}
