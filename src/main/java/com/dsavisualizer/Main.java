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

    private Stage stage;

    @Override
    public void start(Stage stage) {

        this.stage = stage;

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

        Scene scene = new Scene(root, 900, 600);

        stage.setScene(scene);
    }

    private void showAlgorithmSelection() {

        AlgorithmSelectionView view = new AlgorithmSelectionView();

        VBox layout = view.createView();

        Scene scene = new Scene(layout, 900, 600);

        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}