package com.dsavisualizer;

import com.dsavisualizer.view.AlgorithmSelectionView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {

    // Window size used for every screen. Change it here, in one place.
    private static final double WINDOW_WIDTH = 1150;
    private static final double WINDOW_HEIGHT = 700;

    // The window cannot be dragged smaller than this, so the three-panel
    // visualizer (sidebar + cards + code panel) never gets squeezed.
    private static final double MIN_WIDTH = 1000;
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

    private void showDashboard() {

        BorderPane root = new BorderPane();

        Label title = new Label("DSA VISUALIZER");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Learn • Explore • Visualize");
        subtitle.setStyle("-fx-font-size: 14px;");

        VBox header = new VBox(5, title, subtitle);
        header.setAlignment(Pos.CENTER);

        Button startButton = new Button("Start Visualizing");

        VBox center = new VBox(20, startButton);
        center.setAlignment(Pos.CENTER);

        root.setTop(header);
        root.setCenter(center);

        startButton.setOnAction(event -> showAlgorithmSelection());

        showScene(root);
    }

    private void showAlgorithmSelection() {

        // this::showDashboard is a METHOD REFERENCE (a short form of a lambda).
        // The menu screen runs it when its Back button is clicked, so the menu
        // can go "back" without knowing anything about Main.
        AlgorithmSelectionView view = new AlgorithmSelectionView(this::showDashboard);

        VBox layout = view.createView();

        showScene(layout);
    }

    /**
     * Puts a screen into the window. If the window is already open, it keeps
     * its current size (so a resized or maximized window stays that way).
     */
    private void showScene(javafx.scene.Parent root) {

        if (stage.getScene() == null) {
            stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        } else {
            stage.getScene().setRoot(root);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
