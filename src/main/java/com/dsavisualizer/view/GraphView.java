package com.dsavisualizer.view;

import com.dsavisualizer.algorithms.Edge;
import com.dsavisualizer.algorithms.Graph;
import com.dsavisualizer.algorithms.GraphAlgorithm;
import com.dsavisualizer.algorithms.GraphStep;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

import javafx.util.Duration;

import java.util.List;

/**
 * ONE shared visualizer for every graph algorithm (BFS, DFS, Dijkstra, Kruskal).
 *
 * It has the same layout as the sorting and searching screens. The graph is
 * drawn inside a Pane, where every node is placed at an exact x / y position:
 *   - an edge is a Line between two node centres,
 *   - a node is a round Label,
 *   - a weight (or a Dijkstra distance) is a small Label.
 * All of them are nodes of the JavaFX Scene Graph.
 *
 * Like the other screens, it only DISPLAYS the GraphStep snapshots made by
 * the algorithm. "index" is the position of the step currently shown
 * (-1 = nothing shown yet).
 */
public class GraphView implements AppScreen {

    private static final double GRAPH_WIDTH = 440;
    private static final double GRAPH_HEIGHT = 250;
    private static final double NODE_SIZE = 40;

    private final GraphAlgorithm algorithm;
    private final Navigator navigator;

    // The example graph, and the node the algorithm starts from
    private final Graph graph = Graph.createSample();
    private int start = 0;

    private List<GraphStep> steps;

    private int index = -1;

    private BorderPane root;

    private Pane graphPane;
    private VBox extraBox;
    private VBox codeBox;

    private Label status;
    private Label explanation;
    private Label stepLabel;

    private ComboBox<String> startBox;

    private Slider speedSlider;
    private Button playButton;

    private Timeline timeline;

    private boolean playing = false;

    public GraphView(GraphAlgorithm algorithm, Navigator navigator) {

        this.algorithm = algorithm;
        this.navigator = navigator;

        // The algorithm runs once and returns every snapshot
        this.steps = algorithm.generateSteps(graph, start);
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
        // CENTER
        // =========================

        VBox center = new VBox(8);

        center.setAlignment(Pos.CENTER);

        center.setPadding(new Insets(15, 20, 15, 20));

        Label title = new Label(algorithm.getDisplayName().toUpperCase());

        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label subtitle = new Label(algorithm.getSubtitle());

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #85858d;"
        );

        subtitle.setWrapText(true);
        subtitle.setMaxWidth(430);
        subtitle.setTextAlignment(TextAlignment.CENTER);
        subtitle.setAlignment(Pos.CENTER);

        center.getChildren().addAll(title, subtitle);

        // ----- start node ComboBox (only for algorithms that need it) -----

        if (algorithm.needsStartNode()) {

            startBox = createStartBox();

            HBox startRow = new HBox(10, createSmallLabel("Start node"), startBox);

            startRow.setAlignment(Pos.CENTER);

            center.getChildren().add(startRow);
        }

        // ----- the graph -----

        graphPane = new Pane();

        graphPane.setPrefSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        graphPane.setMinSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        graphPane.setMaxSize(GRAPH_WIDTH, GRAPH_HEIGHT);

        drawGraph(null);

        // ----- the extra list (queue, call stack, sorted edges, ...) -----

        extraBox = new VBox(4);

        extraBox.setAlignment(Pos.CENTER);

        extraBox.setMinHeight(76);

        drawExtra(null);

        status = new Label("Ready to visualize");

        status.setStyle(statusStyle());

        stepLabel = createSmallLabel("Step 0");

        center.getChildren().addAll(graphPane, extraBox, status, stepLabel);

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
        root.setCenter(center);
        root.setRight(codePanel);
        root.setBottom(new VBox(explanation, controls));

