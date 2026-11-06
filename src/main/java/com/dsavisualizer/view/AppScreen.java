package com.dsavisualizer.view;

import javafx.scene.Parent;

/**
 * INTERFACE implemented by every screen of the app
 * (AlgorithmSelectionView, VisualizerView, SearchView).
 *
 * The sidebar only needs two things from the screen it sits in:
 *   createView() - build the JavaFX layout (the root node of the Scene Graph)
 *   stop()       - stop timelines/animations before another screen replaces it
 */
public interface AppScreen {

    Parent createView();

    void stop();
}
