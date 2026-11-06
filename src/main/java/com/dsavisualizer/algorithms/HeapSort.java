package com.dsavisualizer.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Heap Sort engine (generates SortStep snapshots).
 *
 * Idea: look at the array as a tree. The children of index i are at
 * 2i + 1 (left) and 2i + 2 (right). In a MAX HEAP every parent is bigger than
 * its children, so the biggest value is always at index 0.
 *   Phase 1: turn the array into a max heap (siftDown from the last parent).
 *   Phase 2: swap the biggest value (index 0) to the end, shrink the heap by
 *            one, and siftDown the new root. Repeat.
 *
 * Pseudocode lines used (see the constructor):
 *   2  = building the heap (next parent)
 *   5  = swap(arr[0], arr[end])      (largest goes to the end)
 *   10 = compare parent with its children
 *   12 = swap(arr[i], arr[big])      (parent sinks down)
 *   15 = finished
 *
 * Colours: yellow = parent and the larger child being compared,
 * red = cards being swapped, green = card in its final place at the end,
 * dim = not part of the heap any more / outside the range.
 *
 * siftDown calls itself (recursion), so the data it shares with heapSort is
 * kept in fields (work, steps, done) of this object.
 */
public class HeapSort extends SortAlgorithm {

    private int[] work;
    private List<SortStep> steps;
    private boolean[] done;

    public HeapSort() {
        super(
                "Heap Sort",
                "Build a max heap, then move the largest value to the end again and again",
                "Heap Sort sees the array as a tree: the children of index i are 2i+1 and 2i+2.",
                new String[]{
                        "void heapSort(arr) {",
                        "    for (int i = n / 2 - 1; i >= 0; i--)",
                        "        siftDown(arr, i, n);   // build heap",
                        "    for (int end = n - 1; end > 0; end--) {",
                        "        swap(arr[0], arr[end]);",
                        "        siftDown(arr, 0, end);",
                        "    }",
                        "}",
                        "void siftDown(arr, i, size) {",
                        "    big = largest of arr[i] and its children;",
                        "    if (big != i) {",
                        "        swap(arr[i], arr[big]);",
                        "        siftDown(arr, big, size);",
                        "    }",
                        "}"
                }
        );
    }

    @Override
    public List<SortStep> getSteps(int[] array) {

        steps = new ArrayList<>();
        work = array.clone();
        done = new boolean[work.length];

        int n = work.length;

        // Phase 1: build the max heap, starting at the last parent
        for (int i = n / 2 - 1; i >= 0; i--) {

            steps.add(newStep(2, "Building the heap",
                    "Make sure the parent " + work[i] + " at index " + i
                            + " is larger than its children.")
                    .withActive(i)
                    .withRange(0, n - 1));

            siftDown(i, n);
        }

        // Phase 2: move the largest value to the end, shrink the heap
        for (int end = n - 1; end > 0; end--) {

            int largest = work[0];
            int other = work[end];

            work[0] = other;
            work[end] = largest;

            steps.add(newStep(5, "Moving the largest to the end",
                    "The root " + largest + " is the largest value in the heap. "
                            + "Swap it with " + other + " at index " + end
                            + ". " + largest + " is now in its final place.")
                    .withActive(0)
                    .withCompare(end)
                    .withRange(0, end)
                    .withSwapping());

            done[end] = true;

            siftDown(0, end);
        }

        done[0] = true;

        steps.add(new SortStep(work, 15, "Sorting complete!",
                "Heap Sort has finished. All elements are now sorted.")
                .withComplete());

        return steps;
    }

    /**
     * Moves the value at index i down until it is larger than its children.
     * Only the first "size" cards belong to the heap.
     */
    private void siftDown(int i, int size) {

        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // No children: this card is a leaf, nothing to do
        if (left >= size) {
            return;
        }

        // Find the larger child
        int child = left;

        if (right < size && work[right] > work[left]) {
            child = right;
        }

        boolean mustSwap = work[child] > work[i];

        String childText;

        if (right < size) {
            childText = "its children are " + work[left] + " and " + work[right]
                    + ", the larger one is " + work[child];
        } else {
            childText = "its only child is " + work[child];
        }

        steps.add(newStep(10, "Comparing parent with children",
                "The parent " + work[i] + " at index " + i + ": " + childText + ". "
                        + (mustSwap
                        ? work[child] + " is larger, so the parent sinks down."
                        : "The parent is larger, so this part is a heap already."))
                .withActive(i)
                .withCompare(child)
                .withWrongOrder(mustSwap)
                .withRange(0, size - 1));

        if (mustSwap) {

            int parentValue = work[i];
            int childValue = work[child];

            work[i] = childValue;
            work[child] = parentValue;

            steps.add(newStep(12, "Swapping parent and child",
                    "Swap " + parentValue + " and " + childValue
                            + ". The larger value moves up.")
                    .withActive(i)
                    .withCompare(child)
                    .withSwapping()
                    .withRange(0, size - 1));

            // The parent now sits at index "child": check it again
            siftDown(child, size);
        }
    }

    /** New snapshot of the current array, with finished cards marked green. */
    private SortStep newStep(int codeLine, String title, String message) {
        return new SortStep(work, codeLine, title, message)
                .withSortedFlags(done);
    }
}
