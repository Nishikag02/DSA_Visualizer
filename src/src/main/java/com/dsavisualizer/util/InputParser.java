package com.dsavisualizer.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Turns the text typed by the user into numbers, and checks it is valid.
 *
 * All methods are STATIC: they do not need any object data, so you call them
 * directly on the class, e.g. InputParser.parseTarget("23").
 */
public final class InputParser {

    public static final int MAX_SIZE = 7;
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 99;

    // Private constructor: nobody should create an InputParser object.
    private InputParser() {
    }

    /** "5, 2, 8" -> {5, 2, 8}. Throws InvalidInputException if the text is bad. */
    public static int[] parseArray(String text) throws InvalidInputException {

        if (text == null || text.isBlank()) {
            throw new InvalidInputException(
                    "Please enter some numbers separated by commas, e.g. 5, 2, 8.");
        }

        // A List<Integer> grows as we add numbers (a generic ArrayList)
        List<Integer> numbers = new ArrayList<>();

        for (String part : text.split(",")) {
            numbers.add(parseNumber(part));
        }

        if (numbers.size() > MAX_SIZE) {
            throw new InvalidInputException(
                    "Please use at most " + MAX_SIZE + " numbers so they fit on screen.");
        }

        // Copy the list into a normal int[]
        int[] array = new int[numbers.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = numbers.get(i);
        }

        return array;
    }

    /** "23" -> 23. Throws InvalidInputException if the text is bad. */
    public static int parseTarget(String text) throws InvalidInputException {
        return parseNumber(text);
    }

    /** {5, 2, 8} -> "5, 2, 8" (used to fill the text field). */
    public static String arrayToText(int[] array) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < array.length; i++) {

            if (i > 0) {
                builder.append(", ");
            }

            builder.append(array[i]);
        }

        return builder.toString();
    }

    /** Converts one piece of text to a whole number between 0 and 99. */
    private static int parseNumber(String text) throws InvalidInputException {

        String trimmed = (text == null) ? "" : text.trim();

        if (trimmed.isEmpty()) {
            throw new InvalidInputException(
                    "An empty value was found. Check your commas and the target box.");
        }

        int value;

        try {
            value = Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            // Integer.parseInt threw its own exception; we replace it with ours
            throw new InvalidInputException(
                    "\"" + trimmed + "\" is not a whole number.");
        }

        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new InvalidInputException(
                    "Numbers must be between " + MIN_VALUE + " and " + MAX_VALUE + ".");
        }

        return value;
    }
}
