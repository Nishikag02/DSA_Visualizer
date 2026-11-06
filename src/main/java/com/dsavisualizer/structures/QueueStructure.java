package com.dsavisualizer.structures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Queue: FIFO (First In, First Out). Items join at the REAR and leave from
 * the FRONT, like people waiting in a line.
 *
 * The items are kept in a LinkedList, because removing the first element of
 * a LinkedList is fast. The front is index 0, the rear is the last index.
 *
 * Operations: 0 = Enqueue, 1 = Dequeue, 2 = Peek.
 */
public class QueueStructure extends DataStructure {

    private static final int CAPACITY = 6;

    public QueueStructure() {
        super(
                "Queue",
                "First In, First Out: the first item added is the first one removed",
                "A queue adds at the rear and removes from the front, like a line of people.",
                new String[]{"Enqueue", "Dequeue", "Peek"},
                new boolean[]{true, false, false},
                HORIZONTAL,
                CAPACITY,
                new int[]{12, 7, 25},
                new LinkedList<>()
        );
    }

    @Override
    public String[] getOperationCode(int operation) {

        if (operation == 0) {
            return new String[]{
                    "void enqueue(int x) {",
                    "    if (size == MAX)",
                    "        error(\"Full\");",
                    "    arr[rear++] = x;",
                    "}"
            };
        }

        if (operation == 1) {
            return new String[]{
                    "int dequeue() {",
                    "    if (size == 0)",
                    "        error(\"Empty\");",
                    "    return arr[front++];",
                    "}"
            };
        }

        return new String[]{
                "int peek() {",
                "    if (size == 0)",
                "        error(\"Empty\");",
                "    return arr[front];",
                "}"
        };
    }

    @Override
    public List<StructureStep> execute(int operation, int value)
            throws StructureException {

        if (operation == 0) {
            return enqueue(value);
        }

        if (operation == 1) {
            return dequeue();
        }

        return peek();
    }

    private List<StructureStep> enqueue(int value) throws StructureException {

        if (items.size() == CAPACITY) {
            throw new StructureException(
                    "Queue Full: the queue holds " + CAPACITY
                            + " items already, so " + value + " cannot be added.");
        }

        List<StructureStep> steps = new ArrayList<>();

        steps.add(step(2, "Checking if the queue is full",
                "The queue holds " + items.size() + " of " + CAPACITY
                        + " items, so there is room for " + value + "."));

        items.add(value);

        steps.add(step(4, "Enqueuing " + value,
                value + " joins the queue at the rear.")
                .withHighlight(items.size() - 1, StructureStep.ADDED));

        return steps;
    }

    private List<StructureStep> dequeue() throws StructureException {

        if (items.isEmpty()) {
            throw new StructureException(
                    "Queue Empty: the queue has no items, so nothing can be removed.");
        }

        List<StructureStep> steps = new ArrayList<>();

        int front = items.get(0);

        steps.add(step(2, "Checking if the queue is empty",
                "The queue holds " + items.size()
                        + " item(s), so it is not empty."));

        steps.add(step(4, "Dequeuing " + front,
                front + " is at the front, so it leaves the queue first.")
                .withHighlight(0, StructureStep.REMOVED));

        items.remove(0);

        steps.add(step(4, "Dequeued " + front,
                "front moves forward. The new front is "
                        + (items.isEmpty() ? "nothing (the queue is empty)."
                        : items.get(0) + ".")));

        return steps;
    }

    private List<StructureStep> peek() throws StructureException {

        if (items.isEmpty()) {
            throw new StructureException(
                    "The queue is empty, so there is no front value to look at.");
        }

        List<StructureStep> steps = new ArrayList<>();

        steps.add(step(2, "Checking if the queue is empty",
                "The queue holds " + items.size()
                        + " item(s), so it is not empty."));

        steps.add(step(4, "Peeking at the front",
                "The front value is " + items.get(0) + ". Nothing is removed.")
                .withHighlight(0, StructureStep.ACTIVE));

        return steps;
    }
}
