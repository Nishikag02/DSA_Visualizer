package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Binary Search engine (the array MUST be sorted).
 *
 * Every round of the loop records three steps:
 *   1. "Finding the middle"          (line 4)
 *   2. "Comparing with the target"   (line 5, or line 6 if found)
 *   3. "Narrowing the range"         (line 8 or 10) - shows the new range
 *
 * Pseudocode lines used (see SearchAlgorithm.BINARY):
 *   2 = low = 0, high = n - 1
 *   4 = mid = (low + high) / 2
 *   5 = if (arr[mid] == target)
 *   6 = return mid               (found)
 *   8 = low = mid + 1            (target is on the right)
 *   10 = high = mid - 1          (target is on the left)
 *   12 = return -1               (not found)
 */
public class BinarySearch extends SearchAlgorithm {

    public BinarySearch() {
        super(
                "Binary Search",
                "Halve the search range each time (the array must be sorted)",
                "Binary Search checks the middle element and throws away half of the range.",
                new String[]{
                        "// arr must be sorted",
                        "int low = 0, high = n - 1;",
                        "while (low <= high) {",
                        "    int mid = (low + high) / 2;",
                        "    if (arr[mid] == target)",
                        "        return mid;",
                        "    if (arr[mid] < target)",
                        "        low = mid + 1;",
                        "    else",
                        "        high = mid - 1;",
                        "}",
                        "return -1;"
                },
                new int[]{2, 5, 8, 12, 16, 23, 38},
                23,
                true,
                "L = low    M = mid    H = high"
        );
    }

    @Override
    public List<SearchStep> getSteps(int[] array, int target) {

        List<SearchStep> steps = new ArrayList<>();

        int n = array.length;

        int low = 0;
        int high = n - 1;

        steps.add(new SearchStep(array, 2, "Setting the range",
                "Start with the whole array: low = " + low
                        + " and high = " + high + ".")
                .withRange(low, high));

        while (low <= high) {

            int mid = (low + high) / 2;

            steps.add(new SearchStep(array, 4, "Finding the middle",
                    "mid = (" + low + " + " + high + ") / 2 = " + mid
                            + ". We look at the value " + array[mid] + ".")
                    .withRange(low, high)
                    .withMid(mid)
                    .withEliminatedOutside(low, high));

            if (array[mid] == target) {

                steps.add(new SearchStep(array, 6, "Target found!",
                        array[mid] + " equals the target " + target
                                + ". It is at index " + mid + ".")
                        .withRange(low, high)
                        .withMid(mid)
                        .withFound(mid)
                        .withEliminatedOutside(low, high));

                return steps;
            }

            boolean goRight = array[mid] < target;

            steps.add(new SearchStep(array, 5, "Comparing with the target",
                    array[mid] + " is "
                            + (goRight ? "smaller" : "larger")
                            + " than the target " + target + ", so the target can only be on the "
                            + (goRight ? "right" : "left") + " side.")
                    .withRange(low, high)
                    .withMid(mid)
                    .withEliminatedOutside(low, high));

            if (goRight) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }

            steps.add(new SearchStep(array, goRight ? 8 : 10, "Narrowing the range",
                    (goRight
                            ? "Everything up to index " + mid + " is ruled out, so low = " + low + "."
                            : "Everything from index " + mid + " onwards is ruled out, so high = " + high + ".")
                            + (low <= high
                            ? " The search continues in the smaller range."
                            : " The range is now empty."))
                    .withRange(low, high)
                    .withEliminatedOutside(low, high));
        }

        steps.add(new SearchStep(array, 12, "Target not found",
                "low is greater than high, so the range is empty. "
                        + target + " is not in the array.")
                .withEliminated(0, n)
                .withNotFound());

        return steps;
    }
}
