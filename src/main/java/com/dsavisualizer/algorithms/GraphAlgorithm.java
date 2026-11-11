package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * ABSTRACT CLASS for every graph algorithm (BFS, DFS, Dijkstra, Kruskal).
 * It extends Algorithm, so it already has a name, subtitle, intro and code.
 *
 * It uses the TEMPLATE idea: generateSteps(...) is written ONCE here. It
 * prepares empty state arrays and then calls the abstract method
 * buildSteps(start), which each child class implements with its own algorithm.
 *
 * The state arrays (nodeState, edgeState, nodeNote) are PROTECTED: child
 * classes change them while the algorithm runs, and snap(...) takes a copy of
 * them as a GraphStep after every interesting moment.
 */
public abstract class GraphAlgorithm extends Algorithm {

    private final boolean weighted;
    private final boolean needsStartNode;

    protected Graph graph;
    protected int[] nodeState;
    protected int[] edgeState;
    protected String[] nodeNote;
    protected List<GraphStep> steps;

    protected GraphAlgorithm(String displayName, String subtitle,
                             String intro, String[] code,
                             boolean weighted, boolean needsStartNode) {
        super(displayName, subtitle, intro, code);
        this.weighted = weighted;
        this.needsStartNode = needsStartNode;
    }

    /** True if the edge weights matter (they are only drawn then). */
    public boolean isWeighted() {
        return weighted;
    }

    /** True if the user can choose the start node (BFS, DFS, Dijkstra). */
    public boolean needsStartNode() {
        return needsStartNode;
    }

    /** Runs the algorithm on the graph and returns one GraphStep per moment. */
    public List<GraphStep> generateSteps(Graph graph, int start) {

        this.graph = graph;
        this.nodeState = new int[graph.getNodeCount()];
        this.edgeState = new int[graph.getEdgeCount()];
        this.nodeNote = new String[graph.getNodeCount()];
        this.steps = new ArrayList<>();

        for (int i = 0; i < nodeNote.length; i++) {
            nodeNote[i] = "";
        }

        buildSteps(start);

        return steps;
    }

    /** Each algorithm fills "steps" here. */
    protected abstract void buildSteps(int start);

    /** A snapshot of the current node / edge states. */
    protected GraphStep snap(int codeLine, String title, String message) {
        return new GraphStep(nodeState, edgeState, nodeNote, codeLine, title, message);
    }

    /** [0, 2] -> ["A", "C"] (names of nodes, for the extra list). */
    protected List<String> names(List<Integer> nodes) {

        List<String> result = new ArrayList<>();

        for (int node : nodes) {
            result.add(graph.getName(node));
        }

        return result;
    }
}
