package com.dsavisualizer.view;

/**
 * INTERFACE: a promise that a class can "go back to the dashboard".
 *
 * Main implements it. The screens (menu, sorting, searching, sidebar) only
 * know about this interface, not about Main, so they can still send the user
 * back to the first screen when "← Back" is clicked.
 */
public interface Navigator {

    /** Shows the first screen (title + "Start Visualizing"). */
    void showDashboard();
}
