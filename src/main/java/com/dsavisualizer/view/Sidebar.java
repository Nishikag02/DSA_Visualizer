package com.dsavisualizer.view;

import com.dsavisualizer.algorithms.AlgorithmRegistry;
import com.dsavisualizer.algorithms.SearchAlgorithm;
import com.dsavisualizer.algorithms.SortAlgorithm;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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

        VBox sidebar = new VBox(18);

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

        sidebar.getChildren().addAll(appTitle, Styles.createSectionLabel("SORTING"));

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

            sidebar.getChildren().add(button);
        }

        sidebar.getChildren().add(Styles.createSectionLabel("SEARCHING"));

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

            sidebar.getChildren().add(button);
        }

        // Pushes the Menu button to the bottom of the sidebar
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button menuButton = Styles.createControlButton("← Menu");
        menuButton.setMaxWidth(Double.MAX_VALUE);

        menuButton.setOnAction(event -> {
            currentScreen.stop();
            menuButton.getScene().setRoot(
                    new AlgorithmSelectionView(navigator).createView());
        });

        sidebar.getChildren().addAll(spacer, menuButton);

        return sidebar;
    }
}
