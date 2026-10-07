package com.dsavisualizer.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AlgorithmSelectionView {

    public VBox createView() {

        // Title
        Label title = new Label("Choose an Algorithm");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        // Sorting section
        Label sortingLabel = new Label("SORTING");
        sortingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button bubbleSortButton = new Button("Bubble Sort");
        Button selectionSortButton = new Button("Selection Sort");
        Button insertionSortButton = new Button("Insertion Sort");

        // Searching section
        Label searchingLabel = new Label("SEARCHING");
        searchingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button linearSearchButton = new Button("Linear Search");
        Button binarySearchButton = new Button("Binary Search");

        // Back button
        Button backButton = new Button("← Back");

        // Bubble Sort navigation
        bubbleSortButton.setOnAction(event -> {

            BubbleSortView bubbleSortView = new BubbleSortView();

            VBox layout = bubbleSortView.createView();

            // Replace current screen
            bubbleSortButton.getScene().setRoot(layout);
        });

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
}