package com.dsavisualizer.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * One "snapshot" of a data structure while an operation (push, pop, ...)
 * is being explained. It is the data-structure version of SortStep.
 *
 * It stores a COPY of the items, which pseudocode line to highlight, a title
 * and an explanation, and which item (if any) is highlighted and how.
 *
 * Optional data is added with the "with..." methods, which return "this".
 */
public class StructureStep {

    // How the highlighted item is drawn
    public static final int NORMAL = 0;   // no highlight
    public static final int ACTIVE = 1;   // yellow: being looked at
    public static final int ADDED = 2;    // green: just added / found
    public static final int REMOVED = 3;  // red: about to be removed

    /** Copy of the items (index 0 = bottom of a stack / front of a queue / head of a list). */
    public final List<Integer> items;

    /** Line of the operation's pseudocode to highlight (1-based, 0 = none). */
    public final int codeLine;

    public final String title;
    public final String message;

    public int highlightIndex = -1;
    public int highlightMode = NORMAL;

    /** Linked list only: which node the HEAD label sits on (-1 = no node). */
    public int headIndex = 0;

    public StructureStep(List<Integer> items, int codeLine, String title, String message) {
        this.items = new ArrayList<>(items);
        this.codeLine = codeLine;
        this.title = title;
        this.message = message;
    }

    public StructureStep withHighlight(int index, int mode) {
        this.highlightIndex = index;
        this.highlightMode = mode;
        return this;
    }

    public StructureStep withHeadIndex(int headIndex) {
        this.headIndex = headIndex;
        return this;
    }
}
