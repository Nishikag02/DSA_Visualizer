package com.dsavisualizer.view;

import com.dsavisualizer.algorithms.SortAlgorithm;
import com.dsavisualizer.algorithms.SortStep;

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

import javafx.scene.text.TextAlignment;

import javafx.util.Duration;

import java.util.List;

/**
 * ONE shared visualizer for every sorting algorithm.
 *
 * The screen (three panels + explanation bar + controls) is identical for
 * Bubble, Selection and Insertion Sort. The only thing that changes is the
 * SortAlgorithm passed to the constructor, which supplies the title,
 * pseudocode and the List<SortStep> to display.
 *
 * "index" is the position in the steps list of the step currently shown.
 * index == -1 means "nothing shown yet" (initial array).
 */
public class VisualizerView implements AppScreen {

    private static final double CARD_SIZE = 72;
    private static final double CARD_GAP = 14;
    // How far a card moves to go one cell to the right
    private static final double CELL_DISTANCE = CARD_SIZE + CARD_GAP;

    private final SortAlgorithm algorithm;

    private List<SortStep> steps;

    // Index of the step currently displayed (-1 = initial state)
    private int index = -1;

    private final int[] originalArray = {5, 2, 8, 1, 6};

    private BorderPane root;

    private HBox arrayBox;
    private VBox codeBox;

    private Label status;
    private Label explanation;
    private Label stepLabel;

    private Slider speedSlider;
    private Button playButton;

    private Timeline timeline;
    private ParallelTransition moveAnimation;

    private boolean playing = false;
    private boolean animating = false;

    /** Passed along to the sidebar so "← Menu" and "← Back" can reach the dashboard. */
    private final Navigator navigator;

    public VisualizerView(SortAlgorithm algorithm, Navigator navigator) {
        this.algorithm = algorithm;
        this.navigator = navigator;
    }

    @Override
    public BorderPane createView() {

        root = new BorderPane();

        root.setStyle(
                "-fx-background-color: #0b0b0d;"
        );

        // =========================
        // LEFT SIDEBAR
        // =========================

        // The sidebar is shared with the searching screen (see Sidebar.java)
        VBox sidebar = Sidebar.create(
                algorithm.getDisplayName(),
                navigator,
                this
        );

        // =========================
        // CENTER VISUALIZATION
        // =========================

        VBox visualization = new VBox(25);

        visualization.setAlignment(Pos.CENTER);

        visualization.setPadding(new Insets(35));

        Label algorithmTitle =
                new Label(algorithm.getDisplayName().toUpperCase());

        algorithmTitle.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label subtitle = new Label(algorithm.getSubtitle());

        // Long subtitles wrap onto a second line instead of ending in "..."
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(430);
        subtitle.setTextAlignment(TextAlignment.CENTER);

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #85858d;"
        );

        arrayBox = new HBox(CARD_GAP);

        arrayBox.setAlignment(Pos.CENTER);

        createArrayCards(originalArray);

        status = new Label("Ready to visualize");

