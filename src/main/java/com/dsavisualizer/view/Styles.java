package com.dsavisualizer.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Small helper methods shared by every screen (sorting, searching, sidebar),
 * so the dark theme is written ONCE and looks identical everywhere.
 *
 * Every method is STATIC, so no object is needed: Styles.createControlButton("Next").
 */
public final class Styles {

    private Styles() {
    }

    /** Sidebar button. The selected one gets a lighter background. */
    public static Button createAlgorithmButton(String text, boolean selected) {

        Button button = new Button(text);

        button.setMaxWidth(Double.MAX_VALUE);

        if (selected) {

            button.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: #27272c;" +
                            "-fx-background-radius: 7px;" +
                            "-fx-alignment: CENTER_LEFT;" +
                            "-fx-padding: 8px 10px;"
            );

        } else {

            button.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: #9999a1;" +
                            "-fx-background-color: transparent;" +
                            "-fx-alignment: CENTER_LEFT;" +
                            "-fx-padding: 8px 10px;"
            );
        }

        return button;
    }

    /** Dark rounded button used for Reset / Previous / Play / Next / Apply. */
    public static Button createControlButton(String text) {

        Button button = new Button(text);

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

    /** Small grey heading such as "SORTING" or "SEARCHING". */
    public static Label createSectionLabel(String text) {

        Label label = new Label(text);

        label.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #77777f;"
        );

        return label;
    }

    /** CSS for one array card (background, border, text colour, font size). */
    public static String cardStyle(String background, String border,
                                   String textColor, int fontSize) {

        return "-fx-font-size: " + fontSize + "px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + textColor + ";" +
                "-fx-background-color: " + background + ";" +
                "-fx-background-radius: 10px;" +
                "-fx-border-color: " + border + ";" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 10px;";
    }

    /** Fills the code panel: one Label per pseudocode line, with a line number. */
    public static void fillCodeBox(VBox codeBox, String[] code) {

        for (int i = 0; i < code.length; i++) {

            Label line = new Label(
                    String.format("%2s    %s", i + 1, code[i])
            );

            line.setMaxWidth(Double.MAX_VALUE);

            codeBox.getChildren().add(line);
        }

        highlightCodeLine(codeBox, 0);
    }

    /** Highlights one pseudocode line (1-based). 0 = highlight nothing. */
    public static void highlightCodeLine(VBox codeBox, int selectedLine) {

        for (int i = 0; i < codeBox.getChildren().size(); i++) {

            Label line = (Label) codeBox.getChildren().get(i);

            int lineNumber = i + 1;

            if (lineNumber == selectedLine) {

                line.setStyle(
                        "-fx-font-family: 'Consolas';" +
                                "-fx-font-size: 13px;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-color: #292930;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-padding: 5px;"
                );

            } else {

                line.setStyle(
                        "-fx-font-family: 'Consolas';" +
                                "-fx-font-size: 13px;" +
                                "-fx-text-fill: #b8b8c0;" +
                                "-fx-padding: 5px;"
                );
            }
        }
    }
}
