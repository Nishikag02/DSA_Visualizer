package com.dsavisualizer;

import com.dsavisualizer.algorithms.AlgorithmRegistry;
import com.dsavisualizer.view.AlgorithmSelectionView;
import com.dsavisualizer.view.Navigator;
import com.dsavisualizer.view.ScreenSwitcher;
import com.dsavisualizer.view.Styles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The Application class: JavaFX starts here. It creates the Stage (the
 * window) and the Scene, and shows the dashboard first.
 *
 * Main implements the Navigator interface, so the other screens can ask it
 * to show the dashboard again (the "← Back" button).
 */
public class Main extends javafx.application.Application implements Navigator {

    // Window size used for every screen. Change it here, in one place.
    private static final double WINDOW_WIDTH = 1150;
    private static final double WINDOW_HEIGHT = 700;

    // The window cannot be dragged smaller than this, so the three-panel
    // visualizer (sidebar + cards + code panel) never gets squeezed.
    private static final double MIN_WIDTH = 1100;
    private static final double MIN_HEIGHT = 650;

    private Stage stage;

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        showDashboard();

        stage.setTitle("DSA Visualizer");
        stage.show();
    }

    /** Required by the Navigator interface: the screens call this to go back. */
    @Override
    public void showDashboard() {

        Label title = new Label("DSA VISUALIZER");
        title.setStyle(
                "-fx-font-size: 52px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label subtitle = new Label("Learn  •  Explore  •  Visualize");
        subtitle.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-text-fill: #85858d;"
        );

        // Small badges showing what the app contains (the numbers come from the registry)
        HBox badges = new HBox(10,
                createBadge("Sorting", AlgorithmRegistry.getSortAlgorithms().size()),
                createBadge("Searching", AlgorithmRegistry.getSearchAlgorithms().size()),
                createBadge("Data Structures", AlgorithmRegistry.getDataStructures().size()),
                createBadge("Graphs", AlgorithmRegistry.getGraphAlgorithms().size())
        );

        badges.setAlignment(Pos.CENTER);

        Button startButton = Styles.createPrimaryButton("Start Visualizing");

        startButton.setOnAction(event -> showAlgorithmSelection());

        VBox center = new VBox(22, title, subtitle, badges, startButton);

        center.setAlignment(Pos.CENTER);

        // A bit more room above the button
        VBox.setMargin(startButton, new Insets(18, 0, 0, 0));

        Label footer = new Label("UCS553 - Enterprise Web Application   |   Built with Java and JavaFX");
        footer.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #55555c;"
        );

        BorderPane root = new BorderPane();

        root.setStyle("-fx-background-color: #0b0b0d;");

        root.setCenter(center);
        root.setBottom(footer);

        BorderPane.setAlignment(footer, Pos.CENTER);
        BorderPane.setMargin(footer, new Insets(0, 0, 20, 0));

        showScene(root);
    }

    private void showAlgorithmSelection() {

        // "this" is Main, and Main implements Navigator. The menu keeps it and
        // calls navigator.showDashboard() when its Back button is clicked.
        AlgorithmSelectionView view = new AlgorithmSelectionView(this);

        showScene(view.createView());
    }

    /**
     * Puts a screen into the window. The first time, the Scene is created and
     * the stylesheet is added to it. After that, the existing Scene keeps its
     * size (so a resized or maximized window stays that way) and only its root
     * is replaced, with a fade.
     */
    private void showScene(Parent root) {

        if (stage.getScene() == null) {

            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

            String css = Styles.stylesheet();

            if (css != null) {
                scene.getStylesheets().add(css);
            }

            stage.setScene(scene);

        } else {

            ScreenSwitcher.show(stage.getScene(), root);
        }
    }

    /** A small rounded badge such as "Sorting  6". */
    private Label createBadge(String text, int count) {

        Label badge = new Label(text + "  " + count);

        badge.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #b5b5bd;" +
                        "-fx-padding: 6px 12px;" +
                        "-fx-background-color: #151518;" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: #2b2b30;" +
                        "-fx-border-radius: 14px;"
        );

        return badge;
    }

    public static void main(String[] args) {
        launch();
    }
}
