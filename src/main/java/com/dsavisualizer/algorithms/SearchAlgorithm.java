package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ABSTRACT CLASS for every searching algorithm (LinearSearch, BinarySearch,
 * ...). It extends Algorithm and adds what searches need: example numbers, a
 * default target, whether the array must be sorted, and a legend text.
 *
 * Its abstract method getSteps(array, target) is implemented by each child.
 */
public abstract class SearchAlgorithm extends Algorithm {

    private final int[] defaultArray;
    private final int defaultTarget;
    private final boolean needsSortedArray;
    private final String legend;

    protected SearchAlgorithm(String displayName, String subtitle,
                              String intro, String[] code,
                              int[] defaultArray, int defaultTarget,
                              boolean needsSortedArray, String legend) {
        super(displayName, subtitle, intro, code);
        this.defaultArray = defaultArray;
        this.defaultTarget = defaultTarget;
        this.needsSortedArray = needsSortedArray;
        this.legend = legend;
    }

    /** Runs the search and returns one SearchStep per moment. */
    public abstract List<SearchStep> getSteps(int[] array, int target);

    public int[] getDefaultArray() {
        return defaultArray.clone();
    }

    public int getDefaultTarget() {
        return defaultTarget;
    }

    public boolean needsSortedArray() {
        return needsSortedArray;
    }

    public String getLegend() {
        return legend;
    }

    /** Binary Search sorts the numbers first; Linear Search keeps them as typed. */
    public int[] prepareArray(int[] array) {

        int[] copy = array.clone();

        if (needsSortedArray) {

            // Collections.sort works on a List, so copy into an ArrayList first
            List<Integer> numbers = new ArrayList<>();

            for (int value : copy) {
                numbers.add(value);
            }

            Collections.sort(numbers);

            for (int i = 0; i < copy.length; i++) {
                copy[i] = numbers.get(i);
            }
        }

        return copy;
    }

    /** Runs the search on a copy of the array. */
    public List<SearchStep> generateSteps(int[] array, int target) {
        return getSteps(array.clone(), target);
    }
}
