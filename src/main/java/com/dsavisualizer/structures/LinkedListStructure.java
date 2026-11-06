package com.dsavisualizer.structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Singly linked list: every node holds a value and a pointer to the next
 * node. The first node is the HEAD, the last node points to NULL.
 *
 * The values are kept in a java.util.LinkedList (inside the List<Integer>
 * variable of DataStructure). To walk through the nodes one by one, the
 * operations use an ITERATOR: hasNext() asks "is there another node?" and
 * next() gives that node's value and moves on.
 *
 * Operations: 0 = Insert Head, 1 = Insert Tail, 2 = Delete, 3 = Search.
 */
public class LinkedListStructure extends DataStructure {

    // Only 5 nodes fit on the screen
    private static final int CAPACITY = 5;

    public LinkedListStructure() {
        super(
                "Linked List",
                "Nodes joined by pointers: each node knows the next one",
                "In a linked list every node stores a value and a pointer to the next node.",
                new String[]{"Insert Head", "Insert Tail", "Delete", "Search"},
                new boolean[]{true, true, true, true},
                LINKED,
                CAPACITY,
                new int[]{12, 7, 25},
                new LinkedList<>()
        );
    }

    @Override
    public String[] getOperationCode(int operation) {

        if (operation == 0) {
            return new String[]{
                    "void insertHead(int x) {",
                    "    Node n = new Node(x);",
                    "    n.next = head;",
                    "    head = n;",
                    "}"
            };
        }

        if (operation == 1) {
            return new String[]{
                    "void insertTail(int x) {",
                    "    Node n = new Node(x);",
                    "    if (head == null) { head = n; return; }",
                    "    Node cur = head;",
                    "    while (cur.next != null)",
                    "        cur = cur.next;",
                    "    cur.next = n;",
                    "}"
            };
        }

        if (operation == 2) {
            return new String[]{
                    "void delete(int x) {",
                    "    Node cur = head, prev = null;",
                    "    while (cur != null && cur.data != x) {",
                    "        prev = cur; cur = cur.next;",
                    "    }",
                    "    if (cur == null) return;   // not found",
                    "    if (prev == null) head = cur.next;",
                    "    else prev.next = cur.next;",
                    "}"
            };
        }

        return new String[]{
                "boolean search(int x) {",
                "    Node cur = head;",
                "    while (cur != null) {",
                "        if (cur.data == x) return true;",
                "        cur = cur.next;",
                "    }",
                "    return false;",
                "}"
        };
    }

    @Override
    public List<StructureStep> execute(int operation, int value)
            throws StructureException {

        if (operation == 0) {
            return insertHead(value);
        }

        if (operation == 1) {
            return insertTail(value);
        }

        if (operation == 2) {
            return delete(value);
        }

        return search(value);
    }

    private void checkNotFull(int value) throws StructureException {

        if (items.size() == CAPACITY) {
            throw new StructureException(
                    "The list already has " + CAPACITY + " nodes, which is the most "
                            + "this screen can show, so " + value + " cannot be added.");
        }
    }

    // ----------------------------------------------------------------
    // Insert at the head
    // ----------------------------------------------------------------

    private List<StructureStep> insertHead(int value) throws StructureException {

        checkNotFull(value);

        List<StructureStep> steps = new ArrayList<>();

        steps.add(step(2, "Creating a node",
                "A new node holding " + value
                        + " is created. It is not part of the list yet."));

        String oldHead = items.isEmpty()
                ? "null (the list was empty)"
                : "the old head " + items.get(0);

        items.add(0, value);

        // HEAD still sits on the old first node (now at index 1)
        steps.add(step(3, "Linking the new node",
                "The new node's next pointer is set to " + oldHead + ".")
                .withHighlight(0, StructureStep.ADDED)
                .withHeadIndex(items.size() > 1 ? 1 : -1));

        steps.add(step(4, "Moving the head",
                "head now points to the new node " + value + ".")
                .withHighlight(0, StructureStep.ADDED));

        return steps;
    }

    // ----------------------------------------------------------------
    // Insert at the tail (walks to the last node with an Iterator)
    // ----------------------------------------------------------------

