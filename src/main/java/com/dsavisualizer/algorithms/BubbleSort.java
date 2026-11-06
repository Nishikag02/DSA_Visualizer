package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Bubble Sort engine. It does NOT draw anything: it runs the algorithm on a
 * copy of the array and records a SortStep after every interesting moment.
 *
 * Pseudocode lines used (see SortAlgorithm.BUBBLE):
 *   4 = compare arr[j] and arr[j+1]
 *   5 = swap
 *   7 = end of a pass
 *   8 = finished
 */
public class BubbleSort extends SortAlgorithm {

    public BubbleSort() {
        super(
                "Bubble Sort",
                "Compare adjacent elements and move larger values to the end",
                "Bubble Sort repeatedly compares neighboring elements.",
                new String[]{
                        "int[] arr = {5, 2, 8, 1, 6};",
                        "for (int i = 0; i < n - 1; i++) {",
                        "    for (int j = 0; j < n - i - 1; j++) {",
                        "        if (arr[j] > arr[j + 1]) {",
                        "            swap(arr[j], arr[j + 1]);",
                        "        }",
                        "    }",
                        "}"
                }
        );
    }

    @Override
    public List<SortStep> getSteps(int[] array) {

        List<SortStep> steps = new ArrayList<>();

        int[] copy = array.clone();
        int n = copy.length;

        // Elements at index >= sortedFrom are permanently sorted
        int sortedFrom = n;

        for (int i = 0; i < n - 1; i++) {

            boolean swappedInPass = false;

            for (int j = 0; j < n - i - 1; j++) {

                int left = copy[j];
                int right = copy[j + 1];
                boolean wrongOrder = left > right;

                // Comparison step
                steps.add(new SortStep(copy, 4, "Comparing elements",
                        "Comparing " + left + " and " + right + ". "
                                + (wrongOrder
                                ? left + " is larger, so a swap is needed."
                                : "They are already in the correct order."))
                        .withActive(j)
                        .withCompare(j + 1)
                        .withWrongOrder(wrongOrder)
                        .withSorted(sortedFrom, n));

                if (wrongOrder) {

                    copy[j] = right;
                    copy[j + 1] = left;
                    swappedInPass = true;

                    // Swap step (array already holds the swapped values)
                    steps.add(new SortStep(copy, 5, "Swapping elements",
                            "The elements are in the wrong order, "
                                    + "so they smoothly exchange positions.")
                            .withActive(j)
                            .withCompare(j + 1)
                            .withWrongOrder(true)
                            .withSwapping()
                            .withSorted(sortedFrom, n));
                }
            }

            // One more element is now permanently sorted
            sortedFrom = n - i - 1;

            steps.add(new SortStep(copy, 7, "Pass completed",
                    "The largest remaining element has reached its correct position.")
                    .withSorted(sortedFrom, n));

            if (!swappedInPass) {
                break;
            }
        }

        steps.add(new SortStep(copy, 8, "Sorting complete!",
                "Bubble Sort has finished. All elements are now sorted.")
                .withComplete());

        return steps;
    }
}
