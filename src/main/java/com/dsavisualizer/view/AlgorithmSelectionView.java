package com.dsavisualizer.view;

import com.dsavisualizer.algorithms.SortAlgorithm;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AlgorithmSelectionView {

    /** Code to run when "Back" is clicked (given to us by Main). */
    private final Runnable onBack;

    public AlgorithmSelectionView(Runnable onBack) {
        this.onBack = onBack;
    }

    public VBox createView() {

        // Title
        Label title = new Label("Choose an Algorithm");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        // Sorting section
        Label sortingLabel = new Label("SORTING");
        sortingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // All three sorting buttons open the SAME visualizer,
        // only the SortAlgorithm constant is different.
        Button bubbleSortButton = new Button("Bubble Sort");
        bubbleSortButton.setOnAction(event ->
                openSort(SortAlgorithm.BUBBLE, bubbleSortButton));

        Button selectionSortButton = new Button("Selection Sort");
        selectionSortButton.setOnAction(event ->
                openSort(SortAlgorithm.SELECTION, selectionSortButton));

        Button insertionSortButton = new Button("Insertion Sort");
        insertionSortButton.setOnAction(event ->
                openSort(SortAlgorithm.INSERTION, insertionSortButton));

        // Searching section
        Label searchingLabel = new Label("SEARCHING");
        searchingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button linearSearchButton = new Button("Linear Search");
        Button binarySearchButton = new Button("Binary Search");

        // Back button: runs the action Main gave us (shows the dashboard)
        Button backButton = new Button("← Back");
        backButton.setOnAction(event -> onBack.run());

        // Layout
        VBox layout = new VBox(
                15,
                title,
                sortingLabel,
                bubbleSortButton,
                selectionSortButton,
                insertionSortButton,
                searchingLabel,
                linearSearchButton,
                binarySearchButton,
                backButton
        );

        layout.setAlignment(Pos.CENTER);

        return layout;
    }

    /** Builds the shared visualizer for one algorithm and shows it. */
    private void openSort(SortAlgorithm algorithm, Button source) {

        VisualizerView view = new VisualizerView(algorithm, onBack);

        source.getScene().setRoot(view.createView());
    }
}
