package com.dsavisualizer.structures;

/**
 * A CUSTOM EXCEPTION for operations that are not allowed:
 * Stack Overflow / Underflow, Queue Full / Empty, a full list, ...
 *
 * It is a CHECKED exception (it extends Exception), so execute(...) must
 * declare "throws StructureException" and the screen must catch it.
 */
public class StructureException extends Exception {

    private static final long serialVersionUID = 1L;

    public StructureException(String message) {
        super(message);
    }
}
