package com.dsavisualizer.view;

import com.dsavisualizer.algorithms.AlgorithmRegistry;
import com.dsavisualizer.algorithms.GraphAlgorithm;
import com.dsavisualizer.algorithms.SearchAlgorithm;
import com.dsavisualizer.algorithms.SortAlgorithm;
import com.dsavisualizer.structures.DataStructure;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The "Choose a visualization" menu. It implements the AppScreen interface,
 * like the sorting, searching, data structure and graph screens.
 *
 * The menu has four CARDS side by side (an HBox of VBoxes): Sorting,
 * Searching, Data Structures and Graphs. The buttons are created in loops
 * over the lists in AlgorithmRegistry, so a new algorithm or data structure
 * shows up here automatically.
 */
public class AlgorithmSelectionView implements AppScreen {

    /** Lets the Back button return to the dashboard (Main implements it). */
    private final Navigator navigator;

    public AlgorithmSelectionView(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public VBox createView() {

        // Title and subtitle
        Label title = new Label("Choose a visualization");
        title.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label subtitle = new Label("Pick an algorithm or data structure and watch it step by step");
        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #85858d;"
        );

        // ----- Card 1: sorting -----
        // All sorting buttons open the SAME screen (VisualizerView);
        // only the SortAlgorithm object is different.
        VBox sortingCard = createCard("SORTING");

        for (SortAlgorithm algorithm : AlgorithmRegistry.getSortAlgorithms()) {

            Button button = createMenuButton(algorithm.getDisplayName());

            button.setOnAction(event -> ScreenSwitcher.show(button.getScene(),
                    new VisualizerView(algorithm, navigator).createView()));

            sortingCard.getChildren().add(button);
        }

        // ----- Card 2: searching (all open SearchView) -----
        VBox searchingCard = createCard("SEARCHING");

        for (SearchAlgorithm algorithm : AlgorithmRegistry.getSearchAlgorithms()) {

            Button button = createMenuButton(algorithm.getDisplayName());

            button.setOnAction(event -> ScreenSwitcher.show(button.getScene(),
                    new SearchView(algorithm, navigator).createView()));

            searchingCard.getChildren().add(button);
        }

        // ----- Card 3: data structures (all open StructureView) -----
        VBox structuresCard = createCard("DATA STRUCTURES");

        for (DataStructure structure : AlgorithmRegistry.getDataStructures()) {

            Button button = createMenuButton(structure.getDisplayName());

            button.setOnAction(event -> ScreenSwitcher.show(button.getScene(),
                    new StructureView(structure, navigator).createView()));

            structuresCard.getChildren().add(button);
        }

        // ----- Card 4: graphs (all open GraphView) -----
        VBox graphsCard = createCard("GRAPHS");

        for (GraphAlgorithm algorithm : AlgorithmRegistry.getGraphAlgorithms()) {

            Button button = createMenuButton(algorithm.getDisplayName());

            button.setOnAction(event -> ScreenSwitcher.show(button.getScene(),
                    new GraphView(algorithm, navigator).createView()));

            graphsCard.getChildren().add(button);
        }

        // The four cards side by side
        HBox cards = new HBox(20, sortingCard, searchingCard, structuresCard, graphsCard);

        cards.setAlignment(Pos.TOP_CENTER);

        // Back button: asks the Navigator (Main) to show the dashboard
        Button backButton = Styles.createControlButton("← Back");

        backButton.setOnAction(event -> navigator.showDashboard());

        VBox layout = new VBox(8, title, subtitle, cards, backButton);

        layout.setAlignment(Pos.CENTER);

        layout.setStyle("-fx-background-color: #0b0b0d;");

        // A little extra space around the cards and the Back button
        VBox.setMargin(cards, new Insets(28, 0, 28, 0));

        return layout;
    }

    /** A card: a dark rounded panel with a small heading and the buttons below it. */
    private VBox createCard(String heading) {

        VBox card = new VBox(10, Styles.createSectionLabel(heading));

        card.setPrefWidth(225);

        card.setPadding(new Insets(20));

        card.setStyle(
                "-fx-background-color: #111114;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: #252529;" +
                        "-fx-border-radius: 12px;"
        );

        return card;
    }

    /** A button that fills the width of its card. */
    private Button createMenuButton(String text) {

        Button button = Styles.createControlButton(text);

        button.setMaxWidth(Double.MAX_VALUE);

        return button;
    }

    /** The menu has no timeline or animation, so there is nothing to stop. */
    @Override
    public void stop() {
    }
}
