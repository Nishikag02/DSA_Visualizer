package com.dsavisualizer.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * Stack: LIFO (Last In, First Out). Items are added and removed at the TOP.
 *
 * The items are kept in an ArrayList. The top of the stack is the LAST
 * element of the list, so push = add at the end, pop = remove the last.
 *
 * Operations: 0 = Push, 1 = Pop, 2 = Peek.
 */
public class StackStructure extends DataStructure {

    private static final int CAPACITY = 6;

    public StackStructure() {
        super(
                "Stack",
                "Last In, First Out: the last item pushed is the first one popped",
                "A stack only works at the top: push adds, pop removes, peek only looks.",
                new String[]{"Push", "Pop", "Peek"},
                new boolean[]{true, false, false},
                VERTICAL,
                CAPACITY,
                new int[]{12, 7, 25},
                new ArrayList<>()
        );
    }

    @Override
    public String[] getOperationCode(int operation) {

        if (operation == 0) {
            return new String[]{
                    "void push(int x) {",
                    "    if (top == MAX - 1)",
                    "        error(\"Overflow\");",
                    "    arr[++top] = x;",
                    "}"
            };
        }

        if (operation == 1) {
            return new String[]{
                    "int pop() {",
                    "    if (top == -1)",
                    "        error(\"Underflow\");",
                    "    return arr[top--];",
                    "}"
            };
        }

        return new String[]{
                "int peek() {",
                "    if (top == -1)",
                "        error(\"Empty\");",
                "    return arr[top];",
                "}"
        };
    }

    @Override
    public List<StructureStep> execute(int operation, int value)
            throws StructureException {

        if (operation == 0) {
            return push(value);
        }

        if (operation == 1) {
            return pop();
        }

        return peek();
    }

    private List<StructureStep> push(int value) throws StructureException {

        if (items.size() == CAPACITY) {
            throw new StructureException(
                    "Stack Overflow: the stack is full (maximum " + CAPACITY
                            + " items), so " + value + " cannot be pushed.");
        }

        List<StructureStep> steps = new ArrayList<>();

        steps.add(step(2, "Checking for overflow",
                "The stack holds " + items.size() + " of " + CAPACITY
                        + " items, so there is room for " + value + "."));

        items.add(value);

        steps.add(step(4, "Pushing " + value,
                "top moves up by one and " + value
                        + " is stored there. It is the new top.")
                .withHighlight(items.size() - 1, StructureStep.ADDED));

        return steps;
    }

    private List<StructureStep> pop() throws StructureException {

        if (items.isEmpty()) {
            throw new StructureException(
                    "Stack Underflow: the stack is empty, so nothing can be popped.");
        }

        List<StructureStep> steps = new ArrayList<>();

        int topIndex = items.size() - 1;
        int top = items.get(topIndex);

        steps.add(step(2, "Checking for underflow",
                "The stack holds " + items.size()
                        + " item(s), so it is not empty."));

        steps.add(step(4, "Popping " + top,
                "The top value " + top + " is taken off the stack and returned.")
                .withHighlight(topIndex, StructureStep.REMOVED));

        items.remove(topIndex);

        steps.add(step(4, "Popped " + top,
                "top moves down by one. The new top is "
                        + (items.isEmpty() ? "nothing (the stack is empty)."
                        : items.get(items.size() - 1) + ".")));

        return steps;
    }

    private List<StructureStep> peek() throws StructureException {

        if (items.isEmpty()) {
            throw new StructureException(
                    "The stack is empty, so there is no top value to look at.");
        }

        List<StructureStep> steps = new ArrayList<>();

        int topIndex = items.size() - 1;

        steps.add(step(2, "Checking if empty",
                "The stack holds " + items.size()
                        + " item(s), so it is not empty."));

        steps.add(step(4, "Peeking at the top",
                "The top value is " + items.get(topIndex)
                        + ". Nothing is removed.")
                .withHighlight(topIndex, StructureStep.ACTIVE));

        return steps;
    }
}
