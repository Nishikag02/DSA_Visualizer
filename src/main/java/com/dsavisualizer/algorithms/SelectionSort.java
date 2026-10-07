package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

public class SelectionSort {

    public static class Step {

        public int[] array;
        public int currentIndex;
        public int minimumIndex;
        public boolean swapped;
        public int sortedFrom;

        public Step(
                int[] array,
                int currentIndex,
                int minimumIndex,
                boolean swapped,
                int sortedFrom
        ) {
            this.array = array;
            this.currentIndex = currentIndex;
            this.minimumIndex = minimumIndex;
            this.swapped = swapped;
            this.sortedFrom = sortedFrom;
        }
    }

    public List<Step> getSteps(int[] array) {

        List<Step> steps = new ArrayList<>();

        int[] copy = array.clone();
        int n = copy.length;

        for (int i = 0; i < n - 1; i++) {

            int minimumIndex = i;

            for (int j = i + 1; j < n; j++) {

                // Show comparison
                steps.add(
                        new Step(
                                copy.clone(),
                                i,
                                j,
                                false,
                                i
                        )
                );

                if (copy[j] < copy[minimumIndex]) {
                    minimumIndex = j;
                }
            }

            // Swap minimum element into correct position
            if (minimumIndex != i) {

                int temp = copy[i];
                copy[i] = copy[minimumIndex];
                copy[minimumIndex] = temp;

                steps.add(
                        new Step(
                                copy.clone(),
                                i,
                                minimumIndex,
                                true,
                                i
                        )
                );
            }
        }

        // Final sorted state
        steps.add(
                new Step(
                        copy.clone(),
                        -1,
                        -1,
                        false,
                        n
                )
        );

        return steps;
    }
}