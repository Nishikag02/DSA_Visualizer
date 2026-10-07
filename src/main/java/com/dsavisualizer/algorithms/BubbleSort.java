package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

public class BubbleSort {

    public static class Step {

        public int[] array;
        public int firstIndex;
        public int secondIndex;
        public boolean swapped;
        public int sortedFrom;

        public Step(
                int[] array,
                int firstIndex,
                int secondIndex,
                boolean swapped,
                int sortedFrom
        ) {
            this.array = array;
            this.firstIndex = firstIndex;
            this.secondIndex = secondIndex;
            this.swapped = swapped;
            this.sortedFrom = sortedFrom;
        }
    }

    public List<Step> getSteps(int[] array) {

        List<Step> steps = new ArrayList<>();

        int[] copy = array.clone();
        int n = copy.length;

        for (int i = 0; i < n - 1; i++) {

            for (int j = 0; j < n - i - 1; j++) {

                // Show comparison
                steps.add(
                        new Step(
                                copy.clone(),
                                j,
                                j + 1,
                                false,
                                n - i
                        )
                );

                // Swap if elements are in the wrong order
                if (copy[j] > copy[j + 1]) {

                    int temp = copy[j];

                    copy[j] = copy[j + 1];
                    copy[j + 1] = temp;

                    // Show swap
                    steps.add(
                            new Step(
                                    copy.clone(),
                                    j,
                                    j + 1,
                                    true,
                                    n - i
                            )
                    );
                }
            }
        }

        // Final sorted state
        steps.add(
                new Step(
                        copy.clone(),
                        -1,
                        -1,
                        false,
                        0
                )
        );

        return steps;
    }
}