        return root;
    }

    /** Called by the sidebar before another screen replaces this one. */
    @Override
    public void stop() {
        stopPlaying();
    }

    // =====================================================
    // START NODE (ComboBox)
    // =====================================================

    private ComboBox<String> createStartBox() {

        ComboBox<String> box = new ComboBox<>();

        for (int i = 0; i < graph.getNodeCount(); i++) {
            box.getItems().add(graph.getName(i));
        }

        box.getSelectionModel().select(start);

        box.setStyle(
                "-fx-background-color: #151518;" +
                        "-fx-border-color: #35353b;" +
                        "-fx-border-radius: 6px;" +
                        "-fx-background-radius: 6px;"
        );

        // The text shown in the closed ComboBox must be white on the dark background
        box.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-text-fill: white;");
            }
        });

        // Choosing another node runs the algorithm again from that node
        box.setOnAction(event -> {
            start = box.getSelectionModel().getSelectedIndex();
            steps = algorithm.generateSteps(graph, start);
            reset();
        });

        return box;
    }

    // =====================================================
    // DRAWING THE GRAPH
    // =====================================================

    /** Draws the whole graph for one step. step == null = plain graph. */
    private void drawGraph(GraphStep step) {

        graphPane.getChildren().clear();

        // 1. Edges (Lines) - drawn first so the nodes sit on top of them
        for (int e = 0; e < graph.getEdgeCount(); e++) {

            Edge edge = graph.getEdge(e);

            int state = (step == null) ? GraphStep.EDGE_NORMAL : step.edgeState[e];

            Line line = new Line(
                    graph.getX(edge.from), graph.getY(edge.from),
                    graph.getX(edge.to), graph.getY(edge.to)
            );

            line.setStroke(Color.web(edgeColour(state)));
            line.setStrokeWidth(state == GraphStep.EDGE_NORMAL ? 2 : 4);

            if (state == GraphStep.EDGE_REJECTED) {
                line.getStrokeDashArray().addAll(6.0, 6.0);
            }

            graphPane.getChildren().add(line);
        }

        // 2. Weights (small Labels in the middle of every edge)
        if (algorithm.isWeighted()) {

            for (int e = 0; e < graph.getEdgeCount(); e++) {

                Edge edge = graph.getEdge(e);

                int state = (step == null) ? GraphStep.EDGE_NORMAL : step.edgeState[e];

                double middleX = (graph.getX(edge.from) + graph.getX(edge.to)) / 2.0;
                double middleY = (graph.getY(edge.from) + graph.getY(edge.to)) / 2.0;

                Label weight = new Label(String.valueOf(edge.weight));

                weight.setPrefSize(24, 18);
                weight.setAlignment(Pos.CENTER);

                weight.setLayoutX(middleX - 12);
                weight.setLayoutY(middleY - 9);

                weight.setStyle(
                        "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: " + (state == GraphStep.EDGE_NORMAL ? "#9999a1" : edgeColour(state)) + ";" +
                                "-fx-background-color: #0b0b0d;" +
                                "-fx-background-radius: 6px;"
                );

                graphPane.getChildren().add(weight);
            }
        }

        // 3. Nodes (round Labels) and the notes under them
        for (int i = 0; i < graph.getNodeCount(); i++) {

            int state = (step == null) ? GraphStep.UNVISITED : step.nodeState[i];

            Label node = new Label(graph.getName(i));

            node.setPrefSize(NODE_SIZE, NODE_SIZE);
            node.setAlignment(Pos.CENTER);

            node.setLayoutX(graph.getX(i) - NODE_SIZE / 2);
            node.setLayoutY(graph.getY(i) - NODE_SIZE / 2);

            node.setStyle(nodeStyle(state));

            graphPane.getChildren().add(node);

            if (step != null && !step.nodeNote[i].isEmpty()) {

                Label note = new Label(step.nodeNote[i]);

                note.setPrefWidth(NODE_SIZE);
                note.setAlignment(Pos.CENTER);

                note.setLayoutX(graph.getX(i) - NODE_SIZE / 2);
                note.setLayoutY(graph.getY(i) + NODE_SIZE / 2 + 1);

                note.setStyle(
                        "-fx-font-size: 13px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: #d6b84c;"
                );

                graphPane.getChildren().add(note);
            }
        }
    }

    private String edgeColour(int state) {

        if (state == GraphStep.EDGE_EXAMINING) {
            return "#d6b84c";
        }

        if (state == GraphStep.EDGE_USED) {
            return "#4fbd7b";
        }

        if (state == GraphStep.EDGE_REJECTED) {
            return "#d85b6b";
        }

        return "#3a3a40";
    }

    private String nodeStyle(int state) {

        String background = "#151518";
        String border = "#55555c";

        if (state == GraphStep.FRONTIER) {
            background = "#1b2a4d";
            border = "#6b94ff";
        } else if (state == GraphStep.CURRENT) {
            background = "#4a3d16";
            border = "#d6b84c";
        } else if (state == GraphStep.DONE) {
            background = "#183c2a";
            border = "#4fbd7b";
        }

        return "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: " + background + ";" +
                "-fx-background-radius: 20px;" +
                "-fx-border-color: " + border + ";" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;";
    }

    /** Draws the list under the graph (queue, call stack, sorted edges, ...). */
    private void drawExtra(GraphStep step) {

        extraBox.getChildren().clear();

        if (step == null || step.extraTitle.isEmpty()) {
            return;
        }

        Label heading = createSmallLabel(step.extraTitle);

        extraBox.getChildren().add(heading);

        if (step.extraItems.isEmpty()) {

            extraBox.getChildren().add(createSmallLabel("(empty)"));

            return;
        }

        // At most 6 items per row, so long lists (Kruskal's 10 edges) use two rows
        HBox row = null;

        for (int i = 0; i < step.extraItems.size(); i++) {

            if (i % 6 == 0) {

                row = new HBox(4);

                row.setAlignment(Pos.CENTER);

                extraBox.getChildren().add(row);
            }

            Label item = new Label(step.extraItems.get(i));

            // Never shrink the label (a shrunk label would show "...")
            item.setMinWidth(Region.USE_PREF_SIZE);

            boolean highlighted = (i == step.extraHighlight);

            item.setStyle(
                    "-fx-font-size: 11px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 3px 5px;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-background-color: " + (highlighted ? "#4a3d16" : "#1a1a1e") + ";" +
                            "-fx-border-radius: 5px;" +
                            "-fx-border-color: " + (highlighted ? "#d6b84c" : "#35353b") + ";"
            );

            row.getChildren().add(item);
        }
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

    /** Shows one step: graph, extra list, highlighted code line, title, explanation. */
    private void showStep(GraphStep step) {

        drawGraph(step);

        drawExtra(step);

        Styles.highlightCodeLine(codeBox, step.codeLine);

        status.setText(step.title);

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

        drawGraph(null);

        drawExtra(null);

        Styles.highlightCodeLine(codeBox, 0);

        status.setText("Ready to visualize");

        explanation.setText(algorithm.getIntro());

        updateStepLabel();
    }

    // =====================================================
    // SMALL HELPERS
    // =====================================================

    private String statusStyle() {

        return "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #d4d4d8;";
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
