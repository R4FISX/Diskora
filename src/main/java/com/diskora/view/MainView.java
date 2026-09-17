package com.diskora.view;

import com.diskora.util.AnimationUtils;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public final class MainView extends BorderPane {

    public MainView(SidebarView sidebar) {
        getStyleClass().add("app-shell");
        setLeft(sidebar);
    }

    public void showContent(Node content) {
        Node currentContent = getCenter();
        if (currentContent == null) {
            setCenter(content);
            AnimationUtils.playPageEntrance(content);
            return;
        }

        javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(
                javafx.util.Duration.millis(140), currentContent);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> {
            setCenter(content);
            AnimationUtils.playPageEntrance(content);
        });
        fadeOut.play();
    }
}
