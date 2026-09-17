package com.diskora.view;

import com.diskora.util.ViewUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public final class PlaceholderView extends VBox {

    public PlaceholderView(String pageName) {
        getStyleClass().add("page");
        setAlignment(Pos.CENTER);
        setPadding(new Insets(36));
        setSpacing(12);

        Label icon = new Label("✦");
        icon.getStyleClass().add("placeholder-icon");
        Label eyebrow = ViewUtils.eyebrow("EM BREVE");
        Label title = new Label(pageName);
        title.getStyleClass().add("page-title");
        Label description = new Label("Esta área fará parte de uma próxima versão do Diskora.");
        description.getStyleClass().add("page-description");
        getChildren().addAll(icon, eyebrow, title, description);
    }
}
