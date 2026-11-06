package com.dsavisualizer.algorithms;

/**
 * One "snapshot" of a searching algorithm (the search version of SortStep).
 *
 * Searching never changes the array, so a step only needs to remember
 * WHICH cards are special at this moment:
 *   currentIndex - card being checked (Linear Search)
 *   low, mid, high - the three pointers of Binary Search
 *   foundIndex - where the target was found (-1 = not found yet)
 *   eliminated[i] - true if card i is no longer part of the search
 *   notFound - true on the final step if the target is not in the array
 *
 * Optional data is added with the "with..." methods, which return "this"
 * so calls can be chained (same style as SortStep).
 */
public class SearchStep {

    public final int[] array;
    public final int codeLine;
    public final String title;
    public final String message;

    public int currentIndex = -1;
    public int low = -1;
    public int mid = -1;
    public int high = -1;
    public int foundIndex = -1;

    public boolean[] eliminated;

    public boolean notFound = false;

    public SearchStep(int[] array, int codeLine, String title, String message) {
        this.array = array.clone();
        this.codeLine = codeLine;
        this.title = title;
        this.message = message;
        this.eliminated = new boolean[array.length];
    }

    public SearchStep withCurrent(int index) {
        this.currentIndex = index;
        return this;
    }

    public SearchStep withRange(int low, int high) {
        this.low = low;
        this.high = high;
        return this;
    }

    public SearchStep withMid(int mid) {
        this.mid = mid;
        return this;
    }

    public SearchStep withFound(int index) {
        this.foundIndex = index;
        return this;
    }

    public SearchStep withNotFound() {
        this.notFound = true;
        return this;
    }

    /** Marks indices from (inclusive) to to (exclusive) as eliminated. */
    public SearchStep withEliminated(int from, int to) {
        for (int i = Math.max(0, from); i < Math.min(to, eliminated.length); i++) {
            eliminated[i] = true;
        }
        return this;
    }

    /** Marks everything outside the range low..high as eliminated. */
    public SearchStep withEliminatedOutside(int low, int high) {
        withEliminated(0, low);
        withEliminated(high + 1, eliminated.length);
        return this;
    }
}
