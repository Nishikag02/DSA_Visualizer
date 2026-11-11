package com.dsavisualizer.view;

import com.dsavisualizer.structures.DataStructure;
import com.dsavisualizer.structures.StructureException;
import com.dsavisualizer.structures.StructureStep;
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

import javafx.scene.text.TextAlignment;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * ONE shared screen for every data structure (Stack, Queue, Linked List).
 *
 * The sorting and searching screens are played with Next / Previous. A data
 * structure is different: the user presses an OPERATION button (Push, Pop,
 * Insert, ...), the structure changes, and the steps of that operation are
 * played automatically with a Timeline, one KeyFrame per step.
 *
 * The DataStructure object keeps its items between button clicks. This class
 * only DRAWS the StructureStep snapshots it gets back.
 */
public class StructureView implements AppScreen {

    private final DataStructure structure;
    private final Navigator navigator;

    private BorderPane root;

    private VBox visualBox;
    private VBox codeBox;

    private Label status;
    private Label explanation;

    private TextField valueField;
    private Slider speedSlider;

    // The operation buttons are kept in a list so they can be disabled while playing
    private final List<Button> operationButtons = new ArrayList<>();
    private Button resetButton;

    private Timeline timeline;

    public StructureView(DataStructure structure, Navigator navigator) {
        this.structure = structure;
        this.navigator = navigator;
    }

    @Override
    public BorderPane createView() {

        root = new BorderPane();

        root.setStyle("-fx-background-color: #0b0b0d;");

        // =========================
        // LEFT SIDEBAR (shared)
        // =========================

        VBox sidebar = Sidebar.create(
                structure.getDisplayName(),
                navigator,
                this
        );

        // =========================
        // CENTER
        // =========================

        VBox center = new VBox(14);

        center.setAlignment(Pos.CENTER);

        center.setPadding(new Insets(20));

        Label title = new Label(structure.getDisplayName().toUpperCase());

        title.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label subtitle = new Label(structure.getSubtitle());

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #85858d;"
        );

        subtitle.setWrapText(true);
        subtitle.setMaxWidth(430);
        subtitle.setTextAlignment(TextAlignment.CENTER);
        subtitle.setAlignment(Pos.CENTER);

        // The structure is drawn inside this box (it is rebuilt for every step)
        visualBox = new VBox();

        visualBox.setAlignment(Pos.CENTER);

        visualBox.setMinHeight(240);

        status = new Label("Ready");

        status.setStyle(statusStyle("#d4d4d8"));

        // ----- value box + operation buttons -----

        valueField = new TextField();

        valueField.setPromptText("0-99");
        valueField.setPrefWidth(70);

        valueField.setStyle(
                "-fx-background-color: #151518;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: #35353b;" +
                        "-fx-border-radius: 6px;" +
                        "-fx-background-radius: 6px;"
        );

        HBox valueRow = new HBox(10, createSmallLabel("Value"), valueField);

        valueRow.setAlignment(Pos.CENTER);

        HBox operationRow = new HBox(10);

        operationRow.setAlignment(Pos.CENTER);

        String[] names = structure.getOperationNames();

        for (int i = 0; i < names.length; i++) {

            // The lambda needs a final copy of the loop variable
            int operation = i;

            Button button = Styles.createControlButton(names[i]);

            button.setOnAction(event -> runOperation(operation));

            operationButtons.add(button);

            operationRow.getChildren().add(button);
        }

        Label hint = createSmallLabel(
                "The Value box is used by operations that need a number.");

        center.getChildren().addAll(
                title,
                subtitle,
                visualBox,
                status,
                valueRow,
                operationRow,
                hint
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

        // Before any click, show the code of the first operation (Push / Enqueue / Insert Head)
        showCode(0);

        ScrollPane codeScroll = new ScrollPane(codeBox);

        codeScroll.setFitToWidth(true);

        codeScroll.setStyle(
                "-fx-background: #0d0d0f;" +
                        "-fx-background-color: #0d0d0f;"
        );

        codePanel.getChildren().addAll(codeTitle, codeScroll);

        // =========================
        // EXPLANATION + CONTROLS
        // =========================

        explanation = new Label(structure.getIntro());

        explanation.setPadding(new Insets(18));

        explanation.setMaxWidth(Double.MAX_VALUE);

        explanation.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #b5b5bd;" +
                        "-fx-background-color: #111114;" +
                        "-fx-border-color: #252529;" +
                        "-fx-border-width: 1px 0 1px 0;"
        );

        resetButton = Styles.createControlButton("↶ Reset");

