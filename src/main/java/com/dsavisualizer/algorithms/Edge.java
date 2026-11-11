package com.dsavisualizer.algorithms;

/**
 * One undirected edge of a Graph: it joins node "from" and node "to"
 * and has a weight (used by Dijkstra and Kruskal, ignored by BFS and DFS).
 *
 * The fields are public and final: they are set once in the constructor
 * and never change.
 */
public class Edge {

    public final int from;
    public final int to;
    public final int weight;

    public Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}
