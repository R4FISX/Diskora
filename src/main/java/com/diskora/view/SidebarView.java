package com.diskora.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class SidebarView extends VBox {

    private final Map<String, Button> buttons = new LinkedHashMap<>();

    public SidebarView(Consumer<String> onNavigation) {
        getStyleClass().add("sidebar");
        setPrefWidth(232);
        setPadding(new Insets(28, 16, 20, 16));
        setSpacing(8);

        Label brand = new Label("DISKORA");
        brand.getStyleClass().add("brand");
        Label subtitle = new Label("LOCAL DISK ANALYZER");
        subtitle.getStyleClass().add("brand-subtitle");
        VBox brandBox = new VBox(4, brand, subtitle);
        brandBox.setPadding(new Insets(0, 8, 28, 8));

        getChildren().add(brandBox);
        addButton("Dashboard", "⌂", onNavigation);
        addButton("Armazenamento", "▣", onNavigation);
        addButton("Analisar", "⌕", onNavigation);
        addButton("Limpeza", "✦", onNavigation);
        addButton("Configurações", "⚙", onNavigation);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        getChildren().add(spacer);

        Label status = new Label("●  SOMENTE LEITURA");
        status.getStyleClass().add("sidebar-status");
        getChildren().add(status);
    }

    public void select(String page) {
        buttons.forEach((name, button) -> button.pseudoClassStateChanged(
                javafx.css.PseudoClass.getPseudoClass("selected"), name.equals(page)));
    }

    private void addButton(String name, String icon, Consumer<String> onNavigation) {
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("nav-icon");

        Button button = new Button(name);
        button.setGraphic(iconLabel);
        button.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
        button.setGraphicTextGap(12);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.getStyleClass().add("nav-button");
        button.setOnAction(event -> onNavigation.accept(name));

        buttons.put(name, button);
        getChildren().add(button);
    }
}