    private List<StructureStep> insertTail(int value) throws StructureException {

        checkNotFull(value);

        List<StructureStep> steps = new ArrayList<>();

        steps.add(step(2, "Creating a node",
                "A new node holding " + value
                        + " is created. It is not part of the list yet."));

        if (items.isEmpty()) {

            items.add(value);

            steps.add(step(3, "The list is empty",
                    "head is null, so head now points to the new node.")
                    .withHighlight(0, StructureStep.ADDED));

            return steps;
        }

        steps.add(step(4, "Starting at the head",
                "cur = head, which is the first node (" + items.get(0) + ").")
                .withHighlight(0, StructureStep.ACTIVE));

        Iterator<Integer> iterator = items.iterator();

        int index = 0;

        while (iterator.hasNext()) {

            int current = iterator.next();

            boolean last = !iterator.hasNext();

            steps.add(step(5, "Is cur.next null?",
                    "cur is " + current + " (position " + index + "). "
                            + (last
                            ? "Its next pointer is null, so it is the last node."
                            : "Its next pointer is not null, so move to the next node."))
                    .withHighlight(index, StructureStep.ACTIVE));

            index++;
        }

        items.add(value);

        steps.add(step(7, "Linking the new node",
                "cur.next now points to the new node " + value
                        + ". It is the new last node.")
                .withHighlight(items.size() - 1, StructureStep.ADDED));

        return steps;
    }

    // ----------------------------------------------------------------
    // Delete the first node that holds the value
    // ----------------------------------------------------------------

    private List<StructureStep> delete(int value) {

        List<StructureStep> steps = new ArrayList<>();

        if (items.isEmpty()) {

            steps.add(step(6, "Nothing to delete",
                    "The list is empty, so " + value + " cannot be deleted."));

            return steps;
        }

        steps.add(step(2, "Starting at the head",
                "cur = head (" + items.get(0) + ") and prev = null.")
                .withHighlight(0, StructureStep.ACTIVE));

        Iterator<Integer> iterator = items.iterator();

        int index = 0;
        int foundAt = -1;

        while (iterator.hasNext()) {

            int current = iterator.next();

            boolean match = current == value;

            steps.add(step(3, "Checking the node",
                    "cur = " + current + ". "
                            + (match
                            ? "It equals " + value + ", so this is the node to delete."
                            : current + " is not " + value + ", so move to the next node."))
                    .withHighlight(index,
                            match ? StructureStep.REMOVED : StructureStep.ACTIVE));

            if (match) {
                foundAt = index;
                break;
            }

            index++;
        }

        if (foundAt == -1) {

            steps.add(step(6, "Not found",
                    "cur reached null: " + value
                            + " is not in the list, so nothing is deleted."));

            return steps;
        }

        if (foundAt == 0) {

            steps.add(step(7, "Moving the head",
                    "prev is null, so head = cur.next. The first node is unlinked.")
                    .withHighlight(0, StructureStep.REMOVED));

        } else {

            steps.add(step(8, "Unlinking the node",
                    "prev.next = cur.next, so the node " + value
                            + " is skipped and removed from the chain.")
                    .withHighlight(foundAt, StructureStep.REMOVED));
        }

        items.remove(foundAt);

        steps.add(step(foundAt == 0 ? 7 : 8, "Node deleted",
                "The list now has " + items.size() + " node(s)."));

        return steps;
    }

    // ----------------------------------------------------------------
    // Search for a value
    // ----------------------------------------------------------------

    private List<StructureStep> search(int value) {

        List<StructureStep> steps = new ArrayList<>();

        if (items.isEmpty()) {

            steps.add(step(7, "Not found",
                    "The list is empty, so " + value + " is not in it."));

            return steps;
        }

        steps.add(step(2, "Starting at the head",
                "cur = head, the first node (" + items.get(0) + ").")
                .withHighlight(0, StructureStep.ACTIVE));

        Iterator<Integer> iterator = items.iterator();

        int index = 0;

        while (iterator.hasNext()) {

            int current = iterator.next();

            if (current == value) {

                steps.add(step(4, "Found!",
                        "cur.data is " + current + ", which equals " + value
                                + ". The value is at position " + index + ".")
                        .withHighlight(index, StructureStep.ADDED));

                return steps;
            }

            steps.add(step(4, "Checking the node",
                    "cur.data is " + current + ", not " + value
                            + ". Move to the next node.")
                    .withHighlight(index, StructureStep.ACTIVE));

            index++;
        }

        steps.add(step(7, "Not found",
                "cur reached null, so " + value + " is not in the list."));

        return steps;
    }
}
