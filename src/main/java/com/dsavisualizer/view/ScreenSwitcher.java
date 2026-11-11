package com.dsavisualizer.view;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.util.Duration;

/**
 * The ONE place that changes the screen shown in the window.
 *
 * A JavaFX window shows one Scene. To show another screen we replace the
 * ROOT of that scene (the top node of the Scene Graph) with the root of the
 * new screen, and play a short FadeTransition so the change is smooth:
 * the new screen goes from transparent (opacity 0) to visible (opacity 1).
 *
 * The method is STATIC, so it is called on the class itself:
 * ScreenSwitcher.show(scene, newRoot).
 */
public final class ScreenSwitcher {

    private static final double FADE_MILLIS = 220;

    private ScreenSwitcher() {
    }

    public static void show(Scene scene, Parent newRoot) {

        newRoot.setOpacity(0);

        scene.setRoot(newRoot);

        FadeTransition fade = new FadeTransition(Duration.millis(FADE_MILLIS), newRoot);

        fade.setFromValue(0);
        fade.setToValue(1);

        fade.play();
    }
}
