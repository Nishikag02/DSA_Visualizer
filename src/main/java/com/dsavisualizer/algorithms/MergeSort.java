package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Merge Sort engine (generates SortStep snapshots).
 *
 * Idea: split the array in two halves, sort each half (the same way, by
 * calling mergeSort again = RECURSION), then MERGE the two sorted halves.
 *
 * Pseudocode lines used (see the constructor):
 *   3  = mid = (left + right) / 2          (splitting)
 *   10 = copy the smaller of arr[i], arr[j] (comparing the halves)
 *   12 = copy what is left of either half
 *   13 = write the merged values back to arr
 *   7  = finished
 *
 * The array on screen only changes when a merge is finished ("Merged!").
 * While merging, the steps show which two values are compared and what the
 * merged list looks like so far in the explanation text.
 * Green cards = the block that was just merged (it is sorted now). Dim cards
 * = not part of the range we are working on. Only the very last step turns
 * the whole array green.
 *
 * The recursion needs to share some data between its methods, so the data
 * is kept in fields (work, steps) of this object.
 */
public class MergeSort extends SortAlgorithm {

    private int[] work;
    private List<SortStep> steps;

    public MergeSort() {
        super(
                "Merge Sort",
                "Split the array in halves, sort each half, then merge them",
                "Merge Sort splits the array again and again, then merges sorted halves.",
                new String[]{
                        "void mergeSort(arr, left, right) {",
                        "    if (left >= right) return;",
                        "    int mid = (left + right) / 2;",
                        "    mergeSort(arr, left, mid);",
                        "    mergeSort(arr, mid + 1, right);",
                        "    merge(arr, left, mid, right);",
                        "}",
                        "// merge: i = left, j = mid + 1",
                        "while (i <= mid && j <= right) {",
                        "    copy the smaller of arr[i], arr[j]",
                        "}",
                        "copy what is left of either half",
                        "write the merged values back to arr"
                }
        );
    }

    @Override
    public List<SortStep> getSteps(int[] array) {

        steps = new ArrayList<>();
        work = array.clone();

        mergeSort(0, work.length - 1);

        steps.add(new SortStep(work, 7, "Sorting complete!",
                "Merge Sort has finished. All elements are now sorted.")
                .withComplete());

        return steps;
    }

    /** Recursive method: sorts work[left..right]. */
    private void mergeSort(int left, int right) {

        // One element (or none) is already sorted: nothing to do
        if (left >= right) {
            return;
        }

        int mid = (left + right) / 2;

        steps.add(newStep(3, "Splitting the range",
                "Split " + describe(left, right) + " into "
                        + describe(left, mid) + " and "
                        + describe(mid + 1, right) + ".")
                .withRange(left, right));

        mergeSort(left, mid);
        mergeSort(mid + 1, right);

        merge(left, mid, right);
    }

    /** Merges the sorted halves work[left..mid] and work[mid+1..right]. */
    private void merge(int left, int mid, int right) {

        int[] merged = new int[right - left + 1];
        int count = 0;

        int i = left;
        int j = mid + 1;

        while (i <= mid && j <= right) {

            boolean takeLeft = work[i] <= work[j];

            int value = takeLeft ? work[i] : work[j];

            String reason;

            if (work[i] == work[j]) {
                reason = "They are equal, so the left one is copied first.";
            } else {
                reason = value + " is smaller, so it is copied first.";
            }

            // Remember which two cards are compared, then move on
            int leftIndex = i;
            int rightIndex = j;

            merged[count] = value;
            count++;

            if (takeLeft) {
                i++;
            } else {
                j++;
            }

            steps.add(newStep(10, "Comparing the halves",
                    "Comparing " + work[leftIndex] + " (left half) with "
                            + work[rightIndex] + " (right half). " + reason
                            + mergedText(merged, count))
                    .withActive(leftIndex)
                    .withCompare(rightIndex)
                    .withRange(left, right));
        }

        // One half is empty: copy the rest of the other half
        if (i <= mid || j <= right) {

            int restStart = (i <= mid) ? i : j;
            int restEnd = (i <= mid) ? mid : right;

            String rest = describe(restStart, restEnd);

            while (i <= mid) {
                merged[count] = work[i];
                count++;
                i++;
            }

            while (j <= right) {
                merged[count] = work[j];
                count++;
                j++;
            }

            steps.add(newStep(12, "Copying the rest",
                    "One half is finished, so the remaining "
                            + rest + " are copied as they are."
                            + mergedText(merged, count))
                    .withRange(left, right));
        }

        // Write the merged values back into the array
        for (int k = 0; k < merged.length; k++) {
            work[left + k] = merged[k];
        }

        steps.add(newStep(13, "Merged!",
                "The two halves are merged into " + describe(left, right)
                        + ". This block is now sorted.")
                .withRange(left, right)
                .withSorted(left, right + 1));
    }

    /** New snapshot of the current array. */
    private SortStep newStep(int codeLine, String title, String message) {
        return new SortStep(work, codeLine, title, message);
    }

    /** The text " Merged so far: [1, 2]" added to the end of a message. */
    private String mergedText(int[] merged, int count) {

        StringBuilder text = new StringBuilder(" Merged so far: [");

        for (int k = 0; k < count; k++) {

            if (k > 0) {
                text.append(", ");
            }

            text.append(merged[k]);
        }

        text.append("]");

        return text.toString();
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