        resetButton.setOnAction(event -> reset());

        Label speedText = new Label("Speed");

        speedText.setStyle(
                "-fx-text-fill: #9999a1;" +
                        "-fx-font-size: 12px;"
        );

        // A higher value = a longer pause between steps (slower)
        speedSlider = new Slider(300, 1500, 800);

        speedSlider.setPrefWidth(120);

        HBox controls = new HBox(10, resetButton, speedText, speedSlider);

        controls.setAlignment(Pos.CENTER);

        controls.setPadding(new Insets(15));

        controls.setStyle("-fx-background-color: #111114;");

        // =========================
        // ROOT LAYOUT
        // =========================

        root.setLeft(sidebar);
        root.setCenter(center);
        root.setRight(codePanel);
        root.setBottom(new VBox(explanation, controls));

        // Draw the starting items
        drawStructure(structure.snapshot("Ready", structure.getIntro()));

        return root;
    }

    /** Called by the sidebar before another screen replaces this one. */
    @Override
    public void stop() {

        if (timeline != null) {
            timeline.stop();
        }
    }

    // =====================================================
    // RUNNING AN OPERATION
    // =====================================================

    /** Called when an operation button is clicked. */
    private void runOperation(int operation) {

        int value = 0;

        try {

            // Only operations that need a number read the Value box
            if (structure.needsValue(operation)) {
                value = InputParser.parseValue(valueField.getText());
            }

            List<StructureStep> steps = structure.execute(operation, value);

            showCode(operation);

            playSteps(steps);

        } catch (InvalidInputException e) {

            // The Value box is wrong: tell the user, nothing changes
            showAlert(Alert.AlertType.ERROR, "Invalid input", e.getMessage());

        } catch (StructureException e) {

            // The operation is not allowed (overflow, underflow, ...)
            status.setText("Operation not possible");
            status.setStyle(statusStyle("#d85b6b"));
            explanation.setText(e.getMessage());

            showAlert(Alert.AlertType.WARNING, "Operation not possible", e.getMessage());
        }
    }

    /** Plays the steps one after another: one KeyFrame per step. */
    private void playSteps(List<StructureStep> steps) {

        stop();

        setBusy(true);

        double pause = speedSlider.getValue();

        timeline = new Timeline();

        for (int i = 0; i < steps.size(); i++) {

            StructureStep step = steps.get(i);

            // Step i is shown after i * pause milliseconds (the first one at once)
            timeline.getKeyFrames().add(
                    new KeyFrame(
                            Duration.millis(pause * i),
                            event -> showStep(step)
                    )
            );
        }

        // After the last step, the buttons work again
        timeline.getKeyFrames().add(
                new KeyFrame(
                        Duration.millis(pause * (steps.size() - 1) + 300),
                        event -> setBusy(false)
                )
        );

        timeline.play();
    }

    /** Shows one step: drawing, highlighted code line, title and explanation. */
    private void showStep(StructureStep step) {

        drawStructure(step);

        Styles.highlightCodeLine(codeBox, step.codeLine);

        status.setText(step.title);
        status.setStyle(statusStyle("#d4d4d8"));

        explanation.setText(step.message);
    }

    private void setBusy(boolean busy) {

        for (Button button : operationButtons) {
            button.setDisable(busy);
        }
    }

    private void reset() {

        stop();

        structure.reset();

        setBusy(false);

        showCode(0);

        drawStructure(structure.snapshot("Ready", structure.getIntro()));

        status.setText("Ready");
        status.setStyle(statusStyle("#d4d4d8"));

        explanation.setText(structure.getIntro());
    }

    /** Fills the code panel with the pseudocode of one operation. */
    private void showCode(int operation) {

        codeBox.getChildren().clear();

        Styles.fillCodeBox(codeBox, structure.getOperationCode(operation));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {

        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);

        Styles.applyDarkTheme(alert);

        alert.showAndWait();
    }

    // =====================================================
    // DRAWING
    // =====================================================

    /** Draws the items of one step in the layout of this structure. */
    private void drawStructure(StructureStep step) {

        visualBox.getChildren().clear();

        int layout = structure.getLayout();

        if (layout == DataStructure.VERTICAL) {
            drawVertical(step);
        } else if (layout == DataStructure.HORIZONTAL) {
            drawHorizontal(step);
        } else {
            drawLinked(step);
        }
    }

    /** Stack: boxes on top of each other, the TOP is the highest filled box. */
    private void drawVertical(StructureStep step) {

        VBox column = new VBox(4);

        column.setAlignment(Pos.CENTER);

        int capacity = structure.getCapacity();

        // The highest slot is drawn first (at the top of the screen)
        for (int slot = capacity - 1; slot >= 0; slot--) {

            Label box = createBox(step, slot, 170, 34);

            Label tag = createTag(slot == step.items.size() - 1 ? "← TOP" : "");

            tag.setPrefWidth(60);

            HBox row = new HBox(12, box, tag);

            row.setAlignment(Pos.CENTER);

            column.getChildren().add(row);
        }

        visualBox.getChildren().add(column);
    }

    /** Queue: boxes in a row, FRONT on the left and REAR on the right end. */
    private void drawHorizontal(StructureStep step) {

        HBox row = new HBox(8);

        row.setAlignment(Pos.CENTER);

        int capacity = structure.getCapacity();
        int size = step.items.size();

        for (int slot = 0; slot < capacity; slot++) {

            String text = "";

            if (slot == 0 && size > 0) {
                text = "FRONT";
            }

            if (slot == size - 1) {
                text = (text.isEmpty()) ? "REAR" : "F = R";
            }

            Label tag = createTag(text);

            tag.setPrefHeight(18);

            Label box = createBox(step, slot, 56, 56);

            VBox column = new VBox(4, tag, box);

            column.setAlignment(Pos.CENTER);

            row.getChildren().add(column);
        }

        visualBox.getChildren().add(row);
    }

    /** Linked list: nodes joined by arrows, the last arrow points to NULL. */
    private void drawLinked(StructureStep step) {

        if (step.items.isEmpty()) {

            Label empty = new Label("head → NULL");

            empty.setStyle(
                    "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #77777f;"
            );

            visualBox.getChildren().add(empty);

            return;
        }

        HBox row = new HBox(0);

        row.setAlignment(Pos.CENTER);

        for (int i = 0; i < step.items.size(); i++) {

            Label tag = createTag(i == step.headIndex ? "HEAD" : "");

            tag.setPrefHeight(18);

            Label node = createBox(step, i, 52, 52);

            VBox column = new VBox(4, tag, node);

            column.setAlignment(Pos.CENTER);

            Label arrow = new Label("→");

            arrow.setPrefWidth(28);
            arrow.setAlignment(Pos.CENTER);

            arrow.setStyle(
                    "-fx-font-size: 20px;" +
                            "-fx-text-fill: #77777f;"
            );

            // The arrow sits level with the box (below the tag)
            VBox arrowColumn = new VBox(arrow);

            arrowColumn.setAlignment(Pos.BOTTOM_CENTER);

            arrowColumn.setPadding(new Insets(0, 0, 10, 0));

            row.getChildren().addAll(column, arrowColumn);
        }

        Label end = new Label("NULL");

        end.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #77777f;"
        );

        VBox endColumn = new VBox(end);

        endColumn.setAlignment(Pos.BOTTOM_CENTER);

        endColumn.setPadding(new Insets(0, 0, 18, 0));

        row.getChildren().add(endColumn);

        visualBox.getChildren().add(row);
    }

    /**
     * One box. If the slot holds an item it shows the number, coloured
     * according to the step; otherwise it is an empty dashed slot.
     */
    private Label createBox(StructureStep step, int slot, double width, double height) {

        Label box = new Label();

        box.setAlignment(Pos.CENTER);

        box.setPrefSize(width, height);

        if (slot >= step.items.size()) {

            // Empty slot
            box.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-border-color: #2b2b30;" +
                            "-fx-border-style: dashed;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 10px;"
            );

            return box;
        }

        box.setText(String.valueOf(step.items.get(slot)));

        if (slot == step.highlightIndex && step.highlightMode == StructureStep.ADDED) {
            box.setStyle(Styles.cardStyle("#183c2a", "#4fbd7b", "white", 18));
        } else if (slot == step.highlightIndex && step.highlightMode == StructureStep.REMOVED) {
            box.setStyle(Styles.cardStyle("#4b2028", "#d85b6b", "white", 18));
        } else if (slot == step.highlightIndex && step.highlightMode == StructureStep.ACTIVE) {
            box.setStyle(Styles.cardStyle("#4a3d16", "#d6b84c", "white", 18));
        } else {
            box.setStyle(Styles.cardStyle("#151518", "#55555c", "white", 18));
        }

        return box;
    }

    /** Small yellow label such as TOP, FRONT, REAR, HEAD. */
    private Label createTag(String text) {

        Label tag = new Label(text);

        tag.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #d6b84c;"
        );

        return tag;
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
}
