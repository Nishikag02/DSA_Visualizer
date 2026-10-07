package com.dsavisualizer.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VisualizerView {

    public BorderPane createView() {

        // =====================================================
        // MAIN ROOT
        // =====================================================

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: #0b0b0d;"
        );

        // =====================================================
        // LEFT SIDEBAR
        // =====================================================

        VBox sidebar = new VBox(18);

        sidebar.setPrefWidth(210);

        sidebar.setPadding(
                new Insets(25, 15, 25, 15)
        );

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

        Label sortingTitle = new Label("SORTING");

        sortingTitle.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #77777f;"
        );

        Button bubbleButton =
                createAlgorithmButton("Bubble Sort", true);

        Button selectionButton =
                createAlgorithmButton("Selection Sort", false);

        Button insertionButton =
                createAlgorithmButton("Insertion Sort", false);

        Label searchingTitle = new Label("SEARCHING");

        searchingTitle.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #77777f;"
        );

        Button linearButton =
                createAlgorithmButton("Linear Search", false);

        Button binaryButton =
                createAlgorithmButton("Binary Search", false);

        sidebar.getChildren().addAll(
                appTitle,
                sortingTitle,
                bubbleButton,
                selectionButton,
                insertionButton,
                searchingTitle,
                linearButton,
                binaryButton
        );

        // =====================================================
        // CENTER VISUALIZATION
        // =====================================================

        VBox visualization = new VBox(25);

        visualization.setAlignment(Pos.CENTER);

        visualization.setPadding(
                new Insets(35)
        );

        Label algorithmTitle =
                new Label("BUBBLE SORT");

        algorithmTitle.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label subtitle =
                new Label(
                        "Compare adjacent elements and move larger values to the end"
                );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #85858d;"
        );

        HBox arrayBox = new HBox(14);

        arrayBox.setAlignment(Pos.CENTER);

        int[] array = {5, 2, 8, 1, 6};

        for (int number : array) {

            Label card =
                    new Label(String.valueOf(number));

            card.setAlignment(Pos.CENTER);

            card.setPrefSize(72, 72);

            card.setStyle(
                    "-fx-font-size: 24px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: #151518;" +
                            "-fx-background-radius: 10px;" +
                            "-fx-border-color: #55555c;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 10px;"
            );

            arrayBox.getChildren().add(card);
        }

        Label status =
                new Label("Ready to visualize");

        status.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #d4d4d8;"
        );

        visualization.getChildren().addAll(
                algorithmTitle,
                subtitle,
                arrayBox,
                status
        );

        // =====================================================
        // RIGHT CODE PANEL
        // =====================================================

        VBox codePanel = new VBox(15);

        codePanel.setPrefWidth(430);

        codePanel.setPadding(
                new Insets(25)
        );

        codePanel.setStyle(
                "-fx-background-color: #111114;" +
                        "-fx-border-color: #252529;" +
                        "-fx-border-width: 0 0 0 1px;"
        );

        Label codeTitle =
                new Label("CODE");

        codeTitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #888891;"
        );

        VBox codeBox =
                new VBox(4);

        codeBox.setPadding(
                new Insets(15)
        );

        codeBox.setStyle(
                "-fx-background-color: #0d0d0f;" +
                        "-fx-background-radius: 8px;"
        );

        addCodeLine(
                codeBox,
                "1",
                "int[] arr = {5, 2, 8, 1, 6};"
        );

        addCodeLine(
                codeBox,
                "2",
                "for (int i = 0; i < n - 1; i++) {"
        );

        addCodeLine(
                codeBox,
                "3",
                "    for (int j = 0; j < n - i - 1; j++) {"
        );

        addCodeLine(
                codeBox,
                "4",
                "        if (arr[j] > arr[j + 1]) {"
        );

        addCodeLine(
                codeBox,
                "5",
                "            swap(arr[j], arr[j + 1]);"
        );

        addCodeLine(
                codeBox,
                "6",
                "        }"
        );

        addCodeLine(
                codeBox,
                "7",
                "    }"
        );

        addCodeLine(
                codeBox,
                "8",
                "}"
        );

        ScrollPane codeScroll =
                new ScrollPane(codeBox);

        codeScroll.setFitToWidth(true);

        codeScroll.setStyle(
                "-fx-background: #0d0d0f;" +
                        "-fx-background-color: #0d0d0f;"
        );

        codePanel.getChildren().addAll(
                codeTitle,
                codeScroll
        );

        // =====================================================
        // EXPLANATION
        // =====================================================

        Label explanation =
                new Label(
                        "Bubble Sort repeatedly compares neighboring elements."
                );

        explanation.setPadding(
                new Insets(18)
        );

        explanation.setMaxWidth(Double.MAX_VALUE);

        explanation.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #b5b5bd;" +
                        "-fx-background-color: #111114;" +
                        "-fx-border-color: #252529;" +
                        "-fx-border-width: 1px 0 1px 0;"
        );

        // =====================================================
        // CONTROLS
        // =====================================================

        Button resetButton =
                createControlButton("↶ Reset");

        Button previousButton =
                createControlButton("◀ Previous");

        Button playButton =
                createControlButton("▶ Play");

        Button nextButton =
                createControlButton("Next ▶");

        HBox controls =
                new HBox(
                        10,
                        resetButton,
                        previousButton,
                        playButton,
                        nextButton
                );

        controls.setAlignment(Pos.CENTER);

        controls.setPadding(
                new Insets(15)
        );

        controls.setStyle(
                "-fx-background-color: #111114;"
        );

        // =====================================================
        // ROOT LAYOUT
        // =====================================================

        root.setLeft(sidebar);
        root.setCenter(visualization);
        root.setRight(codePanel);

        VBox bottom =
                new VBox(
                        explanation,
                        controls
                );

        root.setBottom(bottom);

        return root;
    }

    // =========================================================
    // ALGORITHM BUTTON
    // =========================================================

    private Button createAlgorithmButton(
            String text,
            boolean selected
    ) {

        Button button =
                new Button(text);

        button.setMaxWidth(Double.MAX_VALUE);

        if (selected) {

            button.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: #27272c;" +
                            "-fx-background-radius: 7px;" +
                            "-fx-alignment: CENTER_LEFT;" +
                            "-fx-padding: 10px;"
            );

        } else {

            button.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: #9999a1;" +
                            "-fx-background-color: transparent;" +
                            "-fx-alignment: CENTER_LEFT;" +
                            "-fx-padding: 10px;"
            );
        }

        return button;
    }

    // =========================================================
    // CONTROL BUTTON
    // =========================================================

    private Button createControlButton(
            String text
    ) {

        Button button =
                new Button(text);

        button.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #d4d4d8;" +
                        "-fx-background-color: #1a1a1e;" +
                        "-fx-border-color: #35353b;" +
                        "-fx-border-radius: 7px;" +
                        "-fx-background-radius: 7px;" +
                        "-fx-padding: 9px 14px;"
        );

        return button;
    }

    // =========================================================
    // CODE LINE
    // =========================================================

    private void addCodeLine(
            VBox codeBox,
            String number,
            String code
    ) {

        Label line =
                new Label(
                        String.format(
                                "%2s    %s",
                                number,
                                code
                        )
                );

        line.setStyle(
                "-fx-font-family: 'Consolas';" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #b8b8c0;"
        );

        codeBox.getChildren().add(line);
    }
}