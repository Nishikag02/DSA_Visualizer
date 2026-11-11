package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Dijkstra's algorithm: the shortest distance from the start node to every
 * other node (all weights are positive).
 *
 * Idea: keep a distance for every node (infinity at the start, 0 for the
 * start node). Repeatedly take the unvisited node with the SMALLEST distance
 * - its distance is now final - and check if going through it gives its
 * neighbours a shorter distance (this check is called "relaxing" an edge).
 *
 * A COMPARATOR sorts the waiting nodes by distance so the smallest is first.
 *
 * Pseudocode lines used (see the constructor):
 *   2 = all distances = infinity, dist[start] = 0
 *   4 = take the unvisited node with the smallest distance
 *   7 = compare dist[u] + w with dist[v]
 *   8 = update dist[v]
 *   11 = finished
 *
 * Colours: blue = has a distance but is not final, yellow = being processed,
 * green = distance is final, green line = the best known way to that node.
 * The number under a node is its current distance.
 */
public class DijkstraAlgorithm extends GraphAlgorithm {

    // Distance of a node that has not been reached yet
    private static final int INFINITY = Integer.MAX_VALUE;

    public DijkstraAlgorithm() {
        super(
                "Dijkstra's Algorithm",
                "Find the shortest distance from the start to every node",
                "Dijkstra always takes the closest unvisited node and improves its neighbours' distances.",
                new String[]{
                        "void dijkstra(int start) {",
                        "    dist[] = INFINITY; dist[start] = 0;",
                        "    while (unvisited nodes remain) {",
                        "        u = unvisited node, smallest dist;",
                        "        mark u as visited;",
                        "        for (each edge (u, v, w)) {",
                        "            if (dist[u] + w < dist[v])",
                        "                dist[v] = dist[u] + w;",
                        "        }",
                        "    }",
                        "}"
                },
                true,
                true
        );
    }

    @Override
    protected void buildSteps(int start) {

        int n = graph.getNodeCount();

        int[] dist = new int[n];
        boolean[] finished = new boolean[n];
        int[] parentEdge = new int[n];

        for (int i = 0; i < n; i++) {
            dist[i] = INFINITY;
            parentEdge[i] = -1;
            nodeNote[i] = "∞";
        }

        dist[start] = 0;
        nodeNote[start] = "0";
        nodeState[start] = GraphStep.FRONTIER;

        List<Integer> waiting = new ArrayList<>();
        waiting.add(start);

        steps.add(snap(2, "Setting the distances",
                "Every distance starts at infinity (∞), except the start node "
                        + graph.getName(start) + " which is 0.")
                .withExtra("Waiting nodes (distance)", waitingText(waiting, dist), -1));

        while (!waiting.isEmpty()) {

            // The COMPARATOR puts the node with the smallest distance first
            Collections.sort(waiting, new Comparator<Integer>() {
                @Override
                public int compare(Integer first, Integer second) {
                    return dist[first] - dist[second];
                }
            });

            int u = waiting.remove(0);

            finished[u] = true;
            nodeState[u] = GraphStep.CURRENT;

            steps.add(snap(4, "Taking the closest node: " + graph.getName(u),
                    graph.getName(u) + " has the smallest distance (" + dist[u]
                            + ") of all unvisited nodes, so " + dist[u]
                            + " is its final shortest distance.")
                    .withExtra("Waiting nodes (distance)", waitingText(waiting, dist), -1));

            for (int edge : graph.getEdgeIndexes(u)) {

                int v = graph.other(edge, u);

                // Nodes that are already final cannot get shorter
                if (finished[v]) {
                    continue;
                }

                int weight = graph.getEdge(edge).weight;
                int newDistance = dist[u] + weight;

                boolean better = newDistance < dist[v];

                String oldText = (dist[v] == INFINITY) ? "∞" : String.valueOf(dist[v]);

                steps.add(snap(7, "Checking the edge " + graph.getEdgeName(edge),
                        "dist[" + graph.getName(u) + "] + " + weight + " = " + newDistance
                                + ", and the current dist[" + graph.getName(v) + "] is "
                                + oldText + ". "
                                + (better
                                ? newDistance + " is smaller, so we update it."
                                : "No improvement."))
                        .withEdge(edge, GraphStep.EDGE_EXAMINING)
                        .withExtra("Waiting nodes (distance)", waitingText(waiting, dist), -1));

                if (better) {

                    dist[v] = newDistance;
                    parentEdge[v] = edge;

                    nodeNote[v] = String.valueOf(newDistance);
                    nodeState[v] = GraphStep.FRONTIER;

                    if (!waiting.contains(v)) {
                        waiting.add(v);
                    }

                    refreshTreeEdges(parentEdge);

                    steps.add(snap(8, "Updating " + graph.getName(v),
                            "dist[" + graph.getName(v) + "] = " + newDistance
                                    + ". The best way to " + graph.getName(v)
                                    + " is now through " + graph.getName(u) + ".")
                            .withExtra("Waiting nodes (distance)", waitingText(waiting, dist), -1));
                }
            }

            nodeState[u] = GraphStep.DONE;
        }

        steps.add(snap(11, "All shortest distances found",
                "Every node is final. The green lines show the shortest way "
                        + "from " + graph.getName(start) + " to each node.")
                .withExtra("Waiting nodes (distance)", waitingText(waiting, dist), -1));
    }

    /** Green lines = the best known edge into every node. */
    private void refreshTreeEdges(int[] parentEdge) {

        for (int i = 0; i < edgeState.length; i++) {
            edgeState[i] = GraphStep.EDGE_NORMAL;
        }

        for (int edge : parentEdge) {

            if (edge >= 0) {
                edgeState[edge] = GraphStep.EDGE_USED;
            }
        }
    }

    /** ["C:2", "B:4"] for the nodes that are waiting. */
    private List<String> waitingText(List<Integer> waiting, int[] dist) {

        List<String> result = new ArrayList<>();

        for (int node : waiting) {
            result.add(graph.getName(node) + ":" + dist[node]);
        }

        return result;
    }
}
