package com.dsavisualizer.view;

import com.dsavisualizer.algorithms.SearchAlgorithm;
import com.dsavisualizer.algorithms.SearchStep;
import com.dsavisualizer.util.InputParser;
import com.dsavisualizer.util.InvalidInputException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.util.Duration;

import java.util.List;

/**
 * ONE shared visualizer for every searching algorithm (Linear, Binary, ...).
 *
 * It has the same three-panel look as VisualizerView (sorting), but searching
 * never swaps cards, so there is no slide animation - the cards only change
 * colour. The user can type their own array and target in two TextFields.
 *
 * "index" is the position in the steps list of the step currently shown.
 * index == -1 means "nothing shown yet" (initial array).
 */
public class SearchView implements AppScreen {

    private static final double CARD_SIZE = 56;
    private static final double CARD_GAP = 8;

    private final SearchAlgorithm algorithm;

    // Passed on to the sidebar so "← Menu" -> "← Back" can reach the dashboard
    private final Navigator navigator;

    // The numbers and the value we are searching for
    private int[] array;
    private int target;

    private List<SearchStep> steps;

    // Index of the step currently displayed (-1 = initial state)
    private int index = -1;

    private BorderPane root;

    private HBox cellBox;
    private VBox codeBox;

    private Label status;
    private Label explanation;
    private Label stepLabel;

    private TextField arrayField;
    private TextField targetField;

    private Slider speedSlider;
    private Button playButton;

    private Timeline timeline;

    private boolean playing = false;

    public SearchView(SearchAlgorithm algorithm, Navigator navigator) {

        this.algorithm = algorithm;
        this.navigator = navigator;

        // Start with the algorithm's example numbers
        this.array = algorithm.getDefaultArray();
        this.target = algorithm.getDefaultTarget();

        // The engine runs once and returns every snapshot
        this.steps = algorithm.generateSteps(array, target);
    }

    @Override
    public BorderPane createView() {

        root = new BorderPane();

        root.setStyle("-fx-background-color: #0b0b0d;");

        // =========================
        // LEFT SIDEBAR (shared)
        // =========================

        VBox sidebar = Sidebar.create(
                algorithm.getDisplayName(),
                navigator,
                this
        );

        // =========================
        // CENTER VISUALIZATION
        // =========================

        VBox visualization = new VBox(20);

        visualization.setAlignment(Pos.CENTER);

        visualization.setPadding(new Insets(20));

        Label algorithmTitle = new Label(algorithm.getDisplayName().toUpperCase());

        algorithmTitle.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label subtitle = new Label(algorithm.getSubtitle());

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #85858d;"
        );

        // ----- input row: array + target + Apply -----

        arrayField = createTextField(InputParser.arrayToText(array), 170);
        targetField = createTextField(String.valueOf(target), 60);

        Button applyButton = Styles.createControlButton("Apply");

        HBox inputRow = new HBox(
                10,
                createSmallLabel("Array"),
                arrayField,
                createSmallLabel("Target"),
                targetField,
                applyButton
        );

        inputRow.setAlignment(Pos.CENTER);

        Label hint = createSmallLabel(
                algorithm.needsSortedArray()
                        ? "1 to 7 numbers (0-99). Binary Search sorts them for you."
                        : "1 to 7 numbers (0-99), separated by commas."
        );

        cellBox = new HBox(CARD_GAP);

        cellBox.setAlignment(Pos.CENTER);

        updateCells(null);

        status = new Label("Ready to visualize");

        status.setStyle(statusStyle("#d4d4d8"));

        stepLabel = createSmallLabel("Step 0");

        Label legend = createSmallLabel(algorithm.getLegend());

        visualization.getChildren().addAll(
                algorithmTitle,
                subtitle,
                inputRow,
                hint,
                cellBox,
                status,
                stepLabel,
                legend
        );

        // =========================
        // RIGHT CODE PANEL
        // =========================

        VBox codePanel = new VBox(15);

        codePanel.setPrefWidth(430);

        codePanel.setPadding(new Insets(25));

        codePanel.setStyle(
                "-fx-background-color: #111114;" +
                        "-fx-border-color: #252529;" +
                        "-fx-border-width: 0 0 0 1px;"
        );

        Label codeTitle = new Label("CODE");

        codeTitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #888891;"
        );

        codeBox = new VBox(4);

        codeBox.setPadding(new Insets(15));

        codeBox.setStyle(
                "-fx-background-color: #0d0d0f;" +
                        "-fx-background-radius: 8px;"
        );

        Styles.fillCodeBox(codeBox, algorithm.getCode());

        ScrollPane codeScroll = new ScrollPane(codeBox);

        codeScroll.setFitToWidth(true);

        codeScroll.setStyle(
                "-fx-background: #0d0d0f;" +
                        "-fx-background-color: #0d0d0f;"
        );

        codePanel.getChildren().addAll(codeTitle, codeScroll);

        // =========================
        // EXPLANATION
        // =========================

        explanation = new Label(algorithm.getIntro());

        explanation.setPadding(new Insets(18));

        explanation.setMaxWidth(Double.MAX_VALUE);

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

        Button resetButton = Styles.createControlButton("↶ Reset");
        Button previousButton = Styles.createControlButton("◀ Previous");
        playButton = Styles.createControlButton("▶ Play");
        Button nextButton = Styles.createControlButton("Next ▶");

        Label speedText = new Label("Speed");

        speedText.setStyle(
                "-fx-text-fill: #9999a1;" +
                        "-fx-font-size: 12px;"
        );

        speedSlider = new Slider(200, 1500, 700);

        speedSlider.setPrefWidth(120);

        HBox controls = new HBox(
                10,
                resetButton,
                previousButton,
                playButton,
                nextButton,
                speedText,
                speedSlider
        );

        controls.setAlignment(Pos.CENTER);

        controls.setPadding(new Insets(15));

        controls.setStyle("-fx-background-color: #111114;");

        updateStepLabel();

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

        resetButton.setOnAction(event -> reset());

        playButton.setOnAction(event -> {
            if (playing) {
                stopPlaying();
            } else {
                startPlaying();
            }
        });

        // Apply button, and pressing Enter inside either text field
        applyButton.setOnAction(event -> applyInput());
        arrayField.setOnAction(event -> applyInput());
        targetField.setOnAction(event -> applyInput());

        // Changing the speed while playing restarts the timeline
        speedSlider.valueProperty().addListener(
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
        root.setCenter(visualization);
        root.setRight(codePanel);

        VBox bottom = new VBox(explanation, controls);

        root.setBottom(bottom);

        return root;
    }

    /** Called by the sidebar before another screen replaces this one. */
    @Override
    public void stop() {
        stopPlaying();
    }

    // =====================================================
    // USER INPUT (validation with exceptions + Alert)
    // =====================================================

    /** Reads both text fields, checks them, and restarts the visualization. */
    private void applyInput() {

        try {

            // These two lines may throw InvalidInputException
            int[] typed = InputParser.parseArray(arrayField.getText());
            int newTarget = InputParser.parseTarget(targetField.getText());

            stopPlaying();

            // Binary Search sorts the numbers; Linear Search keeps the order
            array = algorithm.prepareArray(typed);
            target = newTarget;

            arrayField.setText(InputParser.arrayToText(array));

            steps = algorithm.generateSteps(array, target);

            reset();

            if (!sameValues(typed, array)) {
                explanation.setText(
                        "Binary Search needs a sorted array, so the numbers were sorted for you.");
            }

        } catch (InvalidInputException e) {

            // Bad input: tell the user with a dialog; nothing else changes
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Invalid input");
            alert.setHeaderText("Please check your input");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    /** True if both arrays hold the same numbers in the same order. */
    private boolean sameValues(int[] first, int[] second) {

        if (first.length != second.length) {
            return false;
        }

        for (int i = 0; i < first.length; i++) {
            if (first[i] != second[i]) {
                return false;
            }
        }

        return true;
    }

    // =====================================================
    // CARDS (one column per number: pointer, card, index)
    // =====================================================

    /** Rebuilds all cards. step == null means "plain cards, nothing special". */
    private void updateCells(SearchStep step) {

        cellBox.getChildren().clear();

        for (int i = 0; i < array.length; i++) {

            Label pointer = new Label(pointerText(step, i));

            pointer.setPrefHeight(18);

            pointer.setStyle(
                    "-fx-font-size: 12px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #d6b84c;"
            );

            Label card = new Label(String.valueOf(array[i]));

            card.setAlignment(Pos.CENTER);

            card.setPrefSize(CARD_SIZE, CARD_SIZE);

            card.setStyle(cardStyleFor(step, i));

            Label indexLabel = createSmallLabel(String.valueOf(i));

            VBox cell = new VBox(5, pointer, card, indexLabel);

            cell.setAlignment(Pos.CENTER);

            cell.setPrefWidth(CARD_SIZE);

            cellBox.getChildren().add(cell);
        }
    }

    /** Text above a card: i for Linear Search, L / M / H for Binary Search. */
    private String pointerText(SearchStep step, int i) {

        if (step == null) {
            return "";
        }

        StringBuilder text = new StringBuilder();

        if (i == step.currentIndex) {
            addPointer(text, "i");
        }
        if (i == step.low) {
            addPointer(text, "L");
        }
        if (i == step.mid) {
            addPointer(text, "M");
        }
        if (i == step.high) {
            addPointer(text, "H");
        }

        return text.toString();
    }

    private void addPointer(StringBuilder text, String letter) {

        if (text.length() > 0) {
            text.append("/");
        }

        text.append(letter);
    }

    /** Colour of card i for one step (first matching rule wins). */
    private String cardStyleFor(SearchStep step, int i) {

        // Plain card (no step shown yet)
        if (step == null) {
            return Styles.cardStyle("#151518", "#55555c", "white", 20);
        }

        // Target found: green
        if (i == step.foundIndex) {
            return Styles.cardStyle("#183c2a", "#4fbd7b", "white", 20);
        }

        // Target not found: every card red
        if (step.notFound) {
            return Styles.cardStyle("#4b2028", "#d85b6b", "white", 20);
        }

        // Card being checked (current / mid): yellow
        if (i == step.currentIndex || i == step.mid) {
            return Styles.cardStyle("#4a3d16", "#d6b84c", "white", 20);
        }

        // Already checked / outside the range: dim
        if (step.eliminated[i]) {
            return Styles.cardStyle("#0e0e10", "#2b2b30", "#55555c", 20);
        }

        // Still part of the search: normal
        return Styles.cardStyle("#151518", "#55555c", "white", 20);
    }

    // =====================================================
    // NEXT / PREVIOUS
    // =====================================================

    private void showNextStep() {

        if (index + 1 >= steps.size()) {
            stopPlaying();
            return;
        }

        index++;

        showStep(steps.get(index));

        // Stop automatically after the last step
        if (index == steps.size() - 1) {
            stopPlaying();
        }
    }

    private void showPreviousStep() {

        if (index < 0) {
            return;
        }

        index--;

        if (index < 0) {
            showInitialState();
            return;
        }

        showStep(steps.get(index));
    }

    /** Shows one step: cards, highlighted code line, title and explanation. */
    private void showStep(SearchStep step) {

        updateCells(step);

        Styles.highlightCodeLine(codeBox, step.codeLine);

        status.setText(step.title);

        // Green text when found, red when not found
        if (step.foundIndex >= 0) {
            status.setStyle(statusStyle("#4fbd7b"));
        } else if (step.notFound) {
            status.setStyle(statusStyle("#d85b6b"));
        } else {
            status.setStyle(statusStyle("#d4d4d8"));
        }

        explanation.setText(step.message);

        updateStepLabel();
    }

    private void updateStepLabel() {

        stepLabel.setText("Step " + (index + 1) + " / " + steps.size());
    }

    // =====================================================
    // PLAY / PAUSE (Timeline)
    // =====================================================

    private void startPlaying() {

        if (index >= steps.size() - 1) {
            reset();
        }

        playing = true;

        playButton.setText("⏸ Pause");

        createTimeline();

        timeline.play();
    }

    private void createTimeline() {

        timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(speedSlider.getValue()),
                        event -> showNextStep()
                )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
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

        playButton.setText("▶ Play");
    }

    // =====================================================
    // RESET
    // =====================================================

    private void reset() {

        stopPlaying();

        index = -1;

        showInitialState();
    }

    private void showInitialState() {

        updateCells(null);

        Styles.highlightCodeLine(codeBox, 0);

        status.setText("Ready to visualize");

        status.setStyle(statusStyle("#d4d4d8"));

        explanation.setText(algorithm.getIntro());

        updateStepLabel();
    }

    // =====================================================
    // SMALL HELPERS
    // =====================================================

    private String statusStyle(String colour) {

        return "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + colour + ";";
    }

    private Label createSmallLabel(String text) {

        Label label = new Label(text);

        label.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #77777f;"
        );

        return label;
    }

    private TextField createTextField(String text, double width) {

        TextField field = new TextField(text);

        field.setPrefWidth(width);

        field.setStyle(
                "-fx-background-color: #151518;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: #35353b;" +
                        "-fx-border-radius: 6px;" +
                        "-fx-background-radius: 6px;"
        );

        return field;
    }
}
