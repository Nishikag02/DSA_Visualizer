package com.dsavisualizer.view;

import com.dsavisualizer.algorithms.AlgorithmRegistry;
import com.dsavisualizer.algorithms.GraphAlgorithm;
import com.dsavisualizer.algorithms.SearchAlgorithm;
import com.dsavisualizer.algorithms.SortAlgorithm;
import com.dsavisualizer.structures.DataStructure;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The "Choose an Algorithm" menu. It implements the AppScreen interface,
 * like the sorting, searching, data structure and graph screens.
 *
 * The menu has four COLUMNS (an HBox of VBoxes): Sorting, Searching, Data
 * Structures and Graphs. The buttons are created in loops over the lists in
 * AlgorithmRegistry, so a new algorithm or data structure shows up here
 * automatically.
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

        // ----- Column 1: sorting -----
        // All sorting buttons open the SAME screen (VisualizerView);
        // only the SortAlgorithm object is different.
        VBox sortingColumn = createColumn("SORTING");

        for (SortAlgorithm algorithm : AlgorithmRegistry.getSortAlgorithms()) {

            Button button = new Button(algorithm.getDisplayName());

            button.setOnAction(event -> button.getScene().setRoot(
                    new VisualizerView(algorithm, navigator).createView()));

            sortingColumn.getChildren().add(button);
        }

        // ----- Column 2: searching (all open SearchView) -----
        VBox searchingColumn = createColumn("SEARCHING");

        for (SearchAlgorithm algorithm : AlgorithmRegistry.getSearchAlgorithms()) {

            Button button = new Button(algorithm.getDisplayName());

            button.setOnAction(event -> button.getScene().setRoot(
                    new SearchView(algorithm, navigator).createView()));

            searchingColumn.getChildren().add(button);
        }

        // ----- Column 3: data structures (all open StructureView) -----
        VBox structuresColumn = createColumn("DATA STRUCTURES");

        for (DataStructure structure : AlgorithmRegistry.getDataStructures()) {

            Button button = new Button(structure.getDisplayName());

            button.setOnAction(event -> button.getScene().setRoot(
                    new StructureView(structure, navigator).createView()));

            structuresColumn.getChildren().add(button);
        }

        // ----- Column 4: graphs (all open GraphView) -----
        VBox graphsColumn = createColumn("GRAPHS");

        for (GraphAlgorithm algorithm : AlgorithmRegistry.getGraphAlgorithms()) {

            Button button = new Button(algorithm.getDisplayName());

            button.setOnAction(event -> button.getScene().setRoot(
                    new GraphView(algorithm, navigator).createView()));

            graphsColumn.getChildren().add(button);
        }

        // The four columns side by side
        HBox columns = new HBox(40,
                sortingColumn, searchingColumn, structuresColumn, graphsColumn);

        columns.setAlignment(Pos.TOP_CENTER);

        // Back button: asks the Navigator (Main) to show the dashboard
        Button backButton = new Button("← Back");
        backButton.setOnAction(event -> navigator.showDashboard());

        VBox layout = new VBox(30, title, columns, backButton);
        layout.setAlignment(Pos.CENTER);

        return layout;
    }

    /** A column: a bold heading with the buttons stacked below it. */
    private VBox createColumn(String heading) {

        Label label = new Label(heading);
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox column = new VBox(10, label);
        column.setAlignment(Pos.TOP_CENTER);

        return column;
    }

    /** The menu has no timeline or animation, so there is nothing to stop. */
    @Override
    public void stop() {
    }
}
