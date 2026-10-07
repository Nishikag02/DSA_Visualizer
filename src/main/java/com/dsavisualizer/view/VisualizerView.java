package com.dsavisualizer.view;

import com.dsavisualizer.algorithms.BubbleSort;

import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.util.Duration;

import java.util.List;

public class VisualizerView {

    private List<BubbleSort.Step> steps;

    private int currentStep = 0;

    private final int[] originalArray = {5, 2, 8, 1, 6};

    private HBox arrayBox;
    private VBox codeBox;

    private Label status;
    private Label explanation;
    private Label stepLabel;

    private Slider speedSlider;
    private Button playButton;

    private Timeline timeline;

    private boolean playing = false;
    private boolean animating = false;

    public BorderPane createView() {

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: #0b0b0d;"
        );

        // =========================
        // LEFT SIDEBAR
        // =========================

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

        Label appTitle =
                new Label("DSA VISUAL");

        appTitle.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label sortingTitle =
                new Label("SORTING");

        sortingTitle.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #77777f;"
        );

        Button bubbleButton =
                createAlgorithmButton(
                        "Bubble Sort",
                        true
                );

        Button selectionButton =
                createAlgorithmButton(
                        "Selection Sort",
                        false
                );

        Button insertionButton =
                createAlgorithmButton(
                        "Insertion Sort",
                        false
                );

        Label searchingTitle =
                new Label("SEARCHING");

        searchingTitle.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #77777f;"
        );

        Button linearButton =
                createAlgorithmButton(
                        "Linear Search",
                        false
                );

        Button binaryButton =
                createAlgorithmButton(
                        "Binary Search",
                        false
                );

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

        // =========================
        // CENTER VISUALIZATION
        // =========================

        VBox visualization =
                new VBox(25);

        visualization.setAlignment(
                Pos.CENTER
        );

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

        arrayBox =
                new HBox(14);

        arrayBox.setAlignment(
                Pos.CENTER
        );

        createArrayCards(
                originalArray
        );

        status =
                new Label(
                        "Ready to visualize"
                );

        status.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #d4d4d8;"
        );

        stepLabel =
                new Label(
                        "Step 0"
                );

        stepLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #77777f;"
        );

        visualization.getChildren().addAll(
                algorithmTitle,
                subtitle,
                arrayBox,
                status,
                stepLabel
        );

        // =========================
        // RIGHT CODE PANEL
        // =========================

        VBox codePanel =
                new VBox(15);

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

        codeBox =
                new VBox(4);

        codeBox.setPadding(
                new Insets(15)
        );

        codeBox.setStyle(
                "-fx-background-color: #0d0d0f;" +
                        "-fx-background-radius: 8px;"
        );

        addCodeLine(
                "1",
                "int[] arr = {5, 2, 8, 1, 6};"
        );

        addCodeLine(
                "2",
                "for (int i = 0; i < n - 1; i++) {"
        );

        addCodeLine(
                "3",
                "    for (int j = 0; j < n - i - 1; j++) {"
        );

        addCodeLine(
                "4",
                "        if (arr[j] > arr[j + 1]) {"
        );

        addCodeLine(
                "5",
                "            swap(arr[j], arr[j + 1]);"
        );

        addCodeLine(
                "6",
                "        }"
        );

        addCodeLine(
                "7",
                "    }"
        );

        addCodeLine(
                "8",
                "}"
        );

        ScrollPane codeScroll =
                new ScrollPane(codeBox);

        codeScroll.setFitToWidth(
                true
        );

        codeScroll.setStyle(
                "-fx-background: #0d0d0f;" +
                        "-fx-background-color: #0d0d0f;"
        );

        codePanel.getChildren().addAll(
                codeTitle,
                codeScroll
        );

        // =========================
        // EXPLANATION
        // =========================

        explanation =
                new Label(
                        "Bubble Sort repeatedly compares neighboring elements."
                );

        explanation.setPadding(
                new Insets(18)
        );

        explanation.setMaxWidth(
                Double.MAX_VALUE
        );

        explanation.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #b5b5bd;" +
                        "-fx-background-color: #111114;" +
                        "-fx-border-color: #252529;" +
                        "-fx-border-width: 1px 0 1px 0;"
        );

        // =========================
        // CONTROLS
        // =========================

        Button resetButton =
                createControlButton(
                        "↶ Reset"
                );

        Button previousButton =
                createControlButton(
                        "◀ Previous"
                );

        playButton =
                createControlButton(
                        "▶ Play"
                );

        Button nextButton =
                createControlButton(
                        "Next ▶"
                );

        Label speedText =
                new Label("Speed");

        speedText.setStyle(
                "-fx-text-fill: #9999a1;" +
                        "-fx-font-size: 12px;"
        );

        speedSlider =
                new Slider(
                        200,
                        1500,
                        700
                );

        speedSlider.setPrefWidth(
                120
        );

        HBox controls =
                new HBox(
                        10,
                        resetButton,
                        previousButton,
                        playButton,
                        nextButton,
                        speedText,
                        speedSlider
                );

        controls.setAlignment(
                Pos.CENTER
        );

        controls.setPadding(
                new Insets(15)
        );

        controls.setStyle(
                "-fx-background-color: #111114;"
        );

        // =========================
        // CREATE BUBBLE SORT STEPS
        // =========================

        BubbleSort bubbleSort =
                new BubbleSort();

        steps =
                bubbleSort.getSteps(
                        originalArray
                );

        // =========================
        // BUTTON ACTIONS
        // =========================

        nextButton.setOnAction(event -> {

            if (playing) {
                stopPlaying();
            }

            showNextStep();
        });

        previousButton.setOnAction(event -> {

            if (playing) {
                stopPlaying();
            }

            showPreviousStep();
        });

        resetButton.setOnAction(
                event -> reset()
        );

        playButton.setOnAction(event -> {

            if (playing) {
                stopPlaying();
            } else {
                startPlaying();
            }
        });

        speedSlider.valueProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {

                            if (playing) {
                                restartTimeline();
                            }
                        }
                );

        // =========================
        // ROOT LAYOUT
        // =========================

        root.setLeft(sidebar);

        root.setCenter(
                visualization
        );

        root.setRight(
                codePanel
        );

        VBox bottom =
                new VBox(
                        explanation,
                        controls
                );

        root.setBottom(
                bottom
        );

        return root;
    }

    // =====================================================
    // ARRAY CARDS
    // =====================================================

    private void createArrayCards(
            int[] array
    ) {

        arrayBox.getChildren().clear();

        for (int number : array) {

            Label card =
                    new Label(
                            String.valueOf(number)
                    );

            card.setAlignment(
                    Pos.CENTER
            );

            card.setPrefSize(
                    72,
                    72
            );

            setNormalCardStyle(
                    card
            );

            arrayBox.getChildren().add(
                    card
            );
        }
    }

    // =====================================================
    // UPDATE ARRAY
    // =====================================================

    private void updateArray(
            BubbleSort.Step step
    ) {

        arrayBox.getChildren().clear();

        for (
                int i = 0;
                i < step.array.length;
                i++
        ) {

            Label card =
                    new Label(
                            String.valueOf(
                                    step.array[i]
                            )
                    );

            card.setAlignment(
                    Pos.CENTER
            );

            card.setPrefSize(
                    72,
                    72
            );

            // Final sorted state
            if (
                    step.codeLine == 8
            ) {

                setSortedCardStyle(
                        card
                );

            }

            // Permanently sorted elements
            else if (
                    i >= step.sortedFrom
            ) {

                setSortedCardStyle(
                        card
                );

            }

            // Currently compared elements
            else if (
                    i == step.firstIndex
                            ||
                            i == step.secondIndex
            ) {

                /*
                 * WRONG ORDER / SWAP
                 * -----------------
                 * Red means the two elements
                 * are in the wrong order.
                 */
                if (
                        step.swapped
                                ||
                                step.wrongOrder
                ) {

                    setSwapCardStyle(
                            card
                    );

                }

                /*
                 * NORMAL COMPARISON
                 * -----------------
                 * Yellow means we are checking
                 * these two elements.
                 */
                else {

                    setCompareCardStyle(
                            card
                    );
                }

            }

            // Normal card
            else {

                setNormalCardStyle(
                        card
                );
            }

            arrayBox.getChildren().add(
                    card
            );
        }
    }

    // =====================================================
    // NEXT STEP
    // =====================================================

    private void showNextStep() {

        if (animating) {
            return;
        }

        if (
                currentStep >=
                        steps.size()
        ) {

            stopPlaying();

            return;
        }

        BubbleSort.Step step =
                steps.get(
                        currentStep
                );

        if (step.swapped) {

            animateSwap(
                    step
            );

        } else {

            updateArray(
                    step
            );

            highlightCodeLine(
                    step.codeLine
            );

            updateExplanation(
                    step
            );

            currentStep++;

            updateStepLabel();
        }
    }

    // =====================================================
    // SWAP ANIMATION
    // =====================================================

    private void animateSwap(
            BubbleSort.Step step
    ) {

        animating = true;

        highlightCodeLine(
                step.codeLine
        );

        updateExplanation(
                step
        );

        if (
                step.firstIndex < 0
                        ||
                        step.secondIndex < 0
        ) {

            animating = false;

            return;
        }

        Node first =
                arrayBox.getChildren()
                        .get(
                                step.firstIndex
                        );

        Node second =
                arrayBox.getChildren()
                        .get(
                                step.secondIndex
                        );

        double distance = 86;

        TranslateTransition moveFirst =
                new TranslateTransition(
                        Duration.millis(650),
                        first
                );

        TranslateTransition moveSecond =
                new TranslateTransition(
                        Duration.millis(650),
                        second
                );

        moveFirst.setByX(
                distance
        );

        moveSecond.setByX(
                -distance
        );

        ParallelTransition swapAnimation =
                new ParallelTransition(
                        moveFirst,
                        moveSecond
                );

        swapAnimation.setOnFinished(
                event -> {

                    currentStep++;

                    updateArray(
                            step
                    );

                    updateStepLabel();

                    animating = false;
                }
        );

        swapAnimation.play();
    }

    // =====================================================
    // PREVIOUS STEP
    // =====================================================

    private void showPreviousStep() {

        if (animating) {
            return;
        }

        if (currentStep <= 0) {
            return;
        }

        currentStep--;

        BubbleSort.Step step =
                steps.get(
                        currentStep
                );

        updateArray(
                step
        );

        highlightCodeLine(
                step.codeLine
        );

        updateExplanation(
                step
        );

        updateStepLabel();
    }

    // =====================================================
    // STEP LABEL
    // =====================================================

    private void updateStepLabel() {

        stepLabel.setText(
                "Step " +
                        currentStep +
                        " / " +
                        steps.size()
        );
    }

    // =====================================================
    // PLAY
    // =====================================================

    private void startPlaying() {

        if (
                currentStep >=
                        steps.size()
        ) {

            reset();
        }

        playing = true;

        playButton.setText(
                "⏸ Pause"
        );

        createTimeline();

        timeline.play();
    }

    private void createTimeline() {

        timeline =
                new Timeline(
                        new KeyFrame(
                                Duration.millis(
                                        speedSlider.getValue()
                                ),
                                event ->
                                        showNextStep()
                        )
                );

        timeline.setCycleCount(
                Timeline.INDEFINITE
        );
    }

    private void restartTimeline() {

        if (!playing) {
            return;
        }

        if (timeline != null) {
            timeline.stop();
        }

        createTimeline();

        timeline.play();
    }

    private void stopPlaying() {

        playing = false;

        if (timeline != null) {
            timeline.stop();
        }

        playButton.setText(
                "▶ Play"
        );
    }

    // =====================================================
    // RESET
    // =====================================================

    private void reset() {

        stopPlaying();

        animating = false;

        currentStep = 0;

        createArrayCards(
                originalArray
        );

        highlightCodeLine(
                0
        );

        status.setText(
                "Ready to visualize"
        );

        explanation.setText(
                "Bubble Sort repeatedly compares neighboring elements."
        );

        updateStepLabel();
    }

    // =====================================================
    // EXPLANATION
    // =====================================================

    private void updateExplanation(
            BubbleSort.Step step
    ) {

        if (step.swapped) {

            status.setText(
                    "Swapping elements"
            );

            explanation.setText(
                    "The elements are in the wrong order, " +
                            "so they smoothly exchange positions."
            );

        }

        else if (
                step.firstIndex != -1
                        &&
                        step.secondIndex != -1
        ) {

            status.setText(
                    "Comparing elements"
            );

            int left =
                    step.array[
                            step.firstIndex
                            ];

            int right =
                    step.array[
                            step.secondIndex
                            ];

            if (left > right) {

                explanation.setText(
                        "Comparing " +
                                left +
                                " and " +
                                right +
                                ". " +
                                left +
                                " is larger, so a swap is needed."
                );

            }

            else {

                explanation.setText(
                        "Comparing " +
                                left +
                                " and " +
                                right +
                                ". " +
                                "They are already in the correct order."
                );
            }

        }

        else if (
                step.codeLine == 7
        ) {

            status.setText(
                    "Pass completed"
            );

            explanation.setText(
                    "The largest remaining element " +
                            "has reached its correct position."
            );

        }

        else {

            status.setText(
                    "Sorting complete!"
            );

            explanation.setText(
                    "Bubble Sort has finished. " +
                            "All elements are now sorted."
            );
        }
    }

    // =====================================================
    // CODE HIGHLIGHTING
    // =====================================================

    private void highlightCodeLine(
            int selectedLine
    ) {

        for (
                int i = 0;
                i < codeBox.getChildren().size();
                i++
        ) {

            Label line =
                    (Label)
                            codeBox
                                    .getChildren()
                                    .get(i);

            int lineNumber =
                    i + 1;

            if (
                    lineNumber ==
                            selectedLine
            ) {

                line.setStyle(
                        "-fx-font-family: 'Consolas';" +
                                "-fx-font-size: 13px;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-color: #292930;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-padding: 5px;"
                );

            }

            else {

                line.setStyle(
                        "-fx-font-family: 'Consolas';" +
                                "-fx-font-size: 13px;" +
                                "-fx-text-fill: #b8b8c0;" +
                                "-fx-padding: 5px;"
                );
            }
        }
    }

    // =====================================================
    // CARD STYLES
    // =====================================================

    private void setNormalCardStyle(
            Label card
    ) {

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
    }

    private void setCompareCardStyle(
            Label card
    ) {

        card.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: #4a3d16;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-color: #d6b84c;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;"
        );
    }

    private void setSwapCardStyle(
            Label card
    ) {

        card.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: #4b2028;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-color: #d85b6b;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;"
        );
    }

    private void setSortedCardStyle(
            Label card
    ) {

        card.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: #183c2a;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-color: #4fbd7b;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;"
        );
    }

    // =====================================================
    // SIDEBAR BUTTON
    // =====================================================

    private Button createAlgorithmButton(
            String text,
            boolean selected
    ) {

        Button button =
                new Button(text);

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        if (selected) {

            button.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: #27272c;" +
                            "-fx-background-radius: 7px;" +
                            "-fx-alignment: CENTER_LEFT;" +
                            "-fx-padding: 10px;"
            );

        }

        else {

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

    // =====================================================
    // CONTROL BUTTON
    // =====================================================

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

    // =====================================================
    // CODE LINE
    // =====================================================

    private void addCodeLine(
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

        line.setMaxWidth(
                Double.MAX_VALUE
        );

        line.setStyle(
                "-fx-font-family: 'Consolas';" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #b8b8c0;" +
                        "-fx-padding: 5px;"
        );

        codeBox.getChildren().add(
                line
        );
    }
}