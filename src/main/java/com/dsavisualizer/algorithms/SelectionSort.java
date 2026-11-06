package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Selection Sort engine (generates SortStep snapshots).
 *
 * Pseudocode lines used (see SortAlgorithm.SELECTION):
 *   3 = min = i
 *   5 = compare arr[j] with arr[min]
 *   6 = min = j
 *   9 = swap arr[i] and arr[min]
 *   10 = end of pass / finished
 *
 * Colours: active (i) and compare (j) = yellow, key (current minimum) = blue.
 */
public class SelectionSort extends SortAlgorithm {

    public SelectionSort() {
        super(
                "Selection Sort",
                "Find the smallest remaining value and place it at the front",
                "Selection Sort finds the minimum of the unsorted part and swaps it into place.",
                new String[]{
                        "int[] arr = {5, 2, 8, 1, 6};",
                        "for (int i = 0; i < n - 1; i++) {",
                        "    int min = i;",
                        "    for (int j = i + 1; j < n; j++) {",
                        "        if (arr[j] < arr[min]) {",
                        "            min = j;",
                        "        }",
                        "    }",
                        "    swap(arr[i], arr[min]);",
                        "}"
                }
        );
    }

    @Override
    public List<SortStep> getSteps(int[] array) {

        List<SortStep> steps = new ArrayList<>();

        int[] copy = array.clone();
        int n = copy.length;

        for (int i = 0; i < n - 1; i++) {

            int min = i;

            steps.add(new SortStep(copy, 3, "Starting pass",
                    "Assume " + copy[i] + " at index " + i
                            + " is the smallest value so far.")
                    .withActive(i)
                    .withKey(min)
                    .withSorted(0, i));

            for (int j = i + 1; j < n; j++) {

                boolean smaller = copy[j] < copy[min];

                steps.add(new SortStep(copy, 5, "Comparing elements",
                        "Comparing " + copy[j] + " with the current minimum "
                                + copy[min] + ". "
                                + (smaller
                                ? copy[j] + " is smaller, so it becomes the new minimum."
                                : copy[min] + " stays the minimum."))
                        .withActive(i)
                        .withCompare(j)
                        .withKey(min)
                        .withSorted(0, i));

                if (smaller) {
                    min = j;

                    steps.add(new SortStep(copy, 6, "New minimum found",
                            "The minimum is now " + copy[min]
                                    + " at index " + min + ".")
                            .withActive(i)
                            .withKey(min)
                            .withSorted(0, i));
                }
            }

            if (min != i) {

                int smallest = copy[min];

                int temp = copy[i];
                copy[i] = copy[min];
                copy[min] = temp;

                steps.add(new SortStep(copy, 9, "Swapping elements",
                        "Swap the minimum " + smallest
                                + " into position " + i + ".")
                        .withActive(i)
                        .withCompare(min)
                        .withSwapping()
                        .withSorted(0, i));
            } else {
                steps.add(new SortStep(copy, 9, "No swap needed",
                        copy[i] + " is already the smallest remaining value.")
                        .withActive(i)
                        .withSorted(0, i));
            }

            steps.add(new SortStep(copy, 10, "Pass completed",
                    "Position " + i + " now holds its final value, "
                            + copy[i] + ".")
                    .withSorted(0, i + 1));
        }

        steps.add(new SortStep(copy, 10, "Sorting complete!",
                "Selection Sort has finished. All elements are now sorted.")
                .withComplete());

        return steps;
    }
}
