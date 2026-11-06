package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Linear Search engine. It does NOT draw anything: it checks the cards one by
 * one and records a SearchStep for every check.
 *
 * Pseudocode lines used (see SearchAlgorithm.LINEAR):
 *   3 = if (arr[i] == target)
 *   4 = return i      (found)
 *   7 = return -1     (not found)
 */
public class LinearSearch extends SearchAlgorithm {

    public LinearSearch() {
        super(
                "Linear Search",
                "Check the elements one by one until the target is found",
                "Linear Search looks at every element from left to right.",
                new String[]{
                        "// find target in arr",
                        "for (int i = 0; i < n; i++) {",
                        "    if (arr[i] == target) {",
                        "        return i;      // found",
                        "    }",
                        "}",
                        "return -1;             // not found"
                },
                new int[]{5, 2, 8, 1, 6},
                8,
                false,
                "i = index being checked"
        );
    }

    @Override
    public List<SearchStep> getSteps(int[] array, int target) {

        List<SearchStep> steps = new ArrayList<>();

        int n = array.length;

        for (int i = 0; i < n; i++) {

            boolean match = array[i] == target;

            steps.add(new SearchStep(array, 3, "Checking index " + i,
                    "Is " + array[i] + " equal to the target " + target + "? "
                            + (match ? "Yes!" : "No, move to the next element."))
                    .withCurrent(i)
                    .withEliminated(0, i));

            if (match) {

                steps.add(new SearchStep(array, 4, "Target found!",
                        "The target " + target + " is at index " + i + ".")
                        .withCurrent(i)
                        .withFound(i)
                        .withEliminated(0, i));

                return steps;
            }
        }

        steps.add(new SearchStep(array, 7, "Target not found",
                "Every element was checked. " + target
                        + " is not in the array.")
                .withEliminated(0, n)
                .withNotFound());

        return steps;
    }
}