        status.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #d4d4d8;"
        );

        stepLabel = new Label("Step 0");

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

        // Pseudocode comes from the selected algorithm
        String[] code = algorithm.getCode();

        Styles.fillCodeBox(codeBox, code);

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

        // =========================
        // GENERATE THE STEPS
        // =========================

        // The algorithm engine runs once and returns every snapshot
        steps = algorithm.generateSteps(originalArray);

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

    // =====================================================
    // STOPPING (called by the sidebar before it switches screen)
    // =====================================================

    /** Called by the sidebar before another screen replaces this one. */
    @Override
    public void stop() {
        stopEverything();
    }

    /** Stops the timeline and any running card animation. */
    private void stopEverything() {

        stopPlaying();

        if (moveAnimation != null) {
            moveAnimation.stop();
        }

        animating = false;
    }

    // =====================================================
    // ARRAY CARDS
    // =====================================================

    private void createArrayCards(int[] array) {

        arrayBox.getChildren().clear();

        for (int number : array) {

            Label card = new Label(String.valueOf(number));

            card.setAlignment(Pos.CENTER);

            card.setPrefSize(CARD_SIZE, CARD_SIZE);

            setNormalCardStyle(card);

            arrayBox.getChildren().add(card);
        }
    }

    // =====================================================
    // UPDATE ARRAY (colours every card for one step)
    // =====================================================

    private void updateArray(SortStep step) {

        arrayBox.getChildren().clear();

        for (int i = 0; i < step.array.length; i++) {

            Label card = new Label(String.valueOf(step.array[i]));

            card.setAlignment(Pos.CENTER);

            card.setPrefSize(CARD_SIZE, CARD_SIZE);

            boolean isPair =
                    i == step.activeIndex || i == step.compareIndex;

            // Final sorted state: everything green
            if (step.complete) {
                setSortedCardStyle(card);
            }
            // Key (Insertion) or current minimum (Selection): blue
            else if (i == step.keyIndex) {
                setKeyCardStyle(card);
            }
            // Compared pair in wrong order, or swapping/shifting: red
            else if (isPair
                    && (step.swapping || step.shifting || step.wrongOrder)) {
                setSwapCardStyle(card);
            }
            // Compared pair, no problem: yellow
            else if (isPair) {
                setCompareCardStyle(card);
            }
            // Already sorted area: green
            else if (step.sorted[i]) {
                setSortedCardStyle(card);
            }
            // Outside the range being worked on (Merge / Quick Sort): dim
            else if (step.isOutsideRange(i)) {
                card.setStyle(Styles.cardStyle("#0e0e10", "#2b2b30", "#55555c", 24));
            }
            // Normal card
            else {
                setNormalCardStyle(card);
            }

            arrayBox.getChildren().add(card);
        }
    }

    // =====================================================
    // NEXT STEP
    // =====================================================

    private void showNextStep() {

        if (animating) {
            return;
        }

        if (index + 1 >= steps.size()) {
            stopPlaying();
            return;
        }

        int target = index + 1;

        SortStep step = steps.get(target);

        if (step.needsMoveAnimation()) {

            animateMove(step, target);

        } else {

            index = target;

            showStep(step);

            // Stop automatically after the last step
            if (index == steps.size() - 1) {
                stopPlaying();
            }
        }
    }

    /** Shows one step without animation (used by Next and Previous). */
    private void showStep(SortStep step) {

        updateArray(step);

        highlightCodeLine(step.codeLine);

        status.setText(step.title);

        explanation.setText(step.message);

        updateStepLabel();
    }

    // =====================================================
    // SWAP / SHIFT ANIMATION
    // =====================================================

    /*
     * The two cards slide towards each other's position.
     * The distance depends on how many cells apart they are, so this works
     * for neighbours (Bubble Sort) and far-apart cards (Selection Sort).
     * When the slide ends, the cards are rebuilt from the step's array,
     * which already contains the swapped values.
     */
    private void animateMove(SortStep step, int target) {

        animating = true;

        highlightCodeLine(step.codeLine);

        status.setText(step.title);

        explanation.setText(step.message);

        Node first = arrayBox.getChildren().get(step.activeIndex);
        Node second = arrayBox.getChildren().get(step.compareIndex);

        double distance =
                (step.compareIndex - step.activeIndex) * CELL_DISTANCE;

        // Faster speed setting = faster slide (but never slower than 650 ms)
        double millis =
                Math.max(150, Math.min(650, speedSlider.getValue() * 0.8));

        TranslateTransition moveFirst =
                new TranslateTransition(Duration.millis(millis), first);

        TranslateTransition moveSecond =
                new TranslateTransition(Duration.millis(millis), second);

        moveFirst.setByX(distance);
        moveSecond.setByX(-distance);

        moveAnimation = new ParallelTransition(moveFirst, moveSecond);

        moveAnimation.setOnFinished(event -> {

            index = target;

            updateArray(step);

            updateStepLabel();

            animating = false;
        });

        moveAnimation.play();
    }

    // =====================================================
    // PREVIOUS STEP
    // =====================================================

    private void showPreviousStep() {

        if (animating) {
            return;
        }

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

    // =====================================================
    // STEP LABEL
    // =====================================================

    private void updateStepLabel() {

        stepLabel.setText(
                "Step " + (index + 1) + " / " + steps.size()
        );
    }

    // =====================================================
    // PLAY / PAUSE
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

        stopEverything();

        index = -1;

        showInitialState();
    }

    private void showInitialState() {

        createArrayCards(originalArray);

        highlightCodeLine(0);

        status.setText("Ready to visualize");

        explanation.setText(algorithm.getIntro());

        updateStepLabel();
    }

    // =====================================================
    // CODE HIGHLIGHTING
    // =====================================================

    private void highlightCodeLine(int selectedLine) {

        Styles.highlightCodeLine(codeBox, selectedLine);
    }

    // =====================================================
    // CARD STYLES
    // =====================================================

    private void setCardStyle(Label card, String background, String border) {

        card.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: " + background + ";" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-color: " + border + ";" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;"
        );
    }

    private void setNormalCardStyle(Label card) {
        setCardStyle(card, "#151518", "#55555c");
    }

    private void setCompareCardStyle(Label card) {
        setCardStyle(card, "#4a3d16", "#d6b84c");
    }

    private void setSwapCardStyle(Label card) {
        setCardStyle(card, "#4b2028", "#d85b6b");
    }

    private void setSortedCardStyle(Label card) {
        setCardStyle(card, "#183c2a", "#4fbd7b");
    }

    // Key / current minimum (new colour, blue)
    private void setKeyCardStyle(Label card) {
        setCardStyle(card, "#1b2a4d", "#6b94ff");
    }
}
