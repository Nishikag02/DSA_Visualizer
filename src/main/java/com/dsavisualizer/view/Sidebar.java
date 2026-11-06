package com.dsavisualizer.view;

import com.dsavisualizer.algorithms.AlgorithmRegistry;
import com.dsavisualizer.algorithms.SearchAlgorithm;
import com.dsavisualizer.algorithms.SortAlgorithm;
import com.dsavisualizer.structures.DataStructure;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * The left sidebar, used by BOTH the sorting screen and the searching screen.
 *
 * Writing it once here means a new algorithm only has to be added to its
 * AlgorithmRegistry - the sidebar button appears automatically on every screen.
 *
 * create(...) is STATIC: it builds and returns a new VBox each time.
 *
 * Parameters:
 *   selectedName      - display name of the algorithm shown right now (highlighted)
 *   navigator         - lets a screen go back to the dashboard (Main implements it)
 *   currentScreen     - the screen the sidebar sits in; we call its stop() before
 *                       switching, so no timeline keeps running in the background
 */
public final class Sidebar {

    private Sidebar() {
    }

    public static VBox create(String selectedName,
                              Navigator navigator,
                              AppScreen currentScreen) {

        VBox sidebar = new VBox(8);

        sidebar.setPrefWidth(210);

        sidebar.setPadding(new Insets(25, 15, 25, 15));

        sidebar.setStyle(
                "-fx-background-color: #111114;" +
                        "-fx-border-color: #252529;" +
                        "-fx-border-width: 0 1px 0 0;"
        );

        Label appTitle = new Label("DSA VISUAL");

        appTitle.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label sortingLabel = Styles.createSectionLabel("SORTING");
        sortingLabel.setPadding(new Insets(14, 0, 0, 0));

        // All algorithm buttons go into this VBox, which scrolls if the window is
        // too small (so adding more algorithms never breaks the layout)
        VBox list = new VBox(8);

        // Position of the selected button inside the list (-1 = none yet)
        int selectedPosition = -1;

        list.getChildren().add(sortingLabel);

        // One button per sorting algorithm (from the registry)
        for (SortAlgorithm algorithm : AlgorithmRegistry.getSortAlgorithms()) {

            boolean selected = algorithm.getDisplayName().equals(selectedName);

            Button button = Styles.createAlgorithmButton(
                    algorithm.getDisplayName(), selected);

            button.setOnAction(event -> {
                if (!selected) {
                    currentScreen.stop();
                    button.getScene().setRoot(
                            new VisualizerView(algorithm, navigator).createView());
                }
            });

            if (selected) {
                selectedPosition = list.getChildren().size();
            }

            list.getChildren().add(button);
        }

        Label searchingLabel = Styles.createSectionLabel("SEARCHING");
        searchingLabel.setPadding(new Insets(14, 0, 0, 0));

        list.getChildren().add(searchingLabel);

        // One button per searching algorithm (from the registry)
        for (SearchAlgorithm algorithm : AlgorithmRegistry.getSearchAlgorithms()) {

            boolean selected = algorithm.getDisplayName().equals(selectedName);

            Button button = Styles.createAlgorithmButton(
                    algorithm.getDisplayName(), selected);

            button.setOnAction(event -> {
                if (!selected) {
                    currentScreen.stop();
                    button.getScene().setRoot(
                            new SearchView(algorithm, navigator).createView());
                }
            });

            if (selected) {
                selectedPosition = list.getChildren().size();
            }

            list.getChildren().add(button);
        }

        Label structuresLabel = Styles.createSectionLabel("DATA STRUCTURES");
        structuresLabel.setPadding(new Insets(14, 0, 0, 0));

        list.getChildren().add(structuresLabel);

        // One button per data structure (from the registry)
        for (DataStructure structure : AlgorithmRegistry.getDataStructures()) {

            boolean selected = structure.getDisplayName().equals(selectedName);

            Button button = Styles.createAlgorithmButton(
                    structure.getDisplayName(), selected);

            button.setOnAction(event -> {
                if (!selected) {
                    currentScreen.stop();
                    button.getScene().setRoot(
                            new StructureView(structure, navigator).createView());
                }
            });

            if (selected) {
                selectedPosition = list.getChildren().size();
            }

            list.getChildren().add(button);
        }

        // The ScrollPane takes all the free height, which pushes the
        // Menu button to the bottom of the sidebar
        ScrollPane scroll = new ScrollPane(list);

        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: transparent;"
        );

        VBox.setVgrow(scroll, Priority.ALWAYS);

        // Scroll so the selected button is visible: 0.0 = top, 1.0 = bottom
        int total = list.getChildren().size();

        if (selectedPosition >= 0 && total > 1) {
            scroll.setVvalue((double) selectedPosition / (total - 1));
        }

        Button menuButton = Styles.createControlButton("← Menu");
        menuButton.setMaxWidth(Double.MAX_VALUE);

        menuButton.setOnAction(event -> {
            currentScreen.stop();
            menuButton.getScene().setRoot(
                    new AlgorithmSelectionView(navigator).createView());
        });

        sidebar.getChildren().addAll(appTitle, scroll, menuButton);

        return sidebar;
    }
}
