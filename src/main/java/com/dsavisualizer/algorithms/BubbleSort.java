package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

public class BubbleSort {

    public static class Step {

        public int[] array;
        public int firstIndex;
        public int secondIndex;
        public boolean swapped;
        public int codeLine;

        public Step(
                int[] array,
                int firstIndex,
                int secondIndex,
                boolean swapped,
                int codeLine
        ) {
            this.array = array;
            this.firstIndex = firstIndex;
            this.secondIndex = secondIndex;
            this.swapped = swapped;
            this.codeLine = codeLine;
        }
    }

    public List<Step> getSteps(int[] array) {

        List<Step> steps = new ArrayList<>();

        int[] copy = array.clone();

        int n = copy.length;

        // Start outer loop
        for (int i = 0; i < n - 1; i++) {

            // Start inner loop
            for (int j = 0; j < n - i - 1; j++) {

                // Line 4:
                // Compare adjacent elements
                steps.add(
                        new Step(
                                copy.clone(),
                                j,
                                j + 1,
                                false,
                                4
                        )
                );

                // If elements are in wrong order
                if (copy[j] > copy[j + 1]) {

                    // Swap
                    int temp = copy[j];

                    copy[j] = copy[j + 1];

                    copy[j + 1] = temp;

                    // Line 5:
                    // Show swap
                    steps.add(
                            new Step(
                                    copy.clone(),
                                    j,
                                    j + 1,
                                    true,
                                    5
                            )
                    );
                }
            }
        }

        // Final state
        steps.add(
                new Step(
                        copy.clone(),
                        -1,
                        -1,
                        false,
                        8
                )
        );

        return steps;
    }
}