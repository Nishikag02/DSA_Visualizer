package com.dsavisualizer.algorithms;

import java.util.List;
import java.util.function.Function;

/**
 * Describes everything that is DIFFERENT between sorting algorithms:
 * name, subtitle, intro text, pseudocode and the engine that makes the steps.
 *
 * The shared VisualizerView receives one of these constants and builds the
 * same screen for all of them. Adding a new sort = adding one constant here.
 *
 * The last constructor argument is a LAMBDA: a Function that takes an int[]
 * and returns a List<SortStep>.
 */
public enum SortAlgorithm {

    BUBBLE(
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
            },
            array -> new BubbleSort().getSteps(array)
    ),

    SELECTION(
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
            },
            array -> new SelectionSort().getSteps(array)
    ),

    INSERTION(
            "Insertion Sort",
            "Insert each value into its correct place in the sorted left part",
            "Insertion Sort builds a sorted section one element at a time.",
            new String[]{
                    "int[] arr = {5, 2, 8, 1, 6};",
                    "for (int i = 1; i < n; i++) {",
                    "    int key = arr[i];",
                    "    int j = i - 1;",
                    "    while (j >= 0 && arr[j] > key) {",
                    "        arr[j + 1] = arr[j];",
                    "        j--;",
                    "    }",
                    "    arr[j + 1] = key;",
                    "}"
            },
            array -> new InsertionSort().getSteps(array)
    );

    private final String displayName;
    private final String subtitle;
    private final String intro;
    private final String[] code;
    private final Function<int[], List<SortStep>> stepGenerator;

    SortAlgorithm(String displayName, String subtitle, String intro,
                  String[] code, Function<int[], List<SortStep>> stepGenerator) {
        this.displayName = displayName;
        this.subtitle = subtitle;
        this.intro = intro;
        this.code = code;
        this.stepGenerator = stepGenerator;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getIntro() {
        return intro;
    }

    public String[] getCode() {
        return code;
    }

    /** Runs the algorithm on a copy of the array and returns all steps. */
    public List<SortStep> generateSteps(int[] array) {
        return stepGenerator.apply(array.clone());
    }
}
