package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Quick Sort engine (generates SortStep snapshots).
 *
 * Idea: pick a PIVOT (the last element of the range), move every smaller
 * value to its left and every larger value to its right (PARTITION), then
 * the pivot is in its final place. Sort the left part and the right part the
 * same way (RECURSION).
 *
 * Pseudocode lines used (see the constructor):
 *   2  = if (low >= high) return       (one element left)
 *   3  = pivot = arr[high]
 *   6  = if (arr[j] <= pivot)          (compare)
 *   7  = i++; swap(arr[i], arr[j])
 *   10 = swap(arr[i + 1], arr[high])   (pivot goes to its final place)
 *   11 = quickSort(arr, low, i)        (left part)
 *   12 = quickSort(arr, i + 2, high)   (right part)
 *   13 = finished
 *
 * Colours: blue = the pivot, yellow = card compared with the pivot,
 * red = cards being swapped, green = card in its final place,
 * dim = outside the part we are working on.
 */
public class QuickSort extends SortAlgorithm {

    private int[] work;
    private List<SortStep> steps;
    private boolean[] done;

    public QuickSort() {
        super(
                "Quick Sort",
                "Pick a pivot, put smaller values left and larger values right",
                "Quick Sort partitions the array around a pivot, then sorts both sides.",
                new String[]{
                        "void quickSort(arr, low, high) {",
                        "    if (low >= high) return;",
                        "    int pivot = arr[high];",
                        "    int i = low - 1;",
                        "    for (int j = low; j < high; j++) {",
                        "        if (arr[j] <= pivot) {",
                        "            i++; swap(arr[i], arr[j]);",
                        "        }",
                        "    }",
                        "    swap(arr[i + 1], arr[high]);",
                        "    quickSort(arr, low, i);",
                        "    quickSort(arr, i + 2, high);",
                        "}"
                }
        );
    }

    @Override
    public List<SortStep> getSteps(int[] array) {

        steps = new ArrayList<>();
        work = array.clone();
        done = new boolean[work.length];

        quickSort(0, work.length - 1);

        steps.add(new SortStep(work, 13, "Sorting complete!",
                "Quick Sort has finished. All elements are now sorted.")
                .withComplete());

        return steps;
    }

    /** Recursive method: sorts work[low..high]. */
    private void quickSort(int low, int high) {

        // Empty part: nothing to do
        if (low > high) {
            return;
        }

        // One element: it is already in its final place
        if (low == high) {

            steps.add(newStep(2, "Single element",
                    "Only " + work[low] + " is left in this part, "
                            + "so it is already in its final place.")
                    .withRange(low, high));

            done[low] = true;

            return;
        }

        int pivot = work[high];
        int i = low - 1;

        steps.add(newStep(3, "Choosing the pivot",
                "The last element, " + pivot + ", of "
                        + describe(low, high) + " becomes the pivot.")
                .withRange(low, high)
                .withKey(high));

        for (int j = low; j < high; j++) {

            boolean smaller = work[j] <= pivot;

            steps.add(newStep(6, "Comparing with the pivot",
                    "Is " + work[j] + " <= pivot " + pivot + "? "
                            + (smaller
                            ? "Yes, it belongs on the left side."
                            : "No, it stays on the right side."))
                    .withRange(low, high)
                    .withKey(high)
                    .withActive(j));

            if (smaller) {

                i++;

                if (i != j) {

                    int first = work[i];
                    int second = work[j];

                    work[i] = second;
                    work[j] = first;

                    steps.add(newStep(7, "Swapping elements",
                            "Swap " + first + " and " + second
                                    + " so the smaller value " + second
                                    + " moves to the left side.")
                            .withRange(low, high)
                            .withKey(high)
                            .withActive(i)
                            .withCompare(j)
                            .withSwapping());

                } else {

                    steps.add(newStep(7, "Already in place",
                            work[j] + " is already next to the smaller values, "
                                    + "so nothing needs to move.")
                            .withRange(low, high)
                            .withKey(high)
                            .withActive(j));
                }
            }
        }

        // Put the pivot into its final place
        int place = i + 1;

        if (place != high) {

            int other = work[place];

            work[place] = pivot;
            work[high] = other;

            steps.add(newStep(10, "Placing the pivot",
                    "Swap the pivot " + pivot + " with " + other
                            + ". The pivot is now in its final place at index "
                            + place + ".")
                    .withRange(low, high)
                    .withKey(place)
                    .withActive(place)
                    .withCompare(high)
                    .withSwapping());

        } else {

            steps.add(newStep(10, "Pivot already in place",
                    "The pivot " + pivot + " is already in its final place at index "
                            + place + ".")
                    .withRange(low, high)
                    .withKey(place));
        }

        done[place] = true;

        // Sort the left part, then the right part
        if (low <= place - 1) {

            steps.add(newStep(11, "Sorting the left part",
                    "Now sort the part left of the pivot: "
                            + describe(low, place - 1) + ".")
                    .withRange(low, place - 1));

            quickSort(low, place - 1);
        }

        if (place + 1 <= high) {

            steps.add(newStep(12, "Sorting the right part",
                    "Now sort the part right of the pivot: "
                            + describe(place + 1, high) + ".")
                    .withRange(place + 1, high));

            quickSort(place + 1, high);
        }
    }

    /** New snapshot of the current array, with finished cards marked green. */
    private SortStep newStep(int codeLine, String title, String message) {
        return new SortStep(work, codeLine, title, message)
                .withSortedFlags(done);
    }

    /** "[5, 2, 8]" for work[from..to]. */
    private String describe(int from, int to) {

        StringBuilder text = new StringBuilder("[");

        for (int k = from; k <= to; k++) {

            if (k > from) {
                text.append(", ");
            }

            text.append(work[k]);
        }

        text.append("]");

        return text.toString();
    }
}
