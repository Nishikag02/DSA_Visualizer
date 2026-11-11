package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Kruskal's algorithm: build a MINIMUM SPANNING TREE (the cheapest set of
 * edges that connects all nodes without a cycle).
 *
 * Idea: sort the edges by weight (a COMPARATOR does this). Go through them
 * from the cheapest. Take an edge if it joins two DIFFERENT groups of nodes;
 * skip it if both ends are already in the same group (it would make a cycle).
 *
 * The groups are tracked with a "parent" array: find(x) follows the parents
 * up to the leader of x's group; two nodes are in the same group if they
 * have the same leader.
 *
 * Pseudocode lines used (see the constructor):
 *   2 = sort the edges
 *   4 = check an edge: are the two ends in different groups?
 *   5 = add the edge to the tree
 *   7 = skip the edge (cycle)
 *   10 = finished
 *
 * Colours: yellow line = edge being checked, green line = edge in the tree,
 * red line = edge skipped because of a cycle, green node = already in the tree.
 */
public class KruskalAlgorithm extends GraphAlgorithm {

    public KruskalAlgorithm() {
        super(
                "Kruskal's MST",
                "Build the cheapest tree that connects every node",
                "Kruskal takes the cheapest edges first and skips any edge that would make a cycle.",
                new String[]{
                        "void kruskal() {",
                        "    sort edges by weight;",
                        "    for (Edge e : sortedEdges) {",
                        "        if (find(e.u) != find(e.v)) {",
                        "            add e to the tree; union(e.u, e.v);",
                        "        } else {",
                        "            skip e;   // it would make a cycle",
                        "        }",
                        "    }",
                        "}"
                },
                true,
                false
        );
    }

    @Override
    protected void buildSteps(int start) {

        int n = graph.getNodeCount();

        // 1. Sort the edge numbers by weight (smallest first)
        List<Integer> sorted = new ArrayList<>();

        for (int i = 0; i < graph.getEdgeCount(); i++) {
            sorted.add(i);
        }

        Collections.sort(sorted, new Comparator<Integer>() {
            @Override
            public int compare(Integer first, Integer second) {
                return graph.getEdge(first).weight - graph.getEdge(second).weight;
            }
        });

        List<String> sortedText = new ArrayList<>();

        for (int edge : sorted) {
            sortedText.add(graph.getEdgeName(edge) + " " + graph.getEdge(edge).weight);
        }

        steps.add(snap(2, "Sorting the edges",
                "Sort all edges by weight, smallest first. Every node starts "
                        + "in its own group.")
                .withExtra("Edges by weight", sortedText, -1));

        // 2. Every node is its own group: parent[i] = i
        int[] parent = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        int accepted = 0;
        int total = 0;

        for (int k = 0; k < sorted.size(); k++) {

            int edgeIndex = sorted.get(k);
            Edge edge = graph.getEdge(edgeIndex);

            String name = graph.getEdgeName(edgeIndex);

            int leaderFrom = find(parent, edge.from);
            int leaderTo = find(parent, edge.to);

            boolean different = leaderFrom != leaderTo;

            steps.add(snap(4, "Checking the edge " + name,
                    "The next cheapest edge is " + name + " (weight " + edge.weight
                            + "). " + graph.getName(edge.from) + " and "
                            + graph.getName(edge.to) + " are "
                            + (different
                            ? "in different groups, so the edge is safe to use."
                            : "already in the same group, so the edge would make a cycle."))
                    .withEdge(edgeIndex, GraphStep.EDGE_EXAMINING)
                    .withExtra("Edges by weight", sortedText, k));

            if (different) {

                // union: join the two groups
                parent[leaderFrom] = leaderTo;

                edgeState[edgeIndex] = GraphStep.EDGE_USED;
                nodeState[edge.from] = GraphStep.DONE;
                nodeState[edge.to] = GraphStep.DONE;

                accepted++;
                total += edge.weight;

                steps.add(snap(5, "Adding the edge " + name,
                        "The edge " + name + " joins the two groups. It becomes part of "
                                + "the tree. Total weight so far: " + total + "."
                                + " (" + accepted + " of " + (n - 1) + " edges)")
                        .withExtra("Edges by weight", sortedText, k));

                if (accepted == n - 1) {
                    break;
                }

            } else {

                steps.add(snap(7, "Skipping the edge " + name,
                        "Both ends are already connected through other edges, so "
                                + name + " is skipped.")
                        .withEdge(edgeIndex, GraphStep.EDGE_REJECTED)
                        .withExtra("Edges by weight", sortedText, k));
            }
        }

        steps.add(snap(10, "Minimum spanning tree complete",
                "The tree has " + accepted + " edges and connects all " + n
                        + " nodes. Its total weight is " + total + ".")
                .withExtra("Edges by weight", sortedText, -1));
    }

    /** Follows the parents up to the leader of the group that contains x. */
    private int find(int[] parent, int x) {

        while (parent[x] != x) {
            x = parent[x];
        }

        return x;
    }
}
