package com.dsavisualizer.view;

import com.dsavisualizer.algorithms.SelectionSort;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;

public class SelectionSortView {

    private static final int[] ORIGINAL_ARRAY = {5, 2, 8, 1, 6};

    public VBox createView() {

        int[] array = ORIGINAL_ARRAY.clone();

        // =========================
        // TITLE
        // =========================

        Label title = new Label("SELECTION SORT");

        title.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #111827;"
        );

        Label subtitle = new Label(
                "Find the smallest element and place it in the correct position"
        );

        subtitle.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-text-fill: #6b7280;"
        );

        // =========================
        // ARRAY CARDS
        // =========================

        HBox arrayBox = new HBox(16);

        arrayBox.setAlignment(Pos.CENTER);

        createCards(array, arrayBox);

        // =========================
        // STATUS
        // =========================

        Label status = new Label("Ready to sort");

        status.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #374151;"
        );

        // =========================
        // EXPLANATION
        // =========================

        Label explanation = new Label(
                "Selection Sort repeatedly finds the smallest element."
        );

        explanation.setWrapText(true);
        explanation.setMaxWidth(600);

        explanation.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #6b7280;" +
                        "-fx-background-color: white;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-padding: 14px 20px;"
        );

        // =========================
        // SPEED
        // =========================

        Label speedLabel = new Label("Animation Speed");

        speedLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #4b5563;"
        );

        Slider speedSlider = new Slider(0.3, 1.2, 0.7);

        speedSlider.setPrefWidth(180);

        HBox speedBox = new HBox(
                10,
                speedLabel,
                speedSlider
        );

        speedBox.setAlignment(Pos.CENTER);

        // =========================
        // BUTTONS
        // =========================

        Button startButton = new Button("▶  Start Sorting");

        startButton.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: #4f46e5;" +
                        "-fx-background-radius: 9px;" +
                        "-fx-padding: 11px 22px;"
        );

        Button resetButton = new Button("↻  Reset");

        resetButton.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #374151;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #d1d5db;" +
                        "-fx-border-radius: 9px;" +
                        "-fx-background-radius: 9px;" +
                        "-fx-padding: 11px 18px;"
        );

        Button backButton = new Button("←  Back");

        backButton.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #6b7280;"
        );

        HBox buttonBox = new HBox(
                12,
                startButton,
                resetButton
        );

        buttonBox.setAlignment(Pos.CENTER);

        // =========================
        // START SORTING
        // =========================

        startButton.setOnAction(event -> {

            startButton.setDisable(true);

            SelectionSort selectionSort = new SelectionSort();

            List<SelectionSort.Step> steps =
                    selectionSort.getSteps(array);

            Timeline timeline = new Timeline();

            double speed = speedSlider.getValue();

            for (int i = 0; i < steps.size(); i++) {

                int stepIndex = i;

                KeyFrame keyFrame = new KeyFrame(
                        Duration.seconds(stepIndex * speed),
                        action -> {

                            SelectionSort.Step step =
                                    steps.get(stepIndex);

                            updateCards(
                                    step,
                                    arrayBox,
                                    status,
                                    explanation
                            );
                        }
                );

                timeline.getKeyFrames().add(keyFrame);
            }

            timeline.setOnFinished(event2 -> {

                status.setText("✓ Sorting Complete!");

                explanation.setText(
                        "All elements are now in their correct positions."
                );

                startButton.setDisable(false);
            });

            timeline.play();
        });

        // =========================
        // RESET
        // =========================

        resetButton.setOnAction(event -> {

            arrayBox.getChildren().clear();

            createCards(
                    ORIGINAL_ARRAY,
                    arrayBox
            );

            status.setText("Ready to sort");

            explanation.setText(
                    "Selection Sort repeatedly finds the smallest element."
            );

            startButton.setDisable(false);
        });

        // =========================
        // MAIN LAYOUT
        // =========================

        VBox layout = new VBox(
                20,
                title,
                subtitle,
                arrayBox,
                status,
                explanation,
                speedBox,
                buttonBox,
                backButton
        );

        layout.setAlignment(Pos.CENTER);

        layout.setStyle(
                "-fx-background-color: #f3f4f6;" +
                        "-fx-padding: 40px;"
        );

        return layout;
    }

    // =========================================================
    // CREATE NUMBER CARDS
    // =========================================================

    private void createCards(
            int[] array,
            HBox arrayBox
    ) {

        for (int number : array) {

            Label card = new Label(
                    String.valueOf(number)
            );

            card.setAlignment(Pos.CENTER);

            card.setPrefSize(70, 70);

            applyNormalStyle(card);

            arrayBox.getChildren().add(card);
        }
    }

    // =========================================================
    // UPDATE CARDS
    // =========================================================

    private void updateCards(
            SelectionSort.Step step,
            HBox arrayBox,
            Label status,
            Label explanation
    ) {

        int[] currentArray = step.array;

        // Update all numbers
        for (int i = 0; i < currentArray.length; i++) {

            Label card =
                    (Label) arrayBox
                            .getChildren()
                            .get(i);

            card.setText(
                    String.valueOf(currentArray[i])
            );

            applyNormalStyle(card);
        }

        // Final state
        if (step.currentIndex == -1) {

            for (int i = 0;
                 i < currentArray.length;
                 i++) {

                Label card =
                        (Label) arrayBox
                                .getChildren()
                                .get(i);

                applySortedStyle(card);
            }

            status.setText(
                    "✓ Sorting Complete!"
            );

            explanation.setText(
                    "Every element is now in its correct position."
            );

            return;
        }

        // Highlight current comparison
        Label first =
                (Label) arrayBox
                        .getChildren()
                        .get(step.currentIndex);

        Label second =
                (Label) arrayBox
                        .getChildren()
                        .get(step.minimumIndex);

        if (step.swapped) {

            applySwapStyle(first);
            applySwapStyle(second);

            status.setText(
                    "↔ Swapping elements"
            );

            explanation.setText(
                    "The smallest element is moved into its correct position."
            );

        } else {

            applyCompareStyle(first);
            applyCompareStyle(second);

            status.setText(
                    "Comparing "
                            + step.array[step.currentIndex]
                            + " and "
                            + step.array[step.minimumIndex]
            );

            explanation.setText(
                    "Selection Sort checks which element is smaller."
            );
        }
    }

    // =========================================================
    // NORMAL CARD
    // =========================================================

    private void applyNormalStyle(Label card) {

        card.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f2937;" +
                        "-fx-background-color: white;" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: #d1d5db;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 14px;"
        );
    }

    // =========================================================
    // COMPARISON CARD
    // =========================================================

    private void applyCompareStyle(Label card) {

        card.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #92400e;" +
                        "-fx-background-color: #fef3c7;" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: #f59e0b;" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-radius: 14px;"
        );
    }

    // =========================================================
    // SWAP CARD
    // =========================================================

    private void applySwapStyle(Label card) {

        card.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #991b1b;" +
                        "-fx-background-color: #fee2e2;" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: #ef4444;" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-radius: 14px;"
        );
    }

    // =========================================================
    // SORTED CARD
    // =========================================================

    private void applySortedStyle(Label card) {

        card.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #166534;" +
                        "-fx-background-color: #dcfce7;" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: #22c55e;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 14px;"
        );
    }
}