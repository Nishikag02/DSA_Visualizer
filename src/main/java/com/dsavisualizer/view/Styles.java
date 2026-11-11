package com.dsavisualizer.view;

import javafx.scene.control.Alert;
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

    /**
     * Path of the stylesheet (src/main/resources/styles.css), or null if it
     * cannot be found. getResource looks inside the compiled project.
     */
    public static String stylesheet() {

        java.net.URL url = Styles.class.getResource("/styles.css");

        return (url == null) ? null : url.toExternalForm();
    }

    /** Gives an Alert dialog the same dark look as the rest of the app. */
    public static void applyDarkTheme(Alert alert) {

        String css = stylesheet();

        if (css != null) {
            alert.getDialogPane().getStylesheets().add(css);
        }
    }

    /**
     * Sidebar button. The look (and the hover effect) is written in
     * styles.css; here we only choose the style class.
     */
    public static Button createAlgorithmButton(String text, boolean selected) {

        Button button = new Button(text);

        button.setMaxWidth(Double.MAX_VALUE);

        button.getStyleClass().add(selected ? "sidebar-button-selected" : "sidebar-button");

        return button;
    }

    /** Dark rounded button used for Reset / Previous / Play / Next / Apply / menu items. */
    public static Button createControlButton(String text) {

        Button button = new Button(text);

        button.getStyleClass().add("control-button");

        return button;
    }

    /** The big white button on the dashboard. */
    public static Button createPrimaryButton(String text) {

        Button button = new Button(text);

        button.getStyleClass().add("primary-button");

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
