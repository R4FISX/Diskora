package com.diskora.util;

import javafx.scene.control.Label;

public final class ViewUtils {

    private ViewUtils() {
    }

    public static Label eyebrow(String text) {
        Label label = new Label(text.toUpperCase());
        label.getStyleClass().add("eyebrow");
        return label;
    }
}
