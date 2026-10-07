package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

public class BubbleSort {

    public static class Step {

        public int[] array;
        public int firstIndex;
        public int secondIndex;
        public boolean swapped;
        public boolean wrongOrder;
        public int codeLine;
        public int sortedFrom;

        public Step(
                int[] array,
                int firstIndex,
                int secondIndex,
                boolean swapped,
                boolean wrongOrder,
                int codeLine,
                int sortedFrom
        ) {
            this.array = array;
            this.firstIndex = firstIndex;
            this.secondIndex = secondIndex;
            this.swapped = swapped;
            this.wrongOrder = wrongOrder;
            this.codeLine = codeLine;
            this.sortedFrom = sortedFrom;
        }
    }

    public List<Step> getSteps(int[] array) {

        List<Step> steps =
                new ArrayList<>();

        int[] copy =
                array.clone();

        int n =
                copy.length;

        int sortedFrom = n;

        for (int i = 0; i < n - 1; i++) {

            boolean swappedInPass = false;

            for (
                    int j = 0;
                    j < n - i - 1;
                    j++
            ) {

                boolean wrongOrder =
                        copy[j] > copy[j + 1];

                // Comparison step
                steps.add(
                        new Step(
                                copy.clone(),
                                j,
                                j + 1,
                                false,
                                wrongOrder,
                                4,
                                sortedFrom
                        )
                );

                if (wrongOrder) {

                    int temp =
                            copy[j];

                    copy[j] =
                            copy[j + 1];

                    copy[j + 1] =
                            temp;

                    swappedInPass = true;

                    // Swap step
                    steps.add(
                            new Step(
                                    copy.clone(),
                                    j,
                                    j + 1,
                                    true,
                                    true,
                                    5,
                                    sortedFrom
                            )
                    );
                }
            }

            // One more element is now permanently sorted
            sortedFrom =
                    n - i - 1;

            steps.add(
                    new Step(
                            copy.clone(),
                            -1,
                            -1,
                            false,
                            false,
                            7,
                            sortedFrom
                    )
            );

            if (!swappedInPass) {
                break;
            }
        }

        // Final state
        steps.add(
                new Step(
                        copy.clone(),
                        -1,
                        -1,
                        false,
                        false,
                        8,
                        0
                )
        );

        return steps;
    }
}