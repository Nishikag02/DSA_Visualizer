package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Depth-First Search: go as deep as possible along one path, and only go
 * back (BACKTRACK) when there is no new node left.
 *
 * It is written with RECURSION: dfs(u) calls dfs(v) for every new neighbour
 * v. The calls that are still running are the "call stack", which is kept in
 * an ArrayList so it can be shown on screen.
 *
 * Pseudocode lines used (see the constructor):
 *   2 = visit u
 *   3 = back at u, continue with the next neighbour
 *   4 = check a neighbour
 *   5 = call dfs(v)  (go deeper)
 *   7 = dfs(u) is finished
 *
 * Colours: blue = waiting on the call stack, yellow = the node we are
 * visiting now, green = finished, green line = the edge we walked along.
 */
public class DepthFirstSearch extends GraphAlgorithm {

    private Set<Integer> visited;
    private List<Integer> callStack;
    private List<Integer> order;

    public DepthFirstSearch() {
        super(
                "DFS",
                "Depth-First Search: go as deep as possible, then backtrack (recursion)",
                "DFS follows one path as far as it can, then backs up and tries the next one.",
                new String[]{
                        "void dfs(int u) {",
                        "    visited.add(u);",
                        "    for (int v : neighbours(u)) {",
                        "        if (!visited.contains(v))",
                        "            dfs(v);",
                        "    }",
                        "}"
                },
                false,
                true
        );
    }

    @Override
    protected void buildSteps(int start) {

        visited = new HashSet<>();
        callStack = new ArrayList<>();
        order = new ArrayList<>();

        dfs(start);

        steps.add(snap(7, "Search complete",
                "Every reachable node is visited. Visit order: " + orderText() + ".")
                .withExtra("Call stack", names(callStack), -1));
    }

    /** Recursive method: visits u and everything reachable from it. */
    private void dfs(int u) {

        visited.add(u);
        callStack.add(u);
        order.add(u);

        nodeState[u] = GraphStep.CURRENT;

        steps.add(snap(2, "Visiting " + graph.getName(u),
                "dfs(" + graph.getName(u) + ") is called. Mark "
                        + graph.getName(u) + " as visited.")
                .withExtra("Call stack", names(callStack), -1));

        for (int edge : graph.getEdgeIndexes(u)) {

            int v = graph.other(edge, u);

            boolean seen = visited.contains(v);

            steps.add(snap(4, "Checking neighbour " + graph.getName(v),
                    graph.getName(v) + " is a neighbour of " + graph.getName(u) + ". "
                            + (seen
                            ? "It is already visited, so skip it."
                            : "It is not visited yet."))
                    .withEdge(edge, GraphStep.EDGE_EXAMINING)
                    .withExtra("Call stack", names(callStack), -1));

            if (!seen) {

                edgeState[edge] = GraphStep.EDGE_USED;

                // u waits on the call stack while we go deeper
                nodeState[u] = GraphStep.FRONTIER;

                steps.add(snap(5, "Going deeper to " + graph.getName(v),
                        "Call dfs(" + graph.getName(v) + "): follow the edge "
                                + graph.getName(u) + "-" + graph.getName(v) + ".")
                        .withExtra("Call stack", names(callStack), -1));

                dfs(v);

                // dfs(v) has returned: we are back at u
                nodeState[u] = GraphStep.CURRENT;

                steps.add(snap(3, "Back at " + graph.getName(u),
                        "dfs(" + graph.getName(v) + ") is finished, so we backtrack to "
                                + graph.getName(u) + " and check its next neighbour.")
                        .withExtra("Call stack", names(callStack), -1));
            }
        }

        nodeState[u] = GraphStep.DONE;
        callStack.remove(callStack.size() - 1);

        steps.add(snap(7, "Finished " + graph.getName(u),
                "All neighbours of " + graph.getName(u) + " are visited, so dfs("
                        + graph.getName(u) + ") returns"
                        + (callStack.isEmpty()
                        ? "."
                        : " to " + graph.getName(callStack.get(callStack.size() - 1)) + "."))
                .withExtra("Call stack", names(callStack), -1));
    }

    private String orderText() {

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
