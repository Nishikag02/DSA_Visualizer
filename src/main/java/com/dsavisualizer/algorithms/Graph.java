package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A small undirected weighted graph.
 *
 * Nodes are numbered 0, 1, 2, ... and have a name (A, B, C, ...) and a
 * position on the screen. The edges are kept in an ArrayList<Edge>; an edge
 * is identified by its index in that list.
 *
 * The graph only STORES data. The algorithms (BFS, DFS, ...) read it, and
 * GraphView draws it.
 */
public class Graph {

    private final String[] names;
    private final int[] x;
    private final int[] y;
    private final List<Edge> edges = new ArrayList<>();

    public Graph(String[] names, int[] x, int[] y) {
        this.names = names;
        this.x = x;
        this.y = y;
    }

    /** The example graph used by every graph screen (7 nodes, 10 edges). */
    public static Graph createSample() {

        Graph graph = new Graph(
                new String[]{"A", "B", "C", "D", "E", "F", "G"},
                new int[]{50, 150, 150, 270, 270, 270, 390},
                new int[]{125, 40, 210, 125, 40, 210, 125}
        );

        graph.addEdge(0, 1, 4);   // A-B
        graph.addEdge(0, 2, 2);   // A-C
        graph.addEdge(1, 2, 1);   // B-C
        graph.addEdge(1, 4, 5);   // B-E
        graph.addEdge(2, 3, 8);   // C-D
        graph.addEdge(2, 5, 10);  // C-F
        graph.addEdge(3, 4, 2);   // D-E
        graph.addEdge(3, 5, 3);   // D-F
        graph.addEdge(4, 6, 6);   // E-G
        graph.addEdge(5, 6, 4);   // F-G

        return graph;
    }

    public void addEdge(int from, int to, int weight) {
        edges.add(new Edge(from, to, weight));
    }

    public int getNodeCount() {
        return names.length;
    }

    public String getName(int node) {
        return names[node];
    }

    public int getX(int node) {
        return x[node];
    }

    public int getY(int node) {
        return y[node];
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public Edge getEdge(int edgeIndex) {
        return edges.get(edgeIndex);
    }

    /** "B-C" for edge number edgeIndex. */
    public String getEdgeName(int edgeIndex) {

        Edge edge = edges.get(edgeIndex);

        return names[edge.from] + "-" + names[edge.to];
    }

    /** The node at the other end of an edge, seen from node "node". */
    public int other(int edgeIndex, int node) {

        Edge edge = edges.get(edgeIndex);

        return (edge.from == node) ? edge.to : edge.from;
    }

    /**
     * Indexes of all edges that touch "node", ordered by the number of the
     * neighbour (so the neighbours are always visited A, B, C, ... order).
     */
    public List<Integer> getEdgeIndexes(int node) {

        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < edges.size(); i++) {

            Edge edge = edges.get(i);

            if (edge.from == node || edge.to == node) {
                result.add(i);
            }
        }

        // A COMPARATOR decides the order: compare the two neighbours' numbers
        final int current = node;

        Collections.sort(result, new Comparator<Integer>() {
            @Override
            public int compare(Integer first, Integer second) {
                return other(first, current) - other(second, current);
            }
        });

        return result;
    }
}
