package com.diskora.util;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.util.List;

/** Small, reusable animations used by the JavaFX presentation layer. */
public final class AnimationUtils {

    private static final Duration ENTRANCE_DURATION = Duration.millis(320);

    private AnimationUtils() {
    }

    public static void playPageEntrance(Node node) {
        node.setOpacity(0);
        node.setTranslateY(12);

        FadeTransition fade = new FadeTransition(ENTRANCE_DURATION, node);
        fade.setFromValue(0);
        fade.setToValue(1);
        TranslateTransition slide = new TranslateTransition(ENTRANCE_DURATION, node);
        slide.setFromY(12);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(fade, slide).play();
    }

    public static void playStaggered(List<? extends Node> nodes) {
        for (int index = 0; index < nodes.size(); index++) {
            reveal(nodes.get(index), index * 70L);
        }
    }

    public static void animateProgress(ProgressBar progressBar, double target) {
        double safeTarget = Math.max(0, Math.min(1, target));
        progressBar.setProgress(0);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.millis(850),
                        new KeyValue(progressBar.progressProperty(), safeTarget, Interpolator.EASE_OUT)));
        timeline.play();
    }

    public static void installHoverLift(Node node) {
        node.setOnMouseEntered(event -> animateLift(node, -3, 1.012));
        node.setOnMouseExited(event -> animateLift(node, 0, 1));
    }

    public static void installNavHover(Node node) {
        node.setOnMouseEntered(event -> animateLift(node, -1, 1.015));
        node.setOnMouseExited(event -> animateLift(node, 0, 1));
    }

    public static void playPulse(Node node) {
        FadeTransition pulse = new FadeTransition(Duration.millis(1_600), node);
        pulse.setFromValue(0.55);
        pulse.setToValue(1);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(FadeTransition.INDEFINITE);
        pulse.play();
    }

    public static void playSpin(Node node) {
        node.setRotate(0);
        javafx.animation.RotateTransition rotate = new javafx.animation.RotateTransition(
                Duration.millis(500), node);
        rotate.setByAngle(360);
        rotate.setInterpolator(Interpolator.EASE_OUT);
        rotate.play();
    }

    private static void reveal(Node node, long delayMillis) {
        node.setOpacity(0);
        node.setTranslateY(8);

        FadeTransition fade = new FadeTransition(Duration.millis(280), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        TranslateTransition slide = new TranslateTransition(Duration.millis(280), node);
        slide.setFromY(8);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_OUT);
        ParallelTransition transition = new ParallelTransition(fade, slide);
        transition.setDelay(Duration.millis(delayMillis));
        transition.play();
    }

    private static void animateLift(Node node, double translateY, double scale) {
        TranslateTransition lift = new TranslateTransition(Duration.millis(140), node);
        lift.setToY(translateY);
        lift.setInterpolator(Interpolator.EASE_OUT);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(140), node);
        scaleTransition.setToX(scale);
        scaleTransition.setToY(scale);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        new ParallelTransition(lift, scaleTransition).play();
    }
}
