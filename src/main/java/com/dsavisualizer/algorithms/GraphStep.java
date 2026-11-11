package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * One "snapshot" of a graph algorithm (the graph version of SortStep).
 *
 * It stores a COPY of the state of every node and every edge, a note to
 * print under each node (for example Dijkstra's distance), and an "extra
 * list" that is drawn under the graph (the queue of BFS, the call stack of
 * DFS, the sorted edges of Kruskal, ...).
 *
 * Optional data is added with the "with..." methods, which return "this".
 */
public class GraphStep {

    // Node states (colour of the circle)
    public static final int UNVISITED = 0;  // not reached yet (grey)
    public static final int FRONTIER = 1;   // found / waiting (blue)
    public static final int CURRENT = 2;    // being processed now (yellow)
    public static final int DONE = 3;       // finished (green)

    // Edge states (colour of the line)
    public static final int EDGE_NORMAL = 0;     // grey
    public static final int EDGE_EXAMINING = 1;  // yellow: being looked at now
    public static final int EDGE_USED = 2;       // green: part of the result
    public static final int EDGE_REJECTED = 3;   // red: not used

    public final int codeLine;
    public final String title;
    public final String message;

    public final int[] nodeState;
    public final int[] edgeState;
    public final String[] nodeNote;

    public String extraTitle = "";
    public List<String> extraItems = new ArrayList<>();
    public int extraHighlight = -1;

    public GraphStep(int[] nodeState, int[] edgeState, String[] nodeNote,
                     int codeLine, String title, String message) {
        this.nodeState = nodeState.clone();
        this.edgeState = edgeState.clone();
        this.nodeNote = nodeNote.clone();
        this.codeLine = codeLine;
        this.title = title;
        this.message = message;
    }

    /** Changes the state of one edge in THIS snapshot only. */
    public GraphStep withEdge(int edgeIndex, int state) {
        this.edgeState[edgeIndex] = state;
        return this;
    }

    /** Changes the state of one node in THIS snapshot only. */
    public GraphStep withNode(int node, int state) {
        this.nodeState[node] = state;
        return this;
    }

    /** The list drawn under the graph, and which item (if any) is highlighted. */
    public GraphStep withExtra(String title, List<String> items, int highlight) {
        this.extraTitle = title;
        this.extraItems = new ArrayList<>(items);
        this.extraHighlight = highlight;
        return this;
    }
}
