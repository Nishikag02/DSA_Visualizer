package com.dsavisualizer.algorithms;

/**
 * One "snapshot" of a sorting algorithm.
 *
 * Every algorithm (Bubble, Selection, Insertion) produces a List<SortStep>.
 * The visualizer never runs the algorithm itself - it only DISPLAYS these snapshots.
 *
 * Required data goes in the constructor. Optional data is added with the
 * "with..." methods, which return "this" so calls can be chained:
 *
 *     new SortStep(copy, 4, "Comparing elements", "Comparing 5 and 2")
 *             .withActive(0)
 *             .withCompare(1);
 */
public class SortStep {

    /** A COPY of the array at this moment (so later changes don't affect old steps). */
    public final int[] array;

    /** Pseudocode line to highlight (1-based). 0 = no line. */
    public final int codeLine;

    /** Short heading shown under the array, e.g. "Comparing elements". */
    public final String title;

    /** Longer explanation shown in the bottom bar. */
    public final String message;

    /** Main index being looked at (-1 = none). */
    public int activeIndex = -1;

    /** Second index being compared (-1 = none). */
    public int compareIndex = -1;

    /** Index of the key (Insertion Sort) or current minimum (Selection Sort). -1 = none. */
    public int keyIndex = -1;

    /** sorted[i] == true means the card at index i is already in its final area. */
    public boolean[] sorted;

    /** True if activeIndex and compareIndex are exchanged in this step (animated). */
    public boolean swapping = false;

    /** True if an element is shifted in this step (Insertion Sort, animated like a swap). */
    public boolean shifting = false;

    /** True if the compared pair is in the wrong order (cards turn red). */
    public boolean wrongOrder = false;

    /** True only for the final step. */
    public boolean complete = false;

    public SortStep(int[] array, int codeLine, String title, String message) {
        this.array = array.clone();
        this.codeLine = codeLine;
        this.title = title;
        this.message = message;
        this.sorted = new boolean[array.length];
    }

    public SortStep withActive(int index) {
        this.activeIndex = index;
        return this;
    }

    public SortStep withCompare(int index) {
        this.compareIndex = index;
        return this;
    }

    public SortStep withKey(int index) {
        this.keyIndex = index;
        return this;
    }

    /** Marks indices from (inclusive) to to (exclusive) as sorted. */
    public SortStep withSorted(int from, int to) {
        for (int i = Math.max(0, from); i < Math.min(to, sorted.length); i++) {
            sorted[i] = true;
        }
        return this;
    }

    public SortStep withSwapping() {
        this.swapping = true;
        return this;
    }

    public SortStep withShifting() {
        this.shifting = true;
        return this;
    }

    public SortStep withWrongOrder(boolean wrongOrder) {
        this.wrongOrder = wrongOrder;
        return this;
    }

    /** Final step: everything is sorted. */
    public SortStep withComplete() {
        this.complete = true;
        withSorted(0, sorted.length);
        return this;
    }

    /** True if this step needs the two-card sliding animation. */
    public boolean needsMoveAnimation() {
        return (swapping || shifting) && activeIndex >= 0 && compareIndex >= 0;
    }
}
