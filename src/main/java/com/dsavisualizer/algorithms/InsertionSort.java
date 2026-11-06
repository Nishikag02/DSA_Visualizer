package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Insertion Sort engine (generates SortStep snapshots).
 *
 * Pseudocode lines used (see SortAlgorithm.INSERTION):
 *   3 = key = arr[i]
 *   5 = while (j >= 0 && arr[j] > key)   (comparison)
 *   6 = arr[j + 1] = arr[j]              (shift)
 *   9 = arr[j + 1] = key                 (insert)
 *   10 = finished
 *
 * VISUAL TRICK: while the key is "in hand" we show it moving left one cell at
 * a time (the key and the shifted element swap places on screen). This looks
 * the same as shifting, and lets us reuse the same two-card slide animation.
 * keyIndex always tells the view where the key card currently is.
 */
public class InsertionSort {

    public List<SortStep> getSteps(int[] array) {

        List<SortStep> steps = new ArrayList<>();

        int[] copy = array.clone();
        int n = copy.length;

        for (int i = 1; i < n; i++) {

            int key = copy[i];
            int keyPos = i;

            steps.add(new SortStep(copy, 3, "Picking the key",
                    "Take " + key + " at index " + i
                            + " as the key and insert it into the sorted part on its left.")
                    .withActive(i)
                    .withKey(keyPos)
                    .withSorted(0, i));

            int j = i - 1;

            while (j >= 0) {

                boolean greater = copy[j] > key;

                steps.add(new SortStep(copy, 5, "Comparing elements",
                        "Comparing " + copy[j] + " with the key " + key + ". "
                                + (greater
                                ? copy[j] + " is larger, so it shifts one place right."
                                : copy[j] + " is not larger, so the key stops here."))
                        .withCompare(j)
                        .withKey(keyPos)
                        .withWrongOrder(greater)
                        .withSorted(0, i));

                if (!greater) {
                    break;
                }

                // Shift copy[j] right; the key takes its place on screen
                int shifted = copy[j];
                copy[j + 1] = shifted;
                copy[j] = key;

                steps.add(new SortStep(copy, 6, "Shifting element",
                        "Shift " + shifted + " one position to the right to make room for the key.")
                        .withActive(j)
                        .withCompare(j + 1)
                        .withKey(j)
                        .withShifting()
                        .withWrongOrder(true)
                        .withSorted(0, i));

                keyPos = j;
                j--;
            }

            steps.add(new SortStep(copy, 9, "Inserting the key",
                    "Insert the key " + key + " at index " + keyPos + ". "
                            + "The left part is sorted up to index " + i + ".")
                    .withKey(keyPos)
                    .withSorted(0, i + 1));
        }

        steps.add(new SortStep(copy, 10, "Sorting complete!",
                "Insertion Sort has finished. All elements are now sorted.")
                .withComplete());

        return steps;
    }
}
