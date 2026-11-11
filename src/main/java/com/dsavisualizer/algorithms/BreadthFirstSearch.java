package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Breadth-First Search: visit the start node, then all its neighbours, then
 * their neighbours, ... level by level.
 *
 * Collections used:
 *   LinkedList<Integer> queue  - first in, first out (nodes waiting to be visited)
 *   HashSet<Integer> discovered - nodes that were already put into the queue
 *   ArrayList<Integer> order    - the order in which nodes were visited
 *
 * Pseudocode lines used (see the constructor):
 *   2 = add the start node to the queue
 *   4 = take a node out of the queue
 *   6 = check a neighbour
 *   7 = add a new neighbour to the queue
 *   11 = finished
 *
 * Colours: blue = in the queue, yellow = being visited, green = finished,
 * green line = the edge that discovered a node.
 */
public class BreadthFirstSearch extends GraphAlgorithm {

    public BreadthFirstSearch() {
        super(
                "BFS",
                "Breadth-First Search: visit the graph level by level using a queue",
                "BFS visits the start node, then all its neighbours, then their neighbours.",
                new String[]{
                        "void bfs(int start) {",
                        "    queue.add(start); discovered.add(start);",
                        "    while (!queue.isEmpty()) {",
                        "        int u = queue.remove();",
                        "        for (int v : neighbours(u)) {",
                        "            if (!discovered.contains(v)) {",
                        "                discovered.add(v); queue.add(v);",
                        "            }",
                        "        }",
                        "    }",
                        "}"
                },
                false,
                true
        );
    }

    @Override
    protected void buildSteps(int start) {

        LinkedList<Integer> queue = new LinkedList<>();
        Set<Integer> discovered = new HashSet<>();
        List<Integer> order = new ArrayList<>();

        queue.add(start);
        discovered.add(start);
        nodeState[start] = GraphStep.FRONTIER;

        steps.add(snap(2, "Starting at " + graph.getName(start),
                "Put the start node " + graph.getName(start)
                        + " into the queue and mark it as discovered.")
                .withExtra("Queue", names(queue), -1));

        while (!queue.isEmpty()) {

            int u = queue.removeFirst();

            nodeState[u] = GraphStep.CURRENT;
            order.add(u);

            steps.add(snap(4, "Taking " + graph.getName(u) + " from the queue",
                    "Remove " + graph.getName(u) + " from the front of the queue "
                            + "and visit it. Now look at its neighbours.")
                    .withExtra("Queue", names(queue), -1));

            for (int edge : graph.getEdgeIndexes(u)) {

                int v = graph.other(edge, u);

                boolean seen = discovered.contains(v);

                steps.add(snap(6, "Checking neighbour " + graph.getName(v),
                        graph.getName(v) + " is a neighbour of " + graph.getName(u) + ". "
                                + (seen
                                ? "It was discovered before, so skip it."
                                : "It is new."))
                        .withEdge(edge, GraphStep.EDGE_EXAMINING)
                        .withExtra("Queue", names(queue), -1));

                if (!seen) {

                    discovered.add(v);
                    queue.add(v);

                    nodeState[v] = GraphStep.FRONTIER;
                    edgeState[edge] = GraphStep.EDGE_USED;

                    steps.add(snap(7, "Adding " + graph.getName(v) + " to the queue",
                            "Mark " + graph.getName(v) + " as discovered and add it "
                                    + "to the back of the queue.")
                            .withExtra("Queue", names(queue), -1));
                }
            }

            nodeState[u] = GraphStep.DONE;
        }

        steps.add(snap(11, "Search complete",
                "The queue is empty. Visit order: " + orderText(order) + ".")
                .withExtra("Queue", names(queue), -1));
    }

    /** "A, B, C" for a list of node numbers. */
    private String orderText(List<Integer> order) {

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < order.size(); i++) {

            if (i > 0) {
                text.append(", ");
            }

            text.append(graph.getName(order.get(i)));
        }

        return text.toString();
    }
}
