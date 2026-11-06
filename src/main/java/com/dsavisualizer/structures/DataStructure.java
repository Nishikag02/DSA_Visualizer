package com.dsavisualizer.structures;

import java.util.List;

/**
 * ABSTRACT CLASS for every data structure (Stack, Queue, Linked List).
 *
 * It keeps what they all share: a name, subtitle, intro, the names of the
 * operations (the buttons), how it is drawn, its capacity and the items.
 *
 * The items are stored in a List<Integer> VARIABLE. The child class decides
 * which list object goes inside (ArrayList or LinkedList). This works
 * because both implement the List interface (POLYMORPHISM).
 *
 * Each child must implement two abstract methods:
 *   getOperationCode(op) - the pseudocode of one operation
 *   execute(op, value)   - does the operation and returns the steps to show
 */
public abstract class DataStructure {

    // How the structure is drawn on screen
    public static final int VERTICAL = 0;    // Stack: boxes on top of each other
    public static final int HORIZONTAL = 1;  // Queue: boxes in a row
    public static final int LINKED = 2;      // Linked list: nodes with arrows

    private final String displayName;
    private final String subtitle;
    private final String intro;
    private final String[] operationNames;
    private final boolean[] needsValue;
    private final int layout;
    private final int capacity;
    private final int[] initialValues;

    // protected: the child classes work directly with the items
    protected final List<Integer> items;

    protected DataStructure(String displayName, String subtitle, String intro,
                            String[] operationNames, boolean[] needsValue,
                            int layout, int capacity, int[] initialValues,
                            List<Integer> items) {
        this.displayName = displayName;
        this.subtitle = subtitle;
        this.intro = intro;
        this.operationNames = operationNames;
        this.needsValue = needsValue;
        this.layout = layout;
        this.capacity = capacity;
        this.initialValues = initialValues;
        this.items = items;

        reset();
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

    public String[] getOperationNames() {
        return operationNames;
    }

    /** True if the operation needs a number from the Value box (e.g. Push). */
    public boolean needsValue(int operation) {
        return needsValue[operation];
    }

    public int getLayout() {
        return layout;
    }

    /** Maximum number of items (so everything fits on screen). */
    public int getCapacity() {
        return capacity;
    }

    /** Puts the starting items back. */
    public final void reset() {

        items.clear();

        for (int value : initialValues) {
            items.add(value);
        }
    }

    /** A step that just shows the structure as it is now (nothing highlighted). */
    public StructureStep snapshot(String title, String message) {
        return new StructureStep(items, 0, title, message);
    }

    /** Pseudocode lines of one operation. */
    public abstract String[] getOperationCode(int operation);

    /**
     * Performs one operation and returns the steps that explain it.
     * Throws StructureException if the operation is not allowed
     * (for example Pop on an empty stack); then nothing changes.
     */
    public abstract List<StructureStep> execute(int operation, int value)
            throws StructureException;

    /** Short way to create a step from the current items. */
    protected StructureStep step(int codeLine, String title, String message) {
        return new StructureStep(items, codeLine, title, message);
    }
}
