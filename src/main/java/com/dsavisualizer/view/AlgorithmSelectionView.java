package com.dsavisualizer.view;

import com.dsavisualizer.algorithms.AlgorithmRegistry;
import com.dsavisualizer.algorithms.SearchAlgorithm;
import com.dsavisualizer.algorithms.SortAlgorithm;
import com.dsavisualizer.structures.DataStructure;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * The "Choose an Algorithm" menu. It implements the AppScreen interface,
 * like the sorting and searching screens.
 *
 * The buttons are created in loops over the lists in AlgorithmRegistry, so a
 * new algorithm or data structure shows up here automatically.
 */
public class AlgorithmSelectionView implements AppScreen {

    /** Lets the Back button return to the dashboard (Main implements it). */
    private final Navigator navigator;

    public AlgorithmSelectionView(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public VBox createView() {

        // Title
        Label title = new Label("Choose an Algorithm");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        // Layout: a VBox stacks everything from top to bottom
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(title);

        // Sorting section
        Label sortingLabel = new Label("SORTING");
        sortingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        layout.getChildren().add(sortingLabel);

        // All sorting buttons open the SAME screen (VisualizerView);
        // only the SortAlgorithm object is different.
        for (SortAlgorithm algorithm : AlgorithmRegistry.getSortAlgorithms()) {

            Button button = new Button(algorithm.getDisplayName());

            button.setOnAction(event -> button.getScene().setRoot(
                    new VisualizerView(algorithm, navigator).createView()));

            layout.getChildren().add(button);
        }

        // Searching section
        Label searchingLabel = new Label("SEARCHING");
        searchingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        layout.getChildren().add(searchingLabel);

        // All searching buttons open the SAME screen (SearchView)
        for (SearchAlgorithm algorithm : AlgorithmRegistry.getSearchAlgorithms()) {

            Button button = new Button(algorithm.getDisplayName());

            button.setOnAction(event -> button.getScene().setRoot(
                    new SearchView(algorithm, navigator).createView()));

            layout.getChildren().add(button);
        }

        // Data structures section
        Label structuresLabel = new Label("DATA STRUCTURES");
        structuresLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        layout.getChildren().add(structuresLabel);

        // All data structure buttons open the SAME screen (StructureView)
        for (DataStructure structure : AlgorithmRegistry.getDataStructures()) {

            Button button = new Button(structure.getDisplayName());

            button.setOnAction(event -> button.getScene().setRoot(
                    new StructureView(structure, navigator).createView()));

            layout.getChildren().add(button);
        }

        // Back button: asks the Navigator (Main) to show the dashboard
        Button backButton = new Button("← Back");
        backButton.setOnAction(event -> navigator.showDashboard());
        layout.getChildren().add(backButton);

        return layout;
    }

    /** The menu has no timeline or animation, so there is nothing to stop. */
    @Override
    public void stop() {
    }
}
