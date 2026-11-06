package com.dsavisualizer.util;

/**
 * A CUSTOM EXCEPTION (Unit 2 of the syllabus).
 *
 * It is thrown by InputParser when the user types something that cannot be
 * used (letters instead of numbers, too many numbers, ...).
 *
 * It extends Exception, so it is a CHECKED exception: any method that can
 * throw it must either catch it (try/catch) or declare it with "throws".
 * The view catches it and shows the message in an Alert dialog.
 */
public class InvalidInputException extends Exception {

    // Exceptions are Serializable (Unit 3), so Java likes a version number
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }
}
