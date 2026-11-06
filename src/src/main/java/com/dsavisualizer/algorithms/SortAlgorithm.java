package com.dsavisualizer.algorithms;

import java.util.List;

/**
 * ABSTRACT CLASS for every sorting algorithm (BubbleSort, SelectionSort,
 * InsertionSort, ...). It extends Algorithm, so it already has a name,
 * subtitle, intro and pseudocode.
 *
 * It adds one ABSTRACT method, getSteps(...). Every child class MUST
 * implement it (that is POLYMORPHISM): the visualizer calls getSteps() on a
 * SortAlgorithm variable and does not care which sort is inside.
 */
public abstract class SortAlgorithm extends Algorithm {

    protected SortAlgorithm(String displayName, String subtitle,
                            String intro, String[] code) {
        super(displayName, subtitle, intro, code);
    }

    /** Runs the sort on a copy of the array and returns one SortStep per moment. */
    public abstract List<SortStep> getSteps(int[] array);

    /** Same as getSteps, but makes sure the original array is never touched. */
    public List<SortStep> generateSteps(int[] array) {
        return getSteps(array.clone());
    }
}
