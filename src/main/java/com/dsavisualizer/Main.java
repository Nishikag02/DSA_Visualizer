package com.dsavisualizer;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // Main layout
        BorderPane root = new BorderPane();

        // Header
        Label title = new Label("DSA VISUALIZER");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Learn • Explore • Visualize");
        subtitle.setStyle("-fx-font-size: 14px;");

        VBox header = new VBox(5, title, subtitle);
        header.setAlignment(Pos.CENTER);

        // Button
        Button startButton = new Button("Start Visualizing");

        VBox center = new VBox(20, startButton);
        center.setAlignment(Pos.CENTER);

        // Add components
        root.setTop(header);
        root.setCenter(center);

        // Scene
        Scene scene = new Scene(root, 900, 600);

        // Stage
        stage.setTitle("DSA Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}